package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Activation;
import model.rest.sonarapi.DetailRegle;
import model.rest.sonarapi.Regle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDetailRegle extends TestAbstractModele<DetailRegle>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetRegle(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation null
        assertThat(objetTest.getRegle()).isNull();

        // Test getter et setter
        Regle regle = new Regle();
        objetTest.setRegle(regle);
        assertThat(objetTest.getRegle()).isEqualTo(regle);

        // Protection null setter
        objetTest.setRegle(null);
        assertThat(objetTest.getRegle()).isEqualTo(regle);
    }

    @Test
    public void testGetActivations(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getActivations(), l -> objetTest.setActivations(l), new Activation(), "activations");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
