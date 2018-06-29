package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColClarity;

public class TestTypeColClarity
{
    @Test
    public void testSize()
    {
        assertEquals(8, TypeColClarity.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Actif", TypeColClarity.ACTIF.getValeur());
        assertEquals("Code projet", TypeColClarity.CLARITY.getValeur());
        assertEquals("Libellé projet", TypeColClarity.LIBELLE.getValeur());
        assertEquals("Chef de projet", TypeColClarity.CPI.getValeur());
        assertEquals("Edition", TypeColClarity.EDITION.getValeur());
        assertEquals("Direction", TypeColClarity.DIRECTION.getValeur());
        assertEquals("Département", TypeColClarity.DEPARTEMENT.getValeur());
        assertEquals("Service", TypeColClarity.SERVICE.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colActif", TypeColClarity.ACTIF.getNomCol());
        assertEquals("colClarity", TypeColClarity.CLARITY.getNomCol());
        assertEquals("colLib", TypeColClarity.LIBELLE.getNomCol());
        assertEquals("colCpi", TypeColClarity.CPI.getNomCol());
        assertEquals("colEdition", TypeColClarity.EDITION.getNomCol());
        assertEquals("colDir", TypeColClarity.DIRECTION.getNomCol());
        assertEquals("colDepart", TypeColClarity.DEPARTEMENT.getNomCol());
        assertEquals("colService", TypeColClarity.SERVICE.getNomCol());
    }
}
