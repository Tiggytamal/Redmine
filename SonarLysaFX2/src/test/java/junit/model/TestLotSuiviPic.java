package junit.model;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.LotSuiviPic;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLotSuiviPic extends TestAbstractModele<LotSuiviPic>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetLot(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNumero(), (s) -> objetTest.setNumero(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelle(), (s) -> objetTest.setLibelle(s));
    }

    @Test
    public void testGetProjetClarity(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getProjetClarity(), (s) -> objetTest.setProjetClarity(s));
    }

    @Test
    public void testGetCpiProjet(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCpiProjet(), (s) -> objetTest.setCpiProjet(s));
    }

    @Test
    public void testGetEdition(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEdition(), (s) -> objetTest.setEdition(s));
    }

    @Test
    public void testGetNbreComposants(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNbreComposants(), (i) -> objetTest.setNbreComposants(i));
    }

    @Test
    public void testGetNbrePaquets(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNbrePaquets(), (i) -> objetTest.setNbrePaquets(i));
    }

    @Test
    public void testGetBuild(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getBuild(), (i) -> objetTest.setBuild(i));
    }

    @Test
    public void testGetDevtu(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDevtu(), (i) -> objetTest.setDevtu(i));
    }

    @Test
    public void testGetTfon(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getTfon(), (i) -> objetTest.setTfon(i));
    }

    @Test
    public void testGetVmoe(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getVmoe(), (i) -> objetTest.setVmoe(i));
    }

    @Test
    public void testGetVmoa(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getVmoa(), (i) -> objetTest.setVmoa(i));
    }

    @Test
    public void testGetLivraison(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getLivraison(), (i) -> objetTest.setLivraison(i));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
