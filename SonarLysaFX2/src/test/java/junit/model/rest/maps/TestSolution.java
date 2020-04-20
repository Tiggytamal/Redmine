package junit.model.rest.maps;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.maps.Solution;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestSolution extends TestAbstractModele<Solution>
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
    public void testGetCodeProduit(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCodeProduit(), s -> objetTest.setCodeProduit(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelle(), s -> objetTest.setLibelle(s));
    }

    @Test
    public void testGetStatutS(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatutS(), s -> objetTest.setStatutS(s));
    }

    @Test
    public void testGetInstancesDeploiment(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getInstancesDeploiment(), s -> objetTest.setInstancesDeploiment(s), EMPTY, "instancesDeploiment");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
