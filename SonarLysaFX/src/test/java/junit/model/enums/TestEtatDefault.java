package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.EtatDefaut;

/**
 * Classe de test de l'numération EtatDefault
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestEtatDefault implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(EtatDefaut.NOUVEAU, EtatDefaut.valueOf(EtatDefaut.NOUVEAU.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, EtatDefaut.values().length);
    }

}
