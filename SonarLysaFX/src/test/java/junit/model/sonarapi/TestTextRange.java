package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.TextRange;

public class TestTextRange
{
    /*---------- ATTRIBUTS ----------*/

    private TextRange modele;
    private TextRange modeleNull;
    private static final int STARTLINE = 123;
    private static final int ENDLINE = 234;
    private static final int STARTOFFSET = 1234;
    private static final int ENDOFFSET = 2345;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new TextRange(STARTLINE, ENDLINE, STARTOFFSET, ENDOFFSET);
        modeleNull = new TextRange();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetStartLine()
    {
        assertEquals(STARTLINE, modele.getStartLine());
        assertEquals(0, modeleNull.getStartLine());
    }
    
    @Test
    public void testSetStartLine()
    {
        modele.setStartLine(10);
        assertEquals(10, modele.getStartLine());
        modeleNull.setStartLine(STARTLINE);
        assertEquals(STARTLINE, modeleNull.getStartLine());
    }
    
    @Test
    public void testGetEndLine()
    {
        assertEquals(ENDLINE, modele.getEndLine());
        assertEquals(0, modeleNull.getEndLine());
    }
    
    @Test
    public void testSetEndLine()
    {
        modele.setEndLine(10);
        assertEquals(10, modele.getEndLine());
        modeleNull.setEndLine(ENDLINE);
        assertEquals(ENDLINE, modeleNull.getEndLine());
    }
    
    @Test
    public void testGetStartOffset()
    {
        assertEquals(STARTOFFSET, modele.getStartOffset());
        assertEquals(0, modeleNull.getStartOffset());
    }
    
    @Test
    public void testSetStartOffset()
    {
        modele.setStartOffset(10);
        assertEquals(10, modele.getStartOffset());
        modeleNull.setStartOffset(STARTOFFSET);
        assertEquals(STARTOFFSET, modeleNull.getStartOffset());
    }
    
    @Test
    public void testGetEndOffset()
    {
        assertEquals(ENDOFFSET, modele.getEndOffset());
        assertEquals(0, modeleNull.getEndOffset());
    }
    
    @Test
    public void testSetEndOffset()
    {
        modele.setEndOffset(10);
        assertEquals(10, modele.getEndOffset());
        modeleNull.setEndOffset(ENDOFFSET);
        assertEquals(ENDOFFSET, modeleNull.getEndOffset());
    }
}
