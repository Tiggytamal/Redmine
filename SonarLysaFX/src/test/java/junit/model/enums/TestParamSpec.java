package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.ParamSpec;
import model.enums.TypeParamSpec;

public class TestParamSpec implements TestEnums 
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(ParamSpec.VERSIONS, ParamSpec.valueOf(ParamSpec.VERSIONS.toString()));        
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(8, ParamSpec.values().length);
    }
    
    @Test
    public void testGetNom()
    {
        assertFalse(ParamSpec.VERSIONS.getNom().isEmpty());
        assertFalse(ParamSpec.TEXTEDEFECT.getNom().isEmpty());
        assertFalse(ParamSpec.TEXTESECURITE.getNom().isEmpty());
        assertFalse(ParamSpec.MEMBRESJAVA.getNom().isEmpty());
        assertFalse(ParamSpec.MEMBRESDATASTAGE.getNom().isEmpty());
        assertFalse(ParamSpec.RECAPDEFECT.getNom().isEmpty());
        assertFalse(ParamSpec.VERSIONSCOMPOSANTS.getNom().isEmpty());
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
