package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColGrProjet;

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
        assertEquals(2, TypeColGrProjet.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Nom Projet", TypeColGrProjet.NOM.getValeur());
        assertEquals("Groupe", TypeColGrProjet.GROUPE.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colNom", TypeColGrProjet.NOM.getNomCol());
        assertEquals("colGroupe", TypeColGrProjet.GROUPE.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColGrProjet.NOM, TypeColGrProjet.valueOf(TypeColGrProjet.NOM.toString()));    
    }
}
