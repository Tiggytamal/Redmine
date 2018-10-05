package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColGrProjet;

public class TestTypeColNPC implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(1, TypeColGrProjet.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Nom Projet", TypeColGrProjet.NOM.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colNom", TypeColGrProjet.NOM.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColGrProjet.NOM, TypeColGrProjet.valueOf(TypeColGrProjet.NOM.toString()));    
    }
}
