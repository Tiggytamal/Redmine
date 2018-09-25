package junit.control.xml;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import dao.DaoFactory;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.JunitBase;
import model.FichiersXML;
import model.ProprietesXML;
import model.bdd.Edition;
import model.enums.TypeColChefServ;
import model.enums.TypeFichier;
import utilities.Statics;
import utilities.TechnicalException;

@RunWith (JfxRunner.class)
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
        handler.recupererXMLResources(FichiersXML.class);
        handler.recupererXMLResources(ProprietesXML.class);
    }

    @Test
    public void testRecupEditionDepuisExcel()
    {
        DaoFactory.getDao(Edition.class).recupDonneesDepuisExcel(new File(getClass().getResource(Statics.ROOT + "Codification_des_Editions.xlsx").getFile()));
    }
    
    @Test
    public void testRecupProjetsNPCDepuisExcel()
    {
        handler.recupProjetsNPCDepuisExcel(new File(getClass().getResource(Statics.ROOT + "projets_npc.xlsx").getFile()));
    }
    
    @Test (expected = TechnicalException.class)
    public void testSaveInfosException() throws Exception
    {        
        Whitebox.invokeMethod(handler, "saveInfos", TypeFichier.RESPSERVICE, TypeColChefServ.class, new File("аз&;:["));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}