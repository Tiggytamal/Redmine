package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.ChefService;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestChefService extends TestAbstractBDDModele<ChefService, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetChefServiceInconnu(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = ChefService.getChefServiceInconnu("inco");
        assertThat(objetTest.getNom()).isEqualTo("Chef de Service inconnu");
        assertThat(objetTest.getFiliere()).isEmpty();
        assertThat(objetTest.getDirection()).isEmpty();
        assertThat(objetTest.getDepartement()).isEmpty();
        assertThat(objetTest.getService()).isEqualTo("inco");
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getService());
    }

    @Test
    public void testUpdate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        ChefService chef = ChefService.getChefServiceInconnu("inco");
        chef.setNom(NOM);
        chef.setFiliere("fil");
        chef.setDirection("direct");
        chef.setDepartement("depart");

        objetTest.update(chef);
        assertThat(objetTest.getFiliere()).isEqualTo("fil");
        assertThat(objetTest.getDirection()).isEqualTo("direct");
        assertThat(objetTest.getDepartement()).isEqualTo("depart");
        assertThat(objetTest.getService()).isEqualTo("inco");
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        objetTest.setDepartement(TESTSTRING);
        objetTest.setDirection(TESTSTRING);
        objetTest.setFiliere(TESTSTRING);
        objetTest.setNom(NOM);
        objetTest.setService(TESTSTRING);

        // Test simple
        ChefService chefService = ModelFactory.build(ChefService.class);
        testSimpleEquals(chefService);

        // Test autres paramètres
        chefService.setDepartement(TESTSTRING);
        assertThat(objetTest.equals(chefService)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(chefService.hashCode());

        chefService.setDirection(TESTSTRING);
        assertThat(objetTest.equals(chefService)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(chefService.hashCode());

        chefService.setFiliere(TESTSTRING);
        assertThat(objetTest.equals(chefService)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(chefService.hashCode());

        chefService.setNom(NOM);
        assertThat(objetTest.equals(chefService)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(chefService.hashCode());

        chefService.setService(TESTSTRING);
        assertThat(objetTest.equals(chefService)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(chefService.hashCode());
    }

    @Test
    public void testGetDirection(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDirection(), s -> objetTest.setDirection(s));
    }

    @Test
    public void testGetFiliere(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getFiliere(), s -> objetTest.setFiliere(s));
    }

    @Test
    public void testGetService(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getService(), s -> objetTest.setService(s));
    }

    @Test
    public void testGetDepartement(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDepartement(), s -> objetTest.setDepartement(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
