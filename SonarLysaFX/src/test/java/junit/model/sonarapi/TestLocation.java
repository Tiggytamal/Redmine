package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Location;
import model.rest.sonarapi.TextRange;

public class TestLocation
{
    /*---------- ATTRIBUTS ----------*/

    private Location modele;
    private Location modeleNull;
    private static final TextRange TEXTRANGE = new TextRange();
    private static final String MSG = "msg";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Location(TEXTRANGE, MSG);
        modeleNull = new Location();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetTextRange()
    {
        assertEquals(TEXTRANGE, modele.getTextRange());
        assertNotNull(modeleNull.getTextRange());
    }
    
    @Test
    public void testSetTotal()
    {
        modele.setTextRange(null);
        assertNotNull(modele.getTextRange());
        modeleNull.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modeleNull.getTextRange());
    }
    
    @Test
    public void testGetMsg()
    {
        assertEquals(MSG, modele.getMsg());
        assertNotNull(modeleNull.getMsg());
        assertTrue(modeleNull.getMsg().isEmpty());
    }
    
    @Test
    public void testSetMsg()
    {
        modele.setMsg(NEWVAL);
        assertEquals(NEWVAL, modele.getMsg());
        modeleNull.setMsg(MSG);
        assertEquals(MSG, modeleNull.getMsg());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
