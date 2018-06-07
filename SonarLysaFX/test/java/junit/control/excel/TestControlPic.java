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
public class TestControlPic extends TestControlExcel<TypeColPic, ControlPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlPic()
    {
        super(TypeColPic.class, "Lots_Pic.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel(map -> map.size() == 1931);
    }
    
    @Test(expected = FunctionalException.class)
    @Override
    public void initSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        for(int i = wb.getNumberOfSheets() - 1; i >= 0; i--)
        {
            wb.removeSheetAt(i);
        }
        
        Whitebox.invokeMethod(handler, "initSheet");
    }
    
    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne ne sert pas.
        calculIndiceColonnes(0);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
