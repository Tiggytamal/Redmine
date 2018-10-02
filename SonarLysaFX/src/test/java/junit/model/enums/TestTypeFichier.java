package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeDonnee;

public class TestTypeFichier implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(7, TypeDonnee.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeDonnee.APPS, TypeDonnee.valueOf(TypeDonnee.APPS.toString()));    
    }
}
