package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColPbApps;

public class TestTypeColPbApps implements TestEnums
{
    @Test
    public void testGetValeur()
    {
        assertEquals("Code Application", TypeColPbApps.CODEAPPS.getValeur());
        assertEquals("Actif", TypeColPbApps.ACTIF.getValeur());
        assertEquals("Libellé", TypeColPbApps.LIB.getValeur());
        assertEquals("Top appli open", TypeColPbApps.OPEN.getValeur());
        assertEquals("Top appli MainFrame", TypeColPbApps.MAINFRAME.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colCode", TypeColPbApps.CODEAPPS.getNomCol());
        assertEquals("colActif", TypeColPbApps.ACTIF.getNomCol());
        assertEquals("colLib", TypeColPbApps.LIB.getNomCol());
        assertEquals("colOpen", TypeColPbApps.OPEN.getNomCol());
        assertEquals("colMainFrame", TypeColPbApps.MAINFRAME.getNomCol());
    }
    
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColPbApps.MAINFRAME, TypeColPbApps.valueOf(TypeColPbApps.MAINFRAME.toString()));         
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, TypeColPbApps.values().length);       
    }

}
