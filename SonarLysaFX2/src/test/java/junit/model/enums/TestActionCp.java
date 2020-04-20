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
import model.enums.ActionCp;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestActionCp extends TestAbstractEnum<ActionCp>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionCp.valueOf("PURGER")).isEqualTo(ActionCp.PURGER);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionCp.values().length).isEqualTo(2);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionCp.from("Purger")).isEqualTo(ActionCp.PURGER);
        assertThat(ActionCp.from("Clôturer")).isEqualTo(ActionCp.CLOTURER);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0Purger", "\0Clôturer", "autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionCp.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionCp.from - action inconnue : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionCp.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionCp.from - argument vide ou nul.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionCp.PURGER.getValeur()).isEqualTo("Purger");
        assertThat(ActionCp.CLOTURER.getValeur()).isEqualTo("Clôturer");
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionCp.PURGER.toString()).isEqualTo("Purger");
        assertThat(ActionCp.CLOTURER.toString()).isEqualTo("Clôturer");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(ActionCp.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
