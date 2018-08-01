package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.CHCouCDM;

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
