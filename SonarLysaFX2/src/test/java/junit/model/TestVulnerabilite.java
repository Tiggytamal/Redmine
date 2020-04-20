package junit.model;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.Vulnerabilite;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestVulnerabilite extends TestAbstractModele<Vulnerabilite>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetSeverite(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeverite(), s -> objetTest.setSeverite(s));
    }

    @Test
    public void testGetComposant(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getComposant(), s -> objetTest.setComposant(s));
    }

    @Test
    public void testGetStatus(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatus(), s -> objetTest.setStatus(s));
    }

    @Test
    public void testGetMessage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMessage(), s -> objetTest.setMessage(s));
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateCreation(), s -> objetTest.setDateCreation(s));
    }

    @Test
    public void testGetLot(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLot(), s -> objetTest.setLot(s));
    }

    @Test
    public void testGetClarity(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getClarity(), s -> objetTest.setClarity(s));
    }

    @Test
    public void testGetAppli(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getAppli(), s -> objetTest.setAppli(s));
    }

    @Test
    public void testGetLib(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLib(), s -> objetTest.setLib(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
