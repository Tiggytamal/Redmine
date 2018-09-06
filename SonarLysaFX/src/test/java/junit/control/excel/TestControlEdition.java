package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlEdition;
import model.enums.TypeColEdition;
import utilities.FunctionalException;

public class TestControlEdition extends TestControlExcelRead<TypeColEdition, ControlEdition, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlEdition()
    {
        super(TypeColEdition.class, "Codification_des_Editions.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne ne sert pas.
        testCalculIndiceColonnes(0);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        // Premiers test génériques
        Map<String, String> map = testRecupDonneesDepuisExcel(a -> a.size() == 72);
        
        // Test sur le format des clefs/valeurs dans la map
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            assertTrue(entry.getKey(), entry.getKey().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue(), entry.getValue().matches("^CHC(_CDM){0,1}20[12][0-9]\\-S[0-5][0-9]$"));
        }
    }
    
    @Test (expected = FunctionalException.class)
    public void testPrepareLibelleException() throws Exception
    {       
        // Test de remontée de l'erreur
        Whitebox.invokeMethod(handler, "prepareLibelle", "mauvais libelle");
    }
    
    @Test
    public void testPrepareLibelle() throws Exception
    {       
        String methode = "prepareLibelle";
        // Tests des différents cas de la préparation du libellé
        assertEquals("CHC_CDM2018-S32", Whitebox.invokeMethod(handler, methode, "CDM2018-S32"));
        assertEquals("CHC_CDM2021-S01", Whitebox.invokeMethod(handler, methode, "CDM2021-S01 / CHC2017-S22"));
        assertEquals("CHC_CDM2019-S16", Whitebox.invokeMethod(handler, methode, "CHC2013-S51 / CDM2019-S16"));
        assertEquals("CHC2017-S15", Whitebox.invokeMethod(handler, methode, "CHC2017-S15"));
    }
    
    @Test
    @Override
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(handler, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheetAt(0), sheet);
    }
    
    @Test(expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        Whitebox.invokeMethod(handler, "initSheet");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}