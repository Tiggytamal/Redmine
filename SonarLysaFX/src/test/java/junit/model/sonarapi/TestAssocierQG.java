package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.AssocierQG;

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
    public void getGateId()
    {
        assertEquals(GATEID, modele.getGateId());
        assertNull(modeleNull.getGateId());
    }
    
    @Test
    public void setGateId()
    {
        modele.setGateId(NEWVAL);
        assertEquals(NEWVAL, modele.getGateId());
        modeleNull.setGateId(GATEID);
        assertEquals(GATEID, modeleNull.getGateId());
    }
    
    @Test
    public void getProjectId()
    {
        assertEquals(PROJECTID, modele.getProjectId());
        assertNull(modeleNull.getProjectId());
    }
    
    @Test
    public void setKey()
    {
        modele.setProjectId(NEWVAL);
        assertEquals(NEWVAL, modele.getProjectId());
        modeleNull.setProjectId(PROJECTID);
        assertEquals(PROJECTID, modeleNull.getProjectId());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}