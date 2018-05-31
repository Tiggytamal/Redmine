package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void getIndex()
    {
        assertEquals(INDEX, modele.getIndex());
        assertEquals(0, modeleNull.getIndex());
    }
    
    @Test
    public void setIndex()
    {
        modele.setIndex(50);
        assertEquals(50, modele.getIndex());
        modeleNull.setIndex(INDEX);
        assertEquals(INDEX, modeleNull.getIndex());
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