package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColAppsW;

public class TestTypeColAppsW implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(9, TypeColAppsW.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Code Application", TypeColAppsW.CODEAPPS.getValeur());
        assertEquals("Actif", TypeColAppsW.ACTIF.getValeur());
        assertEquals("Libellé", TypeColAppsW.LIB.getValeur());
        assertEquals("Top appli open", TypeColAppsW.OPEN.getValeur());
        assertEquals("Top appli MainFrame", TypeColAppsW.MAINFRAME.getValeur());
        assertEquals("Criticité", TypeColAppsW.CRITICITE.getValeur());
        assertEquals("Vulnérabilitès", TypeColAppsW.VULN.getValeur());
        assertEquals("LDC SonarQube", TypeColAppsW.LDCSONAR.getValeur());
        assertEquals("LDC MainFrame", TypeColAppsW.LDCMAIN.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colCode", TypeColAppsW.CODEAPPS.getNomCol());
        assertEquals("colActif", TypeColAppsW.ACTIF.getNomCol());
        assertEquals("colLib", TypeColAppsW.LIB.getNomCol());
        assertEquals("colOpen", TypeColAppsW.OPEN.getNomCol());
        assertEquals("colMainFrame", TypeColAppsW.MAINFRAME.getNomCol());
        assertEquals("colCrit", TypeColAppsW.CRITICITE.getNomCol());
        assertEquals("colVuln", TypeColAppsW.VULN.getNomCol());
        assertEquals("colLDCSonar", TypeColAppsW.LDCSONAR.getNomCol());
        assertEquals("colLDCMain", TypeColAppsW.LDCMAIN.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColAppsW.LDCSONAR, TypeColAppsW.valueOf(TypeColAppsW.LDCSONAR.toString()));    
    }
}
