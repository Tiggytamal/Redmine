package junit.control.excel;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlChefService;
import model.RespService;
import model.enums.TypeColChefServ;
import utilities.FunctionalException;

public class ControlChefServiceTest extends ControlExcelTest<TypeColChefServ, ControlChefService, Map<String, RespService>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public ControlChefServiceTest()
    {
        super(TypeColChefServ.class, "/resources/Reorg_managers.xlsx");
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
        recupDonneesDepuisExcel((map) -> map.size() == 28);
    }
    
    @Test
    public void initSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(handler, "initSheet");
        assertTrue(sheet != null);
        assertTrue(sheet == wb.getSheetAt(0));
    }
    
    @Test(expected = FunctionalException.class)
    public void initSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        Whitebox.invokeMethod(handler, "initSheet"); 
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}