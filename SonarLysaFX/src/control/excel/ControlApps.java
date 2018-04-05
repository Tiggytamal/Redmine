package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColApps;

public class ControlApps extends ControlExcel<TypeColApps, Map<String, Boolean>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colApps;
    private int colActif;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ControlApps(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public Map<String, Boolean> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        Map<String, Boolean> retour = new HashMap<>();
        
        for (Row row : sheet)
        {
            Boolean bool;
            String actif = getCellStringValue(row, colActif);
            if ("Actif".equals(actif))
                bool = Boolean.TRUE;
            else
                bool = Boolean.FALSE;
            
            
            retour.put(getCellStringValue(row, colApps), bool);
        }
        return retour;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    protected void initEnum()
    {
        enumeration = TypeColApps.class;

    }

    @Override
    protected Sheet initSheet()
    {
        return wb.getSheetAt(0);
    }
}