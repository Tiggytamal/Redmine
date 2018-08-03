package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.ParamBool;

public class TestParamBool implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(ParamBool.VUESSUIVI, ParamBool.valueOf(ParamBool.VUESSUIVI.toString()));    
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, ParamBool.values().length);
    }
    
    @Test
    public void testGetNom()
    {
        assertFalse(ParamBool.VUESSUIVI.getNom().isEmpty());      
        assertFalse(ParamBool.SUPPSONAR.getNom().isEmpty());   
    }

}
