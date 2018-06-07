package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Location;
import model.sonarapi.TextRange;

public class TestLocation
{
    /*---------- ATTRIBUTS ----------*/

    private Location modele;
    private Location modeleNull;
    private static final TextRange TEXTRANGE = new TextRange();
    private static final String MSG = "msg";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Location(TEXTRANGE, MSG);
        modeleNull = new Location();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getTextRange()
    {
        assertEquals(TEXTRANGE, modele.getTextRange());
        assertNull(modeleNull.getTextRange());
    }
    
    @Test
    public void setTotal()
    {
        modele.setTextRange(null);
        assertNull(modele.getTextRange());
        modeleNull.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modeleNull.getTextRange());
    }
    
    @Test
    public void getMsg()
    {
        assertEquals(MSG, modele.getMsg());
        assertNull(modeleNull.getMsg());
    }
    
    @Test
    public void setMsg()
    {
        modele.setMsg(NEWVAL);
        assertEquals(NEWVAL, modele.getMsg());
        modeleNull.setMsg(MSG);
        assertEquals(MSG, modeleNull.getMsg());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
