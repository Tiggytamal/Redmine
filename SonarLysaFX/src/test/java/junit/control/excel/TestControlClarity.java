package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlClarity;
import model.InfoClarity;
import model.enums.TypeColClarity;

public class TestControlClarity extends TestControlExcel<TypeColClarity, ControlClarity, Map<String, InfoClarity>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlClarity()
    {
        super(TypeColClarity.class, "Referentiel_Projets.xlsm");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne n'est pas utilisée
        testCalculIndiceColonnes(0);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 5878);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}