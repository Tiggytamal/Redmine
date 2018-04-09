package junit.control.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
public class ControlXMLTest
{
    private ControlXML handler;

    @Before
    public void init()
    {
        handler = new ControlXML();
    }

    @Test
    @TestInJfxThread
    public void recuprerParamXML() throws InvalidFormatException, JAXBException, IOException
    {
        handler.recupererXML(FichiersXML.class);
        handler.recupererXML(ProprietesXML.class);
    }

    @Test
    public void recupListeAppsDepuisExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.recupListeAppsDepuisExcel(new File(getClass().getResource("/resources/liste_applis.xlsx").getFile()));       
    }

    @Test
    public void recupInfosClarityDepuisExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.recupInfosClarityDepuisExcel(new File(getClass().getResource("/resources/Codification_des_Editions.xls").getFile()));
    }

    @Test
    public void recupLotsPicDepuisExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.recupLotsPicDepuisExcel(new File(getClass().getResource("/resources/lots_Pic.xlsx").getFile()));
    }

    @Test
    public void recupChefServiceDepuisExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.recupChefServiceDepuisExcel(new File(getClass().getResource("/resources/liste applis.xlsx").getFile()));
    }

    @Test
    public void recupEditionDepuisExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.recupEditionDepuisExcel(new File(getClass().getResource("/resources/liste applis.xlsx").getFile()));
    }
    
    @Test (expected = TechnicalException.class)
    public void saveInfosException() throws Exception
    {        
        Whitebox.invokeMethod(handler, "saveInfos", TypeFichier.RESPSERVICE, TypeColChefServ.class, new File("a"));
    }
}