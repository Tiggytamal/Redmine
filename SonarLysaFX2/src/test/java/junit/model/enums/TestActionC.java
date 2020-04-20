package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import junit.AutoDisplayName;
import model.enums.ActionC;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestActionC extends TestAbstractEnum<ActionC>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionC.valueOf("PURGER")).isEqualTo(ActionC.PURGER);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionC.values().length).isEqualTo(3);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionC.from("Purger")).isEqualTo(ActionC.PURGER);
        assertThat(ActionC.from("Supp. contrôle app.")).isEqualTo(ActionC.SUPPCONTROLEAPPLI);
        assertThat(ActionC.from("Supp. contrôle dette tech.")).isEqualTo(ActionC.SUPPCONTROLEDETTE);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings = { "\0Purger", "\0Supp. contrôle app.", "\0Supp. contrôle dette tech.","autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionC.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionC.from - action inconnue : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionC.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionC.from - argument vide ou nul.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionC.PURGER.getValeur()).isEqualTo("Purger");
        assertThat(ActionC.SUPPCONTROLEAPPLI.getValeur()).isEqualTo("Supp. contrôle app.");
        assertThat(ActionC.SUPPCONTROLEDETTE.getValeur()).isEqualTo("Supp. contrôle dette tech.");
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionC.PURGER.toString()).isEqualTo("Purger");
        assertThat(ActionC.SUPPCONTROLEAPPLI.toString()).isEqualTo("Supp. contrôle app.");
        assertThat(ActionC.SUPPCONTROLEDETTE.toString()).isEqualTo("Supp. contrôle dette tech.");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        testValeurInstanciation(ActionC.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
