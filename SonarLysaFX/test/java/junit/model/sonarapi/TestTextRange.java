package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.TextRange;

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
    public void getStartLine()
    {
        assertEquals(STARTLINE, modele.getStartLine());
        assertEquals(0, modeleNull.getStartLine());
    }
    
    @Test
    public void setStartLine()
    {
        modele.setStartLine(10);
        assertEquals(10, modele.getStartLine());
        modeleNull.setStartLine(STARTLINE);
        assertEquals(STARTLINE, modeleNull.getStartLine());
    }
    
    @Test
    public void getEndLine()
    {
        assertEquals(ENDLINE, modele.getEndLine());
        assertEquals(0, modeleNull.getEndLine());
    }
    
    @Test
    public void setEndLine()
    {
        modele.setEndLine(10);
        assertEquals(10, modele.getEndLine());
        modeleNull.setEndLine(ENDLINE);
        assertEquals(ENDLINE, modeleNull.getEndLine());
    }
    
    @Test
    public void getStartOffset()
    {
        assertEquals(STARTOFFSET, modele.getStartOffset());
        assertEquals(0, modeleNull.getStartOffset());
    }
    
    @Test
    public void setStartOffset()
    {
        modele.setStartOffset(10);
        assertEquals(10, modele.getStartOffset());
        modeleNull.setStartOffset(STARTOFFSET);
        assertEquals(STARTOFFSET, modeleNull.getStartOffset());
    }
    
    @Test
    public void getEndOffset()
    {
        assertEquals(ENDOFFSET, modele.getEndOffset());
        assertEquals(0, modeleNull.getEndOffset());
    }
    
    @Test
    public void setEndOffset()
    {
        modele.setEndOffset(10);
        assertEquals(10, modele.getEndOffset());
        modeleNull.setEndOffset(ENDOFFSET);
        assertEquals(ENDOFFSET, modeleNull.getEndOffset());
    }
}
