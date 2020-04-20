package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.TextRange;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTextRange extends TestAbstractModele<TextRange>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetStartLine(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getStartLine(), s -> objetTest.setStartLine(s));
    }

    @Test
    public void testGetEndLine(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getEndLine(), s -> objetTest.setEndLine(s));
    }

    @Test
    public void testGetStartOffset(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getStartOffset(), s -> objetTest.setStartOffset(s));
    }

    @Test
    public void testGetEndOffset(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getEndOffset(), s -> objetTest.setEndOffset(s));
    }

}
