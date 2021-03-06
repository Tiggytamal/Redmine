package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColVul;

/**
 * Classe de test de l'�num�ration TypeColVul
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class TestTypeColVul implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(9, TypeColVul.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Lot", TypeColVul.LOT.getValeur());
        assertEquals("Severit�", TypeColVul.SEVERITE.getValeur());
        assertEquals("Code Clarity", TypeColVul.CLARITY.getValeur());
        assertEquals("Status", TypeColVul.STATUS.getValeur());
        assertEquals("Message", TypeColVul.MESSAGE.getValeur());
        assertEquals("Date cr�ation", TypeColVul.DATECREA.getValeur());
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

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColVul.DATECREA, TypeColVul.valueOf(TypeColVul.DATECREA.toString()));    
    }
}
