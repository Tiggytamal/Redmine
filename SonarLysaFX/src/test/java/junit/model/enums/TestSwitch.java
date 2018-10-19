package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.Switch;

/**
 * Classe de test de l'numération CDCouCDM
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestSwitch implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Switch.XML, Switch.valueOf(Switch.XML.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, Switch.values().length);
    }
}
