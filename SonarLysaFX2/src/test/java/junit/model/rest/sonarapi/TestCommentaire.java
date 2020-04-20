package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Commentaire;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCommentaire extends TestAbstractModele<Commentaire>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetLogin(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLogin(), s -> objetTest.setLogin(s));
    }

    @Test
    public void testGetHtmlText(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getHtmlText(), s -> objetTest.setHtmlText(s));
    }

    @Test
    public void testGetMarkdown(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMarkdown(), s -> objetTest.setMarkdown(s));
    }

    @Test
    public void testGetUpdatable(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getUpdatable(), s -> objetTest.setUpdatable(s));
    }

    @Test
    public void testGetCreatedAt(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCreatedAt(), s -> objetTest.setCreatedAt(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
