package junit.control.excel;

import java.util.List;
import java.util.Map;

import control.excel.ControlExtractCompo;
import model.bdd.ComposantSonar;
import model.enums.TypeColCompo;

public class TestControlExtractCompo extends TestControlExcelWrite<TypeColCompo, ControlExtractCompo, Map<TypeColCompo, List<ComposantSonar>>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractCompo()
    {
        super(TypeColCompo.class, "extractCompo.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
