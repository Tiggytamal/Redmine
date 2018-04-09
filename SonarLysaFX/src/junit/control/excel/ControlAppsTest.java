package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlExcel;
import model.enums.TypeColApps;

public class ControlAppsTest extends ControlExcelTest<TypeColApps, ControlExcel<TypeColApps, Map<String, Boolean>>, Map<String, Boolean>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public ControlAppsTest()
    {
        super(TypeColApps.class, "/resources/liste_applis.xlsx"); 
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel((map) -> map.size() == 1842);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}