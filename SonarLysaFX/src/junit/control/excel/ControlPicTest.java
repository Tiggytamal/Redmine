package junit.control.excel;

import java.util.Map;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExcel;
import model.LotSuiviPic;
import model.enums.TypeColPic;
import utilities.FunctionalException;

/**
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ControlPicTest extends ControlExcelTest<TypeColPic, ControlExcel<TypeColPic, Map<String, LotSuiviPic>>, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/
    
    public ControlPicTest()
    {
        super(TypeColPic.class, "/resources/Lots_Pic.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel((map) -> map.size() == 1861);
    }
    
    @Test(expected = FunctionalException.class)
    public void initSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        for(int i = wb.getNumberOfSheets() - 1; i >= 0; i--)
        {
            wb.removeSheetAt(i);
        }
        
        Whitebox.invokeMethod(handler, "initSheet");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
