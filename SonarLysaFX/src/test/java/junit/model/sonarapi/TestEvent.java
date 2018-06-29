package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Event;

public class TestEvent
{
    /*---------- ATTRIBUTS ----------*/

    private Event modele;
    private Event modeleNull;
    private static final String ID = "id";
    private static final String RK = "rk";
    private static final String N = "n";
    private static final String C = "c";
    private static final String DT = "dt";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Event(ID, RK, N, C, DT);
        modeleNull = new Event();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void testSetId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(ID);
        assertEquals(ID, modeleNull.getId());
    }
    
    @Test
    public void testGetRk()
    {
        assertEquals(RK, modele.getRk());
        assertNull(modeleNull.getRk());
    }
    
    @Test
    public void testSetRk()
    {
        modele.setRk(NEWVAL);
        assertEquals(NEWVAL, modele.getRk());
        modeleNull.setRk(RK);
        assertEquals(RK, modeleNull.getRk());
    }
    
    @Test
    public void testGetN()
    {
        assertEquals(N, modele.getN());
        assertNull(modeleNull.getN());
    }
    
    @Test
    public void testSetN()
    {
        modele.setN(NEWVAL);
        assertEquals(NEWVAL, modele.getN());
        modeleNull.setN(N);
        assertEquals(N, modeleNull.getN());
    }
    
    @Test
    public void testGetC()
    {
        assertEquals(C, modele.getC());
        assertNull(modeleNull.getC());
    }
    
    @Test
    public void testSetC()
    {
        modele.setC(NEWVAL);
        assertEquals(NEWVAL, modele.getC());
        modeleNull.setC(C);
        assertEquals(C, modeleNull.getC());
    }
    
    @Test
    public void testGetDt()
    {
        assertEquals(DT, modele.getDt());
        assertNull(modeleNull.getDt());
    }
    
    @Test
    public void testSetDt()
    {
        modele.setDt(NEWVAL);
        assertEquals(NEWVAL, modele.getDt());
        modeleNull.setDt(DT);
        assertEquals(DT, modeleNull.getDt());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}