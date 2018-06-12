package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Condition;
import model.sonarapi.StatusPeriode;
import model.sonarapi.StatusProjet;

public class TestStatusProjet
{
    /*---------- ATTRIBUTS ----------*/

    private StatusProjet modele;
    private StatusProjet modeleNull;
    private static final String STATUS = "status";
    private static final List<Condition> CONDITIONS = new ArrayList<>();
    private static final List<StatusPeriode> PROJETS = new ArrayList<>();

    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        CONDITIONS.add(new Condition());
        PROJETS.add(new StatusPeriode());
        modele = new StatusProjet(STATUS, CONDITIONS, PROJETS);
        modeleNull = new StatusProjet();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNull(modeleNull.getStatus());
    }
    
    @Test
    public void setStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(STATUS);
        assertEquals(STATUS, modeleNull.getStatus());
    }
    
    @Test
    public void getConditions()
    {
        assertEquals(CONDITIONS, modele.getConditions());
        assertNotNull(modeleNull.getConditions());
        assertTrue(modeleNull.getConditions().isEmpty());
    }
    
    @Test
    public void setConditions()
    {
        modele.setConditions(null);
        assertNull(modele.getConditions());
        modeleNull.setConditions(CONDITIONS);
        assertEquals(CONDITIONS, modeleNull.getConditions());
    }
    
    @Test
    public void getPeriodes()
    {
        assertEquals(PROJETS, modele.getPeriodes());
        assertNotNull(modeleNull.getPeriodes());
        assertTrue(modeleNull.getPeriodes().isEmpty());
    }
    
    @Test
    public void setPeriodes()
    {
        modele.setPeriodes(null);
        assertNull(modele.getPeriodes());
        modeleNull.setPeriodes(PROJETS);
        assertEquals(PROJETS, modeleNull.getPeriodes());
    }
}