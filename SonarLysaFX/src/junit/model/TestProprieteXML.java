package junit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.ModelFactory;
import model.ProprietesXML;

public class TestProprieteXML
{
    /*---------- ATTRIBUTS ----------*/


    private ProprietesXML propriete;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        propriete = ModelFactory.getModel(ProprietesXML.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void proprieteXML() throws Exception
    {
        // Test initialisation des map à la construction       
        assertFalse(propriete.getMapParams() == null);
        assertTrue(propriete.getMapParams().isEmpty());
        assertFalse(propriete.getMapParamsBool() == null);
        assertTrue(propriete.getMapParamsBool().isEmpty());
        assertFalse(propriete.getMapPlans() == null);
        assertTrue(propriete.getMapPlans().isEmpty());
        testMap("getMapColsSuivi");
        testMap("getMapColsClarity");
        testMap("getMapColsChefServ");
        testMap("getMapColsPic");
        testMap("getMapColsEdition");
        testMap("getMapColsApps");        
    }
    
    
    @Test
    public void controleDonnees()
    {
        // Initialisation. mock des méthode de contrôle
        
        // Test all true
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    private <T> void testMap(String methode) throws Exception
    {
        Map<T, String> map = Whitebox.invokeMethod(propriete, methode);
        assertFalse(map == null);
        assertTrue(map.isEmpty());
    }
    /*---------- ACCESSEURS ----------*/
}
