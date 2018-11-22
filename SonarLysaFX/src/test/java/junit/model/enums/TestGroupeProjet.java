package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.GroupeProduit;

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
        assertEquals(GroupeProduit.NPC, GroupeProduit.valueOf(GroupeProduit.NPC.toString()));
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, GroupeProduit.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("", GroupeProduit.AUCUN.getValeur());
        assertEquals("NPC", GroupeProduit.NPC.getValeur());
    }

}
