package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.Param;

public class TestTypeInfoMail
{
    @Test
    public void testSize()
    {
        assertEquals(5, Param.values().length);
    }
}
