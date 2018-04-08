package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.InfoClarity;
import model.ModelFactory;
import model.enums.TypeColClarity;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlClarity extends ControlExcel<TypeColClarity, Map<String, InfoClarity>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    // Attention les noms des variables doivent être identiques aux données dans l'énumération TypeColClarity
    private int colActif;
    private int colClarity;
    private int colLib;
    private int colCpi;
    private int colEdition;
    private int colDir;
    private int colDepart;
    private int colService;
   
    /*---------- CONSTRUCTEURS ----------*/

    public ControlClarity(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, InfoClarity> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        Map<String, InfoClarity> retour = new HashMap<>();
        
        // Itération sur la feuille hormis la ligne des titres, et récupération des lignes qui ont un code Clarity
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if (row.getCell(colClarity) != null)
            {
                InfoClarity info = creerInfoClarityDepuisExcel(row);
                retour.put(info.getCodeClarity(), info);
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Création d'un objet InfoClarity depuis une ligne du fichier Excel
     * @param row
     * @return
     */
    private InfoClarity creerInfoClarityDepuisExcel(Row row)
    {
        InfoClarity retour = ModelFactory.getModel(InfoClarity.class);
        retour.setChefProjet(getCellStringValue(row, colCpi));
        retour.setCodeClarity(getCellStringValue(row, colClarity));
        retour.setDepartement(getCellStringValue(row, colDepart));
        retour.setDirection(getCellStringValue(row, colDir));
        retour.setEdition(getCellStringValue(row, colEdition));
        retour.setLibelleProjet(getCellStringValue(row, colLib));
        retour.setService(getCellStringValue(row, colService));
        String actif = getCellStringValue(row, colActif);
        if ("Oui".equals(actif))
            retour.setActif(true);
        else
            retour.setActif(false);
        
        return retour;
    }

    @Override
    protected void initEnum()
    {
        enumeration = TypeColClarity.class;        
    }

    @Override
    protected Sheet initSheet()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier est vide");
        return sheet;
    }

    /*---------- ACCESSEURS ----------*/

}