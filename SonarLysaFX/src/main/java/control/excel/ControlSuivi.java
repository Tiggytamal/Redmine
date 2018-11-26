package control.excel;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import control.word.ControlRapport;
import dao.ListeDao;
import model.ModelFactory;
import model.bdd.ChefService;
import model.bdd.ComposantSonar;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.EtatDefaut;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.QG;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeRapport;
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
public class ControlSuivi extends AbstractControlExcelRead<TypeColSuivi, List<DefautQualite>> implements Contraintes
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String SQ = "SUIVI Défaults Qualité";
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
    private int colProduit;
    private int colDureeAno;
    private int colDateReouv;
    private int colDateMepPrev;

    // Controleurs
    private ControlRapport controlRapport;
    private Map<String, List<ComposantSonar>> mapCompoByLot;

    /** contrainte de validitée de la colonne Action */
    protected String[] contraintes;

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuivi(File file)
    {
        super(file);

        // Initialisation des parties constantes des liens
        contraintes = initContraintes();
        
        mapCompoByLot = new HashMap<>();
        List<ComposantSonar> compos = ListeDao.daoCompo.readAll();
        for (ComposantSonar compo : compos)
        {
            mapCompoByLot.computeIfAbsent(compo.getLotRTC().getLot(), k -> new ArrayList<>()).add(compo);
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<DefautQualite> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<DefautQualite> retour = new ArrayList<>();
        Map<String, LotRTC> lotsRTC = ListeDao.daoLotRTC.readAllMap();

        // Itération sur chaque ligne pour créer les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // protection pour les lignes vides
            if (row == null)
                continue;

            retour.add(creerDqDepuisExcel(row, lotsRTC));
        }
        return retour;
    }

    public void calculStatistiques(Iterable<DefautQualite> dqsEnBase)
    {
        // Réinitialisation de la feuille
        Sheet sheet = wb.getSheet(STATS);
        if (sheet != null)
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        sheet = wb.createSheet(STATS);
        creerTitresStats(sheet);

        // Données calculées
        int[] valeurs = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursM = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursT = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursE = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        // calcul des dates
        LocalDate today = LocalDate.now();
        LocalDate debutMois = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(1).minusDays(1L);
        LocalDate fin = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate debutTrim = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(3).minusDays(1L);

        // Itération sur les anomalies
        for (DefautQualite dq : dqsEnBase)
        {
            calculDonnee(dq, debutMois, debutTrim, fin, valeurs);

            if (dq.getDateCreation() != null)
                calculDonnee(dq, debutMois, debutTrim, fin, valeursM);

            if (dq.getDateReso() != null)
                calculDonnee(dq, debutMois, debutTrim, fin, valeursT);

            if (dq.getDateCreation() != null && dq.getDateReso() == null)
                calculEnCours(dq, today, valeursE);
        }

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "Erreur SonarQube detectées", valeurs);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "anomalies RTC créées", valeursM);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "anomalies RTC résolues", valeursT);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        valoriserCellule(row, 0, centre, "Anomalies en cours : ");

        row = sheet.createRow(sheet.getLastRowNum() + 2);
        creerLigneTotalAno(row, "Total : ", valeursE[Calcul.TOTAL.ordinal()], valeursE[Calcul.TOTALSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus d'une semaine : ", valeursE[Calcul.TOTALS.ordinal()], valeursE[Calcul.TOTALSSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus d'un mois : ", valeursE[Calcul.TOTALM.ordinal()], valeursE[Calcul.TOTALMSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus de 3 mois : ", valeursE[Calcul.TOTALT.ordinal()], valeursE[Calcul.TOTALTSEC.ordinal()]);
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
     * @param dqsATraiter
     * @param anoAajouter
     * @param lotsEnErreurSonar
     * @param lotsSecurite
     * @param lotsRelease
     * @param sheet
     * @param matiere
     * @param task
     * @throws IOException
     */
    public void majFeuilleDefaultsQualite(List<DefautQualite> dqsATraiter, Sheet sheet, Matiere matiere)
    {
        // Rangement anomalies par date de détection
        Collections.sort(dqsATraiter, (o1, o2) -> o1.getDateDetection().compareTo(o2.getDateDetection()));

        // Mise à jour anomalies
        for (DefautQualite dq : dqsATraiter)
        {
            // Quitte le traitment si l'anomalie n'est pas à remonter dans le fichier
            if (testAnoOK(dq))
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(dq);

            // Calcul du motif
            FillPatternType pattern = calculMotif(dq);

            // Création de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneSQ(row, dq, couleur, pattern);
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

    private void calculDonnee(DefautQualite dq, LocalDate debutMois, LocalDate debutTrim, LocalDate fin, int[] valeurs)
    {

        valeurs[Calcul.TOTAL.ordinal()]++;
        if (dq.isSecurite())
            valeurs[Calcul.TOTALSEC.ordinal()]++;

        if (dq.getDateDetection().isAfter(debutMois) && dq.getDateDetection().isBefore(fin))
        {
            valeurs[Calcul.TOTALM.ordinal()]++;
            if (dq.isSecurite())
                valeurs[Calcul.TOTALMSEC.ordinal()]++;
        }
        if (dq.getDateDetection().isAfter(debutTrim) && dq.getDateDetection().isBefore(fin))
        {
            valeurs[Calcul.TOTALT.ordinal()]++;
            if (dq.isSecurite())
                valeurs[Calcul.TOTALTSEC.ordinal()]++;
        }
    }

    private void calculEnCours(DefautQualite dq, LocalDate today, int[] valeursE)
    {
        valeursE[Calcul.TOTAL.ordinal()]++;

        if (dq.isSecurite())
            valeursE[Calcul.TOTALSEC.ordinal()]++;

        if (dq.getDateCreation().isBefore(today.minusMonths(3L)))
        {
            valeursE[Calcul.TOTALT.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALTSEC.ordinal()]++;
        }
        else if (dq.getDateCreation().isBefore(today.minusMonths(1L)))
        {
            valeursE[Calcul.TOTALM.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALMSEC.ordinal()]++;
        }
        else if (dq.getDateCreation().isBefore(today.minusWeeks(1L)))
        {
            valeursE[Calcul.TOTALS.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALSSEC.ordinal()]++;
        }
    }

    private boolean testAnoOK(DefautQualite dq)
    {
        // Données
        EtatDefaut etatAno = dq.getEtatDefaut();
        LotRTC lotRTC = dq.getLotRTC();
        EtatLot etatLot = lotRTC.getEtatLot();
        QG qg = lotRTC.getQualityGate();

        // On considère les anomalies abandonnées comme bonnes
        if (etatAno == EtatDefaut.ABANDONNE)
            return true;

        // On ne reprend pas les anomalies closes avec un lot terminé ou livré à l'édition
        if ((etatLot == EtatLot.EDITION || etatLot == EtatLot.TERMINE) && etatAno == EtatDefaut.CLOS)
            return true;

        // Si l'anomalie est close mais que le QG est toujours en erreur, on met l'anomalie à vérifier et nouvelle
        if (etatAno == EtatDefaut.CLOS && qg == QG.ERROR)
        {
            dq.setAction(TypeAction.VERIFIER);
            dq.setDateDetection(LocalDate.now());
            dq.setEtatDefaut(EtatDefaut.NOUVEAU);
            return false;
        }

        return etatAno == EtatDefaut.CLOS;
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
     * @param dq
     *            anomalie
     * @param lotsEnErreurSonar
     *            Liste des lots en erreur
     * @param anoLot
     *            numéro du lot
     * @param lotsRelease
     *            Liste des lots en version Release
     * @return
     */
    private IndexedColors calculCouleurLigne(DefautQualite dq)
    {
        IndexedColors couleur;

        // Mise en vert des anomalies avec un Quality Gate bon
        if (dq.getLotRTC().getQualityGate() != QG.ERROR)
            couleur = IndexedColors.LIGHT_GREEN;

        // Les lots qui ont besoin juste d'un réassemblage sont en bleu
        else if (TypeAction.ASSEMBLER == dq.getAction())
            couleur = IndexedColors.LIGHT_TURQUOISE;

        // Lot proches de leur date de MEP
        else if (dq.getDateMepPrev().minusWeeks(3).isBefore(LocalDate.now()))
            couleur = IndexedColors.LIGHT_YELLOW;

        // Le reste est en blanc
        else
            couleur = IndexedColors.WHITE;

        // Les lots déjà traités une première fois sont en gris
        if (TypeAction.VERIFIER == dq.getAction())
            couleur = IndexedColors.GREY_25_PERCENT;

        // Remise de la couleur à orange si le lot n'a pas encore été traité
        if (!dq.calculTraitee())
            couleur = IndexedColors.LIGHT_ORANGE;

        return couleur;
    }

    private FillPatternType calculMotif(DefautQualite dq)
    {
        // On cherche dans la map des composants, tous ceux qui sont liès au lot du défaut. Si un des composants a une application mal renseignée, on change le fond de la ligne.
        for (ComposantSonar compo : mapCompoByLot.computeIfAbsent(dq.getLotRTC().getLot(), k -> new ArrayList<>()))
        {
            if (!compo.getAppli().isReferentiel())
                return FillPatternType.LEAST_DOTS;
        }
        return FillPatternType.SOLID_FOREGROUND;
    }

    /**
     * Crée une ligne correspondante à une anomalie dans le fichier Excel
     * 
     * @param row
     * @param dq
     * @param couleur
     */
    private void creerLigneSQ(Row row, DefautQualite dq, IndexedColors couleur, FillPatternType pattern)
    {
        // 1. Contrôles des entrées
        if (couleur == null || row == null || dq == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls - méthode control.excel.ControlSuivi.creerLigneSQ");

        // 2. Données de l'anomalie
        LotRTC lotRTC = dq.getLotRTC();
        ProjetClarity projetClarity = lotRTC.getProjetClarity();
        if (projetClarity == null)
            projetClarity = ProjetClarity.getProjetClarityInconnu(Statics.INCONNU);
        ChefService chefService = projetClarity.getChefService();
        if (chefService == null)
            chefService = ChefService.getChefServiceInconnu(Statics.INCONNU);

        // 3. Création des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur, pattern);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, pattern, HorizontalAlignment.CENTER);
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
        valoriserCellule(row, colEdition, centre, lotRTC.getEdition().getNom());

        // Numéro du lot
        Cell cell = valoriserCellule(row, colLot, centre, lotRTC.getLot());
        ajouterLiens(cell, dq.getLiensLot());

        // Environnement
        valoriserCellule(row, colEnv, centre, lotRTC.getEtatLot());

        // Numéros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = dq.getNumeroAnoRTC();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);
            ajouterLiens(cell, dq.getLiensAno());
        }

        // Etat anomalie
        valoriserCellule(row, colEtat, normal, dq.getEtatRTC());

        // Anomalie de sécurite
        if (dq.isSecurite())
            valoriserCellule(row, colSec, centre, Statics.X);
        else
            valoriserCellule(row, colSec, centre, Statics.EMPTY);

        // Remarques
        valoriserCellule(row, colRemarque, normal, dq.getRemarque());

        // Version composants
        valoriserCellule(row, colVer, centre, dq.getTypeVersion());

        // Date création
        valoriserCellule(row, colDateCrea, date, dq.getDateCreation());

        // Date création
        valoriserCellule(row, colDateDetec, date, dq.getDateDetection());

        // Date de réouverture
        valoriserCellule(row, colDateReouv, date, dq.getDateReouv());

        // Date relance
        valoriserCellule(row, colDateRel, date, dq.getDateRelance());

        // Date resolution
        valoriserCellule(row, colDateRes, date, dq.getDateReso());

        // Date de mise ne production prévisionnelle
        valoriserCellule(row, colDateMepPrev, date, dq.getDateMepPrev());

        // Durée anomalie
        valoriserFormuleCellule(row, colDureeAno, centre, creerFormule(row));

        // Date mise à jour de l'état de l'anomalie
        valoriserCellule(row, colDateMajEtat, date, lotRTC.getDateMajEtat());

        // Matiere
        valoriserCellule(row, colMatiere, centre, lotRTC.getMatieresString());

        // Projet RTC
        valoriserCellule(row, colProjetRTC, centre, lotRTC.getProjetRTC());

        // Action
        valoriserCellule(row, colAction, centre, dq.getAction());

        // Groupe composant
        valoriserCellule(row, colProduit, centre, lotRTC.getGroupeProduit().getValeur());
    }

    private String creerFormule(Row row)
    {
        // Numéro de cellule de la date de création de l'anomalie
        String dateCrea = CellReference.convertNumToColString(colDateCrea) + (row.getRowNum() + 1);

        // Numéro de cellule de la date de résolution de l'anomalie
        String dateReso = CellReference.convertNumToColString(colDateRes) + (row.getRowNum() + 1);

        // Création de la formule =SI(S4="";""; SI(U4 = "";AUJOURDHUI() - S4; U4 - S4))
        StringBuilder builder = new StringBuilder("IF(");
        builder.append(dateCrea).append(" = \"\" , \"\" , IF( ").append(dateReso).append(" = \"\" , TODAY() - ").append(dateCrea).append(" , ").append(dateReso).append(" - ").append(dateCrea)
                .append(" ))");
        return builder.toString();
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
    private void creerLigneCalcul(Row row, String stat, int[] valeurs)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);

        final String DONT = " dont ";
        final String SECURITE = " de sécurité";

        valoriserCellule(row, Index.DONNEEI.ordinal(), centre, stat);
        valoriserCellule(row, Index.TOTALI.ordinal(), centre, valeurs[Calcul.TOTAL.ordinal()] + DONT + valeurs[Calcul.TOTALSEC.ordinal()] + SECURITE);
        valoriserCellule(row, Index.MENSUELI.ordinal(), centre, valeurs[Calcul.TOTALM.ordinal()] + DONT + valeurs[Calcul.TOTALMSEC.ordinal()] + SECURITE);
        valoriserCellule(row, Index.TRIMI.ordinal(), centre, valeurs[Calcul.TOTALT.ordinal()] + DONT + valeurs[Calcul.TOTALTSEC.ordinal()] + SECURITE);
    }

    private void creerLigneTotalAno(Row row, String texte, int total, int secu)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);

        final String DONT = " dont ";
        final String SECURITE = " de sécurité";

        valoriserCellule(row, 0, centre, new StringBuilder(texte).append(total).append(DONT).append(secu).append(SECURITE));
    }

    /**
     * Crée une anomalie depuis les informations du fichier Excel
     * 
     * @param row
     * @param lotsRTC
     * @return
     */
    private DefautQualite creerDqDepuisExcel(Row row, Map<String, LotRTC> lotsRTC)
    {
        DefautQualite retour = ModelFactory.build(DefautQualite.class);
        retour.setLotRTC(lotsRTC.get(Utilities.testLot(getCellStringValue(row, colLot))));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.calculTraitee();
        return retour;
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
    
    private enum Calcul
    {
        TOTAL,
        TOTALSEC,
        TOTALM,
        TOTALMSEC,
        TOTALT,
        TOTALTSEC,
        TOTALS,
        TOTALSSEC;
    }
    
    // @formatter:on
}
