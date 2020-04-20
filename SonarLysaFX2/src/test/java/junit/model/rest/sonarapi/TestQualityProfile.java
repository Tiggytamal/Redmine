package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.QualityProfile;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestQualityProfile extends TestAbstractModele<QualityProfile>
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
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetLangage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLangage(), s -> objetTest.setLangage(s));
    }

    @Test
    public void testGetNomLangage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNomLangage(), s -> objetTest.setNomLangage(s));
    }

    @Test
    public void testGetParentKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getParentKey(), s -> objetTest.setParentKey(s));
    }

    @Test
    public void testIsInherited(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isInherited(), b -> objetTest.setInherited(b));
    }

    @Test
    public void testGetIsBuiltIn(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isBuiltIn(), b -> objetTest.setBuiltIn(b));
    }

    @Test
    public void testGetCompteRegleActive(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getCompteRegleActive(), i -> objetTest.setCompteRegleActive(i));
    }

    @Test
    public void testGetCompteRegleDepreciee(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getCompteRegleDepreciee(), i -> objetTest.setCompteRegleDepreciee(i));
    }

    @Test
    public void testGetCompteProjet(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getCompteProjet(), i -> objetTest.setCompteProjet(i));
    }

    @Test
    public void testGetIsDefaut(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDefaut(), b -> objetTest.setDefaut(b));
    }

    @Test
    public void testGetReglesUpdatedAt(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getReglesUpdatedAt(), s -> objetTest.setReglesUpdatedAt(s));
    }

    @Test
    public void testGetUserUpdatedAt(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getUserUpdatedAt(), s -> objetTest.setUserUpdatedAt(s));
    }

    @Test
    public void testGetLastUsed(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getLastUsed(), s -> objetTest.setLastUsed(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
