package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.AjouterProjet;

public class TestAjouterProjet
{
    /*---------- ATTRIBUTS ----------*/

    private AjouterProjet modele;
    private AjouterProjet modeleNull;
    private static final String KEY = "key";
    private static final String PROJECT = "projectKey";
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        modele = new AjouterProjet(KEY, PROJECT);
        modeleNull = new AjouterProjet();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNotNull(modeleNull.getKey());
        assertTrue(modeleNull.getKey().isEmpty());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
    
    @Test
    public void testGetProjectKey()
    {
        assertEquals(PROJECT, modele.getProjectKey());
        assertNotNull(modeleNull.getProjectKey());
        assertTrue(modeleNull.getProjectKey().isEmpty());
    }
    
    @Test
    public void testSetProjectKey()
    {
        modele.setProjectKey(NEWVAL);
        assertEquals(NEWVAL, modele.getProjectKey());
        modeleNull.setProjectKey(PROJECT);
        assertEquals(PROJECT, modeleNull.getProjectKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}