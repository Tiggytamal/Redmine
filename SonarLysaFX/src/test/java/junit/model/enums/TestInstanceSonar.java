package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.InstanceSonar;

public class TestInstanceSonar implements TestEnums
{

    @Override
    @Test
    public void testConstructeur()
    {
        assertEquals(InstanceSonar.LEGACY, InstanceSonar.valueOf(InstanceSonar.LEGACY.toString()));
    }

    @Override
    @Test
    public void testSize()
    {
        assertEquals(2, InstanceSonar.values().length);
    }

}
