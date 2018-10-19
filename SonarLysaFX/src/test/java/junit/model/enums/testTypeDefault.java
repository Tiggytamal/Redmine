package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeDefault;

public class testTypeDefault implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeDefault.SONAR, TypeDefault.valueOf(TypeDefault.SONAR.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, TypeDefault.values().length);
    }

}
