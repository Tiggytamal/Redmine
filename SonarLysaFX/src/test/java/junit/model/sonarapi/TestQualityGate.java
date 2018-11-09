package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.QualityGate;

public class TestQualityGate
{
    /*---------- ATTRIBUTS ----------*/

    private QualityGate modele;
    private QualityGate modeleNull;
    private static final String ID = "10";
    private static final String NAME = "20";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new QualityGate(ID, NAME);
        modeleNull = new QualityGate();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetId()
    {
        assertEquals(ID, modele.getId());
        assertNotNull(modeleNull.getId());
        assertTrue(modeleNull.getId().isEmpty());
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
    public void testGetName()
    {
        assertEquals(NAME, modele.getName());
        assertNotNull(modeleNull.getName());
        assertTrue(modeleNull.getName().isEmpty());
    }
    
    @Test
    public void testSetName()
    {
        modele.setName(NEWVAL);
        assertEquals(NEWVAL, modele.getName());
        modeleNull.setName(NAME);
        assertEquals(NAME, modeleNull.getName());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
