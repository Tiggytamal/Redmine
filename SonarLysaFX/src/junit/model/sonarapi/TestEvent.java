package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
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
    public void getId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void setId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getId());
    }
    
    @Test
    public void getRk()
    {
        assertEquals(RK, modele.getRk());
        assertNull(modeleNull.getRk());
    }
    
    @Test
    public void setRk()
    {
        modele.setRk(NEWVAL);
        assertEquals(NEWVAL, modele.getRk());
        modeleNull.setRk(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getRk());
    }
    
    @Test
    public void getN()
    {
        assertEquals(N, modele.getN());
        assertNull(modeleNull.getN());
    }
    
    @Test
    public void setN()
    {
        modele.setN(NEWVAL);
        assertEquals(NEWVAL, modele.getN());
        modeleNull.setN(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getN());
    }
    
    @Test
    public void getC()
    {
        assertEquals(C, modele.getC());
        assertNull(modeleNull.getC());
    }
    
    @Test
    public void setC()
    {
        modele.setC(NEWVAL);
        assertEquals(NEWVAL, modele.getC());
        modeleNull.setC(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getC());
    }
    
    @Test
    public void getDt()
    {
        assertEquals(DT, modele.getDt());
        assertNull(modeleNull.getDt());
    }
    
    @Test
    public void setDt()
    {
        modele.setDt(NEWVAL);
        assertEquals(NEWVAL, modele.getDt());
        modeleNull.setDt(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getDt());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}