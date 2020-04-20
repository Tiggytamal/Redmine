package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.ConditionQG;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestConditionQG extends TestAbstractModele<ConditionQG>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetId(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getId(), i -> objetTest.setId(i));
    }

    @Test
    public void testGetMetrique(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMetrique(), s -> objetTest.setMetrique(s));
    }

    @Test
    public void testGetOperateur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getOperateur(), s -> objetTest.setOperateur(s));
    }

    @Test
    public void testGetPeriode(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPeriode(), i -> objetTest.setPeriode(i));
    }

    @Test
    public void testGetSeuilWarning(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeuilWarning(), s -> objetTest.setSeuilWarning(s));
    }

    @Test
    public void testGetSeuilErreur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeuilErreur(), s -> objetTest.setSeuilErreur(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
