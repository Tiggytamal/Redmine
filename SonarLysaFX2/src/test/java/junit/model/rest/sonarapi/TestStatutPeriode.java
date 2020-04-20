package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.StatutPeriode;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatutPeriode extends TestAbstractModele<StatutPeriode>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getIndex(), i -> objetTest.setIndex(i));
    }

    @Test
    public void testGetMode(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMode(), s -> objetTest.setMode(s));
    }

    @Test
    public void testGetDate(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDate(), dt -> objetTest.setDate(dt));
    }

    @Test
    public void testGetParameter(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getParameter(), s -> objetTest.setParameter(s));
    }
}
