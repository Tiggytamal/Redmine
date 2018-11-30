package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColSuiviApps;

/**
 * Classe de test de l'numération TypeColSuiviApps
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestTypeColSuiviApps implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColSuiviApps.COMPO, TypeColSuiviApps.valueOf(TypeColSuiviApps.COMPO.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, TypeColSuiviApps.values().length);
    }

    @Test
    public void testGetValeur()
    {
        assertEquals("Composant", TypeColSuiviApps.COMPO.getValeur());
        assertEquals("Code actuel", TypeColSuiviApps.ACTUEL.getValeur());
        assertEquals("Code à valoriser", TypeColSuiviApps.NEW.getValeur());
        assertEquals("Etat", TypeColSuiviApps.ETAT.getValeur());
        assertEquals("Lot", TypeColSuiviApps.LOT.getValeur());
    }

    @Test
    public void testGetNomCol()
    {
        assertEquals("colCompo", TypeColSuiviApps.COMPO.getNomCol());
        assertEquals("colActuel", TypeColSuiviApps.ACTUEL.getNomCol());
        assertEquals("colNew", TypeColSuiviApps.NEW.getNomCol());
        assertEquals("colEtat", TypeColSuiviApps.ETAT.getNomCol());
        assertEquals("colLot", TypeColSuiviApps.LOT.getNomCol());
    }
}
