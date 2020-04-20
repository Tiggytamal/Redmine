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
import model.enums.TypeObjetSonar;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeObjetSonar extends TestAbstractEnum<TypeObjetSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeObjetSonar.valueOf("APPLI")).isEqualTo(TypeObjetSonar.APPLI);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeObjetSonar.values().length).isEqualTo(7);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeObjetSonar.from("APP")).isEqualTo(TypeObjetSonar.APPLI);
        assertThat(TypeObjetSonar.from("BRC")).isEqualTo(TypeObjetSonar.SUBPROJECT);
        assertThat(TypeObjetSonar.from("DIR")).isEqualTo(TypeObjetSonar.DIRECTORY);
        assertThat(TypeObjetSonar.from("FIL")).isEqualTo(TypeObjetSonar.FILE);
        assertThat(TypeObjetSonar.from("SVW")).isEqualTo(TypeObjetSonar.PORTFOLIO);
        assertThat(TypeObjetSonar.from("VW")).isEqualTo(TypeObjetSonar.PORTFOLIO);
        assertThat(TypeObjetSonar.from("TRK")).isEqualTo(TypeObjetSonar.PROJECT);
        assertThat(TypeObjetSonar.from("UTS")).isEqualTo(TypeObjetSonar.TESTFILE);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0Reouvrir", "\0Ajouter com.", "\0Verifier", "\0Assembler", "\0Clôturer", "\0Abandonner", "\0Relancer", "\0Reouvrir", "inconnu" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeObjetSonar.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeObjetSonar.from - valeur non gérée : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TypeObjetSonar.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.TypeObjetSonar.from - valeur envoyée nulle ou vide.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeObjetSonar.APPLI.getValeur()).isEqualTo("APP");
        assertThat(TypeObjetSonar.SUBPROJECT.getValeur()).isEqualTo("BRC");
        assertThat(TypeObjetSonar.DIRECTORY.getValeur()).isEqualTo("DIR");
        assertThat(TypeObjetSonar.FILE.getValeur()).isEqualTo("FIL");
        assertThat(TypeObjetSonar.PORTFOLIO.getValeur()).isEqualTo("VW");
        assertThat(TypeObjetSonar.PROJECT.getValeur()).isEqualTo("TRK");
        assertThat(TypeObjetSonar.TESTFILE.getValeur()).isEqualTo("UTS");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(TypeObjetSonar.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
