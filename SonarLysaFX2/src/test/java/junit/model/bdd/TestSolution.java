package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.ComposantBase;
import model.bdd.Produit;
import model.bdd.Solution;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestSolution extends TestAbstractBDDModele<Solution, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initial
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNom());

        // Test avec changement nom
        objetTest.setNom(NOM);
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNom());
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        objetTest.setNom(NOM);
        objetTest.setProduit(ModelFactory.build(Produit.class));

        // Test simple
        Solution solution = ModelFactory.build(Solution.class);
        testSimpleEquals(solution);

        // Test autres paramètres
        solution.setNom(NOM);
        assertThat(objetTest.equals(solution)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(solution.hashCode());

        solution.setProduit(ModelFactory.build(Produit.class));
        assertThat(objetTest.equals(solution)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(solution.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getNom()).isNotNull();
        assertThat(objetTest.getNom()).isEmpty();

        // Test setter et getter
        objetTest.setNom(NOM);
        assertThat(objetTest.getNom()).isEqualTo(NOM);

        // Protection remise à null ou vide
        objetTest.setNom(null);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        objetTest.setNom(Statics.EMPTY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
    }

    @Test
    public void testGetProduit(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getProduit()).isNull();

        // Test getter et setter
        Produit produit = ModelFactory.build(Produit.class);
        objetTest.setProduit(produit);
        assertThat(objetTest.getProduit()).isEqualTo(produit);
        assertThat(produit.getListeSolution()).contains(objetTest);

        // Protection null
        objetTest.setProduit(null);
        assertThat(objetTest.getProduit()).isEqualTo(produit);
    }

    @Test
    public void testGetListeComposants(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListeComposants(), (l) -> objetTest.setListeComposants(l), ModelFactory.build(ComposantBase.class), "listeComposants");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
