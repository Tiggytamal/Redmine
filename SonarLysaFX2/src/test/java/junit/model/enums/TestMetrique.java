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
import model.enums.Metrique;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMetrique extends TestAbstractEnum<Metrique>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test création Metrique
        assertThat(Metrique.from("alert_status")).isEqualTo(Metrique.QG);
        assertThat(Metrique.from("duplicated_lines_density")).isEqualTo(Metrique.DUPLICATION);
        assertThat(Metrique.from("blocker_violations")).isEqualTo(Metrique.BLOQUANT);
        assertThat(Metrique.from("critical_violations")).isEqualTo(Metrique.CRITIQUE);
        assertThat(Metrique.from("application")).isEqualTo(Metrique.APPLI);
        assertThat(Metrique.from("edition")).isEqualTo(Metrique.EDITION);
        assertThat(Metrique.from("bugs")).isEqualTo(Metrique.BUGS);
        assertThat(Metrique.from("vulnerabilities")).isEqualTo(Metrique.VULNERABILITIES);
        assertThat(Metrique.from("ncloc")).isEqualTo(Metrique.LDC);
        assertThat(Metrique.from("security_rating")).isEqualTo(Metrique.SECURITY);
        assertThat(Metrique.from("line_coverage")).isEqualTo(Metrique.PCTCOVERLDC);
        assertThat(Metrique.from("new_line_coverage")).isEqualTo(Metrique.NEWPCTCOVERLDC);
        assertThat(Metrique.from("new_lines_to_cover")).isEqualTo(Metrique.NEWLDCTOCOVER);
        assertThat(Metrique.from("new_uncovered_lines")).isEqualTo(Metrique.NEWLDCNOCOVER);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "inconnu", "\0alert_status", "\0duplicated_lines_density", "\0blocker_violations", "\0critical_violations", "\0application", "\0edition", "\0bugs", "\0vulnerabilities", "\0ncloc",
            "\0security_rating", "\0overall_line_coverage", "\0new_line_coverage", "\0new_overall_lines_to_cover", "\0new_overall_uncovered_lines" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Metrique.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.Metrique.from - valeur non gérée : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Metrique.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.Metrique.from - valeur envoyée nulle ou vide.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Metrique.QG.getValeur()).isEqualTo("alert_status");
        assertThat(Metrique.DUPLICATION.getValeur()).isEqualTo("duplicated_lines_density");
        assertThat(Metrique.BLOQUANT.getValeur()).isEqualTo("blocker_violations");
        assertThat(Metrique.CRITIQUE.getValeur()).isEqualTo("critical_violations");
        assertThat(Metrique.APPLI.getValeur()).isEqualTo("application");
        assertThat(Metrique.EDITION.getValeur()).isEqualTo("edition");
        assertThat(Metrique.BUGS.getValeur()).isEqualTo("bugs");
        assertThat(Metrique.VULNERABILITIES.getValeur()).isEqualTo("vulnerabilities");
        assertThat(Metrique.LDC.getValeur()).isEqualTo("ncloc");
        assertThat(Metrique.SECURITY.getValeur()).isEqualTo("security_rating");
        assertThat(Metrique.PCTCOVERLDC.getValeur()).isEqualTo("line_coverage");
        assertThat(Metrique.NEWPCTCOVERLDC.getValeur()).isEqualTo("new_line_coverage");
        assertThat(Metrique.NEWLDCTOCOVER.getValeur()).isEqualTo("new_lines_to_cover");
        assertThat(Metrique.NEWLDCNOCOVER.getValeur()).isEqualTo("new_uncovered_lines");
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Metrique.values().length).isEqualTo(14);
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(Metrique.class, testInfo);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Metrique.valueOf(Metrique.LDC.toString())).isEqualTo(Metrique.LDC);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
