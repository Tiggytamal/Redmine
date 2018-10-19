package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.ParamSpec;
import model.enums.TypeParamSpec;

/**
 * Classe de test de l'énumération ParamSpec
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
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
        assertEquals(10, ParamSpec.values().length);
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
        assertFalse(ParamSpec.TEXTEAPPLI.getNom().isEmpty());
        assertFalse(ParamSpec.TEXTERELANCE.getNom().isEmpty());
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
        assertEquals(TypeParamSpec.TEXTAREA, ParamSpec.TEXTEAPPLI.getType());
        assertEquals(TypeParamSpec.TEXTAREA, ParamSpec.TEXTERELANCE.getType());
    }
}
