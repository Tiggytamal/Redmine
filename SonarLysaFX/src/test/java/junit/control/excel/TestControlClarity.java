package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlClarity;
import model.bdd.ProjetClarity;
import model.enums.TypeColClarity;

public class TestControlClarity extends TestControlExcelRead<TypeColClarity, ControlClarity, Map<String, ProjetClarity>>
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
        testRecupDonneesDepuisExcel(map -> map.size() == 1096);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}