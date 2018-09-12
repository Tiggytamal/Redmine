package control.excel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColUA;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlUA extends AbstractControlExcelRead<TypeColUA, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /** Nom de la feuille des données */
    private static final String SHEET = "Catalogue_UA-APPLI_E30";
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    private int colUA;
    private int colAppli;
    
    /*---------- CONSTRUCTEURS ----------*/


    protected ControlUA(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Récupération de la feuille Excel pour le traitement.
     */
    @Override
    protected Sheet initSheet()
    {
        Sheet sheet;
        try
        {
            sheet = wb.getSheet(SHEET);
            if (sheet == null)
                throw new IllegalArgumentException();
        } 
        catch (IllegalArgumentException e)
        {
            LOGPLANTAGE.error(e);
            throw new FunctionalException(Severity.ERROR, "Le fichier est vide");
        }
        return sheet;
    }
    
    @Override
    public Map<String, String> recupDonneesDepuisExcel()
    {
        // Initialisation map
        Map<String, String> retour = new HashMap<>();
        
        Sheet sheet = wb.getSheet(SHEET);
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);    
            retour.put(getCellStringValue(row, colUA), getCellStringValue(row, colAppli));
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
