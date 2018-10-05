package control.excel;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.task.AbstractTask;
import control.word.ControlRapport;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Anomalie;
import model.bdd.ChefService;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.EtatAnoSuivi;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.QG;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeRapport;
import model.enums.TypeVersion;
import model.utilities.ControlModelInfo;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de gestion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlSuivi extends AbstractControlExcelRead<TypeColSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    // Constantes statiques
    private static final String SQ = "SUIVI Qualité";
    private static final String AC = "Anomalies closes";
    private static final String STATS = "Statistiques";

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
    private int colNpc;

    // Controleurs
    private ControlRapport controlRapport;
    private ControlModelInfo controlModelInfo;

    /** contrainte de validitée de la colonne Action */
    protected String[] contraintes;

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuivi(File file)
    {
        super(file);

        // Initialisation des parties constantes des liens
        controlModelInfo = new ControlModelInfo();
        initContraintes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<Anomalie> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();
        Map<String, LotRTC> lotsRTC = DaoFactory.getDao(LotRTC.class).readAllMap();

        // Itération sur chaque ligne pour créer les anomalies
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
     * Récupère les anomalies en corus depuis le fichier Excel
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
     * Récupère les anomalies closes depuis le fichier Excel
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
     * Mets à jour les liste des anomalies avec les anomalies abadonnées
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

        // Récupération des versions en paramètre
        String[] versions = proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONS).split(";");
        for (String version : versions)
        {
            // Création de la feuille de calcul
            Sheet sheet = wb.getSheet(version);
            if (sheet == null)
                continue;

            Iterator<Row> iter = sheet.rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                String lot = Utilities.testLot(getCellStringValue(row, Index.DONNEEI.ordinal()));
                String traite = getCellStringValue(row, Index.TOTALI.ordinal());
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

    public void calculStatistiques(List<Anomalie> anosEnBase)
    {
        // Réinitialisation de la feuille
        Sheet sheet = wb.getSheet(STATS);
        if (sheet != null)
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        sheet = wb.createSheet(STATS);
        creerTitresStats(sheet);

        // Données calculées
        int erreursQG = 0;
        int erreursQGSecu = 0;
        int erreursQGM = 0;
        int erreursQGMSecu = 0;
        int erreursQGT = 0;
        int erreursQGTSecu = 0;

        int anoRTCOuvertes = 0;
        int anoRTCOuvertesSecu = 0;
        int anoRTCOuvertesM = 0;
        int anoRTCOuvertesMSecu = 0;
        int anoRTCOuvertesT = 0;
        int anoRTCOuvertesTSecu = 0;

        int anoRTCResolues = 0;
        int anoRTCResoluesSecu = 0;
        int anoRTCResoluesM = 0;
        int anoRTCResoluesMSecu = 0;
        int anoRTCResoluesT = 0;
        int anoRTCResoluesTSecu = 0;

        // calcul des dates
        LocalDate today = LocalDate.now();
        LocalDate debutMois = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(1).minusDays(1L);
        LocalDate fin = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate debutTrim = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(3).minusDays(1L);

        for (Anomalie ano : anosEnBase)
        {

            erreursQG++;
            if (ano.isSecurite())
                erreursQGSecu++;
            
            if (ano.getDateDetection().isAfter(debutMois) && ano.getDateDetection().isBefore(fin))
            {
                erreursQGM++;
                if (ano.isSecurite())
                    erreursQGMSecu++;
            }
            if (ano.getDateDetection().isAfter(debutTrim) && ano.getDateDetection().isBefore(fin))
            {
                erreursQGT++;
                if (ano.isSecurite())
                    erreursQGTSecu++;
            }

            if (ano.getDateCreation() != null)
            {
                anoRTCOuvertes++;
                if (ano.isSecurite())
                    anoRTCOuvertesSecu++;
                
                if (ano.getDateCreation().isAfter(debutMois) && ano.getDateCreation().isBefore(fin))
                {
                    anoRTCOuvertesM++;
                    if (ano.isSecurite())
                        anoRTCOuvertesSecu++;
                }
                
                if (ano.getDateCreation().isAfter(debutTrim) && ano.getDateCreation().isBefore(fin))
                {
                    anoRTCOuvertesT++;
                    if (ano.isSecurite())
                        anoRTCOuvertesTSecu++;
                }
            }

            if (ano.getDateReso() != null)
            {
                anoRTCResolues++;
                if (ano.isSecurite())
                    anoRTCResoluesSecu++;
                if (ano.getDateReso().isAfter(debutMois) && ano.getDateReso().isBefore(fin))
                {
                    anoRTCResoluesM++;
                    if (ano.isSecurite())
                        anoRTCResoluesMSecu++;
                }
                if (ano.getDateReso().isAfter(debutTrim) && ano.getDateReso().isBefore(fin))
                {
                    anoRTCResoluesT++;
                    if (ano.isSecurite())
                        anoRTCResoluesTSecu++;
                }
            }
        }

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneVersion(row, "Erreur SonarQube detectées", erreursQGM, erreursQGMSecu, erreursQGT, erreursQGTSecu, erreursQG, erreursQGSecu);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneVersion(row, "anomalies RTC créées", anoRTCOuvertesM, anoRTCOuvertesMSecu, anoRTCOuvertesT, anoRTCOuvertesTSecu, anoRTCOuvertes, anoRTCOuvertesSecu);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneVersion(row, "anomalies RTC résolues", anoRTCResoluesM, anoRTCResoluesMSecu, anoRTCResoluesT, anoRTCResoluesTSecu, anoRTCResolues, anoRTCResoluesSecu);

        autosizeColumns(sheet);
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
            FileOutputStream output = new FileOutputStream(
                    new StringBuilder(proprietesXML.getMapParams().get(Param.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString());
            wb.write(output);
            output.close();
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
     * @param anosATraiter
     * @param anoAajouter
     * @param lotsEnErreurSonar
     * @param lotsSecurite
     * @param lotsRelease
     * @param sheet
     * @param matiere
     * @throws IOException
     */
    public void majFeuillePrincipale(List<Anomalie> anosATraiter, Sheet sheet, Matiere matiere)
    {
        // Rangement anomalies par date de détection
        Collections.sort(anosATraiter, (o1, o2) -> o1.getDateDetection().compareTo(o2.getDateDetection()));

        // Mise à jour anomalies déjà créées
        for (Anomalie ano : anosATraiter)
        {
            // Quitte le traitment si l'anomalie n'est pas à remonter dans le fichier
            if (testAnoOK(ano))
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(ano);

            // Création de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneSQ(row, ano, couleur);
        }

        if (sheet.getLastRowNum() > 0)
            ajouterDataValidation(sheet);
        autosizeColumns(sheet);

        wb.setActiveSheet(wb.getSheetIndex(sheet));
        controlRapport.creerFichier();
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Sheet initSheet()
    {
        // Récupération de la feuille principale
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Le fichier n'a pas de page Suivi Qualité");
        return sheet;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean testAnoOK(Anomalie ano)
    {
        // Données
        EtatAnoSuivi etatAno = ano.getEtatAnoSuivi();
        LotRTC lotRTC = ano.getLotRTC();
        EtatLot etatLot = lotRTC.getEtatLot();
        QG qg = lotRTC.getQualityGate();

        // On considère les anomalies abandonnées comme bonnes
        if (etatAno == EtatAnoSuivi.ABANDONNEE)
            return true;

        // On ne reprend pas les anomalies closes avec un lot terminé ou livré à l'édition
        if ((etatLot == EtatLot.EDITION || etatLot == EtatLot.TERMINE) && etatAno == EtatAnoSuivi.CLOSE)
            return true;

        // Si l'anomalie est close mais que le QG est toujours en erreur, on met l'anomalie à vérifier et nouvelle
        if (etatAno == EtatAnoSuivi.CLOSE && qg == QG.ERROR)
        {
            ano.setAction(TypeAction.VERIFIER);
            ano.setDateDetection(LocalDate.now());
            ano.setEtatAnoSuivi(EtatAnoSuivi.NOUVELLE);
            return false;
        }

        return etatAno == EtatAnoSuivi.CLOSE;
    }

    /**
     * 
     * @param feuille
     * @param lotsRTC
     * @param task
     * @return
     */
    private List<Anomalie> recupListeAnomaliesDepuisFeuille(String feuille, Map<String, LotRTC> lotsRTC, AbstractTask task)
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(feuille);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();

        int size = sheet.getLastRowNum();

        // Itération sur chaque ligne pour créer les anomalies
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
     * Crée la ligne de titre des statistiques
     * 
     * @param sheet
     */
    private void creerTitresStats(Sheet sheet)
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
                case DONNEEI:
                    cell.setCellValue(Index.DONNEEI.getValeur());
                    break;

                case MENSUELI:
                    cell.setCellValue(Index.MENSUELI.getValeur());
                    break;

                case TRIMI:
                    cell.setCellValue(Index.TRIMI.getValeur());
                    break;

                case TOTALI:
                    cell.setCellValue(Index.TOTALI.getValeur());
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
    private IndexedColors calculCouleurLigne(Anomalie ano)
    {
        IndexedColors couleur;

        // Mise en vert des anomalies avec un Quality Gate bon
        if (ano.getLotRTC().getQualityGate() != QG.ERROR)
            couleur = IndexedColors.LIGHT_GREEN;

        // Les lots qui ont besoin juste d'un réassemblage sont en bleu
        else if (TypeAction.ASSEMBLER == ano.getAction())
            couleur = IndexedColors.LIGHT_TURQUOISE;

        // Le reste est en blanc
        else
            couleur = IndexedColors.WHITE;

        // Les lots déjà traités une première fois sont en gris
        if (TypeAction.VERIFIER == ano.getAction())
            couleur = IndexedColors.GREY_25_PERCENT;

        // Remise de la couleur à orange si le lot n'a pas encore été traité
        if (!ano.isTraitee())
            couleur = IndexedColors.LIGHT_ORANGE;

        return couleur;
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

        // 2. Données de l'anomalie
        LotRTC lotRTC = ano.getLotRTC();
        ProjetClarity projetClarity = lotRTC.getProjetClarity();
        if (projetClarity == null)
            projetClarity = ProjetClarity.getProjetClarityInconnu(Statics.INCONNU);
        ChefService chefService = projetClarity.getChefService();
        if (chefService == null)
            chefService = ChefService.getChefServiceInconnu(Statics.INCONNU);

        // 3. Création des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle date = wb.createCellStyle();
        date.cloneStyleFrom(centre);
        date.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        // 4. Valorisation des cellules avec les données de l'anomalie

        // Direction
        valoriserCellule(row, colDir, normal, projetClarity.getDirection());

        // Département
        valoriserCellule(row, colDepart, normal, projetClarity.getDepartement());

        // Service
        valoriserCellule(row, colService, normal, projetClarity.getService());

        // Responsable service
        valoriserCellule(row, colResp, normal, chefService.getNom());

        // code projet Clarity
        valoriserCellule(row, colClarity, normal, projetClarity.getCode());

        // libelle projet
        valoriserCellule(row, colLib, normal, lotRTC.getLibelle());

        // Cpi du lot
        valoriserCellule(row, colCpi, normal, lotRTC.getCpiProjet());

        // Edition
        valoriserCellule(row, colEdition, centre, lotRTC.getEdition());

        // Numéro du lot
        Cell cell = valoriserCellule(row, colLot, centre, lotRTC.getLot());
        ajouterLiens(cell, ano.getLiensLot());

        // Environnement
        valoriserCellule(row, colEnv, centre, lotRTC.getEtatLot());

        // Numéros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnoRTC();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);

            // Rajout de "&id=", car cela fait planter la désérialisation du fichier de paramètres
            ajouterLiens(cell, ano.getLiensAno());
        }

        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtatRTC());

        // Anomalie de sécurite
        if (ano.isSecurite())
            valoriserCellule(row, colSec, centre, Statics.X);
        else
            valoriserCellule(row, colSec, centre, Statics.EMPTY);

        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque());

        // Version composants
        valoriserCellule(row, colVer, centre, ano.getTypeVersion());

        // Date création
        valoriserCellule(row, colDateCrea, date, ano.getDateCreation());

        // Date création
        valoriserCellule(row, colDateDetec, date, ano.getDateDetection());

        // Date relance
        valoriserCellule(row, colDateRel, date, ano.getDateRelance());

        // Date resolution
        valoriserCellule(row, colDateRes, date, ano.getDateReso());

        // Date mise à jour de l'état de l'anomalie
        valoriserCellule(row, colDateMajEtat, date, lotRTC.getDateMajEtat());

        // Matiere
        valoriserCellule(row, colMatiere, centre, lotRTC.getMatieresString());

        // Projet RTC
        valoriserCellule(row, colProjetRTC, centre, lotRTC.getProjetRTC());

        // Action
        valoriserCellule(row, colAction, centre, ano.getAction());

        // Groupe composant
        valoriserCellule(row, colNpc, centre, lotRTC.getGroupe().getValeur());
    }

    /**
     * CRée une ligne pour une anomalie dans les feuilles de version
     * 
     * @param row
     * @param totelSecu 
     * @param trimSecu 
     * @param menseulSecu 
     * @param ano
     * @param couleur
     * @param traite
     */
    private void creerLigneVersion(Row row, String stat, int mensuel, int menseulSecu, int trim, int trimSecu, int total, int totelSecu)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.BAS, HorizontalAlignment.CENTER);
        
        final String DONT = " dont ";
        final String SECURITE = " de sécurité";

        valoriserCellule(row, Index.DONNEEI.ordinal(), centre, stat);
        valoriserCellule(row, Index.MENSUELI.ordinal(), centre, mensuel + DONT + menseulSecu + SECURITE);
        valoriserCellule(row, Index.TRIMI.ordinal(), centre, trim + DONT + trimSecu + SECURITE);
        valoriserCellule(row, Index.TOTALI.ordinal(), centre, total + DONT + totelSecu + SECURITE);
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
    private void ajouterLiens(Cell cell, String adresse)
    {
        if (cell == null || adresse == null)
            throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent être nulles");

        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFont(font);
        cell.setCellStyle(style);
        link.setAddress(adresse);
        cell.setHyperlink(link);
    }

    /**
     * Crée une anomalie depuis les informations du fichier Excel
     * 
     * @param row
     * @param lotsRTC
     * @return
     */
    private Anomalie creerAnodepuisExcel(Row row, Map<String, LotRTC> lotsRTC)
    {
        Anomalie retour = ModelFactory.getModel(Anomalie.class);
        retour.setLotRTC(lotsRTC.get(getCellStringValue(row, colLot)));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.calculTraitee();
        return retour;
    }

    /**
     * Crée une anomalie depuis les informations du fichier Excel avec plus d'informations
     * 
     * @param row
     * @param lotsRTC
     * @param matiere
     * @return
     */
    private Anomalie creerAnodepuisExcelFull(Row row, Map<String, LotRTC> lotsRTC)
    {
        Anomalie retour = ModelFactory.getModel(Anomalie.class);
        retour.setLotRTC(lotsRTC.get(Utilities.testLot(getCellStringValue(row, colLot))));
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

    /**
     * Initialisation liste des contraintes depuis les paramètres
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
    public ControlRapport getControlRapport()
    {
        return controlRapport;
    }

    public ControlRapport createControlRapport(TypeRapport type)
    {
        controlRapport = new ControlRapport(type);
        return controlRapport;
    }

    /*---------- CLASSE PRIVEE ----------*/

    /**
     * Liste des numéros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Grégoire mathon
     * @since 1.0
     */
    // @formatter:off
    private enum Index 
    {
        DONNEEI(""), 
        MENSUELI("Mensuel"), 
        TRIMI("Trimestrielle"), 
        TOTALI("Total");

        private String valeur;

        private Index(String valeur)
        {
            this.valeur = valeur;
        }

        public String getValeur()
        {
            return valeur;
        }
    }
}
