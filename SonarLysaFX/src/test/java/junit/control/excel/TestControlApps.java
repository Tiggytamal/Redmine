package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlApps;
import model.enums.TypeColApps;

public class TestControlApps extends TestControlExcel<TypeColApps, ControlApps, Map<String, Boolean>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlApps()
    { 
        super(TypeColApps.class, "liste_applis.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée.
        testCalculIndiceColonnes(1);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 1842);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}