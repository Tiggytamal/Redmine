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
import model.enums.TypeBranche;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeBranche extends TestAbstractEnum<TypeBranche>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeBranche.valueOf(TypeBranche.LONG.toString())).isEqualTo(TypeBranche.LONG);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeBranche.values().length).isEqualTo(2);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeBranche.SHORT.getValeur()).isEqualTo("SHORT");
        assertThat(TypeBranche.LONG.getValeur()).isEqualTo("LONG");
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeBranche.from("SHORT")).isEqualTo(TypeBranche.SHORT);
        assertThat(TypeBranche.from("LONG")).isEqualTo(TypeBranche.LONG);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings = { "\0SHORT", "\0LONG", "inconnu" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeBranche.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeBranche.from - valeur non gérée : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeBranche.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeBranche.from - valeur envoyée nulle ou vide.");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(TypeBranche.class, testInfo);
    }
}
