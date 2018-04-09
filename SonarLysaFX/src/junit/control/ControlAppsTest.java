package junit.control;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlAno;
import control.excel.ControlApps;
import model.enums.TypeColApps;
import utilities.FunctionalException;

public class ControlAppsTest
{
    /*---------- ATTRIBUTS ----------*/
    
    private ControlApps handler;
    private File file;
    private Workbook wb;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource("/resources/liste_applis.xlsx").getFile());
        handler = new ControlApps(file);
        wb = (Workbook) Whitebox.getField(ControlAno.class, "wb").get(handler);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        Map<String, Boolean> map = handler.recupDonneesDepuisExcel();
        assertTrue(map != null);
        assertTrue(!map.isEmpty());
        assertTrue(map.size() == 1842);
    }
    
    @Test
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // test - énumérationud bon Type
        assertTrue(Whitebox.getField(ControlAno.class, "enumeration").get(handler).equals(TypeColApps.class));
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