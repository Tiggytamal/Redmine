package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeFichier;

public class TestTypeFichier
{
    @Test
    public void testSize()
    {
        assertEquals(5, TypeFichier.values().length);
    }
}