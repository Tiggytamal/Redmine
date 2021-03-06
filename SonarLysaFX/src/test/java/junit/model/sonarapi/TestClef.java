package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Clef;

public class TestClef
{
    /*---------- ATTRIBUTS ----------*/

    private Clef modele;
    private Clef modeleNull;
    private static final String KEY = "key";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Clef(KEY);
        modeleNull = new Clef();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNotNull(modeleNull.getKey());
        assertTrue(modeleNull.getKey().isEmpty());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
