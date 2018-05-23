package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.ParamSpec;

public class TestParamSpec
{
    @Test
    public void testSize()
    {
        assertEquals(6, ParamSpec.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertFalse(ParamSpec.VERSIONS.toString().isEmpty());
        assertFalse(ParamSpec.TEXTEDEFECT.toString().isEmpty());
        assertFalse(ParamSpec.TEXTESECURITE.toString().isEmpty());
        assertFalse(ParamSpec.MEMBRESJAVA.toString().isEmpty());
        assertFalse(ParamSpec.MEMBRESDATASTAGE.toString().isEmpty());
        assertFalse(ParamSpec.RECAPDEFECT.toString().isEmpty());
    }
}
