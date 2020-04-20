package junit.control.rest;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.rest.ControlMAPS;
import junit.AutoDisplayName;
import model.rest.maps.Fonctionnalite;
import model.rest.maps.Produit;
import model.rest.maps.Solution;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlMAPS extends TestAbstractControlRest<ControlMAPS>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlMAPS() throws Exception
    {
        super();
    }

    @Override
    @BeforeEach
    public void init()
    {
        objetTest = ControlMAPS.INSTANCE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    
    @Test
    public void testConstructor(TestInfo testInfo) throws Throwable
    {
        LOGGER.debug(testInfo.getDisplayName());

        testConstructor_Exception("control.rest.ControlMAPS - Singleton, instanciation interdite!");
    }

    @Test
    public void testGetProduit(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec produit existant
        Produit produit = objetTest.getProduit("P0018");
        assertThat(produit).isNotNull();
        assertThat(produit.getCode()).isNotEmpty();
        assertThat(produit.getDescription()).isNotEmpty();
        assertThat(produit.getSolutions()).isNotNull();
    }
    
    @Test
    public void testGetProduit_Inconnu(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec produit inconnu
        assertThat(objetTest.getProduit("P9999")).isNull();
    }

    @Test
    public void testGetSolution(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec solution existante
        Solution solution = objetTest.getSolution("S0007");
        assertThat(solution).isNotNull();
        assertThat(solution.getCode()).isNotEmpty();
        assertThat(solution.getLibelle()).isNotEmpty();
        assertThat(solution.getCodeProduit()).isNotEmpty();
    }
    
    @Test
    public void testGetSolution_Inconnue(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec solution inconnue
        assertThat(objetTest.getSolution("S9999")).isNull();
    }

    @Test
    public void testGetFonctionnalite(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec fonctionnalité existante
        Fonctionnalite solution = objetTest.getFonctionnalite("F0007");
        assertThat(solution).isNotNull();
        assertThat(solution.getCode()).isNotEmpty();
        assertThat(solution.getLibelle()).isNotEmpty();
        assertThat(solution.getDescription()).isNotEmpty();

        // Appel méthode avec fonctionnalité inconnue
        assertThat(objetTest.getFonctionnalite("F9999")).isNull();
    }
    
    @Test
    public void testGetFonctionnalite_Inconnue(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec fonctionnalité inconnue
        assertThat(objetTest.getFonctionnalite("F9999")).isNull();
    }

    @Test
    public void testProduitExiste_Vrai(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec produit existant
        assertThat(objetTest.produitExiste("P0018")).isTrue();
    }
    
    @Test
    public void testProduitExiste_Faux(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode avec produit inexistant
        assertThat(objetTest.produitExiste("P9999")).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
