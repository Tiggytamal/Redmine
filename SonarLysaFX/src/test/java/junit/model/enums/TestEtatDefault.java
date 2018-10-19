package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.EtatDefault;

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
        assertEquals(EtatDefault.NOUVELLE, EtatDefault.valueOf(EtatDefault.NOUVELLE.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, EtatDefault.values().length);
    }

}
