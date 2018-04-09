package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlExcel;
import model.LotSuiviPic;
import model.enums.TypeColPic;

public class ControlPicMEPTest extends ControlExcelTest<TypeColPic, ControlExcel<TypeColPic, Map<String, LotSuiviPic>>, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public ControlPicMEPTest()
    {
        super(TypeColPic.class, "/resources/MEP_février.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupLotsExcelPourMEP()
    {
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
