package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.getField;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.excel.ControlAppsW;
import control.task.CreerVueParAppsTask;
import de.saxsys.javafx.test.JfxRunner;
import model.ModelFactory;
import model.bdd.Application;
import model.enums.OptionCreerVueParAppsTask;
import model.enums.Param;
import model.enums.TypeColAppsW;
import utilities.Statics;
import utilities.TechnicalException;

@RunWith(JfxRunner.class)
public class TestControlAppsW extends TestControlExcelWrite<TypeColAppsW, ControlAppsW, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private Application appli;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlAppsW()
    {
        super(TypeColAppsW.class, "testAppW.xlsx");
        appli = ModelFactory.build(Application.class);
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
        assertEquals(TypeColAppsW.class, getField(ControlAppsW.class, "enumeration").get(controlTest));
    }

    @Test
    public void testCreerfeuilleSonar() throws IllegalAccessException
    {
        Set<Application> applisOpenSonar = new HashSet<>();
        applisOpenSonar.add(appli);
        controlTest.creerfeuilleSonar(applisOpenSonar, new CreerVueParAppsTask(OptionCreerVueParAppsTask.ALL, file));
        Sheet sheet = wb.getSheet("Périmètre Couverts SonarQube");
        assertNotNull(sheet);
        assertEquals(1, sheet.getLastRowNum());
    }
    
    @Test (expected = TechnicalException.class)
    public void testCalculDeuxiemeFeuilleException() throws Exception
    {
        // Initialisation avec remplacement paramètrepar donnée incorrectes
        List<String> liste = new ArrayList<>();
        String temp = Statics.proprietesXML.getMapParams().get(Param.NOMFICHIERAPPLI);
        Statics.proprietesXML.getMapParams().put(Param.NOMFICHIERAPPLI, "1,;:_");
        
        try
        {
        Whitebox.invokeMethod(controlTest, "calculDeuxiemeFeuille", liste);
        }
        catch (TechnicalException e)
        {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("Impossible de récupérer la feuille excel - fichier : "));
            throw e;
        }
        finally
        {
            Statics.proprietesXML.getMapParams().put(Param.NOMFICHIERAPPLI, temp);
        }
    }
    
    @Test (expected = TechnicalException.class)
    public void testWrite() throws IllegalAccessException
    {
        // Remplacement du fichier par un fichier mal nommé
        Whitebox.getField(ControlAppsW.class, "file").set(controlTest, new File("adc*;,b"));
        
        // Appel crite pour remonter l'exception
        controlTest.write();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
