package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlChefService;
import model.bdd.ChefService;
import model.enums.TypeColChefServ;
import utilities.FunctionalException;

public class TestControlChefService extends TestControlExcelRead<TypeColChefServ, ControlChefService, Map<String, ChefService>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlChefService()
    {
        super(TypeColChefServ.class, "Reorg_managers.xlsx");
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
        testRecupDonneesDepuisExcel(map -> map.size() == 28);
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