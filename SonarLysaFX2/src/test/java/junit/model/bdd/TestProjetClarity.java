package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProjetClarity extends TestAbstractBDDModele<ProjetClarity, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetProjetClarityInconnu(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = ProjetClarity.getProjetClarityInconnu("projet");
        assertThat(objetTest.getCode()).isEqualTo("projet");
        assertThat(objetTest.getChefProjet()).isEqualTo(Statics.INCONNU);
        assertThat(objetTest.getEdition()).isEqualTo(Statics.INCONNU);
        assertThat(objetTest.getDirection()).isEqualTo(Statics.INCONNUE);
        assertThat(objetTest.getDepartement()).isEqualTo(Statics.INCONNU);
        assertThat(objetTest.getService()).isEqualTo(Statics.INCONNU);
        assertThat(objetTest.getLibelleProjet()).isEqualTo("Code Clarity inconnu du référentiel");
        assertThat(objetTest.isActif()).isFalse();
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getCode());
    }

    @Test
    public void testUpdate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        ProjetClarity chef = ProjetClarity.getProjetClarityInconnu("projet");
        chef.setActif(true);
        chef.setChefProjet("chef");
        chef.setEdition("ed");
        chef.setDirection("dir");
        chef.setDepartement("dep");
        chef.setService("serv");
        chef.setLibelleProjet("lib");

        objetTest.update(chef);
        assertThat(objetTest.getCode()).isEmpty();
        assertThat(objetTest.isActif()).isTrue();
        assertThat(objetTest.getChefProjet()).isEqualTo("chef");
        assertThat(objetTest.getEdition()).isEqualTo("ed");
        assertThat(objetTest.getDirection()).isEqualTo("dir");
        assertThat(objetTest.getDepartement()).isEqualTo("dep");
        assertThat(objetTest.getService()).isEqualTo("serv");
        assertThat(objetTest.getLibelleProjet()).isEqualTo("lib");
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        final String a = "a";
        final String edition = "E32";
        final String numero = "12345678";

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setActif(false);
        objetTest.setChefProjet(TESTSTRING);
        objetTest.setChefService(null);
        objetTest.setCode(a);
        objetTest.setDepartement(TESTSTRING);
        objetTest.setDirection(TESTSTRING);
        objetTest.setEdition(edition);
        objetTest.setLibelleProjet(TESTSTRING);
        objetTest.setService(TESTSTRING);

        // Test simple
        ProjetClarity projetClarity = ProjetClarity.getProjetClarityInconnu(numero);
        testSimpleEquals(projetClarity);

        // Test autres paramètres
        projetClarity.setActif(false);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setChefProjet(TESTSTRING);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setChefService(null);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setCode(a);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setDepartement(TESTSTRING);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setDirection(TESTSTRING);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setEdition(edition);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setLibelleProjet(TESTSTRING);
        assertThat(objetTest.equals(projetClarity)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projetClarity.hashCode());

        projetClarity.setService(TESTSTRING);
        assertThat(objetTest.equals(projetClarity)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(projetClarity.hashCode());
    }

    @Test
    public void testIsActif(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isActif(), b -> objetTest.setActif(b));
    }

    @Test
    public void testGetCode(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCode(), s -> objetTest.setCode(s));
    }

    @Test
    public void testGetLibelleProjet(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelleProjet(), s -> objetTest.setLibelleProjet(s));
    }

    @Test
    public void testGetChefProjet(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getChefProjet(), s -> objetTest.setChefProjet(s));
    }

    @Test
    public void testGetEdition(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEdition(), s -> objetTest.setEdition(s));
    }

    @Test
    public void testGetDirection(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDirection(), s -> objetTest.setDirection(s));
    }

    @Test
    public void testGetDepartement(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDepartement(), s -> objetTest.setDepartement(s));
    }

    @Test
    public void testGetService(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getService(), s -> objetTest.setService(s));
    }

    @Test
    public void testGetChefService(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getChefService()).isNull();

        // Test setter et getter
        ChefService chef = ChefService.getChefServiceInconnu("inco");
        objetTest.setChefService(chef);
        assertThat(objetTest.getChefService()).isEqualTo(chef);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
