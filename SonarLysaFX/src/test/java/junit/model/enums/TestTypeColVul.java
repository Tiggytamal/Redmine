package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColVul;

public class TestTypeColVul
{
    @Test
    public void testSize()
    {
        assertEquals(8, TypeColVul.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Lot", TypeColVul.LOT.getValeur());
        assertEquals("Severité", TypeColVul.SEVERITE.getValeur());
        assertEquals("Code Clarity", TypeColVul.CLARITY.getValeur());
        assertEquals("Status", TypeColVul.STATUS.getValeur());
        assertEquals("Message", TypeColVul.MESSAGE.getValeur());
        assertEquals("Date création", TypeColVul.DATECREA.getValeur());
        assertEquals("Appli", TypeColVul.APPLI.getValeur());
        assertEquals("Composant", TypeColVul.COMPOSANT.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colLot", TypeColVul.LOT.getNomCol());
        assertEquals("colSeverity", TypeColVul.SEVERITE.getNomCol());
        assertEquals("colClarity", TypeColVul.CLARITY.getNomCol());
        assertEquals("colStatus", TypeColVul.STATUS.getNomCol());
        assertEquals("colMess", TypeColVul.MESSAGE.getNomCol());
        assertEquals("colDateCrea", TypeColVul.DATECREA.getNomCol());
        assertEquals("colAppli", TypeColVul.APPLI.getNomCol());
        assertEquals("colComp", TypeColVul.COMPOSANT.getNomCol());
    }
}
