package junit.control.xml;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import control.xml.ControlXML;
import dao.DaoFactory;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.JunitBase;
import model.ProprietesXML;
import model.bdd.Edition;
import utilities.Statics;

@RunWith(JfxRunner.class)
public class TestControlXML extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlXML handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        handler = new ControlXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @TestInJfxThread
    public void testRecupererXMLResources()
    {
        handler.recupererXMLResources(ProprietesXML.class);
    }

    @Test
    public void testRecupEditionDepuisExcel()
    {
        DaoFactory.getDao(Edition.class).recupDonneesDepuisExcel(new File(getClass().getResource(Statics.ROOT + "Codification_des_Editions.xlsx").getFile()));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
