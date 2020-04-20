package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.ProjetMobileCenter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProjetMobileCenter extends TestAbstractBDDModele<ProjetMobileCenter, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setNom(NOM);
        assertThat(objetTest.getMapIndex()).isEqualTo(NOM);
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setNom(NOM);
        objetTest.setNumeroLot(123456);

        // Test simple
        ProjetMobileCenter projet = ModelFactory.build(ProjetMobileCenter.class);
        testSimpleEquals(projet);

        // Test autres paramètres
        projet.setNom(NOM);
        assertThat(objetTest.equals(projet)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(projet.hashCode());

        projet.setNumeroLot(123456);
        assertThat(objetTest.equals(projet)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(projet.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetNumeroLot(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNumeroLot(), s -> objetTest.setNumeroLot(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
