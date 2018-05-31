package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Parametre;

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
    public void getClef()
    {
        assertEquals(CLEF, modele.getClef());
        assertNull(modeleNull.getClef());
    }
    
    @Test
    public void setClef()
    {
        modele.setClef(NEWVAL);
        assertEquals(NEWVAL, modele.getClef());
        modeleNull.setClef(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getClef());
    }
    
    @Test
    public void getValeur()
    {
        assertEquals(VALEUR, modele.getValeur());
        assertNull(modeleNull.getValeur());
    }
    
    @Test
    public void setValeur()
    {
        modele.setValeur(NEWVAL);
        assertEquals(NEWVAL, modele.getValeur());
        modeleNull.setValeur(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getValeur());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
