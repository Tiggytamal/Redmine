package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeFichier;

public class TestTypeFichier implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(7, TypeFichier.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeFichier.APPS, TypeFichier.valueOf(TypeFichier.APPS.toString()));    
    }
}
