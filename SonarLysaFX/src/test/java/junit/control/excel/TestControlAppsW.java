package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.powermock.reflect.Whitebox.getField;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import control.excel.ControlAppsW;
import model.Application;
import model.ModelFactory;
import model.enums.TypeColApps;

public class TestControlAppsW extends TestControlExcelWrite<TypeColApps, ControlAppsW, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private Application appli;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlAppsW()
    {
        super(TypeColApps.class, "testAppW.xlsx");
        appli = ModelFactory.getModel(Application.class);
        appli.setActif(true);
        appli.setCode("ABCD");
        appli.setLibelle("Application");
        appli.setMainFrame(false);
        appli.setOpen(true);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testInitEnum() throws IllegalAccessException
    {
        // test - énumération du bon Type
        assertEquals(TypeColApps.class, getField(ControlAppsW.class, "enumeration").get(handler));
    }

    @Test
    public void testCreerfeuilleSonar()
    {
        Set<Application> applisOpenSonar = new HashSet<>();
        applisOpenSonar.add(appli);
        handler.creerfeuilleSonar(applisOpenSonar);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
