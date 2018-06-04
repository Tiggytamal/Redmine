package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.StatusPeriode;

public class TestStatusPeriode
{
    /*---------- ATTRIBUTS ----------*/

    private StatusPeriode modele;
    private StatusPeriode modeleNull;
    private static final int INDEX = 10;
    private static final String MODE = "mode";
    private static final String DATE = "date";
    private static final String PARAMETER = "parameter";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new StatusPeriode(INDEX, MODE, DATE, PARAMETER);
        modeleNull = new StatusPeriode();
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
        modele.setIndex(20);
        assertEquals(20, modele.getIndex());
        modeleNull.setIndex(INDEX);
        assertEquals(INDEX, modeleNull.getIndex());
    }
    
    @Test
    public void getMode()
    {
        assertEquals(MODE, modele.getMode());
        assertNull(modeleNull.getMode());
    }
    
    @Test
    public void setMode()
    {
        modele.setMode(NEWVAL);
        assertEquals(NEWVAL, modele.getMode());
        modeleNull.setMode(MODE);
        assertEquals(MODE, modeleNull.getMode());
    }
    
    @Test
    public void getDate()
    {
        assertEquals(DATE, modele.getDate());
        assertNull(modeleNull.getDate());
    }
    
    @Test
    public void setDate()
    {
        modele.setDate(NEWVAL);
        assertEquals(NEWVAL, modele.getDate());
        modeleNull.setDate(DATE);
        assertEquals(DATE, modeleNull.getDate());
    }
    
    @Test
    public void getParameter()
    {
        assertEquals(PARAMETER, modele.getParameter());
        assertNull(modeleNull.getParameter());
    }
    
    @Test
    public void setParameter()
    {
        modele.setParameter(NEWVAL);
        assertEquals(NEWVAL, modele.getParameter());
        modeleNull.setParameter(PARAMETER);
        assertEquals(PARAMETER, modeleNull.getParameter());
    }
}