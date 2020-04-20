package junit.model.rest.maps;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.maps.Produit;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProduit extends TestAbstractModele<Produit>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetStatut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatut(), s -> objetTest.setStatut(s));
    }

    @Test
    public void testGetCode(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCode(), s -> objetTest.setCode(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelle(), s -> objetTest.setLibelle(s));
    }

    @Test
    public void testGetIdPo(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdPo(), s -> objetTest.setIdPo(s));
    }

    @Test
    public void testGetNomPo(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNomPo(), s -> objetTest.setNomPo(s));
    }

    @Test
    public void testGetCodePP(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCodePP(), s -> objetTest.setCodePP(s));
    }

    @Test
    public void testGetLibellePP(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibellePP(), s -> objetTest.setLibellePP(s));
    }

    @Test
    public void testGetCodeTR(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCodeTR(), s -> objetTest.setCodeTR(s));
    }

    @Test
    public void testGetLibelleTR(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelleTR(), s -> objetTest.setLibelleTR(s));
    }

    @Test
    public void testGetIdPU(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdPU(), s -> objetTest.setIdPU(s));
    }

    @Test
    public void testGetLibellePU(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibellePU(), s -> objetTest.setLibellePU(s));
    }

    @Test
    public void testGetContactPP(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getContactPP(), s -> objetTest.setContactPP(s));
    }

    @Test
    public void testGetDescription(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDescription(), s -> objetTest.setDescription(s));
    }

    @Test
    public void testGetNbreSolutions(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNbreSolutions(), i -> objetTest.setNbreSolutions(i));
    }

    @Test
    public void testGetSolutions(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getSolutions(), s -> objetTest.setSolutions(s), EMPTY, "solutions");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
