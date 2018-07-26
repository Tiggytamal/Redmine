package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.ParamSpec;
import model.enums.TypeParamSpec;

public class TestParamSpec
{
    @Test
    public void testSize()
    {
        assertEquals(8, ParamSpec.values().length);
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
        assertFalse(ParamSpec.VERSIONSCOMPOSANTS.toString().isEmpty());
    }
    
    @Test
    public void testGetType()
    {
        assertEquals(TypeParamSpec.LISTVIEWVERSION, ParamSpec.VERSIONS.getType());
        assertEquals(TypeParamSpec.TEXTAREA, ParamSpec.TEXTESECURITE.getType());
        assertEquals(TypeParamSpec.TEXTAREA, ParamSpec.TEXTEDEFECT.getType());
        assertEquals(TypeParamSpec.TEXTAREA, ParamSpec.RECAPDEFECT.getType());
        assertEquals(TypeParamSpec.LISTVIEWNOM, ParamSpec.MEMBRESJAVA.getType());
        assertEquals(TypeParamSpec.LISTVIEWNOM, ParamSpec.MEMBRESDATASTAGE.getType());
        assertEquals(TypeParamSpec.LISTVIEWNOM, ParamSpec.MEMBRESMAIL.getType());
        assertEquals(TypeParamSpec.LISTVIEWCOMPO, ParamSpec.VERSIONSCOMPOSANTS.getType());
    }
}
