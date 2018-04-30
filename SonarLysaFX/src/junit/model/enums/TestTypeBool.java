package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeBool;

public class TestTypeBool
{
    @Test
    public void testSize()
    {
        assertEquals(1, TypeBool.values().length);
    }
}
