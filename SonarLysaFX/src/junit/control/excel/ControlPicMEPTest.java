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
        super(TypeColPic.class, "/resources/MEP_fevrier.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne ne sert pas.
        calculIndiceColonnes(0);
    }
    
    @Test
    public void recupLotsExcelPourMEP()
    {
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
