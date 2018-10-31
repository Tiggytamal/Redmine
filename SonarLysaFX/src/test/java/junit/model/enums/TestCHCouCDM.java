package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeEdition;

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
        assertEquals(TypeEdition.CDM, TypeEdition.valueOf(TypeEdition.CDM.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeEdition.values().length);
    }
}
