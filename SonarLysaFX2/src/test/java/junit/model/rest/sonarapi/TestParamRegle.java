package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.ParamRegle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamRegle extends TestAbstractModele<ParamRegle>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getValeur(), s -> objetTest.setValeur(s));
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetHtmlDesc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getHtmlDesc(), s -> objetTest.setHtmlDesc(s));
    }

    @Test
    public void testGetDefaultValue(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultValue(), s -> objetTest.setDefaultValue(s));
    }

    @Test
    public void testGetType(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getType(), s -> objetTest.setType(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
