package junit.control.xml;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import model.FichiersXML;
import model.ProprietesXML;
import model.enums.TypeColChefServ;
import model.enums.TypeFichier;
import utilities.TechnicalException;

@RunWith (JfxRunner.class)
public class TestControlXML
{
    private ControlXML handler;

    @Before
    public void init()
    {
        handler = new ControlXML();
    }

    @Test
    @TestInJfxThread
    public void recuprerParamXML()
    {
        handler.recupererXMLResources(FichiersXML.class);
        handler.recupererXMLResources(ProprietesXML.class);
    }

    @Test
    public void recupListeAppsDepuisExcel()
    {
        handler.recupListeAppsDepuisExcel(new File(getClass().getResource("/resources/liste_applis.xlsx").getFile()));       
    }

    @Test
    public void recupInfosClarityDepuisExcel()
    {
        handler.recupInfosClarityDepuisExcel(new File(getClass().getResource("/resources/Referentiel_Projets.xlsm").getFile()));
    }

    @Test
    public void recupLotsPicDepuisExcel()
    {
        handler.recupLotsPicDepuisExcel(new File(getClass().getResource("/resources/lots_Pic.xlsx").getFile()));
    }

    @Test
    public void recupChefServiceDepuisExcel()
    {
        handler.recupChefServiceDepuisExcel(new File(getClass().getResource("/resources/Reorg_managers.xlsx").getFile()));
    }

    @Test
    public void recupEditionDepuisExcel()
    {
        handler.recupEditionDepuisExcel(new File(getClass().getResource("/resources/Codification_des_Editions.xlsx").getFile()));
    }
    
    @Test (expected = TechnicalException.class)
    public void saveInfosException() throws Exception
    {        
        Whitebox.invokeMethod(handler, "saveInfos", TypeFichier.RESPSERVICE, TypeColChefServ.class, new File("аз&;:["));
    }
}