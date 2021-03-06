package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.StatusPeriode;

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
    public void testGetIndex()
    {
        assertEquals(INDEX, modele.getIndex());
        assertEquals(0, modeleNull.getIndex());
    }
    
    @Test
    public void testSetIndex()
    {
        modele.setIndex(20);
        assertEquals(20, modele.getIndex());
        modeleNull.setIndex(INDEX);
        assertEquals(INDEX, modeleNull.getIndex());
    }
    
    @Test
    public void testGetMode()
    {
        assertEquals(MODE, modele.getMode());
        assertNotNull(modeleNull.getMode());
        assertTrue(modeleNull.getMode().isEmpty());
    }
    
    @Test
    public void testSetMode()
    {
        modele.setMode(NEWVAL);
        assertEquals(NEWVAL, modele.getMode());
        modeleNull.setMode(MODE);
        assertEquals(MODE, modeleNull.getMode());
    }
    
    @Test
    public void testGetDate()
    {
        assertEquals(DATE, modele.getDate());
        assertNotNull(modeleNull.getDate());
        assertTrue(modeleNull.getDate().isEmpty());
    }
    
    @Test
    public void testSetDate()
    {
        modele.setDate(NEWVAL);
        assertEquals(NEWVAL, modele.getDate());
        modeleNull.setDate(DATE);
        assertEquals(DATE, modeleNull.getDate());
    }
    
    @Test
    public void testGetParameter()
    {
        assertEquals(PARAMETER, modele.getParameter());
        assertNotNull(modeleNull.getParameter());
        assertTrue(modeleNull.getParameter().isEmpty());
    }
    
    @Test
    public void testSetParameter()
    {
        modele.setParameter(NEWVAL);
        assertEquals(NEWVAL, modele.getParameter());
        modeleNull.setParameter(PARAMETER);
        assertEquals(PARAMETER, modeleNull.getParameter());
    }
}