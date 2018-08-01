package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColChefServ;

public class TestTypeColChefServ implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, TypeColChefServ.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Direction", TypeColChefServ.DIRECTION.getValeur());
        assertEquals("Département", TypeColChefServ.DEPARTEMENT.getValeur());
        assertEquals("Service", TypeColChefServ.SERVICE.getValeur());
        assertEquals("Filière", TypeColChefServ.FILIERE.getValeur());
        assertEquals("Manager", TypeColChefServ.MANAGER.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colDir", TypeColChefServ.DIRECTION.getNomCol());
        assertEquals("colDepart", TypeColChefServ.DEPARTEMENT.getNomCol());
        assertEquals("colService", TypeColChefServ.SERVICE.getNomCol());
        assertEquals("colFil", TypeColChefServ.FILIERE.getNomCol());
        assertEquals("colManager", TypeColChefServ.MANAGER.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColChefServ.DEPARTEMENT, TypeColChefServ.valueOf(TypeColChefServ.DEPARTEMENT.toString()));    
    }
}