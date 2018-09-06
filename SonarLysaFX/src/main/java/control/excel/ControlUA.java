package control.excel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColUA;

public class ControlUA extends AbstractControlExcelRead<TypeColUA, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    
    private int colUA;
    private int colAppli;
    
    /*---------- CONSTRUCTEURS ----------*/


    protected ControlUA(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, String> recupDonneesDepuisExcel()
    {
        // Initialisation map
        Map<String, String> retour = new HashMap<>();
        
        Sheet sheet = wb.getSheetAt(0);
        
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
