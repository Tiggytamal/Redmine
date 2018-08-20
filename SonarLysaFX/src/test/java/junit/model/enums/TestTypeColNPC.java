package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColNPC;

public class TestTypeColNPC implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(1, TypeColNPC.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Nom Projet", TypeColNPC.NOM.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colNom", TypeColNPC.NOM.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColNPC.NOM, TypeColNPC.valueOf(TypeColNPC.NOM.toString()));    
    }
}
