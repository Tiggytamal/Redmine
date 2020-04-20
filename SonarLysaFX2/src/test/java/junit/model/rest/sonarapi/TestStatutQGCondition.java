package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.StatutQGCondition;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatutQGCondition extends TestAbstractModele<StatutQGCondition>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetStatus(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatus(), s -> objetTest.setStatus(s));
    }

    @Test
    public void testGetMetricKeys(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMetricKey(), s -> objetTest.setMetricKey(s));
    }

    @Test
    public void testGetComparator(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getComparator(), s -> objetTest.setComparator(s));
    }

    @Test
    public void testGetPeriodIndex(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPeriodIndex(), i -> objetTest.setPeriodIndex(i));
    }

    @Test
    public void testGetErrorThreshold(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getErrorThreshold(), s -> objetTest.setErrorThreshold(s));
    }
    
    @Test
    public void testGetWarningThreshold(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getWarningThreshold(), s -> objetTest.setWarningThreshold(s));
    }

    @Test
    public void testGetActualValue(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getActualValue(), s -> objetTest.setActualValue(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
