package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeDefaut;

public class testTypeDefault implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeDefaut.SONAR, TypeDefaut.valueOf(TypeDefaut.SONAR.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, TypeDefaut.values().length);
    }

}
