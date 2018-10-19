package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.CHCouCDM;

/**
 * Classe de test de l'numération CDCouCDM
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestCHCouCDM implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(CHCouCDM.CDM, CHCouCDM.valueOf(CHCouCDM.CDM.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, CHCouCDM.values().length);
    }
}
