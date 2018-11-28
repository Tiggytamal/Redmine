package junit.control.excel;

import java.util.Map;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlPic;
import model.LotSuiviPic;
import model.enums.TypeColPic;
import utilities.FunctionalException;

/**
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestControlPic extends TestControlExcelRead<TypeColPic, ControlPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlPic()
    {
        super(TypeColPic.class, "Lots_Pic.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 158);  
    }
    
    @Test(expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        for(int i = wb.getNumberOfSheets() - 1; i >= 0; i--)
        {
            wb.removeSheetAt(i);
        }
        
        Whitebox.invokeMethod(controlTest, "initSheet");
    }
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes.
        testCalculIndiceColonnes(1);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
