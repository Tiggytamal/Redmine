package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.DeserOption;

public class TestDeserOption implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, DeserOption.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(DeserOption.AUCUNE, DeserOption.valueOf(DeserOption.AUCUNE.toString()));
    }
}
