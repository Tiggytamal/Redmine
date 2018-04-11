package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlApps;
import model.enums.TypeColApps;

public class ControlAppsTest extends ControlExcelTest<TypeColApps, ControlApps, Map<String, Boolean>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public ControlAppsTest()
    {
        super(TypeColApps.class, "/resources/liste_applis.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée.
        calculIndiceColonnes(1);
    }
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel((map) -> map.size() == 1842);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}