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
 * @author ETP137 - Gr�goire Mathon
 */
public class ControlSuivi extends AbstractControlExcelRead<TypeColSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    // Les noms des champs doivent correspondre aux valeurs dans l'�num�ration TypeCol
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
    private static final String SQ = "SUIVI Qualit�";
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

    /** contrainte de validit�e de la colonne Action */
    protected String[] contraintes;

    /** logger g�n�ral */
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
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();

        // It�ration sur chaque ligne pour cr�er les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            
            // protection si la ligne ets nulle
            if (row == null)
                continue;

            // Cr�ation anomalie si la ligne n'est pas vide : lot vide.
            Anomalie ano = creerAnodepuisExcel(row);
            if (!ano.getLot().isEmpty())
                retour.add(ano);
        }
        return retour;
    }

    /**
     * Permet de cr�er les feuille des anomalies par version et retourne les anomalies � cr�er
     * 
     * @param nomSheet
     * @param anoAcreer
     * @param anoDejacrees
     * @return
     */
    public List<Anomalie> createSheetError(String nomSheet, Iterable<Anomalie> anoAcreer, Iterable<Anomalie> anoDejacrees)
    {
        // 1. Variables

        // Cr�ation de la feuille de calcul
        Sheet sheet = wb.getSheet(nomSheet);

        // Liste retour des anomalies � cr�er
        List<Anomalie> retour = new ArrayList<>();

        // 2. Sauvegarde donn�es existantes. On it�re pour r�cup�rer tous les num�ros de lot d�j� abandon�s, puis suppression de la feuille
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

        // 3. G�n�ration de la nouvelle feuille

        // Recr�ation de la feuille
        sheet = wb.createSheet(nomSheet);
        Row row;

        // Cr�ation des titres
        creerTitresSE(sheet);

        // 4. Ajout anomalies d�j� cr��es
        for (Anomalie ano : anoDejacrees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (TypeAction.ABANDONNER == ano.getAction())
                creerLigneVersion(row, ano, IndexedColors.WHITE, "A");
            else
                creerLigneVersion(row, ano, IndexedColors.WHITE, "O");
        }

        // 5. It�ration sur les anomalies � cr�er. Si elles sont d�j� dans les anomalies abandonn�es, on cr��e une ligne
        // � l'�tat abandon, sinon on cr�e une ligne � l'�tat non trait� et on ajoute celle-ci aux anomalies � cr�er.
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
        // R�cup�ration feuille principale existante
        Sheet retour = wb.getSheet(SQ);
        if (retour != null)
        {

            // Cr�ation du fichier de sauvegarde et effacement de la feuille
            wb.write(new FileOutputStream(
                    new StringBuilder(proprietesXML.getMapParams().get(Param.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString()));
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }

        // Cr�ation des lignes de titres
        retour = wb.createSheet(SQ);
        creerLigneTitres(retour);
        return retour;
    }

    /**
     * Gestion de la feuille principale des anomalies. Maj des anciennes plus cr�ation des nouvelles
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
        // R�cup�ration feuille et liste des anomalies closes
        Map<String, Anomalie> anoClose = new HashMap<>();
        Sheet sheetClose = saveAnomaliesCloses(anoClose);

        // Mise � jour anomalies d�j� cr��es
        for (Anomalie ano : lotsEnAno)
        {
            ano.getMatieres().add(matiere);
            String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);

            // Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(Statics.SECURITEKO);

            // Calcul version SNAPSHOT ou RELEASE
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Gestion des actions demand�es sur l'anomalie et quitte le traitment en cas d'action de fin (abandon ou cl�ture)
            if (!gestionAction(ano, anoLot, sheetClose))
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(ano, lotsEnErreurSonar, anoLot);

            // Cr�ation de la ligne
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
     * Mise � jour des fichiers pour les anomalies comprenant plusieures type de mati�re
     * 
     * @param anoMultiple
     * @throws IOException
     */
    public void majMultiMatiere(Collection<String> anoMultiple)
    {
        Sheet sheet = wb.getSheet(SQ);

        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Probl�me r�cup�ration feuille Excel principale");

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
     * Cr�e la ligne de titre des feuilles par �dition
     * 
     * @param sheet
     */
    private void creerTitresSE(Sheet sheet)
    {
        // Cr�ation du style des titres
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);

        // Cr�ation des noms des colonnes
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
                    throw new TechnicalException("Nouvel index non g�r� : " + index, null);
            }
        }
    }

    /**
     * Permet de calculer la couleur d'une ligne du fichier Excel : <br>
     * - orange = nouvelle aomalie non trait�e<br>
     * - jaune = anomalie avec au moisn un composant en version fig�e<br>
     * - gris = anomalie d�j� trait�e une premi�re fois avec un QG redevenu rouge<br>
     * - vert = lot dont le QG est devenu vert<br>
     * 
     * @param ano
     *            anomalie
     * @param lotsEnErreurSonar
     *            Liste des lots en erreur
     * @param anoLot
     *            num�ro du lot
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
        // Les lots qui ont besoin juste d'un r�assemblage sont en bleu
        else if (TypeAction.ASSEMBLER == ano.getAction())
            couleur = IndexedColors.LIGHT_TURQUOISE;
        // Le reste est en blanc
        else
            couleur = IndexedColors.WHITE;

        // Les lots d�j� trait� une premi�re fois sont en gris
        if (TypeAction.VERIFIER == ano.getAction())
            couleur = IndexedColors.GREY_25_PERCENT;

        // Remise de la couleur � orange si le lot n'a pas encore �t� trait�
        if (!ano.isTraitee())
            couleur = IndexedColors.LIGHT_ORANGE;

        return couleur;
    }

    /**
     * Gestion des actions possibles pour une anomalie (Creer, Cl�turer, Abandonner)
     * 
     * @param ano
     *            Anomalie � traiter
     * @param anoLot
     *            Num�ro de lot l'anomalie
     * @param sheetClose
     *            Feuille des anomalies closes
     * @return {@code true} si l'on doit continuer le traitement<br>
     *         {@code false} si l'anomalie est � cl�turer ou abandonner
     */
    private boolean gestionAction(Anomalie ano, String anoLot, Sheet sheetClose)
    {
        // Si on doit cl�turer ou abandonner l'anomalie, on la recopie dans la feuille des anomalies closes
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
                LOGGER.warn("L'anomalie " + ano.getNumeroAnomalie() + " n'a pas �t� cl�tur�e. Impossible de la supprimer du fichier de suivi.");
                controlMail.addInfo(TypeInfoMail.ANOABANDONRATE, ano.getLot(), String.valueOf(ano.getNumeroAnomalie()));
                return true;
            }
        }
        // Contr�le si besoin de cr�er une anomalie Sonar
        else if (TypeAction.CREER == ano.getAction())
        {
            int numeroAno = controlRTC.creerDefect(ano);
            if (numeroAno != 0)
            {
                ano.setAction(null);
                ano.setNumeroAnomalie(numeroAno);
                ano.setDateCreation(today);
                ano.calculTraitee();
                LOGGER.info("Cr�ation anomalie " + numeroAno + " pour le lot " + anoLot);
                controlMail.addInfo(TypeInfoMail.ANOSRTCCREES, ano.getLot(), null);
            }
        }
        return true;
    }

    /**
     * Ajoute les anomalies closes � la feuille correspondante. On ne sauvegarde pas les lignes qui n'ont pas donn�es suite � une anomalie Sonar.
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
     * Cr�e une ligne correspondante � une anomalie dans le fichier Excel
     * 
     * @param row
     * @param ano
     * @param couleur
     */
    private void creerLigneSQ(Row row, Anomalie ano, IndexedColors couleur)
    {
        // 1. Contr�les des entr�es
        if (couleur == null || row == null || ano == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas �tre nuls - m�thode control.excel.ControlSuivi.creerLigneSQ");

        // 2. Contr�le des donn�es

        // Contr�le Clarity et mise � jour donn�es
        controleClarity(ano);

        // Contr�le chef de service et mise � jour des donn�es
        controleChefDeService(ano);

        // Controle informations RTC
        try
        {
            controleRTC(ano);
        } 
        catch (PermissionDeniedException e)
        {
            LOGGER.error("Probl�me authorisation acc�s lot : " + ano.getLot());
            LOGPLANTAGE.error(e);

        } 
        catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur RTC depuis mise � jour anomalie : " + ano.getLot(), e);
        }

        // 3. Cr�ation des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle date = wb.createCellStyle();
        date.cloneStyleFrom(centre);
        date.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        // 4. Valorisation des cellules avec les donn�es de l'anomalie

        // Direction
        valoriserCellule(row, colDir, normal, ano.getDirection(), ano.getDirectionComment());

        // D�partement
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

        // Num�ro du lot
        Cell cell = valoriserCellule(row, colLot, centre, ano.getLot(), ano.getLotComment());
        ajouterLiens(cell, lienslots, ano.getLot().substring(Statics.SBTRINGLOT));

        // Environnement
        valoriserCellule(row, colEnv, centre, ano.getEtatLot(), ano.getEtatLotComment());

        // Num�ros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnomalie();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);

            // Rajout de "&id=", car cela fait planter la d�s�rialisation du fichier de param�tres
            ajouterLiens(cell, liensAnos + ano.getProjetRTC().replace(Statics.SPACE, "%20") + Statics.FINLIENSANO, String.valueOf(numeroAno));
        }

        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtat(), ano.getEtatComment());

        // Anomalie de s�curite
        valoriserCellule(row, colSec, centre, ano.getSecurite(), ano.getSecuriteComment());

        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque(), ano.getRemarqueComment());

        // Version composants
        valoriserCellule(row, colVer, centre, ano.getVersion(), ano.getVersionComment());

        // Date cr�ation
        valoriserCellule(row, colDateCrea, date, ano.getDateCreation(), ano.getDateCreationComment());

        // Date cr�ation
        valoriserCellule(row, colDateDetec, date, ano.getDateDetection(), ano.getDateDetectionComment());

        // Date relance
        valoriserCellule(row, colDateRel, date, ano.getDateRelance(), ano.getDateRelanceComment());

        // Date resolution
        valoriserCellule(row, colDateRes, date, ano.getDateReso(), ano.getDateResoComment());

        // Date mise � jour de l'�tat de l'anomalie
        valoriserCellule(row, colDateMajEtat, date, ano.getDateMajEtat(), ano.getDateMajEtatComment());

        // Matiere
        valoriserCellule(row, colMatiere, centre, ano.getMatieresString(), ano.getMatieresComment());

        // Projet RTC
        valoriserCellule(row, colProjetRTC, centre, ano.getProjetRTC(), ano.getProjetRTCComment());

        // Action
        valoriserCellule(row, colAction, centre, ano.getAction(), ano.getActionComment());
    }

    /**
     * CR�e une ligne pour une anomalie dans les feuilles de version
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
     * Cr�e la ligne de titres de la feuille principale
     * 
     * @param sheet
     */
    private void creerLigneTitres(Sheet sheet)
    {
        // Cr�ation de la ligne de titres
        Row titresNew = sheet.createRow(0);

        // On it�re sur la ligne de titres
        for (int i = 0; i < titres.getLastCellNum(); i++)
        {
            Cell newCell = titresNew.createCell(i);
            Cell oldCell = titres.getCell(i);

            copierCellule(newCell, oldCell);
        }
    }

    /**
     * Ajoute un liens � une cellule, soit vers Sonar soit vers RTC
     * 
     * @param cell
     * @param baseAdresse
     * @param variable
     */
    private void ajouterLiens(Cell cell, String baseAdresse, String variable)
    {
        if (cell == null || baseAdresse == null || baseAdresse.isEmpty())
            throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent �tre nulles");

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

            // Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(Statics.SECURITEKO);

            // Ajout de la donn�e de version
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Ajout de la date de d�tection � la date du jour
            ano.setDateDetection(LocalDate.now());

            // Ajout ligne � v�rifier si l'anomalie �tait d�j� dans les closes et qu'elle n'est ni � l'�dition, ni termin�e
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
        // R�cup�ration de la feuille des anos closes.
        Sheet retour = wb.getSheet(AC);
        if (retour != null)
        {
            // It�ration sur les lignes sauf la premi�re qui correspond aux titres. R�cup�ration des informations des anomalies
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
     * Contr�le si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    private Anomalie controleClarity(Anomalie ano)
    {
        // R�cup�ration infox Clarity depuis fichier Excel
        String anoClarity = ano.getProjetClarity();
        if (anoClarity.isEmpty())
            return ano;
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

        // V�rification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
            return ano.majDepuisClarity(map.get(anoClarity));

        String temp = EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on it�re sur les clefs en supprimant les indices de lot, et on prend la premi�re clef correspondante
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux derni�res lettres pour les clefs de plus de 6 caract�res finissants par 0[1-9]
            if (controleKey(anoClarity, key))
                return ano.majDepuisClarity(entry.getValue());

            // On r�cup�re la clef correxpondante la plus �lev�e dans le cas des clef commen�ants par T avec 2 caract�res manquants
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
        // Controle sur l'�tat de l'anomalie (projet Clarity, lot et num�ro anomalie renseign�e
        String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);
        int anoLotInt = Integer.parseInt(anoLot);

        // Controle si le projet RTC est renseign�. Sinon on le r�cup�re depuis Jazz avec le num�ro de lot
        if (ano.getProjetRTC().isEmpty())
            ano.setProjetRTC(controlRTC.recupProjetRTCDepuisWiLot(anoLotInt));

        // Mise � jour de l'�tat de l'anomalie ainsi que les dates de r�solution et de cr�ation
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

        // Mise � jour de l'�tat du lot et de la date de mise � jour
        IWorkItem lotRTC = controlRTC.recupWorkItemDepuisId(anoLotInt);
        EtatLot etatLot = EtatLot.from(controlRTC.recupEtatElement(lotRTC));
        if (ano.getEtatLot() != etatLot)
        {
            LOGGER.info("Lot : " + anoLot + " - nouvel etat Lot : " + etatLot);
            controlMail.addInfo(TypeInfoMail.LOTMAJ, anoLot, etatLot.toString());

            // Si on arrive en VMOA ou que l'on passe � livr� � l'�dition directement, on met l'anomalie � relancer
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
     * Met � jour le responsable de service depuis les informations du fichier XML, si le service est renseign�.<br>
     * Remonte un warning si le service n'est pas connu
     * 
     * @param ano
     * @param mapRespService
     */
    private void controleChefDeService(Anomalie ano)
    {
        // Controle d�finition du service pour l'anomalie
        String anoServ = ano.getService();
        if (anoServ.isEmpty())
            return;

        // Recherche du responsable dans les param�tres et remont�e d'info si non trouv�.
        Map<String, RespService> mapRespService = fichiersXML.getMapRespService();
        if (mapRespService.containsKey(anoServ))
            ano.setResponsableService(mapRespService.get(anoServ).getNom());
        else
        {
            LOGINCONNUE.warn("Pas de responsable de service trouv� pour ce service : " + ano.getService());
            controlMail.addInfo(TypeInfoMail.SERVICESSANSRESP, ano.getLot(), ano.getService());
        }
    }

    /**
     * Cr�e une anomalie depuis les informatiosn du fichier Excel
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
     * Contr�le les valeurs du code Clarity en prenant en compte les diff�rentes erreurs possibles
     * 
     * @param anoClarity
     * @param key
     * @return
     */
    private boolean controleKey(String anoClarity, String key)
    {
        // Retourne une clef avec 6 caract�res maximum si celle-ci finie par un num�ro de lot
        String smallkey = key.length() > CLARITYMINI && key.matches(".*0[0-9E]$") ? key.substring(0, CLARITYMINI + 1) : key;

        // Contr�le si la clef est de type T* ou P*.
        String newKey = key.length() == CLARITYMAX && (key.startsWith("T") || key.startsWith("P")) ? key.substring(0, CLARITYMAX - 1) : smallkey;

        // Retourne la clef clairity de l'anomalie avec 6 caract�res maximum si celle-ci finie par un num�ro de lot
        String smallClarity = anoClarity.length() > CLARITYMINI && anoClarity.matches(".*0[0-9E]$") ? anoClarity.substring(0, CLARITYMINI + 1) : anoClarity;

        // Contr�le si la clef est de type T* ou P*.
        String newClarity = anoClarity.length() == CLARITYMAX && (anoClarity.startsWith("T") 
                || anoClarity.startsWith("P")) ? anoClarity.substring(0, CLARITYMAX - 1) : smallClarity;

        // remplace le dernier du coade Clarity par 0.
        String lastClarity = anoClarity.replace(anoClarity.charAt(anoClarity.length() - 1), '0');

        return anoClarity.equalsIgnoreCase(newKey) || newClarity.equalsIgnoreCase(key) || lastClarity.equalsIgnoreCase(key);
    }

    @Override
    protected Sheet initSheet()
    {
        // R�cup�ration de la feuille principale
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Le fichier n'a pas de page Suivi Qualit�");
        return sheet;
    }

    /**
     * Initialisation liste des contraintes depuis les param�tres
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
     * Ajoute les contr�les de validation de la colonne Action de la feuille
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
     * Liste des num�ros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Gr�goire mathon
     * @since 1.0
     */
    private enum Index 
    {
        LOTI("Lot projet RTC"), 
        EDITIONI("Edition"), 
        ENVI("Etat du lot"), 
        TRAITEI("Trait�e");

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
