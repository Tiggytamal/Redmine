package junit.utilities.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import junit.JunitBase;
import model.enums.TypeMetrique;
import utilities.adapter.TypeMetriqueAdapter;

public class TestTypeMetriqueAdapter extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private TypeMetriqueAdapter handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new TypeMetriqueAdapter();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testUnmarshal() throws Exception
    {
        assertNull(handler.unmarshal(null));
        
        assertEquals(TypeMetrique.LOT, handler.unmarshal("lot"));
    }
    
    @Test
    public void testMarshal() throws Exception
    {
        assertNull(handler.marshal(null));
        
        assertEquals("lot", handler.marshal(TypeMetrique.LOT));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
