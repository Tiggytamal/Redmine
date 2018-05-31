package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeInfoMail;

public class TestTypeInfoMail
{
    @Test
    public void testSize()
    {
        assertEquals(5, TypeInfoMail.values().length);
    }
}
