package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Parametre;

public class TestParametre
{
    /*---------- ATTRIBUTS ----------*/

    private Parametre modele;
    private Parametre modeleNull;
    private static final String CLEF = "clef";
    private static final String VALEUR = "valeur";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Parametre(CLEF, VALEUR);
        modeleNull = new Parametre();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetClef()
    {
        assertEquals(CLEF, modele.getClef());
        assertNotNull(modeleNull.getClef());
        assertTrue(modeleNull.getClef().isEmpty());
    }
    
    @Test
    public void testSetClef()
    {
        modele.setClef(NEWVAL);
        assertEquals(NEWVAL, modele.getClef());
        modeleNull.setClef(CLEF);
        assertEquals(CLEF, modeleNull.getClef());
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals(VALEUR, modele.getValeur());
        assertNotNull(modeleNull.getValeur());
        assertTrue(modeleNull.getValeur().isEmpty());
    }
    
    @Test
    public void testSetValeur()
    {
        modele.setValeur(NEWVAL);
        assertEquals(NEWVAL, modele.getValeur());
        modeleNull.setValeur(VALEUR);
        assertEquals(VALEUR, modeleNull.getValeur());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
