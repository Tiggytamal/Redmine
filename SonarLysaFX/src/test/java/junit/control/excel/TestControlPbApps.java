package junit.control.excel;

import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlPbApps;
import model.CompoPbApps;
import model.enums.TypeColPbApps;

public class TestControlPbApps extends TestControlExcelWrite<TypeColPbApps, ControlPbApps, List<CompoPbApps>>
{

    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlPbApps()
    {
        super(TypeColPbApps.class, "extractPbVul.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerfeuille()
    {
        //TODO
    }
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test que l'on a bien la surcharge avec aucun plantage
        Whitebox.invokeMethod(handler, "calculIndiceColonnes");
    }
    
    @Test
    public void testInitTitres()
    {
        //TODO
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
}
