package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColChefServ;

public class TestTypeColChefServ
{
    @Test
    public void testSize()
    {
        assertEquals(5, TypeColChefServ.values().length);
    }
    
    @Test
    public void getValeur()
    {
        assertEquals("Direction", TypeColChefServ.DIRECTION.getValeur());
        assertEquals("D�partement", TypeColChefServ.DEPARTEMENT.getValeur());
        assertEquals("Service", TypeColChefServ.SERVICE.getValeur());
        assertEquals("Fili�re", TypeColChefServ.FILIERE.getValeur());
        assertEquals("Manager", TypeColChefServ.MANAGER.getValeur());
    }
    
    @Test
    public void getNomCol()
    {
        assertEquals("colDir", TypeColChefServ.DIRECTION.getNomCol());
        assertEquals("colDepart", TypeColChefServ.DEPARTEMENT.getNomCol());
        assertEquals("colService", TypeColChefServ.SERVICE.getNomCol());
        assertEquals("colFil", TypeColChefServ.FILIERE.getNomCol());
        assertEquals("colManager", TypeColChefServ.MANAGER.getNomCol());
    }
}