package junit.control.excel;

import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlUA;
import model.enums.TypeColUA;

public class TestControlUA extends TestControlExcelRead<TypeColUA, ControlUA, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlUA()
    {
        super(TypeColUA.class, "CatalogueUA_20180126(P.Durand).xlsx");

    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        Map<String, String> map = handler.recupDonneesDepuisExcel();
        assertFalse(map.isEmpty());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
