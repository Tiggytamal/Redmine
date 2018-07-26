package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.JunitBase;
import model.ModelFactory;
import model.ProprietesXML;
import model.enums.TypeColR;
import utilities.TechnicalException;

public class TestProprieteXML extends JunitBase
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
    public void testProprieteXML() throws Exception
    {
        // Test initialisation des map à la construction       
        assertNotNull(propriete.getMapParams());
        assertEquals(0, propriete.getMapParams().size());
        assertNotNull(propriete.getMapParamsBool());
        assertEquals(0, propriete.getMapParamsBool().size());
        assertNotNull(propriete.getMapPlans());
        assertEquals(0, propriete.getMapPlans().size());
        testMap("getMapColsSuivi");
        testMap("getMapColsClarity");
        testMap("getMapColsChefServ");
        testMap("getMapColsPic");
        testMap("getMapColsEdition");
        testMap("getMapColsApps");        
    }
    
    
    @Test
    public void testControleDonnees()
    {
        // Initialisation. mock des méthode de contrôle
        
        // Test all true
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetMapException()
    {
        propriete.getMap(TypcColTest.class);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    private <T> void testMap(String methode) throws Exception
    {
        Map<T, String> map = Whitebox.invokeMethod(propriete, methode);
        assertFalse(map == null);
        assertEquals(0, map.size());
    }
    /*---------- ACCESSEURS ----------*/
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private enum TypcColTest implements Serializable, TypeColR
    {
        VIDE;

        @Override
        public String getValeur()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNomCol()
        {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
