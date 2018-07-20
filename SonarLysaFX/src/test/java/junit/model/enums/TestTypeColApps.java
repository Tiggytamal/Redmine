package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColApps;

public class TestTypeColApps
{
    @Test
    public void testSize()
    {
        assertEquals(5, TypeColApps.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Code Application", TypeColApps.CODEAPPS.getValeur());
        assertEquals("Actif", TypeColApps.ACTIF.getValeur());
        assertEquals("Libellé", TypeColApps.LIB.getValeur());
        assertEquals("Top appli open", TypeColApps.OPEN.getValeur());
        assertEquals("Top appli MainFrame", TypeColApps.MAINFRAME.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colCode", TypeColApps.CODEAPPS.getNomCol());
        assertEquals("colActif", TypeColApps.ACTIF.getNomCol());
        assertEquals("colLib", TypeColApps.LIB.getNomCol());
        assertEquals("colOpen", TypeColApps.OPEN.getNomCol());
        assertEquals("colMainFrame", TypeColApps.MAINFRAME.getNomCol());
    }
}
