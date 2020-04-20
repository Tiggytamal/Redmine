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
import model.enums.ActionA;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestActionA extends TestAbstractEnum<ActionA>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionA.valueOf("SUPPRIMER")).isEqualTo(ActionA.SUPPRIMER);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionA.values().length).isEqualTo(2);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionA.from("Supprimer")).isEqualTo(ActionA.SUPPRIMER);
        assertThat(ActionA.from("Modifier")).isEqualTo(ActionA.MODIFIER);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0Supprimer", "\0Modifier", "autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionA.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionA.from - action inconnue : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionA.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionA.from - argument vide ou nul.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionA.SUPPRIMER.getValeur()).isEqualTo("Supprimer");
        assertThat(ActionA.MODIFIER.getValeur()).isEqualTo("Modifier");
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionA.SUPPRIMER.toString()).isEqualTo("Supprimer");
        assertThat(ActionA.MODIFIER.toString()).isEqualTo("Modifier");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        testValeurInstanciation(ActionA.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
