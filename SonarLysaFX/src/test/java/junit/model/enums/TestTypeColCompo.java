package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColCompo;

public class TestTypeColCompo implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, TypeColCompo.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Patrimoine", TypeColCompo.PATRIMOINE.getValeur());
        assertEquals("Sans lots RTC", TypeColCompo.INCONNU.getValeur());
        assertEquals("Lots non livrés", TypeColCompo.NONPROD.getValeur());
        assertEquals("Lots terminés", TypeColCompo.TERMINE.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colPat", TypeColCompo.PATRIMOINE.getNomCol());
        assertEquals("colInco", TypeColCompo.INCONNU.getNomCol());
        assertEquals("colNonProd", TypeColCompo.NONPROD.getNomCol());
        assertEquals("colTermine", TypeColCompo.TERMINE.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColCompo.PATRIMOINE, TypeColCompo.valueOf(TypeColCompo.PATRIMOINE.toString()));    
    }
}
