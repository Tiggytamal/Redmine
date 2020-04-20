package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.ConditionQG;
import model.rest.sonarapi.QualityGate;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestQualityGate extends TestAbstractModele<QualityGate>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetId(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getId(), s -> objetTest.setId(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testIsDefault(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDefault(), b -> objetTest.setDefault(b));
    }

    @Test
    public void testIsBuiltIn(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isBuiltIn(), b -> objetTest.setBuiltIn(b));
    }

    @Test
    public void testGetConditions(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getConditions(), l -> objetTest.setConditions(l), new ConditionQG(), "conditions");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
