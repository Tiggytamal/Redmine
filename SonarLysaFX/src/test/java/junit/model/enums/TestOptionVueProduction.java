package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionVueProduction;
import utilities.Statics;

/**
 * Classe de test de l'énumération OptionVueProduction
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestOptionVueProduction implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, OptionVueProduction.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionVueProduction.ALL, OptionVueProduction.valueOf(OptionVueProduction.ALL.toString()));
    }

    @Test
    public void testGetTitre()
    {
        assertEquals(Statics.EMPTY, OptionVueProduction.ALL.getTitre());
        assertEquals(" (DataStage)", OptionVueProduction.DATASTAGE.getTitre());
    }
}
