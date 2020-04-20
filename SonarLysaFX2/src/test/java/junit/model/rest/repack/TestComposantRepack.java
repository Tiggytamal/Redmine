package junit.model.rest.repack;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.repack.ComposantRepack;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantRepack extends TestAbstractModele<ComposantRepack>
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
    public void testGetIdRtc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdRtc(), s -> objetTest.setIdRtc(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetVersion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersion(), s -> objetTest.setVersion(s));
    }

    @Test
    public void testGetIdBaselineRtc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdBaseLineRtc(), s -> objetTest.setIdBaseLineRtc(s));
    }

    @Test
    public void testGetBaseline(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        testSimpleString(testInfo, () -> objetTest.getBaseline(), s -> objetTest.setBaseline(s));
    }

    @Test
    public void testGetVersionPic(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersionPic(), s -> objetTest.setVersionPic(s));
    }
    
    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateCreation(), s -> objetTest.setDateCreation(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
