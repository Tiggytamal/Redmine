package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColProduit;

/**
 * Classe de test de l'énumération TypeColGrProjet
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeColGrProjet implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeColProduit.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Nom Projet", TypeColProduit.NOM.getValeur());
        assertEquals("Groupe", TypeColProduit.GROUPE.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colNom", TypeColProduit.NOM.getNomCol());
        assertEquals("colGroupe", TypeColProduit.GROUPE.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColProduit.NOM, TypeColProduit.valueOf(TypeColProduit.NOM.toString()));    
    }
}
