package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import junit.AutoDisplayName;
import model.enums.TypeResolution;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeResolution extends TestAbstractEnum<TypeResolution>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeResolution.values().length).isEqualTo(4);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeResolution.valueOf(TypeResolution.WONTFIX.toString())).isEqualTo(TypeResolution.WONTFIX);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeResolution.from("WONTFIX")).isEqualTo(TypeResolution.WONTFIX);
        assertThat(TypeResolution.from("FIXED")).isEqualTo(TypeResolution.FIXED);
        assertThat(TypeResolution.from("FALSE-POSITIVE")).isEqualTo(TypeResolution.FALSEPOSITIVE);
        assertThat(TypeResolution.from("REMOVED")).isEqualTo(TypeResolution.REMOVED);
    }

    @ParameterizedTest
    @ValueSource(strings = { "\0WONTFIX", "\0FIXED", "\0FALSE-POSITIVE", "\0REMOVED", "autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeResolution.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeResolution.from - valeur inconnue : " + valeur);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeResolution.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeResolution.from - argument vide ou nul.");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
