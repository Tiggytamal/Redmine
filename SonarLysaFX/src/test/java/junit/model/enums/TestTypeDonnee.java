package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeDonnee;

/**
 * Classe de test de l'énumération TypeDonnee
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeDonnee implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(10, TypeDonnee.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeDonnee.APPS, TypeDonnee.valueOf(TypeDonnee.APPS.toString()));    
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Applications", TypeDonnee.APPS.getValeur());
        assertEquals("Projets Clarity", TypeDonnee.CLARITY.getValeur());
        assertEquals("Chefs de Service", TypeDonnee.RESPSERVICE.getValeur());
        assertEquals("Editions", TypeDonnee.EDITION.getValeur());
        assertEquals("Composants", TypeDonnee.COMPOSANT.getValeur());
        assertEquals("Lots RTC", TypeDonnee.LOTSRTC.getValeur());
        assertEquals("Groupement de Projets", TypeDonnee.GROUPE.getValeur());
        assertEquals("Defaults qualité", TypeDonnee.DEFAULTQUALITE.getValeur());
        assertEquals("Dates de mise à jour des tables", TypeDonnee.DATEMAJ.getValeur());
        assertEquals("Defaults application", TypeDonnee.DEFAULTAPPLI.getValeur());
    }
}
