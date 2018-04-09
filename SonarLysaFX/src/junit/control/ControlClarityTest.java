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
import control.excel.ControlClarity;
import model.InfoClarity;
import model.enums.TypeColClarity;

public class ControlClarityTest
{
    /*---------- ATTRIBUTS ----------*/

    private ControlClarity handler;
    private File file;
    private Workbook wb;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource("/resources/Referentiel_Projets.xlsm").getFile());
        handler = new ControlClarity(file);
        wb = (Workbook) Whitebox.getField(ControlAno.class, "wb").get(handler);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        Map<String, InfoClarity> map = handler.recupDonneesDepuisExcel();
        assertTrue(map != null);
        assertTrue(!map.isEmpty());
        assertTrue(map.size() == 8277);
    }
    
    @Test
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // test - énumérationud bon Type
        assertTrue(Whitebox.getField(ControlAno.class, "enumeration").get(handler).equals(TypeColClarity.class));
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