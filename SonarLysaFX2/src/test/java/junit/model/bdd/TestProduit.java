package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.Produit;
import model.bdd.Solution;
import model.enums.Param;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProduit extends TestAbstractBDDModele<Produit, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl()
    {
        objetTest.setNom(NOM);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNom());
    }

    @Test
    public void testCalculLiens(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.calculLiens()).isNotNull();
        assertThat(objetTest.calculLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLMAPSPRODUIT) + NOM);
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setNom(NOM);

        // Test simple
        Produit produit = ModelFactory.build(Produit.class);
        testSimpleEquals(produit);

        // Test autres paramètres
        assertThat(objetTest.equals(produit)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(produit.hashCode());

        produit.setNom(NOM);
        assertThat(objetTest.equals(produit)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(produit.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getNom()).isNotNull();
        assertThat(objetTest.getNom()).isEqualTo(NOM);

        String nomProj = "nomProj";
        objetTest.setNom(nomProj);
        assertThat(objetTest.getNom()).isEqualTo(nomProj);

        // Protection null et vide
        objetTest.setNom(null);
        assertThat(objetTest.getNom()).isEqualTo(nomProj);
        objetTest.setNom(EMPTY);
        assertThat(objetTest.getNom()).isEqualTo(nomProj);
    }

    @Test
    public void testGetListeSolution(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet initial
        assertThat(objetTest.getListeSolution()).isNotNull();
        assertThat(objetTest.getListeSolution()).isEmpty();

        // Test getter et setter
        List<Solution> liste = new ArrayList<>();
        liste.add(ModelFactory.build(Solution.class));
        objetTest.setListeSolution(liste);
        assertThat(objetTest.getListeSolution()).isEqualTo(liste);

        // Protection null et vide
        objetTest.setListeSolution(null);
        assertThat(objetTest.getListeSolution()).isNotNull();
        assertThat(objetTest.getListeSolution()).isEqualTo(liste);

        objetTest.setListeSolution(new ArrayList<>());
        assertThat(objetTest.getListeSolution()).isNotNull();
        assertThat(objetTest.getListeSolution()).isEqualTo(liste);

        Whitebox.getField(objetTest.getClass(), "listeSolution").set(objetTest, null);
        assertThat(objetTest.getListeSolution()).isNotNull();
        assertThat(objetTest.getListeSolution()).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
