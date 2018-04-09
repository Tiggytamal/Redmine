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
import control.excel.ControlEdition;
import model.enums.TypeColEdition;

public class ControlEditionTest
{
    private ControlEdition handler;
    private File file;
    private Workbook wb;

    @Before
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource("/resources/Codification_des_Editions.xls").getFile());
        handler = new ControlEdition(file);
        wb = (Workbook) Whitebox.getField(ControlEdition.class, "wb").get(handler);
    }

    @Test
    public void recupVersionDepuisExcel()
    {
        Map<String, String> map = handler.recupDonneesDepuisExcel();
        assertTrue(!map.isEmpty());

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            assertTrue(entry.getKey().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue().matches("^CHC(_CDM){0,1}20[12][0-9]\\-S[0-5][0-9]$"));
        }
    }
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        Map<String, String> map = handler.recupDonneesDepuisExcel();
        assertTrue(map != null);
        assertTrue(!map.isEmpty());
        assertTrue(map.size() == 72);
    }
    
    @Test
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // test - énumération du bon Type
        assertTrue(Whitebox.getField(ControlAno.class, "enumeration").get(handler).equals(TypeColEdition.class));
    }
    
    @Test
    public void initSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(handler, "initSheet");
        assertTrue(sheet != null);
        assertTrue(sheet == wb.getSheetAt(0));
    }
}