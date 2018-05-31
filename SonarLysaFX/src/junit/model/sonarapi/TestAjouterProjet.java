package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
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
        modeleNull.setKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getKey());
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
        modeleNull.setProjectKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getProjectKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}