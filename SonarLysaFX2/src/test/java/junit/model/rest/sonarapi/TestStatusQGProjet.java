package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.StatutQGCondition;
import model.rest.sonarapi.StatutPeriode;
import model.rest.sonarapi.StatutQGProjet;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatusQGProjet extends TestAbstractModele<StatutQGProjet>
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
    public void testGetConditions(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getConditions(), l -> objetTest.setConditions(l), new StatutQGCondition(), "conditions");
    }

    @Test
    public void testGetPeriodes(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getPeriodes(), l -> objetTest.setPeriodes(l), new StatutPeriode(), "periodes");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
