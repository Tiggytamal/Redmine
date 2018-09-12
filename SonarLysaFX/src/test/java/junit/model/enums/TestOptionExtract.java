package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionExtract;

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
