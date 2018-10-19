package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColPbApps;

/**
 * Classe de test de l'énumération TypeColPbApps
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeColPbApps implements TestEnums
{
    @Test
    public void testGetValeur()
    {
        assertEquals("Code Composant", TypeColPbApps.CODE.getValeur());
        assertEquals("Code Application", TypeColPbApps.APPLI.getValeur());
        assertEquals("Etat Application", TypeColPbApps.ETATAPPLI.getValeur());
        assertEquals("Lot RTC", TypeColPbApps.LOT.getValeur());
        assertEquals("Cpi lot", TypeColPbApps.CPILOT.getValeur());
        assertEquals("Département", TypeColPbApps.DEP.getValeur());
        assertEquals("Service", TypeColPbApps.SERVICE.getValeur());
        assertEquals("Chef de Service", TypeColPbApps.CHEFSERV.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colCode", TypeColPbApps.CODE.getNomCol());
        assertEquals("colAppli", TypeColPbApps.APPLI.getNomCol());
        assertEquals("colEtatAppli", TypeColPbApps.ETATAPPLI.getNomCol());
        assertEquals("colLot", TypeColPbApps.LOT.getNomCol());
        assertEquals("colCpiLot", TypeColPbApps.CPILOT.getNomCol());
        assertEquals("colDep", TypeColPbApps.DEP.getNomCol());
        assertEquals("colService", TypeColPbApps.SERVICE.getNomCol());
        assertEquals("colChefServ", TypeColPbApps.CHEFSERV.getNomCol());
    }
    
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColPbApps.DEP, TypeColPbApps.valueOf(TypeColPbApps.DEP.toString()));         
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(8, TypeColPbApps.values().length);       
    }

}
