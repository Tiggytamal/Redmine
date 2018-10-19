package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeVersion;

public class TestTypeVersion implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeVersion.RELEASE, TypeVersion.valueOf(TypeVersion.RELEASE.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeVersion.values().length);
    }

}
