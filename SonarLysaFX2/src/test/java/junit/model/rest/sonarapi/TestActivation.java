package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Activation;
import model.rest.sonarapi.ParamRegle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestActivation extends TestAbstractModele<Activation>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetqProfile(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getqProfile(), s -> objetTest.setqProfile(s));
    }

    @Test
    public void testGetInherit(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getInherit(), s -> objetTest.setInherit(s));
    }

    @Test
    public void testGetSeverity(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeverity(), s -> objetTest.setSeverity(s));
    }

    @Test
    public void testGetParametres(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getParametres(), s -> objetTest.setParametres(s), new ParamRegle(), "parametres");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
