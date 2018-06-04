package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.AjouterProjet;

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
    public void getKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void setKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
    
    @Test
    public void getProjectKey()
    {
        assertEquals(PROJECT, modele.getProjectKey());
        assertNull(modeleNull.getProjectKey());
    }
    
    @Test
    public void setProjectKey()
    {
        modele.setProjectKey(NEWVAL);
        assertEquals(NEWVAL, modele.getProjectKey());
        modeleNull.setProjectKey(PROJECT);
        assertEquals(PROJECT, modeleNull.getProjectKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}