package junit.model.rest.repack;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.repack.RepackREST;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestRepackREST extends TestAbstractModele<RepackREST>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetIdCompHpR(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdCompHpR(), s -> objetTest.setIdCompHpR(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetNomComposantMarimba(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNomComposantMarimba(), s -> objetTest.setNomComposantMarimba(s));
    }

    @Test
    public void testGetIdVCompHpR(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdVCompHpR(), s -> objetTest.setIdVCompHpR(s));
    }

    @Test
    public void testGetVersion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersion(), s -> objetTest.setVersion(s));
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateCreation(), s -> objetTest.setDateCreation(s));
    }

    @Test
    public void testGetIdEdition(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdEdition(), s -> objetTest.setIdEdition(s));
    }

    @Test
    public void testGetIdNgm(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdNgm(), s -> objetTest.setIdNgm(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelle(), s -> objetTest.setLibelle(s));
    }

    @Test
    public void testGetIdGc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdGc(), s -> objetTest.setIdGc(s));
    }

    @Test
    public void testGetNomGc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNomGc(), s -> objetTest.setNomGc(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
