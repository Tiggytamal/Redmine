package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColUA;

public class TestTypeColUA implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeColUA.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Nom UA", TypeColUA.UA.getValeur());
        assertEquals("Code Application", TypeColUA.APPLI.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colUA", TypeColUA.UA.getNomCol());
        assertEquals("colAppli", TypeColUA.APPLI.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColUA.UA, TypeColUA.valueOf(TypeColUA.UA.toString()));    
    }
}
