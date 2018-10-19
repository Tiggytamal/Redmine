package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionDeser;

/**
 * Classe de test de l'énumération OptionDeser
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestOptionDeser implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, OptionDeser.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionDeser.AUCUNE, OptionDeser.valueOf(OptionDeser.AUCUNE.toString()));
    }
}
