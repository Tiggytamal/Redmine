package control.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefaultAppli;
import model.bdd.LotRTC;
import model.enums.EtatDefault;
import model.enums.TypeAction;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

public class ControlSuiviApps extends AbstractControlExcelRead<model.enums.TypeColSuiviApps, List<DefaultAppli>>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String DA = "SUIVI D�faults Appli";

    // Liste des indices des colonnes
    // Les noms des champs doivent correspondre aux valeurs dans l'�num�ration TypeCol
    private int colCompo;
    private int colActuel;
    private int colNew;
    private int colAction;
    private int colEtat;
    private int colDateDetec;
    private int colAnoRTC;
    private int colCpiLot;
    private int colLot;

    /** contrainte de validit�e de la colonne Action */
    protected String[] contraintes;

    /*---------- CONSTRUCTEURS ----------*/

    ControlSuiviApps(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<DefaultAppli> recupDonneesDepuisExcel()
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(DA);

        // Liste de retour
        List<DefaultAppli> retour = new ArrayList<>();

        // It�ration sur chaque ligne pour cr�er les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // protection pour les lignes vides
            if (row == null)
                continue;

            retour.add(creerDefaultdepuisExcel(row));
        }

        return retour;
    }

    public void majFeuilleDefaultsAppli(List<DefaultAppli> dasATraiter, Sheet sheet)
    {
        // Rangement anomalies par date de d�tection
        Collections.sort(dasATraiter, (o1, o2) -> o1.getDateDetection().compareTo(o2.getDateDetection()));

        // Mise � jour anomalies
        for (DefaultAppli da : dasATraiter)
        {
            // Onne prend pas en compte les d�faults clos et abandonn�s
            if (da.getEtatDefault() == EtatDefault.CLOSE || da.getEtatDefault() == EtatDefault.ABANDONNEE)
                continue;

            // Calcul de la couleur de la ligne dans le fichier Excel
            IndexedColors couleur = calculCouleurLigne(da);

            // Cr�ation de la ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneDA(row, da, couleur);
        }
        
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
        // R�cup�ration de la feuille principale
        Sheet sheet = wb.getSheet(DA);
        if (sheet == null)
            throw new FunctionalException(Severity.ERROR, "Le fichier n'a pas de page Suivi D�faults Qualit�");
        return sheet;
    }

    /*---------- METHODES PRIVEES ----------*/

    private DefaultAppli creerDefaultdepuisExcel(Row row)
    {
        DefaultAppli retour = ModelFactory.getModel(DefaultAppli.class);

        retour.setAction(TypeAction.from(getCellStringValue(row, colAction)));

        return retour;
    }

    private void creerLigneDA(Row row, DefaultAppli da, IndexedColors couleur)
    {
        // 1. Contr�les des entr�es
        if (couleur == null || row == null || da == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas �tre nuls - m�thode control.excel.ControlSuiviApps.creerLigneDA");

        // 2. Donn�es de l'anomalies
        ComposantSonar compo = da.getCompo();
        LotRTC lotRTC = compo.getLotRTC();

        // 3. Cr�ation des styles
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        // code corrig�e
        valoriserCellule(row, colNew, centre, da.getAppliCorrigee());

        // Action
        valoriserCellule(row, colAction, centre, da.getAction());

        // Etat
        valoriserCellule(row, colEtat, centre, da.getEtatDefault());

        // Date D�tection
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
        if (da.getDefaultQualite() != null && da.getDefaultQualite().getNumeroAnoRTC() != 0)
        {
            Cell cell = valoriserCellule(row, colAnoRTC, centre, da.getDefaultQualite().getNumeroAnoRTC());
            ajouterLiens(cell, da.getDefaultQualite().getLiensAno());
        }

    }

    /**
     * Gestion des couleurs des lignes du fichier
     * 
     * @param da
     * @return
     */
    private IndexedColors calculCouleurLigne(DefaultAppli da)
    {
        // Les lignes sont blanches de base
        IndexedColors couleur = IndexedColors.WHITE;

        // On met � vert les d�fautls dont le code applicaiton a �t� corrig�
        if (da.getAppliCorrigee().equals(da.getCompo().getAppli().getCode()))
            couleur = IndexedColors.LIGHT_GREEN;
        // On met � bleu les d�fault en cours de traitement
        else if (da.getEtatDefault() == EtatDefault.TRAITEE)
            couleur = IndexedColors.LIGHT_BLUE;

        return couleur;
    }

    /*---------- ACCESSEURS ----------*/

}
