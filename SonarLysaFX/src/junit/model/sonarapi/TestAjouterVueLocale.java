package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.AjouterVueLocale;

public class TestAjouterVueLocale
{
    /*---------- ATTRIBUTS ----------*/

    private AjouterVueLocale modele;
    private AjouterVueLocale modeleNull;
    private static final String KEY = "key";
    private static final String PROJECT = "projectKey";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new AjouterVueLocale(KEY, PROJECT);
        modeleNull = new AjouterVueLocale();
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
    public void getRefKey()
    {
        assertEquals(PROJECT, modele.getRefKey());
        assertNull(modeleNull.getRefKey());
    }
    
    @Test
    public void setRefKey()
    {
        modele.setRefKey(NEWVAL);
        assertEquals(NEWVAL, modele.getRefKey());
        modeleNull.setRefKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getRefKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}