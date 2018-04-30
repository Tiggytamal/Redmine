package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColApps;

public class TestTypeColApps
{
    @Test
    public void testSize()
    {
        assertEquals(2, TypeColApps.values().length);
    }
    
    @Test
    public void getValeur()
    {
        assertEquals("Code Application", TypeColApps.CODEAPPS.getValeur());
        assertEquals("Actif", TypeColApps.ACTIF.getValeur());
    }
    
    @Test
    public void getNomCol()
    {
        assertEquals("colApps", TypeColApps.CODEAPPS.getNomCol());
        assertEquals("colActif", TypeColApps.ACTIF.getNomCol());
    }
}
