package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.CHCouCDM;

public class TestCHCouCDM
{
    @Test
    public void testSize()
    {
        assertEquals(2, CHCouCDM.values().length);
    }
}
