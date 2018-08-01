package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColEdition;

public class TestTypeColEdition implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeColEdition.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Libellé", TypeColEdition.LIBELLE.getValeur());
        assertEquals("Numero de version", TypeColEdition.VERSION.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colLib", TypeColEdition.LIBELLE.getNomCol());
        assertEquals("colVersion", TypeColEdition.VERSION.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColEdition.VERSION, TypeColEdition.valueOf(TypeColEdition.VERSION.toString()));    
    }
}
