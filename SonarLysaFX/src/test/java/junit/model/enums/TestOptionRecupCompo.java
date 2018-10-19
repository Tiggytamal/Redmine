package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionRecupCompo;

/**
 * Classe de test de l'énumération OptionRecupCompo
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestOptionRecupCompo implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, OptionRecupCompo.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionRecupCompo.TERMINE, OptionRecupCompo.valueOf(OptionRecupCompo.TERMINE.toString()));
    }
}
