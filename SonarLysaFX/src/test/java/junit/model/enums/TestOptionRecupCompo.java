package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionRecupCompo;

public class TestOptionRecupCompo implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, OptionRecupCompo.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionRecupCompo.TERMINE, OptionRecupCompo.valueOf(OptionRecupCompo.TERMINE.toString()));
    }
}
