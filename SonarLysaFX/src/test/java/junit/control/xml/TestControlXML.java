package junit.control.xml;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.JunitBase;
import model.FichiersXML;
import model.ProprietesXML;
import model.enums.TypeColChefServ;
import model.enums.TypeFichier;
import utilities.Statics;
import utilities.TechnicalException;

@RunWith (JfxRunner.class)
public class TestControlXML extends JunitBase
{
    private ControlXML handler;

    @Before
    public void init()
    {
        handler = new ControlXML();
    }

    @Test
    @TestInJfxThread
    public void testRecuprerParamXML()
    {
        handler.recupererXMLResources(FichiersXML.class);
        handler.recupererXMLResources(ProprietesXML.class);
    }

    @Test
    public void testRecupListeAppsDepuisExcel()
    {
        handler.recupListeAppsDepuisExcel(new File(getClass().getResource(Statics.RESOURCESTEST + "liste_applis.xlsx").getFile()));       
    }

    @Test
    public void testRecupInfosClarityDepuisExcel()
    {
        handler.recupInfosClarityDepuisExcel(new File(getClass().getResource(Statics.RESOURCESTEST + "Referentiel_Projets.xlsm").getFile()));
    }

    @Test
    public void testRecupChefServiceDepuisExcel()
    {
        handler.recupChefServiceDepuisExcel(new File(getClass().getResource(Statics.RESOURCESTEST + "Reorg_managers.xlsx").getFile()));
    }

    @Test
    public void testRecupEditionDepuisExcel()
    {
        handler.recupEditionDepuisExcel(new File(getClass().getResource(Statics.RESOURCESTEST + "Codification_des_Editions.xlsx").getFile()));
    }
    
    @Test (expected = TechnicalException.class)
    public void testSaveInfosException() throws Exception
    {        
        Whitebox.invokeMethod(handler, "saveInfos", TypeFichier.RESPSERVICE, TypeColChefServ.class, new File("��&;:["));
    }
}