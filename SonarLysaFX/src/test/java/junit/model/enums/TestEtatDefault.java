package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.EtatDefaut;

/**
 * Classe de test de l'num�ration EtatDefault
 * 
 * @author ETP8137 - Gr�goier Mathon
 * @since 1.0
 *
 */
public class TestEtatDefault implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(EtatDefaut.NOUVELLE, EtatDefaut.valueOf(EtatDefaut.NOUVELLE.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, EtatDefaut.values().length);
    }

}
