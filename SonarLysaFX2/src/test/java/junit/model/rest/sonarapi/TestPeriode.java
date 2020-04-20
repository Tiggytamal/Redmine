package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Periode;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestPeriode extends TestAbstractModele<Periode>
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
    public void testGetValeur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getValeur(), s -> objetTest.setValeur(s));
    }

    @Test
    public void testIsBestValue(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isBestValue(), b -> objetTest.setBestValue(b));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
