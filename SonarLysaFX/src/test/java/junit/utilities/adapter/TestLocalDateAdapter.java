package junit.utilities.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import org.junit.Test;

import junit.JunitBase;
import utilities.adapter.LocalDateAdapter;

public class TestLocalDateAdapter extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private LocalDateAdapter handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new LocalDateAdapter();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testUnmarshal() throws Exception
    {
        assertNull(handler.unmarshal(null));
        
        assertEquals(LocalDate.of(2018, 8, 12), handler.unmarshal("2018-08-12"));
    }
    
    @Test
    public void testMarshal() throws Exception
    {
        assertNull(handler.marshal(null));
        
        assertEquals("2018-08-12", handler.marshal(LocalDate.of(2018, 8, 12)));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
