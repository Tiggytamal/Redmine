package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.AjouterVueLocale;

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
    public void testGetRefKey()
    {
        assertEquals(PROJECT, modele.getRefKey());
        assertNotNull(modeleNull.getRefKey());
        assertTrue(modeleNull.getRefKey().isEmpty());
    }
    
    @Test
    public void testSetRefKey()
    {
        modele.setRefKey(NEWVAL);
        assertEquals(NEWVAL, modele.getRefKey());
        modeleNull.setRefKey(PROJECT);
        assertEquals(PROJECT, modeleNull.getRefKey());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}