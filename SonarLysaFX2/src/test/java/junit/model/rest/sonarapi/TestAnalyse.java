package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Analyse;
import model.rest.sonarapi.Event;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAnalyse extends TestAbstractModele<Analyse>
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
    public void testGetDate(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDate(), s -> objetTest.setDate(s));
    }

    @Test
    public void testGetEvents(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getEvents(), l -> objetTest.setEvents(l), new Event(), "events");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
