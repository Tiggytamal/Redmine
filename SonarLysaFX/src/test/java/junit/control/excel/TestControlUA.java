package junit.control.excel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import control.excel.ControlUA;
import model.enums.TypeColUA;
import utilities.FunctionalException;

public class TestControlUA extends TestControlExcelRead<TypeColUA, ControlUA, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlUA()
    {
        super(TypeColUA.class, "CatalogueUA.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        Map<String, String> map = handler.recupDonneesDepuisExcel();
        assertFalse(map.isEmpty());
    }
    

    @Test (expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(wb.getSheetIndex("Catalogue_UA-APPLI_E30"));
        invokeMethod(handler, "initSheet");
    }
    
    @Override
    @Test
    public void testGetCellValueNotNull() throws Exception
    {        
        for (Sheet sheet : wb)
        {
            for (Row row : sheet)
            {
                for (Cell cell: row)
                {
                    assertNotNull(invokeMethod(handler, "getCellStringValue", row, cell.getColumnIndex()));
                    assertNotNull(invokeMethod(handler, "getCellNumericValue", row, cell.getColumnIndex()));
                }               
            }
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
