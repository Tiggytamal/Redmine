package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeParamSpec;

public class TestTypeParamSpec implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeParamSpec.LISTVIEWVERSION, TypeParamSpec.valueOf(TypeParamSpec.LISTVIEWVERSION.toString()));    
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, TypeParamSpec.values().length);
    }

}
