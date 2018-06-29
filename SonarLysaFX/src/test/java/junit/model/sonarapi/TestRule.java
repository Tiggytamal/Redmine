package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Rule;

public class TestRule
{
    /*---------- ATTRIBUTS ----------*/

    private Rule modele;
    private Rule modeleNull;
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String LANG = "lang";
    private static final String LANGNAME = "langname";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Rule(KEY, NAME, STATUS, LANG, LANGNAME);
        modeleNull = new Rule();
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
    public void testGetName()
    {
        assertEquals(NAME, modele.getName());
        assertNull(modeleNull.getName());
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
    public void testGetStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNull(modeleNull.getStatus());
    }
    
    @Test
    public void testSetStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(STATUS);
        assertEquals(STATUS, modeleNull.getStatus());
    }
    
    @Test
    public void testGetLang()
    {
        assertEquals(LANG, modele.getLang());
        assertNull(modeleNull.getLang());
    }
    
    @Test
    public void testSetLang()
    {
        modele.setLang(NEWVAL);
        assertEquals(NEWVAL, modele.getLang());
        modeleNull.setLang(LANG);
        assertEquals(LANG, modeleNull.getLang());
    }
    
    @Test
    public void testGetLangName()
    {
        assertEquals(LANGNAME, modele.getLangName());
        assertNull(modeleNull.getLangName());
    }
    
    @Test
    public void testSetLangName()
    {
        modele.setLangName(NEWVAL);
        assertEquals(NEWVAL, modele.getLangName());
        modeleNull.setLangName(LANGNAME);
        assertEquals(LANGNAME, modeleNull.getLangName());
    }
}