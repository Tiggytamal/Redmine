package junit.control.excel;

import java.util.List;

import org.junit.Test;

import control.excel.ControlApps;
import model.bdd.Application;
import model.enums.TypeColApps;

public class TestControlApps extends TestControlExcelRead<TypeColApps, ControlApps, List<Application>>
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
        // Test initialisation colonnes. Pour ce fichier, la premi�re colonne est utilis�e.
        testCalculIndiceColonnes(1);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(liste -> liste.size() == 1897); 
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}