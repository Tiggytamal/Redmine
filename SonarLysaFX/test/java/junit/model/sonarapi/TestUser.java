package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void getLogin()
    {
        assertEquals(LOGIN, modele.getLogin());
        assertNull(modeleNull.getLogin());
    }
    
    @Test
    public void setLogin()
    {
        modele.setLogin(NEWVAL);
        assertEquals(NEWVAL, modele.getLogin());
        modeleNull.setLogin(LOGIN);
        assertEquals(LOGIN, modeleNull.getLogin());
    }
    
    @Test
    public void getName()
    {
        assertEquals(NAME, modele.getName());
        assertNull(modeleNull.getName());
    }
    
    @Test
    public void setName()
    {
        modele.setName(NEWVAL);
        assertEquals(NEWVAL, modele.getName());
        modeleNull.setName(NAME);
        assertEquals(NAME, modeleNull.getName());
    }
      
    @Test
    public void getActive()
    {
        assertEquals(ACTIVE, modele.getActive());
        assertNull(modeleNull.getActive());
    }
    
    @Test
    public void setActive()
    {
        modele.setActive(NEWVAL);
        assertEquals(NEWVAL, modele.getActive());
        modeleNull.setActive(ACTIVE);
        assertEquals(ACTIVE, modeleNull.getActive());
    }
    
    @Test
    public void getEmail()
    {
        assertEquals(EMAIL, modele.getEmail());
        assertNull(modeleNull.getEmail());
    }
    
    @Test
    public void setEmail()
    {
        modele.setEmail(NEWVAL);
        assertEquals(NEWVAL, modele.getEmail());
        modeleNull.setEmail(EMAIL);
        assertEquals(EMAIL, modeleNull.getEmail());
    }
   
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
