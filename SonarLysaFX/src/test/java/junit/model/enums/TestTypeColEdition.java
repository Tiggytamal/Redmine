package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColEdition;

/**
 * Classe de test de l'énumération TypeColEdition
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeColEdition implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, TypeColEdition.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Libellé", TypeColEdition.LIBELLE.getValeur());
        assertEquals("Numero de version", TypeColEdition.VERSION.getValeur());
        assertEquals("Commentaire", TypeColEdition.COMMENTAIRE.getValeur());
        assertEquals("Semaine", TypeColEdition.SEMAINE.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colLib", TypeColEdition.LIBELLE.getNomCol());
        assertEquals("colVersion", TypeColEdition.VERSION.getNomCol());
        assertEquals("colComment", TypeColEdition.COMMENTAIRE.getNomCol());
        assertEquals("colSemaine", TypeColEdition.SEMAINE.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColEdition.VERSION, TypeColEdition.valueOf(TypeColEdition.VERSION.toString()));    
    }
}
