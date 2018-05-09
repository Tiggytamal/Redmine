package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.TypeParamSpec;

public class TestTypeParamSpec
{
    @Test
    public void testSize()
    {
        assertEquals(5, TypeParamSpec.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertFalse(TypeParamSpec.VERSIONS.toString().isEmpty());
        assertFalse(TypeParamSpec.TEXTEDEFECT.toString().isEmpty());
        assertFalse(TypeParamSpec.TEXTESECURITE.toString().isEmpty());
        assertFalse(TypeParamSpec.MEMBRESJAVA.toString().isEmpty());
        assertFalse(TypeParamSpec.MEMBRESDTATSTAGE.toString().isEmpty());
    }
}
