package control.excel;

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

import control.rtc.ControlRTC;
import control.task.AbstractTask;
import control.word.ControlRapport;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Anomalie;
import model.bdd.LotRTC;
import model.enums.EtatAnoSuivi;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import model.enums.TypeVersion;
import model.utilities.ControlModelInfo;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de gestion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class ControlSuivi extends AbstractControlExcelRead<TypeColSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger g�n�ral */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    // Constantes statiques
    private static final String SQ = "SUIVI Qualit�";
    private static final String AC = "Anomalies closes";

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
    private int colNpc;

    // Liens vers Sonar et RTC
    private String lienslots;
    private String liensAnos;

    // Date du jour
    private final LocalDate today = LocalDate.now();

    // Controleurs
    private ControlRapport controlRapport;
    private ControlRTC controlRTC;
    private ControlModelInfo controlModelInfo;

    /** contrainte de validit�e de la colonne Action */
    protected String[] contraintes;

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuivi(File file)
    {
        super(file);

        // Initialisation des parties constantes des liens
        Map<Param, String> proprietes = proprietesXML.getMapParams();
        lienslots = proprietes.get(Param.LIENSLOTS);
        liensAnos = proprietes.get(Param.LIENSANOS);
        controlRTC = ControlRTC.INSTANCE;
        controlModelInfo = new ControlModelInfo();
        initContraintes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<Anomalie> recupDonneesDepuisExcel()
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();
        Map<String, LotRTC> lotsRTC = DaoFactory.getDao(LotRTC.class).readAllMap();

        // It�ration sur chaque ligne pour cr�er les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // protection pour les lignes vides
            if (row == null)
                continue;

            retour.add(creerAnodepuisExcel(row, lotsRTC));
        }
        return retour;
    }

    /**
     * R�cup�re les anomalies en corus depuis le fichier Excel
     * 
     * @param matiere
     * @param lotsRTC 
     * @param task 
     * @return
     */
    public List<Anomalie> recupAnoEnCoursDepuisExcel(Map<String, LotRTC> lotsRTC, AbstractTask task)
    {
        return recupListeAnomaliesDepuisFeuille(SQ, lotsRTC, task);
    }

    /**
     * R�cup�re les anomalies closes depuis le fichier Excel
     * 
     * @param matiere
     * @return
     */
    public List<Anomalie> recupAnoClosesDepuisExcel(Map<String, LotRTC> lotsRTC, AbstractTask task)
    {
        List<Anomalie> retour = recupListeAnomaliesDepuisFeuille(AC, lotsRTC, task);
        for (Anomalie ano : retour)
        {
            ano.setEtatAnoSuivi(EtatAnoSuivi.CLOSE);
        }
        return retour;
    }

    /**
     * Mets � jour les liste des anomalies avec les anomalies abadonn�es
     * 
     * @param liste
     */
    public Collection<Anomalie> controlAnoAbandon(Iterable<Anomalie> liste, Map<String, LotRTC> lotsRTC)
    {
        Map<String, Anomalie> map = new HashMap<>();
        
        for (Anomalie ano : liste)
        {
            map.put(ano.getLotRTC().getLot(), ano);
        }
        
        // R�cup�ration des versions en param�tre
        String[] versions = proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONS).split(";");
        for (String version : versions)
        {
            // Cr�ation de la feuille de calcul
            Sheet sheet = wb.getSheet(version);
            if (sheet == null)
                continue;

            Iterator<Row> iter = sheet.rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                String lot = getCellStringValue(row, Index.LOTI.ordinal()).substring(Statics.SBTRINGLOT);
                String traite = getCellStringValue(row, Index.TRAITEI.ordinal());
                if ("A".equals(traite) && map.containsKey(lot))
                    map.get(lot).setEtatAnoSuivi(EtatAnoSuivi.ABANDONNEE);
                else if ("A".equals(traite))
                {
                    Anomalie ano = ModelFactory.getModel(Anomalie.class);
                    ano.setEtatAnoSuivi(EtatAnoSuivi.ABANDONNEE);
                    ano.setDateDetection(LocalDate.of(2016, 1, 1));
                    ano.setLotRTC(lotsRTC.get(lot));
                    map.put(ano.getLotRTC().getLot(), ano);
                }
            }
        }

        return map.values();
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
            if (lotsAbandon.contains(ano.getLotRTC().getLot()))
                creerLigneVersion(row, ano, IndexedColors.WHITE, "A");
            else
            {
                creerLigneVersion(row, ano, IndexedColors.LIGHT_YELLOW, "N");
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
            FileOutputStream output = new FileOutputStream(
                    new StringBuilder(proprietesXML.getMapParams().get(Param.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString());
            wb.write(output);
            output.close();
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
     * @param anoEnBase
     * @param anoAajouter
     * @param lotsEnErreurSonar
     * @param lotsSecurite
     * @param lotsRelease
     * @param sheet
     * @param matiere
     * @throws IOException
     */
    public void majFeuillePrincipale(Iterable<Anomalie> anoEnBase, Sheet sheet, Matiere matiere)
    {
        // Mise � jour anomalies d�j� cr��es
        for (Anomalie ano : anoEnBase)
        {
            String anoLot = ano.getLotRTC().getLot();

            // Gestion des actions demand�es sur l'anomalie et quitte le traitment en cas d'action de fin (abandon ou cl�ture)
            if (!gestionAction(ano, anoLot))
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(ano);

            // Cr�ation de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneSQ(row, ano, couleur);
        }

        ajouterNouvellesAnos(sheet, matiere);

        if (sheet.getLastRowNum() > 0)
            ajouterDataValidation(sheet);
        autosizeColumns(sheet);
        if (sheetClose.getLastRowNum() > 0)
            ajouterDataValidation(sheetClose);
        autosizeColumns(sheetClose);

        wb.setActiveSheet(wb.getSheetIndex(sheet));
        controlRapport.creerFichier();
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
    }

    /*---------- METHODES PRIVEES ----------*/

    private List<Anomalie> recupListeAnomaliesDepuisFeuille(String feuille, Map<String, LotRTC> lotsRTC, AbstractTask task)
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(feuille);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();
        
        int size = sheet.getLastRowNum();

        // It�ration sur chaque ligne pour cr�er les anomalies
        for (int i = 1; i <= size; i++)
        {
            Row row = sheet.getRow(i);

            // protection pour les lignes vides
            if (row == null)
                continue;
            
            task.updateProgress(i, size);

            retour.add(creerAnodepuisExcelFull(row, lotsRTC));
        }
        return retour;
    }

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
    private IndexedColors calculCouleurLigne(Anomalie ano)
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
    private boolean gestionAction(Anomalie ano, String anoLot)
    {       
        
        // Modification de l'�tat de l'anomalie delon l'action demand�e
        if (TypeAction.CLOTURER == ano.getAction() || TypeAction.ABANDONNER == ano.getAction())
        {
            if (controlRTC.testSiAnomalieClose(ano.getNumeroAnoRTC()))
            {
                controlRapport.addInfo(TypeInfo.ANOABANDON, ano.getLotRTC().getLot(), String.valueOf(ano.getNumeroAnoRTC()));
                if (TypeAction.CLOTURER == ano.getAction())
                    ano.setEtatAnoSuivi(EtatAnoSuivi.CLOSE);
                else
                    ano.setEtatAnoSuivi(EtatAnoSuivi.ABANDONNEE);
                return false;
            }
            else
            {
                LOGGER.warn("L'anomalie " + ano.getNumeroAnoRTC() + " n'a pas �t� cl�tur�e. Impossible de la supprimer du fichier de suivi.");
                controlRapport.addInfo(TypeInfo.ANOABANDONRATE, ano.getLotRTC().getLot(), String.valueOf(ano.getNumeroAnoRTC()));
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
                ano.setNumeroAnoRTC(numeroAno);
                ano.setDateCreation(today);
                ano.calculTraitee();
                LOGGER.info("Cr�ation anomalie " + numeroAno + " pour le lot " + anoLot);
                controlRapport.addInfo(TypeInfo.ANOSRTCCREES, ano.getLotRTC().getLot(), null);
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
            if (ano.getNumeroAnoRTC() == 0)
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
        controlModelInfo.controleClarity(ano, controlRapport);

        // Contr�le chef de service et mise � jour des donn�es
        controlModelInfo.controleChefDeService(ano, controlRapport);

        // Contr�le si le projet est un projet NPC
        controlModelInfo.controleNPC(ano);

        // Controle informations RTC
        try
        {
            controlModelInfo.controleRTC(ano, controlRapport, controlRTC);
        }
        catch (PermissionDeniedException e)
        {
            LOGGER.error("Probl�me authorisation acc�s lot : " + ano.getLotRTC());
            LOGPLANTAGE.error(e);

        }
        catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur RTC depuis mise � jour anomalie : " + ano.getLotRTC(), e);
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
        valoriserCellule(row, colDir, normal, ano.getLotRTC().getProjetClarity().getDirection());

        // D�partement
        valoriserCellule(row, colDepart, normal, ano.getLotRTC().getProjetClarity().getDepartement());

        // Service
        valoriserCellule(row, colService, normal, ano.getLotRTC().getProjetClarity().getService());

        // Responsable service
        valoriserCellule(row, colResp, normal, ano.getLotRTC().getProjetClarity().getChefService());

        // code projet Clarity
        valoriserCellule(row, colClarity, normal, ano.getLotRTC().getProjetClarity().getCode());

        // libelle projet
        valoriserCellule(row, colLib, normal, ano.getLotRTC().getLibelle());

        // Cpi du lot
        valoriserCellule(row, colCpi, normal, ano.getLotRTC().getCpiProjet());

        // Edition
        valoriserCellule(row, colEdition, centre, ano.getLotRTC().getEdition());

        // Num�ro du lot
        Cell cell = valoriserCellule(row, colLot, centre, ano.getLotRTC().getLot());
        ajouterLiens(cell, lienslots, ano.getLotRTC().getLot());

        // Environnement
        valoriserCellule(row, colEnv, centre, ano.getLotRTC().getEtatLot());

        // Num�ros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnoRTC();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);

            // Rajout de "&id=", car cela fait planter la d�s�rialisation du fichier de param�tres
            ajouterLiens(cell, liensAnos + ano.getLotRTC().getProjetRTC().replace(Statics.SPACE, "%20") + Statics.FINLIENSANO, String.valueOf(numeroAno));
        }

        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtatRTC());

        // Anomalie de s�curite
        valoriserCellule(row, colSec, centre, ano.isSecurite());

        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque());

        // Version composants
        valoriserCellule(row, colVer, centre, ano.getTypeVersion());

        // Date cr�ation
        valoriserCellule(row, colDateCrea, date, ano.getDateCreation());

        // Date cr�ation
        valoriserCellule(row, colDateDetec, date, ano.getDateDetection());

        // Date relance
        valoriserCellule(row, colDateRel, date, ano.getDateRelance());

        // Date resolution
        valoriserCellule(row, colDateRes, date, ano.getDateReso());

        // Date mise � jour de l'�tat de l'anomalie
        valoriserCellule(row, colDateMajEtat, date, ano.getLotRTC().getDateMajEtat());

        // Matiere
        valoriserCellule(row, colMatiere, centre, ano.getMatieresString());

        // Projet RTC
        valoriserCellule(row, colProjetRTC, centre, ano.getLotRTC().getProjetRTC());

        // Action
        valoriserCellule(row, colAction, centre, ano.getAction());

        // Groupe composant
        valoriserCellule(row, colNpc, centre, ano.getGroupe());
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

        valoriserCellule(row, Index.LOTI.ordinal(), centre, ano.getLotRTC());
        valoriserCellule(row, Index.EDITIONI.ordinal(), helper.getStyle(couleur), ano.getLotRTC().getEdition());
        valoriserCellule(row, Index.ENVI.ordinal(), centre, ano.getLotRTC().getEtatLot().getValeur());
        valoriserCellule(row, Index.TRAITEI.ordinal(), centre, traite);
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
    private void ajouterNouvellesAnos(Sheet sheet, Map<String, Anomalie> mapAnoCloses, Matiere matiere)
    {
        for (Anomalie ano : anoAajouter)
        {
            ano.getMatieres().add(matiere);

            // Ajout de la date de d�tection � la date du jour
            ano.setDateDetection(LocalDate.now());

            // Ajout ligne � v�rifier si l'anomalie �tait d�j� dans les closes et qu'elle n'est ni � l'�dition, ni termin�e
            if (mapAnoCloses.containsKey(ano.getLotRTC().getLot()) && ano.getLotRTC().getEtatLot() != EtatLot.EDITION && ano.getLotRTC().getEtatLot() != EtatLot.TERMINE)
            {
                Anomalie anoClose = mapAnoCloses.get(ano.getLotRTC().getLot());
                ano.setDateDetection(today);
                ano.setDateCreation(anoClose.getDateCreation());
                ano.setDateRelance(anoClose.getDateRelance());
                ano.setRemarque(anoClose.getRemarque());
                ano.setNumeroAnoRTC(anoClose.getNumeroAnoRTC());
                ano.setAction(TypeAction.VERIFIER);
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.GREY_25_PERCENT);
                mapAnoCloses.remove(ano.getLotRTC().getLot());
            }
            // Ajoute une ligne orange si la ligne ne provient pas des anomalies closes
            else if (!mapAnoCloses.containsKey(ano.getLotRTC().getLot()))
            {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.LIGHT_ORANGE);
                controlRapport.addInfo(TypeInfo.ANONEW, ano.getLotRTC().getLot(), null);
            }
        }
    }

    /**
     * Cr�e une anomalie depuis les informations du fichier Excel
     * 
     * @param row
     * @param lotsRTC
     * @return
     */
    private Anomalie creerAnodepuisExcel(Row row, Map<String, LotRTC> lotsRTC)
    {
        Anomalie retour = ModelFactory.getModel(Anomalie.class);
        retour.setLotRTC(lotsRTC.get(getCellStringValue(row, colLot).substring(Statics.SBTRINGLOT)));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.calculTraitee();
        return retour;
    }

    /**
     * Cr�e une anomalie depuis les informations du fichier Excel avec plus d'informations
     * 
     * @param row
     * @param lotsRTC
     * @param matiere
     * @return
     */
    private Anomalie creerAnodepuisExcelFull(Row row, Map<String, LotRTC> lotsRTC)
    {
        Anomalie retour = ModelFactory.getModel(Anomalie.class);
        retour.setLotRTC(lotsRTC.get(getCellStringValue(row, colLot).substring(Statics.SBTRINGLOT)));
        retour.setNumeroAnoRTC(getCellNumericValue(row, colAno));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.setDateDetection(getCellNotNullDateValue(row, colDateDetec));
        retour.setDateCreation(getCellDateValue(row, colDateCrea));
        retour.setDateReso(getCellDateValue(row, colDateRes));
        if (Statics.X.equals(getCellStringValue(row, colSec)))
            retour.setSecurite(true);
        if (TypeVersion.SNAPSHOT.toString().equals(getCellStringValue(row, colVer)))
            retour.setTypeVersion(TypeVersion.SNAPSHOT);
        else
            retour.setTypeVersion(TypeVersion.RELEASE);
        try
        {
            controlModelInfo.controleAnoRTC(retour);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
        }
        retour.setMatieresString(getCellStringValue(row, colMatiere));
        retour.calculTraitee();
        return retour;
    }

    private LocalDate getCellNotNullDateValue(Row row, int col)
    {
        LocalDate date = getCellDateValue(row, col);
        if (date != null)
            return getCellDateValue(row, colDateDetec);
        else
            return LocalDate.of(2016, 1, 1);
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
            contraintes[i] = TypeAction.values()[i].getValeur();
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
    public ControlRapport getControlRapport()
    {
        return controlRapport;
    }

    public void createControlRapport(TypeRapport type)
    {
        controlRapport = new ControlRapport(type);
    }

    /**
     * Liste des num�ros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Gr�goire mathon
     * @since 1.0
     */
    private enum Index {
        LOTI("Lot projet RTC"), EDITIONI("Edition"), ENVI("Etat du lot"), TRAITEI("Trait�e");

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
