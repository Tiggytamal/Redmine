package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.InstanceSonar;
import model.enums.QG;
import model.parsing.BrancheJSON;
import model.parsing.ComposantJSON;
import model.parsing.ProjetJSON;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantJSON extends TestAbstractModele<ComposantJSON>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetQualityGate(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getQualityGate()).isNotNull();
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);

        // Test getter et setter
        objetTest.setQualityGate(QG.OK);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);

        // Protection null avec et sans introspection
        objetTest.setQualityGate(null);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);
        setField("qualityGate", null);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);
    }

    @Test
    public void testGetProjet(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getProjet()).isNull();
        ProjetJSON projet = new ProjetJSON();
        objetTest.setProjet(projet);
        assertThat(objetTest.getProjet()).isEqualTo(projet);

        // Protection null
        objetTest.setProjet(null);
        assertThat(objetTest.getProjet()).isEqualTo(projet);
    }

    @Test
    public void testGetBranche(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getBranche()).isNull();
        BrancheJSON branche = new BrancheJSON();
        objetTest.setBranche(branche);
        assertThat(objetTest.getBranche()).isEqualTo(branche);

        // Protection null
        objetTest.setBranche(null);
        assertThat(objetTest.getBranche()).isEqualTo(branche);
    }

    @Test
    public void testGetInstance(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getQualityGate()).isNotNull();
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.LEGACY);

        // Test getter et setter
        objetTest.setInstance(InstanceSonar.MOBILECENTER);
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.MOBILECENTER);

        // Protection null avec et sans introspection
        objetTest.setInstance(null);
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.MOBILECENTER);
        setField("instance", null);
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.LEGACY);
    }

    @Test
    public void testGetNumeroLot(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleInteger(testInfo, () -> objetTest.getNumeroLot(), i -> objetTest.setNumeroLot(i));
    }

    @Test
    public void testGetIdAnalyse(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getIdAnalyse()).isNotNull();
        assertThat(objetTest.getIdAnalyse()).isEmpty();
        objetTest.setIdAnalyse(LOT123456);
        assertThat(objetTest.getIdAnalyse()).isEqualTo(LOT123456);

        // Protection null et vide avec introspection
        objetTest.setIdAnalyse(null);
        assertThat(objetTest.getIdAnalyse()).isEqualTo(LOT123456);
        objetTest.setIdAnalyse(EMPTY);
        assertThat(objetTest.getIdAnalyse()).isEqualTo(LOT123456);
        setField("idAnalyse", null);
        assertThat(objetTest.getIdAnalyse()).isEqualTo(EMPTY);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
