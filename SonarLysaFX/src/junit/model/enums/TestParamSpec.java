package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.enums.ParamSpec;
import model.enums.TypeParamSpec;

public class TestParamSpec
{
    @Test
    public void testSize()
    {
        assertEquals(7, ParamSpec.values().length);
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
    
    @Test
    public void testgetType()
    {
        assertTrue(ParamSpec.VERSIONS.getType() == TypeParamSpec.LISTVIEWVERSION);
        assertTrue(ParamSpec.TEXTESECURITE.getType() == TypeParamSpec.TEXTAREA);
        assertTrue(ParamSpec.TEXTEDEFECT.getType() == TypeParamSpec.TEXTAREA);
        assertTrue(ParamSpec.RECAPDEFECT.getType() == TypeParamSpec.TEXTAREA);
        assertTrue(ParamSpec.MEMBRESJAVA.getType() == TypeParamSpec.LISTVIEWNOM);
        assertTrue(ParamSpec.MEMBRESDATASTAGE.getType() == TypeParamSpec.LISTVIEWNOM);
        assertTrue(ParamSpec.MEMBRESMAIL.getType() == TypeParamSpec.LISTVIEWNOM);
    }
}