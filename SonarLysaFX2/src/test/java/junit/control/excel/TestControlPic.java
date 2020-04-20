package junit.control.excel;

import java.util.Map;

import org.junit.jupiter.api.Test;

import control.excel.ControlPic;
import model.LotSuiviPic;
import model.enums.ColPic;

/**
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class TestControlPic extends TestAbstractControlExcelRead<ColPic, ControlPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlPic()
    {
        super(ColPic.class, "Lots_Pic.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(107, map -> map.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
