package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionMajCompos;

/**
 * Classe de test de l'numération CDCouCDM
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestOptionMajCompos implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionMajCompos.COMPLETE, OptionMajCompos.valueOf(OptionMajCompos.COMPLETE.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, OptionMajCompos.values().length);
    }
}
