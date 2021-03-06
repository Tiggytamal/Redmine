package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColClarity;

/**
 * Classe de test de l'�num�ration TypeColClarity
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class TestTypeColClarity implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(8, TypeColClarity.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Actif", TypeColClarity.ACTIF.getValeur());
        assertEquals("Code projet", TypeColClarity.CLARITY.getValeur());
        assertEquals("Libell� projet", TypeColClarity.LIBELLE.getValeur());
        assertEquals("Chef de projet", TypeColClarity.CPI.getValeur());
        assertEquals("Edition", TypeColClarity.EDITION.getValeur());
        assertEquals("Direction", TypeColClarity.DIRECTION.getValeur());
        assertEquals("D�partement", TypeColClarity.DEPARTEMENT.getValeur());
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

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColClarity.SERVICE, TypeColClarity.valueOf(TypeColClarity.SERVICE.toString()));    
    }
}
