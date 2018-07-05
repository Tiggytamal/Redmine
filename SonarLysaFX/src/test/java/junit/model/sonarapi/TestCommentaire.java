package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Commentaire;

public class TestCommentaire
{
    /*---------- ATTRIBUTS ----------*/

    private Commentaire modele;
    private Commentaire modeleNull;
    private static final String KEY = "key";
    private static final String LOGIN = "login";
    private static final String HTMLTEXT = "html";
    private static final String MARKDOWN = "markdown";
    private static final String UPDATABLE = "true";
    private static final String CREATEDAT = "created";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Commentaire(KEY, LOGIN, HTMLTEXT, MARKDOWN, UPDATABLE, CREATEDAT);
        modeleNull = new Commentaire();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
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
    public void testGetLogin()
    {
        assertEquals(LOGIN, modele.getLogin());
        assertNull(modeleNull.getLogin());
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
    public void testGetHtmlText()
    {
        assertEquals(HTMLTEXT, modele.getHtmlText());
        assertNull(modeleNull.getHtmlText());
    }
    
    @Test
    public void testSetHtmlText()
    {
        modele.setHtmlText(NEWVAL);
        assertEquals(NEWVAL, modele.getHtmlText());
        modeleNull.setHtmlText(HTMLTEXT);
        assertEquals(HTMLTEXT, modeleNull.getHtmlText());
    }
    
    @Test
    public void testGetMarkdown()
    {
        assertEquals(MARKDOWN, modele.getMarkdown());
        assertNull(modeleNull.getMarkdown());
    }
    
    @Test
    public void testSetMarkdown()
    {
        modele.setMarkdown(NEWVAL);
        assertEquals(NEWVAL, modele.getMarkdown());
        modeleNull.setMarkdown(MARKDOWN);
        assertEquals(MARKDOWN, modeleNull.getMarkdown());
    }
    
    @Test
    public void testGetUpdatable()
    {
        assertEquals(UPDATABLE, modele.getUpdatable());
        assertNull(modeleNull.getUpdatable());
    }
    
    @Test
    public void testSetUpdatable()
    {
        modele.setUpdatable(NEWVAL);
        assertEquals(NEWVAL, modele.getUpdatable());
        modeleNull.setUpdatable(UPDATABLE);
        assertEquals(UPDATABLE, modeleNull.getUpdatable());
    }
    
    @Test
    public void testGetCreatedAt()
    {
        assertEquals(CREATEDAT, modele.getCreatedAt());
        assertNull(modeleNull.getCreatedAt());
    }
    
    @Test
    public void testSetCreatedAt()
    {
        modele.setCreatedAt(NEWVAL);
        assertEquals(NEWVAL, modele.getCreatedAt());
        modeleNull.setCreatedAt(CREATEDAT);
        assertEquals(CREATEDAT, modeleNull.getCreatedAt());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}