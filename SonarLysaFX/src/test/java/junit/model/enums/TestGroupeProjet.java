package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.GroupeProjet;

/**
 * Classe de test de l'numération GroupeProjet
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class TestGroupeProjet implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(GroupeProjet.NPC, GroupeProjet.valueOf(GroupeProjet.NPC.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, GroupeProjet.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("", GroupeProjet.AUCUN.getValeur());
        assertEquals("NPC", GroupeProjet.NPC.getValeur());
    }

}
