package junit.utilities.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalTime;

import org.junit.Test;

import junit.JunitBase;
import utilities.adapter.LocalTimeAdapter;

/**
 * Classe de test de LocalTimeAdapter
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TestLocalTimeAdapter extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private LocalTimeAdapter handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init()
    {
        handler = new LocalTimeAdapter();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUnmarshal() throws Exception
    {
        assertNull(handler.unmarshal(null));
        
        assertEquals(LocalTime.of(10, 10, 10), handler.unmarshal("10:10:10"));
    }
    
    @Test
    public void testMarshal() throws Exception
    {
        assertNull(handler.marshal(null));
        
        assertEquals("10:10:10", handler.marshal(LocalTime.of(10, 10, 10)));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
