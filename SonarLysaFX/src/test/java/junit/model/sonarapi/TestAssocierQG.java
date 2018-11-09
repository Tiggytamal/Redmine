package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.AssocierQG;

public class TestAssocierQG
{
    /*---------- ATTRIBUTS ----------*/

    private AssocierQG modele;
    private AssocierQG modeleNull;
    private static final String GATEID = "gate";
    private static final String PROJECTID = "project";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new AssocierQG(GATEID, PROJECTID);
        modeleNull = new AssocierQG();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetGateId()
    {
        assertEquals(GATEID, modele.getGateId());
        assertNotNull(modeleNull.getGateId());
        assertTrue(modeleNull.getGateId().isEmpty());
    }
    
    @Test
    public void testSetGateId()
    {
        modele.setGateId(NEWVAL);
        assertEquals(NEWVAL, modele.getGateId());
        modeleNull.setGateId(GATEID);
        assertEquals(GATEID, modeleNull.getGateId());
    }
    
    @Test
    public void testGetProjectId()
    {
        assertEquals(PROJECTID, modele.getProjectId());
        assertNotNull(modeleNull.getProjectId());
        assertTrue(modeleNull.getProjectId().isEmpty());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setProjectId(NEWVAL);
        assertEquals(NEWVAL, modele.getProjectId());
        modeleNull.setProjectId(PROJECTID);
        assertEquals(PROJECTID, modeleNull.getProjectId());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}