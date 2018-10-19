package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionExtract;

/**
 * Classe de test de l'énumération OptionExtract
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestOptionExtract implements TestEnums
{  
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, OptionExtract.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionExtract.VULNERABILITES, OptionExtract.valueOf(OptionExtract.VULNERABILITES.toString()));
    }
}
