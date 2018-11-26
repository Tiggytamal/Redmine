package control.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.LotRTC;
import model.enums.EtatDefaut;
import model.enums.TypeAction;
import model.enums.TypeColSuiviApps;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

public class ControlSuiviApps extends AbstractControlExcelRead<TypeColSuiviApps, List<DefautAppli>> implements Contraintes
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String DA = "SUIVI Défaults Appli";

    // Liste des indices des colonnes
    // Les noms des champs doivent correspondre aux valeurs dans l'énumération TypeCol
    private int colCompo;
    private int colActuel;
    private int colNew;
    private int colAction;
    private int colEtat;
    private int colDateDetec;
    private int colAnoRTC;
    private int colCpiLot;
    private int colLot;

    /** contrainte de validitée de la colonne Action */
    protected String[] contraintes;

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuiviApps(File file)
    {
        super(file);
        
        contraintes = initContraintes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<DefautAppli> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(DA);

        // Liste de retour
        List<DefautAppli> retour = new ArrayList<>();

        // Itération sur chaque ligne pour créer les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // protection pour les lignes vides
            if (row == null)
                continue;

            retour.add(creerDefautdepuisExcel(row));
        }

        return retour;
    }
    
    /**
     * 
     * 
     * @param dasATraiter
     * @param sheet
     */
    public void majFeuilleDefaultsAppli(List<DefautAppli> dasATraiter, Sheet sheet)
    {
        // Rangement anomalies par date de détection
        Collections.sort(dasATraiter, (o1, o2) -> o1.getDateDetection().compareTo(o2.getDateDetection()));

        // Mise à jour anomalies
        for (DefautAppli da : dasATraiter)
        {
            // Onne prend pas en compte les défaults clos et abandonnés
            if (da.getEtatDefaut() == EtatDefaut.CLOS || da.getEtatDefaut() == EtatDefaut.ABANDONNE || da.getEtatDefaut() == EtatDefaut.LOTCLOS)
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(da);

            // Création de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneDA(row, da, couleur);
        }
        
        if (sheet.getLastRowNum() > 0)
            ajouterDataValidation(sheet);
        autosizeColumns(sheet);
    }

    public Sheet resetFeuilleDA()
    {
        Sheet sheet = wb.getSheet(DA);
        if (sheet != null)
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        Sheet retour = wb.createSheet(DA);
        creerLigneTitres(retour);
        return retour;
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Sheet initSheet()
    {
        // Récupération de la feuille principale
        Sheet sheet = wb.getSheet(DA);
        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Le fichier n'a pas de page Suivi Défaults Qualité");
        return sheet;
    }

    /*---------- METHODES PRIVEES ----------*/

    private DefautAppli creerDefautdepuisExcel(Row row)
    {
        DefautAppli retour = ModelFactory.build(DefautAppli.class);

        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));
        retour.setNomComposant(getCellStringValue(row, colCompo));

        return retour;
    }

    private void creerLigneDA(Row row, DefautAppli da, IndexedColors couleur)
    {
        // 1. Contrôles des entrées
        if (couleur == null || row == null || da == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls - méthode control.excel.ControlSuiviApps.creerLigneDA");

        // 2. Données de l'anomalies
        ComposantSonar compo = da.getCompo();
        LotRTC lotRTC = compo.getLotRTC();

        // 3. Création des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        // code corrigée
        valoriserCellule(row, colNew, centre, da.getAppliCorrigee());

        // Action
        valoriserCellule(row, colAction, centre, da.getAction());

        // Etat
        valoriserCellule(row, colEtat, centre, da.getEtatDefaut());

        // Date Détection
        valoriserCellule(row, colDateDetec, centre, da.getDateDetection());

        // Composant
        valoriserCellule(row, colCompo, normal, compo.getNom());

        // Code actuel
        valoriserCellule(row, colActuel, centre, compo.getAppli().getCode());

        // CPI Projet
        valoriserCellule(row, colCpiLot, centre, lotRTC.getCpiProjet());

        // Lot
        valoriserCellule(row, colLot, centre, lotRTC.getLot());

        // Ano RTC
        if (da.getDefautQualite() != null && da.getDefautQualite().getNumeroAnoRTC() != 0)
        {
            Cell cell = valoriserCellule(row, colAnoRTC, centre, da.getDefautQualite().getNumeroAnoRTC());
            ajouterLiens(cell, da.getDefautQualite().getLiensAno());
        }

    }

    /**
     * Gestion des couleurs des lignes du fichier
     * 
     * @param da
     * @return
     */
    private IndexedColors calculCouleurLigne(DefautAppli da)
    {
        // Les lignes sont blanches de base
        IndexedColors couleur = IndexedColors.WHITE;

        // On met à vert les défautls dont le code applicaiton a été corrigé
        if (da.getCompo().getAppli().isReferentiel())
        {
            couleur = IndexedColors.LIGHT_GREEN;
            da.setEtatDefaut(EtatDefaut.CORRIGE);
        }
        return couleur;
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

}
