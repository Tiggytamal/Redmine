package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Periode;

public class TestPeriode
{
    /*---------- ATTRIBUTS ----------*/

    private Periode modele;
    private Periode modeleNull;
    private static final int INDEX = 10;
    private static final String VALEUR = "valeur";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Periode(INDEX, VALEUR);
        modeleNull = new Periode();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetIndex()
    {
        assertEquals(INDEX, modele.getIndex());
        assertEquals(0, modeleNull.getIndex());
    }
    
    @Test
    public void testSetIndex()
    {
        modele.setIndex(50);
        assertEquals(50, modele.getIndex());
        modeleNull.setIndex(INDEX);
        assertEquals(INDEX, modeleNull.getIndex());
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