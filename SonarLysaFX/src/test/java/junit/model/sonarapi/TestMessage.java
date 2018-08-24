package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Message;

public class TestMessage
{
    /*---------- ATTRIBUTS ----------*/

    private Message modele;
    private Message modeleNull;
    private static final String MSG = "msg";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Message(MSG);
        modeleNull = new Message();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
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
