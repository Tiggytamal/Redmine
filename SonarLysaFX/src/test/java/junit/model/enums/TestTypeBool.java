package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.ParamBool;

public class TestTypeBool
{
    @Test
    public void testSize()
    {
        assertEquals(2, ParamBool.values().length);
    }
}
