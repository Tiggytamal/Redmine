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
import model.enums.ActionDq;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestActionDq extends TestAbstractEnum<ActionDq>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionDq.valueOf("CREER")).isEqualTo(ActionDq.CREER);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionDq.values().length).isEqualTo(6);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionDq.from("Créer")).isEqualTo(ActionDq.CREER);
        assertThat(ActionDq.from("Mise à jour")).isEqualTo(ActionDq.MAJ);
        assertThat(ActionDq.from("Clôturer")).isEqualTo(ActionDq.CLOTURER);
        assertThat(ActionDq.from("Abandonner")).isEqualTo(ActionDq.ABANDONNER);
        assertThat(ActionDq.from("Relancer")).isEqualTo(ActionDq.RELANCER);
        assertThat(ActionDq.from("Rendre obsolète")).isEqualTo(ActionDq.OBSOLETE);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0Créer", "\0Mise à jour", "\0Clôturer", "\0Abandonner", "\0Relancer", "\0Rendre obsolète", "autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionDq.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionDq.from - action inconnue : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ActionDq.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.ActionDq.from - argument vide ou nul.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionDq.CREER.getValeur()).isEqualTo("Créer");
        assertThat(ActionDq.CLOTURER.getValeur()).isEqualTo("Clôturer");
        assertThat(ActionDq.ABANDONNER.getValeur()).isEqualTo("Abandonner");
        assertThat(ActionDq.OBSOLETE.getValeur()).isEqualTo("Rendre obsolète");
        assertThat(ActionDq.RELANCER.getValeur()).isEqualTo("Relancer");
        assertThat(ActionDq.MAJ.getValeur()).isEqualTo("Mise à jour");
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ActionDq.CREER.toString()).isEqualTo("Créer");
        assertThat(ActionDq.CLOTURER.toString()).isEqualTo("Clôturer");
        assertThat(ActionDq.ABANDONNER.toString()).isEqualTo("Abandonner");
        assertThat(ActionDq.OBSOLETE.toString()).isEqualTo("Rendre obsolète");
        assertThat(ActionDq.RELANCER.toString()).isEqualTo("Relancer");
        assertThat(ActionDq.MAJ.toString()).isEqualTo("Mise à jour");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        testValeurInstanciation(ActionDq.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
