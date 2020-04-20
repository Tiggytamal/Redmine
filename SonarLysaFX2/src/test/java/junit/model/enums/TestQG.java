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
import model.enums.QG;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestQG extends TestAbstractEnum<QG>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(QG.valueOf(QG.ERROR.toString())).isEqualTo(QG.ERROR);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(QG.values().length).isEqualTo(5);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(QG.from("OK")).isEqualTo(QG.OK);
        assertThat(QG.from("WARN")).isEqualTo(QG.WARN);
        assertThat(QG.from("ERROR")).isEqualTo(QG.ERROR);
        assertThat(QG.from("NONE")).isEqualTo(QG.NONE);
        assertThat(QG.from("INCONNUE")).isEqualTo(QG.INCONNUE);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0OK", "\0WARN", "\0ERROR", "\0NONE", "\0INCONNUE", "inconnu" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> QG.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.QG.from - valeur non gérée : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> QG.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.QG.from - valeur envoyée nulle ou vide.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(QG.OK.getValeur()).isEqualTo("OK");
        assertThat(QG.WARN.getValeur()).isEqualTo("WARN");
        assertThat(QG.ERROR.getValeur()).isEqualTo("ERROR");
        assertThat(QG.NONE.getValeur()).isEqualTo("NONE");
        assertThat(QG.INCONNUE.getValeur()).isEqualTo("INCONNUE");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(QG.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
