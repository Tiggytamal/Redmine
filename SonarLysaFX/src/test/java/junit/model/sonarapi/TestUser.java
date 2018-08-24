package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.User;

public class TestUser
{
    /*---------- ATTRIBUTS ----------*/

    private User modele;
    private User modeleNull;
    private static final String LOGIN = "id";
    private static final String NAME = "key";
    private static final String ACTIVE = "nom";
    private static final String EMAIL = "description";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new User(LOGIN, NAME, ACTIVE, EMAIL);
        modeleNull = new User();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetLogin()
    {
        assertEquals(LOGIN, modele.getLogin());
        assertNotNull(modeleNull.getLogin());
        assertTrue(modeleNull.getLogin().isEmpty());
    }
    
    @Test
    public void testSetLogin()
    {
        modele.setLogin(NEWVAL);
        assertEquals(NEWVAL, modele.getLogin());
        modeleNull.setLogin(LOGIN);
        assertEquals(LOGIN, modeleNull.getLogin());
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
      
    @Test
    public void testGetActive()
    {
        assertEquals(ACTIVE, modele.getActive());
        assertNotNull(modeleNull.getActive());
        assertTrue(modeleNull.getActive().isEmpty());
    }
    
    @Test
    public void testSetActive()
    {
        modele.setActive(NEWVAL);
        assertEquals(NEWVAL, modele.getActive());
        modeleNull.setActive(ACTIVE);
        assertEquals(ACTIVE, modeleNull.getActive());
    }
    
    @Test
    public void testGetEmail()
    {
        assertEquals(EMAIL, modele.getEmail());
        assertNotNull(modeleNull.getEmail());
        assertTrue(modeleNull.getEmail().isEmpty());
    }
    
    @Test
    public void testSetEmail()
    {
        modele.setEmail(NEWVAL);
        assertEquals(NEWVAL, modele.getEmail());
        modeleNull.setEmail(EMAIL);
        assertEquals(EMAIL, modeleNull.getEmail());
    }
   
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
