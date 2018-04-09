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
import control.excel.ControlChefService;
import model.RespService;
import model.enums.TypeColChefServ;

public class ControlChefServiceTest
{
    /*---------- ATTRIBUTS ----------*/

    private ControlChefService handler;
    private File file;
    private Workbook wb;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource("/resources/Reorg_managers.xlsx").getFile());
        handler = new ControlChefService(file);
        wb = (Workbook) Whitebox.getField(ControlChefService.class, "wb").get(handler);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        Map<String, RespService> map = handler.recupDonneesDepuisExcel();
        assertTrue(map != null);
        assertTrue(!map.isEmpty());
        assertTrue(map.size() == 28);
    }
    
    @Test
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // test - énumérationud bon Type
        assertTrue(Whitebox.getField(ControlAno.class, "enumeration").get(handler).equals(TypeColChefServ.class));
    }
    
    @Test
    public void initSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(handler, "initSheet");
        assertTrue(sheet != null);
        assertTrue(sheet == wb.getSheetAt(0));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}