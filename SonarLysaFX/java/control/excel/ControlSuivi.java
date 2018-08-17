package control.excel;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ibm.team.repository.common.PermissionDeniedException;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.mail.ControlMail;
import control.rtc.ControlRTC;
import model.Anomalie;
import model.InfoClarity;
import model.ModelFactory;
import model.RespService;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeInfoMail;
import utilities.CellHelper;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de getion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Grégoire Mathon
 */
public class ControlSuivi extends AbstractControlExcelRead<TypeColSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    // Les noms des champs doivent correspondre aux valeurs dans l'énumération TypeCol
    private int colDir;
    private int colDepart;
    private int colService;
    private int colResp;
    private int colClarity;
    private int colLib;
    private int colCpi;
    private int colEdition;
    private int colLot;
    private int colEnv;
    private int colAno;
    private int colEtat;
    private int colSec;
    private int colRemarque;
    private int colVer;
    private int colDateCrea;
    private int colDateDetec;
    private int colDateRel;
    private int colMatiere;
    private int colProjetRTC;
    private int colAction;
    private int colDateRes;
    private int colDateMajEtat;

    // Constantes statiques
    private static final String SQ = "SUIVI Qualité";
    private static final String AC = "Anomalies closes";
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";
    private static final short CLARITY7 = 7;
    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;

    // Liens vers Sonar et RTC
    private String lienslots;
    private String liensAnos;

    // Date du jour
    private final LocalDate today = LocalDate.now();

    // Controleurs
    private ControlMail controlMail;
    private ControlRTC controlRTC;

    /** contrainte de validitée de la colonne Action */
    protected String[] contraintes;

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuivi(File file)
    {
        super(file);

        // Initialisation des parties constantes des liens
        Map<Param, String> proprietes = proprietesXML.getMapParams();
        lienslots = proprietes.get(Param.LIENSLOTS);
        liensAnos = proprietes.get(Param.LIENSANOS);
        controlMail = new ControlMail();
        controlRTC = ControlRTC.INSTANCE;
        initContraintes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public List<Anomalie> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();

        // Itération sur chaque ligne pour créer les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            
            // protection si la ligne ets nulle
            if (row == null)
                continue;

            // Création anomalie si la ligne n'est pas vide : lot vide.
            Anomalie ano = creerAnodepuisExcel(row);
            if (!ano.getLot().isEmpty())
                retour.add(ano);
        }
        return retour;
    }

    /**
     * Permet de créer les feuille des anomalies par version et retourne les anomalies à créer
     * 
     * @param nomSheet
     * @param anoAcreer
     * @param anoDejacrees
     * @return
     */
    public List<Anomalie> createSheetError(String nomSheet, Iterable<Anomalie> anoAcreer, Iterable<Anomalie> anoDejacrees)
    {
        // 1. Variables

        // Création de la feuille de calcul
        Sheet sheet = wb.getSheet(nomSheet);

        // Liste retour des anomalies à créer
        List<Anomalie> retour = new ArrayList<>();

        // 2. Sauvegarde données existantes. On itère pour récupérer tous les numéros de lot déjà abandonés, puis suppression de la feuille
        List<String> lotsAbandon = new ArrayList<>();
        if (sheet != null)
        {
            Iterator<Row> iter = sheet.rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                Cell cellLot = row.getCell(Index.LOTI.ordinal());
                Cell cellT = row.getCell(Index.TRAITEI.ordinal());
                if (cellLot.getCellTypeEnum() == CellType.STRING && cellT.getCellTypeEnum() == CellType.STRING && "A".equals(cellT.getStringCellValue()))
                    lotsAbandon.add(row.getCell(Index.LOTI.ordinal()).getStringCellValue());
            }
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        }

        // 3. Génération de la nouvelle feuille

        // Recréation de la feuille
        sheet = wb.createSheet(nomSheet);
        Row row;

        // Création des titres
        creerTitresSE(sheet);

        // 4. Ajout anomalies déjà créées
        for (Anomalie ano : anoDejacrees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (TypeAction.ABANDONNER == ano.getAction())
                creerLigneVersion(row, ano, IndexedColors.WHITE, "A");
            else
                creerLigneVersion(row, ano, IndexedColors.WHITE, "O");
        }

        // 5. Itération sur les anomalies à créer. Si elles sont déjà dans les anomalies abandonnées, on créée une ligne
        // à l'état abandon, sinon on crée une ligne à l'état non traité et on ajoute celle-ci aux anomalies à créer.
        for (Anomalie ano : anoAcreer)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (lotsAbandon.contains(ano.getLot()))
                creerLigneVersion(row, ano, IndexedColors.WHITE, "A");
            else
            {
                creerLigneVersion(row, ano, IndexedColors.LIGHT_YELLOW, "N");
                controlMail.addInfo(TypeInfoMail.ANONEW, ano.getLot(), null);
                retour.add(ano);
            }
        }

        sheet.autoSizeColumn(Index.LOTI.ordinal());
        sheet.autoSizeColumn(Index.EDITIONI.ordinal());
        sheet.autoSizeColumn(Index.ENVI.ordinal());
        sheet.autoSizeColumn(Index.TRAITEI.ordinal());
        return retour;
    }

    /**
     * @param fichier
     * @return
     * @throws IOException
     */
    public Sheet sauvegardeFichier(String fichier) throws IOException
    {
        // Récupération feuille principale existante
        Sheet retour = wb.getSheet(SQ);
        if (retour != null)
        {

            // Création du fichier de sauvegarde et effacement de la feuille
            wb.write(new FileOutputStream(
                    new StringBuilder(proprietesXML.getMapParams().get(Param.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString()));
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }

        // Création des lignes de titres
        retour = wb.createSheet(SQ);
        creerLigneTitres(retour);
        return retour;
    }

    /**
     * Gestion de la feuille principale des anomalies. Maj des anciennes plus création des nouvelles
     * 
     * @param lotsEnAno
     * @param anoAajouter
     * @param lotsEnErreurSonar
     * @param lotsSecurite
     * @param lotsRelease
     * @param sheet
     * @param matiere
     * @throws IOException
     */
    public void majFeuillePrincipale(Iterable<Anomalie> lotsEnAno, List<Anomalie> anoAajouter, Set<String> lotsEnErreurSonar, Set<String> lotsSecurite, Set<String> lotsRelease,
            Sheet sheet, Matiere matiere)
    {
        // Récupération feuille et liste des anomalies closes
        Map<String, Anomalie> anoClose = new HashMap<>();
        Sheet sheetClose = saveAnomaliesCloses(anoClose);

        // Mise à jour anomalies déjà créées
        for (Anomalie ano : lotsEnAno)
        {
            ano.getMatieres().add(matiere);
            String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(Statics.SECURITEKO);

            // Calcul version SNAPSHOT ou RELEASE
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Gestion des actions demandées sur l'anomalie et quitte le traitment en cas d'action de fin (abandon ou clôture)
            if (!gestionAction(ano, anoLot, sheetClose))
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(ano, lotsEnErreurSonar, anoLot);

            // Création de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneSQ(row, ano, couleur);
        }

        ajouterNouvellesAnos(sheet, anoAajouter, anoClose, lotsSecurite, lotsRelease, matiere);
        ajouterAnomaliesCloses(sheetClose, anoClose);

        if (sheet.getLastRowNum() > 0)
            ajouterDataValidation(sheet);
        autosizeColumns(sheet);
        if (sheetClose.getLastRowNum() > 0)
            ajouterDataValidation(sheetClose);
        autosizeColumns(sheetClose);

        wb.setActiveSheet(wb.getSheetIndex(sheet));
        controlMail.envoyerMail(matiere.getTypeMail());
        write();
    }

    /**
     * Mise à jour des fichiers pour les anomalies comprenant plusieures type de matière
     * 
     * @param anoMultiple
     * @throws IOException
     */
    public void majMultiMatiere(Collection<String> anoMultiple)
    {
        Sheet sheet = wb.getSheet(SQ);

        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Problème récupération feuille Excel principale");

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            String lot = getCellStringValue(row, colLot);
            if (anoMultiple.contains(lot.length() < Statics.SBTRINGLOT ? lot : lot.substring(Statics.SBTRINGLOT)))
                row.getCell(colMatiere, MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(Matiere.JAVA + " - " + Matiere.DATASTAGE);
        }
        write();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Crée la ligne de titre des feuilles par édition
     * 
     * @param sheet
     */
    private void creerTitresSE(Sheet sheet)
    {
        // Création du style des titres
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);

        // Création des noms des colonnes
        Row titres = sheet.createRow(0);
        for (Index index : Index.values())
        {
            Cell cell = titres.createCell(index.ordinal());
            cell.setCellStyle(styleTitre);
            switch (index)
            {
                case LOTI:
                    cell.setCellValue(Index.LOTI.toString());
                    break;

                case EDITIONI:
                    cell.setCellValue(Index.EDITIONI.toString());
                    break;

                case ENVI:
                    cell.setCellValue(Index.ENVI.toString());
                    break;

                case TRAITEI:
                    cell.setCellValue(Index.TRAITEI.toString());
                    break;

                default:
                    throw new TechnicalException("Nouvel index non géré : " + index, null);
            }
        }
    }

    /**
     * Permet de calculer la couleur d'une ligne du fichier Excel : <br>
     * - orange = nouvelle aomalie non traitée<br>
     * - jaune = anomalie avec au moisn un composant en version figée<br>
     * - gris = anomalie déjà traitée une première fois avec un QG redevenu rouge<br>
     * - vert = lot dont le QG est devenu vert<br>
     * 
     * @param ano
     *            anomalie
     * @param lotsEnErreurSonar
     *            Liste des lots en erreur
     * @param anoLot
     *            numéro du lot
     * @param lotsRelease
     *            Liste des lots en version Release
     * @return
     */
    private IndexedColors calculCouleurLigne(Anomalie ano, Set<String> lotsEnErreurSonar, String anoLot)
    {
        IndexedColors couleur;

        // Mise en vert des anomalies avec un Quality Gate bon
        if (!lotsEnErreurSonar.contains(anoLot))
            couleur = IndexedColors.LIGHT_GREEN;
        // Les lots qui ont besoin juste d'un réassemblage sont en bleu
        else if (TypeAction.ASSEMBLER == ano.getAction())
            couleur = IndexedColors.LIGHT_TURQUOISE;
        // Le reste est en blanc
        else
            couleur = IndexedColors.WHITE;

        // Les lots déjà traité une première fois sont en gris
        if (TypeAction.VERIFIER == ano.getAction())
            couleur = IndexedColors.GREY_25_PERCENT;

        // Remise de la couleur à orange si le lot n'a pas encore été traité
        if (!ano.isTraitee())
            couleur = IndexedColors.LIGHT_ORANGE;

        return couleur;
    }

    /**
     * Gestion des actions possibles pour une anomalie (Creer, Clôturer, Abandonner)
     * 
     * @param ano
     *            Anomalie à traiter
     * @param anoLot
     *            Numéro de lot l'anomalie
     * @param sheetClose
     *            Feuille des anomalies closes
     * @return {@code true} si l'on doit continuer le traitement<br>
     *         {@code false} si l'anomalie est à clôturer ou abandonner
     */
    private boolean gestionAction(Anomalie ano, String anoLot, Sheet sheetClose)
    {
        // Si on doit clôturer ou abandonner l'anomalie, on la recopie dans la feuille des anomalies closes
        if (TypeAction.CLOTURER == ano.getAction() || TypeAction.ABANDONNER == ano.getAction())
        {
            if (controlRTC.testSiAnomalieClose(ano.getNumeroAnomalie()))
            {
                Row row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.WHITE);
                controlMail.addInfo(TypeInfoMail.ANOABANDON, ano.getLot(), String.valueOf(ano.getNumeroAnomalie()));
                return false;
            }
            else
            {
                LOGGER.warn("L'anomalie " + ano.getNumeroAnomalie() + " n'a pas été clôturée. Impossible de la supprimer du fichier de suivi.");
                controlMail.addInfo(TypeInfoMail.ANOABANDONRATE, ano.getLot(), String.valueOf(ano.getNumeroAnomalie()));
                return true;
            }
        }
        // Contrôle si besoin de créer une anomalie Sonar
        else if (TypeAction.CREER == ano.getAction())
        {
            int numeroAno = controlRTC.creerDefect(ano);
            if (numeroAno != 0)
            {
                ano.setAction(null);
                ano.setNumeroAnomalie(numeroAno);
                ano.setDateCreation(today);
                ano.calculTraitee();
                LOGGER.info("Création anomalie " + numeroAno + " pour le lot " + anoLot);
                controlMail.addInfo(TypeInfoMail.ANOSRTCCREES, ano.getLot(), null);
            }
        }
        return true;
    }

    /**
     * Ajoute les anomalies closes à la feuille correspondante. On ne sauvegarde pas les lignes qui n'ont pas données suite à une anomalie Sonar.
     * 
     * @param sheetClose
     * @param anoClose
     */
    private void ajouterAnomaliesCloses(Sheet sheetClose, Map<String, Anomalie> anoClose)
    {
        Row row;
        for (Anomalie ano : anoClose.values())
        {
            if (ano.getNumeroAnomalie() == 0)
                continue;

            row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
            creerLigneSQ(row, ano, IndexedColors.WHITE);
        }
    }

    /**
     * Crée une ligne correspondante à une anomalie dans le fichier Excel
     * 
     * @param row
     * @param ano
     * @param couleur
     */
    private void creerLigneSQ(Row row, Anomalie ano, IndexedColors couleur)
    {
        // 1. Contrôles des entrées
        if (couleur == null || row == null || ano == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls - méthode control.excel.ControlSuivi.creerLigneSQ");

        // 2. Contrôle des données

        // Contrôle Clarity et mise à jour données
        controleClarity(ano);

        // Contrôle chef de service et mise à jour des données
        controleChefDeService(ano);

        // Controle informations RTC
        try
        {
            controleRTC(ano);
        } 
        catch (PermissionDeniedException e)
        {
            LOGGER.error("Problème authorisation accès lot : " + ano.getLot());
            LOGPLANTAGE.error(e);

        } 
        catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur RTC depuis mise à jour anomalie : " + ano.getLot(), e);
        }

        // 3. Création des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle date = wb.createCellStyle();
        date.cloneStyleFrom(centre);
        date.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        // 4. Valorisation des cellules avec les données de l'anomalie

        // Direction
        valoriserCellule(row, colDir, normal, ano.getDirection(), ano.getDirectionComment());

        // Département
        valoriserCellule(row, colDepart, normal, ano.getDepartement(), ano.getDepartementComment());

        // Service
        valoriserCellule(row, colService, normal, ano.getService(), ano.getServiceComment());

        // Responsable service
        valoriserCellule(row, colResp, normal, ano.getResponsableService(), ano.getResponsableServiceComment());

        // code projet Clarity
        valoriserCellule(row, colClarity, normal, ano.getProjetClarity(), ano.getProjetClarityComment());

        // libelle projet
        valoriserCellule(row, colLib, normal, ano.getLibelleProjet(), ano.getLibelleProjetComment());

        // Cpi du lot
        valoriserCellule(row, colCpi, normal, ano.getCpiProjet(), ano.getCpiProjetComment());

        // Edition
        valoriserCellule(row, colEdition, centre, ano.getEdition(), ano.getEditionComment());

        // Numéro du lot
        Cell cell = valoriserCellule(row, colLot, centre, ano.getLot(), ano.getLotComment());
        ajouterLiens(cell, lienslots, ano.getLot().substring(Statics.SBTRINGLOT));

        // Environnement
        valoriserCellule(row, colEnv, centre, ano.getEtatLot(), ano.getEtatLotComment());

        // Numéros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnomalie();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);

            // Rajout de "&id=", car cela fait planter la désérialisation du fichier de paramètres
            ajouterLiens(cell, liensAnos + ano.getProjetRTC().replace(Statics.SPACE, "%20") + Statics.FINLIENSANO, String.valueOf(numeroAno));
        }

        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtat(), ano.getEtatComment());

        // Anomalie de sécurite
        valoriserCellule(row, colSec, centre, ano.getSecurite(), ano.getSecuriteComment());

        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque(), ano.getRemarqueComment());

        // Version composants
        valoriserCellule(row, colVer, centre, ano.getVersion(), ano.getVersionComment());

        // Date création
        valoriserCellule(row, colDateCrea, date, ano.getDateCreation(), ano.getDateCreationComment());

        // Date création
        valoriserCellule(row, colDateDetec, date, ano.getDateDetection(), ano.getDateDetectionComment());

        // Date relance
        valoriserCellule(row, colDateRel, date, ano.getDateRelance(), ano.getDateRelanceComment());

        // Date resolution
        valoriserCellule(row, colDateRes, date, ano.getDateReso(), ano.getDateResoComment());

        // Date mise à jour de l'état de l'anomalie
        valoriserCellule(row, colDateMajEtat, date, ano.getDateMajEtat(), ano.getDateMajEtatComment());

        // Matiere
        valoriserCellule(row, colMatiere, centre, ano.getMatieresString(), ano.getMatieresComment());

        // Projet RTC
        valoriserCellule(row, colProjetRTC, centre, ano.getProjetRTC(), ano.getProjetRTCComment());

        // Action
        valoriserCellule(row, colAction, centre, ano.getAction(), ano.getActionComment());
    }

    /**
     * CRée une ligne pour une anomalie dans les feuilles de version
     * 
     * @param row
     * @param ano
     * @param couleur
     * @param traite
     */
    private void creerLigneVersion(Row row, Anomalie ano, IndexedColors couleur, String traite)
    {
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        valoriserCellule(row, Index.LOTI.ordinal(), centre, ano.getLot(), null);
        valoriserCellule(row, Index.EDITIONI.ordinal(), helper.getStyle(couleur), ano.getEdition(), null);
        valoriserCellule(row, Index.ENVI.ordinal(), centre, ano.getEtatLot().toString(), null);
        valoriserCellule(row, Index.TRAITEI.ordinal(), centre, traite, null);
    }

    /**
     * Crée la ligne de titres de la feuille principale
     * 
     * @param sheet
     */
    private void creerLigneTitres(Sheet sheet)
    {
        // Création de la ligne de titres
        Row titresNew = sheet.createRow(0);

        // On itère sur la ligne de titres
        for (int i = 0; i < titres.getLastCellNum(); i++)
        {
            Cell newCell = titresNew.createCell(i);
            Cell oldCell = titres.getCell(i);

            copierCellule(newCell, oldCell);
        }
    }

    /**
     * Ajoute un liens à une cellule, soit vers Sonar soit vers RTC
     * 
     * @param cell
     * @param baseAdresse
     * @param variable
     */
    private void ajouterLiens(Cell cell, String baseAdresse, String variable)
    {
        if (cell == null || baseAdresse == null || baseAdresse.isEmpty())
            throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent être nulles");

        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFont(font);
        cell.setCellStyle(style);
        link.setAddress(baseAdresse + variable);
        cell.setHyperlink(link);
    }

    /**
     * Ajoute les nouvelles anomalies au fichier Excel
     * 
     * @param sheet
     * @param anoAajouter
     * @param lotsSecurite
     * @param lotsRelease
     * @param matiere
     */
    private void ajouterNouvellesAnos(Sheet sheet, List<Anomalie> anoAajouter, Map<String, Anomalie> mapAnoCloses, Set<String> lotsSecurite, Set<String> lotsRelease,
            Matiere matiere)
    {
        for (Anomalie ano : anoAajouter)
        {
            ano.getMatieres().add(matiere);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(Statics.SECURITEKO);

            // Ajout de la donnée de version
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Ajout de la date de détection à la date du jour
            ano.setDateDetection(LocalDate.now());

            // Ajout ligne à vérifier si l'anomalie était déjà dans les closes et qu'elle n'est ni à l'édition, ni terminée
            if (mapAnoCloses.containsKey(ano.getLot()) && ano.getEtatLot() != EtatLot.EDITION && ano.getEtatLot() != EtatLot.TERMINE)
            {
                Anomalie anoClose = mapAnoCloses.get(ano.getLot());
                ano.setDateDetection(today);
                ano.setDateCreation(anoClose.getDateCreation());
                ano.setDateRelance(anoClose.getDateRelance());
                ano.setRemarque(anoClose.getRemarque());
                ano.setNumeroAnomalie(anoClose.getNumeroAnomalie());
                ano.setAction(TypeAction.VERIFIER);
                creerLigneSQ(row, ano, IndexedColors.GREY_25_PERCENT);
                mapAnoCloses.remove(ano.getLot());
            }
            // Ajoute une ligne orange si la ligne ne provient pas des anomalies closes
            else if (!mapAnoCloses.containsKey(ano.getLot()))
                creerLigneSQ(row, ano, IndexedColors.LIGHT_ORANGE);
        }
    }

    /**
     * Enregistre toutes les anomalies de la feuille des anomalies closes, puis retourne une feuille vide pour les traitements suivants.
     * 
     * @param anoClose
     * @return
     */
    private Sheet saveAnomaliesCloses(Map<String, Anomalie> anoClose)
    {
        // Récupération de la feuille des anos closes.
        Sheet retour = wb.getSheet(AC);
        if (retour != null)
        {
            // Itération sur les lignes sauf la première qui correspond aux titres. Récupération des informations des anomalies
            for (int i = 1; i < retour.getLastRowNum() + 1; i++)
            {
                Row row = retour.getRow(i);
                
                // Protection si la ligne est nulle
                if (row == null)
                    continue;

                Anomalie ano = creerAnodepuisExcel(row);
                anoClose.put(ano.getLot(), ano);
            }
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }
        retour = wb.createSheet(AC);
        creerLigneTitres(retour);
        return retour;
    }

    /**
     * Contrôle si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    private Anomalie controleClarity(Anomalie ano)
    {
        // Récupération infox Clarity depuis fichier Excel
        String anoClarity = ano.getProjetClarity();
        if (anoClarity.isEmpty())
            return ano;
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

        // Vérification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
            return ano.majDepuisClarity(map.get(anoClarity));

        String temp = EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on itère sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux dernières lettres pour les clefs de plus de 6 caractères finissants par 0[1-9]
            if (controleKey(anoClarity, key))
                return ano.majDepuisClarity(entry.getValue());

            // On récupère la clef correxpondante la plus élevée dans le cas des clef commençants par T avec 2 caractères manquants
            if (testT7 && key.contains(anoClarity) && key.compareTo(temp) > 0)
                temp = key;
        }

        if (!temp.isEmpty())
            return ano.majDepuisClarity(map.get(temp));

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        LOGINCONNUE.warn("Code Clarity inconnu : " + anoClarity + " - " + ano.getLot());
        controlMail.addInfo(TypeInfoMail.CLARITYINCONNU, ano.getLot(), anoClarity);
        ano.setDepartement(Statics.INCONNU);
        ano.setService(Statics.INCONNU);
        ano.setDirection(Statics.INCONNUE);
        ano.setResponsableService(Statics.INCONNU);
        return ano;
    }

    /**
     * @param ano
     * @return
     * @throws TeamRepositoryException
     */
    private Anomalie controleRTC(Anomalie ano) throws TeamRepositoryException
    {
        // Controle sur l'état de l'anomalie (projet Clarity, lot et numéro anomalie renseignée
        String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);
        int anoLotInt = Integer.parseInt(anoLot);

        // Controle si le projet RTC est renseigné. Sinon on le récupère depuis Jazz avec le numéro de lot
        if (ano.getProjetRTC().isEmpty())
            ano.setProjetRTC(controlRTC.recupProjetRTCDepuisWiLot(anoLotInt));

        // Mise à jour de l'état de l'anomalie ainsi que les dates de résolution et de création
        if (ano.getNumeroAnomalie() != 0)
        {
            IWorkItem anoRTC = controlRTC.recupWorkItemDepuisId(ano.getNumeroAnomalie());
            String newEtat = controlRTC.recupEtatElement(anoRTC);
            if (!newEtat.equals(ano.getEtat()))
            {
                LOGGER.info("Lot : " + anoLot + " - nouvel etat anomalie : " + newEtat);
                controlMail.addInfo(TypeInfoMail.ANOMAJ, anoLot, newEtat);
                ano.setEtat(newEtat);
            }
            ano.setDateCreation(DateConvert.convert(LocalDate.class, anoRTC.getCreationDate()));
            if (anoRTC.getResolutionDate() != null)
                ano.setDateReso(DateConvert.convert(LocalDate.class, anoRTC.getResolutionDate()));
        }

        // Mise à jour de l'état du lot et de la date de mise à jour
        IWorkItem lotRTC = controlRTC.recupWorkItemDepuisId(anoLotInt);
        EtatLot etatLot = EtatLot.from(controlRTC.recupEtatElement(lotRTC));
        if (ano.getEtatLot() != etatLot)
        {
            LOGGER.info("Lot : " + anoLot + " - nouvel etat Lot : " + etatLot);
            controlMail.addInfo(TypeInfoMail.LOTMAJ, anoLot, etatLot.toString());

            // Si on arrive en VMOA ou que l'on passe à livré à l'édition directement, on met l'anomalie à relancer
            if (etatLot == EtatLot.VMOA || (etatLot != EtatLot.VMOA && etatLot == EtatLot.EDITION))
            {
                ano.setAction(TypeAction.RELANCER);
                controlMail.addInfo(TypeInfoMail.ANOARELANCER, anoLot, null);
            }
            ano.setEtatLot(etatLot);
        }
        ano.setDateMajEtat(controlRTC.recupDatesEtatsLot(lotRTC).get(etatLot));
        return ano;
    }

    /**
     * Met à jour le responsable de service depuis les informations du fichier XML, si le service est renseigné.<br>
     * Remonte un warning si le service n'est pas connu
     * 
     * @param ano
     * @param mapRespService
     */
    private void controleChefDeService(Anomalie ano)
    {
        // Controle définition du service pour l'anomalie
        String anoServ = ano.getService();
        if (anoServ.isEmpty())
            return;

        // Recherche du responsable dans les paramètres et remontée d'info si non trouvé.
        Map<String, RespService> mapRespService = fichiersXML.getMapRespService();
        if (mapRespService.containsKey(anoServ))
            ano.setResponsableService(mapRespService.get(anoServ).getNom());
        else
        {
            LOGINCONNUE.warn("Pas de responsable de service trouvé pour ce service : " + ano.getService());
            controlMail.addInfo(TypeInfoMail.SERVICESSANSRESP, ano.getLot(), ano.getService());
        }
    }

    /**
     * Crée une anomalie depuis les informatiosn du fichier Excel
     * 
     * @param row
     * @return
     */
    private Anomalie creerAnodepuisExcel(Row row)
    {
        Anomalie retour = ModelFactory.getModel(Anomalie.class);
        retour.setDirection(getCellStringValue(row, colDir));
        retour.setDirectionComment(getCellComment(row, colDir));
        retour.setDepartement(getCellStringValue(row, colDepart));
        retour.setDepartementComment(getCellComment(row, colDepart));
        retour.setService(getCellStringValue(row, colService));
        retour.setServiceComment(getCellComment(row, colService));
        retour.setResponsableService(getCellStringValue(row, colResp));
        retour.setResponsableServiceComment(getCellComment(row, colResp));
        retour.setProjetClarity(getCellStringValue(row, colClarity));
        retour.setProjetClarityComment(getCellComment(row, colClarity));
        retour.setLibelleProjet(getCellStringValue(row, colLib));
        retour.setLibelleProjetComment(getCellComment(row, colLib));
        retour.setCpiProjet(getCellStringValue(row, colCpi));
        retour.setCpiProjetComment(getCellComment(row, colCpi));
        retour.setEdition(getCellStringValue(row, colEdition));
        retour.setEditionComment(getCellComment(row, colEdition));
        retour.setLot(getCellStringValue(row, colLot));
        retour.setLotComment(getCellComment(row, colLot));
        retour.setEtatLot(EtatLot.from(getCellStringValue(row, colEnv)));
        retour.setEtatLotComment(getCellComment(row, colEnv));
        retour.setNumeroAnomalie(getCellNumericValue(row, colAno));
        retour.setNumeroAnomalieComment(getCellComment(row, colAno));
        retour.setEtat(getCellStringValue(row, colEtat));
        retour.setEtatComment(getCellComment(row, colEtat));
        retour.setSecurite(getCellStringValue(row, colSec));
        retour.setSecuriteComment(getCellComment(row, colSec));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setRemarqueComment(getCellComment(row, colRemarque));
        retour.setVersion(getCellStringValue(row, colVer));
        retour.setVersionComment(getCellComment(row, colVer));
        retour.setDateCreation(getCellDateValue(row, colDateCrea));
        retour.setDateCreationComment(getCellComment(row, colDateCrea));
        retour.setDateDetection(getCellDateValue(row, colDateDetec));
        retour.setDateDetectionComment(getCellComment(row, colDateDetec));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setDateRelanceComment(getCellComment(row, colDateRel));
        retour.setDateReso(getCellDateValue(row, colDateRes));
        retour.setDateResoComment(getCellComment(row, colDateRes));
        retour.setDateMajEtat(getCellDateValue(row, colDateMajEtat));
        retour.setDateMajEtatComment(getCellComment(row, colDateMajEtat));
        retour.setMatieresString(getCellStringValue(row, colMatiere));
        retour.setMatieresComment(getCellComment(row, colMatiere));
        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.setActionComment(getCellComment(row, colAction));
        retour.setProjetRTC(getCellStringValue(row, colProjetRTC));
        retour.setProjetRTCComment(getCellComment(row, colProjetRTC));
        retour.calculTraitee();
        return retour;
    }

    /**
     * Contrôle les valeurs du code Clarity en prenant en compte les différentes erreurs possibles
     * 
     * @param anoClarity
     * @param key
     * @return
     */
    private boolean controleKey(String anoClarity, String key)
    {
        // Retourne une clef avec 6 caractères maximum si celle-ci finie par un numéro de lot
        String smallkey = key.length() > CLARITYMINI && key.matches(".*0[0-9E]$") ? key.substring(0, CLARITYMINI + 1) : key;

        // Contrôle si la clef est de type T* ou P*.
        String newKey = key.length() == CLARITYMAX && (key.startsWith("T") || key.startsWith("P")) ? key.substring(0, CLARITYMAX - 1) : smallkey;

        // Retourne la clef clairity de l'anomalie avec 6 caractères maximum si celle-ci finie par un numéro de lot
        String smallClarity = anoClarity.length() > CLARITYMINI && anoClarity.matches(".*0[0-9E]$") ? anoClarity.substring(0, CLARITYMINI + 1) : anoClarity;

        // Contrôle si la clef est de type T* ou P*.
        String newClarity = anoClarity.length() == CLARITYMAX && (anoClarity.startsWith("T") 
                || anoClarity.startsWith("P")) ? anoClarity.substring(0, CLARITYMAX - 1) : smallClarity;

        // remplace le dernier du coade Clarity par 0.
        String lastClarity = anoClarity.replace(anoClarity.charAt(anoClarity.length() - 1), '0');

        return anoClarity.equalsIgnoreCase(newKey) || newClarity.equalsIgnoreCase(key) || lastClarity.equalsIgnoreCase(key);
    }

    @Override
    protected Sheet initSheet()
    {
        // Récupération de la feuille principale
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Le fichier n'a pas de page Suivi Qualité");
        return sheet;
    }

    /**
     * Initialisation liste des contraintes depuis les paramètres
     */
    private void initContraintes()
    {
        contraintes = new String[TypeAction.values().length];
        for (int i = 0; i < contraintes.length; i++)
        {
            contraintes[i] = TypeAction.values()[i].toString();
        }
    }

    /**
     * Ajoute les contrôles de validation de la colonne Action de la feuille
     * 
     * @param sheet
     */
    private void ajouterDataValidation(Sheet sheet)
    {
        // Protection pour les veuilles qui ne sont pas des .xlsx
        XSSFSheet xssfSheet = null;
        if (sheet instanceof XSSFSheet)
            xssfSheet = (XSSFSheet) sheet;
        else
            return;

        XSSFDataValidationConstraint dvContraintes = (XSSFDataValidationConstraint) xssfSheet.getDataValidationHelper().createExplicitListConstraint(contraintes);
        CellRangeAddressList addressList = new CellRangeAddressList(1, xssfSheet.getLastRowNum(), colAction, colAction);
        XSSFDataValidation dataValidation = (XSSFDataValidation) xssfSheet.getDataValidationHelper().createValidation(dvContraintes, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.createErrorBox("Erreur Action", "Valeur pour l'action interdite");
        dataValidation.setShowErrorBox(true);
        sheet.addValidationData(dataValidation);
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Accesseur du controleur de mails
     * 
     * @return
     */
    public ControlMail getControlMail()
    {
        return controlMail;
    }

    /**
     * Liste des numéros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Grégoire mathon
     * @since 1.0
     */
    private enum Index 
    {
        LOTI("Lot projet RTC"), 
        EDITIONI("Edition"), 
        ENVI("Etat du lot"), 
        TRAITEI("Traitée");

        private String string;

        private Index(String string)
        {
            this.string = string;
        }

        @Override
        public String toString()
        {
            return string;
        }
    }
}
