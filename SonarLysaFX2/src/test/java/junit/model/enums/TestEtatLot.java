package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.EtatLot;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEtatLot extends TestAbstractEnum<EtatLot>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatLot.values().length).isEqualTo(11);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatLot.from("Abandonné")).isEqualTo(EtatLot.ABANDONNE);
        assertThat(EtatLot.from("\0Abandonné")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("Nouveau")).isEqualTo(EtatLot.NOUVEAU);
        assertThat(EtatLot.from("\0Nouveau")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("En DEV-TU")).isEqualTo(EtatLot.DEVTU);
        assertThat(EtatLot.from("\0En DEV-TU")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("TFON")).isEqualTo(EtatLot.TFON);
        assertThat(EtatLot.from("\0TFON")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("En Vérification MOE")).isEqualTo(EtatLot.VMOE);
        assertThat(EtatLot.from("\0En Vérification MOE")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("En Validation MOA")).isEqualTo(EtatLot.VMOA);
        assertThat(EtatLot.from("\0En Validation MOA")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("Installé")).isEqualTo(EtatLot.INSTALLE);
        assertThat(EtatLot.from("\0Installé")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("Livré à l'Edition")).isEqualTo(EtatLot.EDITION);
        assertThat(EtatLot.from("\0Livré à l'Edition")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("Terminé")).isEqualTo(EtatLot.TERMINE);
        assertThat(EtatLot.from("\0Terminé")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("Candidat pour la Validation MOA")).isEqualTo(EtatLot.MOA);
        assertThat(EtatLot.from("\0Candidat pour la Validation MOA")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("inconnu")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("\0inconnu")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from("autre")).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from(null)).isEqualTo(EtatLot.INCONNU);
        assertThat(EtatLot.from(Statics.EMPTY)).isEqualTo(EtatLot.INCONNU);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatLot.ABANDONNE.getValeur()).isEqualTo("Abandonné");
        assertThat(EtatLot.NOUVEAU.getValeur()).isEqualTo("Nouveau");
        assertThat(EtatLot.DEVTU.getValeur()).isEqualTo("En DEV-TU");
        assertThat(EtatLot.TFON.getValeur()).isEqualTo("TFON");
        assertThat(EtatLot.VMOE.getValeur()).isEqualTo("En Vérification MOE");
        assertThat(EtatLot.VMOA.getValeur()).isEqualTo("En Validation MOA");
        assertThat(EtatLot.INSTALLE.getValeur()).isEqualTo("Installé");
        assertThat(EtatLot.EDITION.getValeur()).isEqualTo("Livré à l'Edition");
        assertThat(EtatLot.TERMINE.getValeur()).isEqualTo("Terminé");
        assertThat(EtatLot.MOA.getValeur()).isEqualTo("Candidat pour la Validation MOA");
        assertThat(EtatLot.INCONNU.getValeur()).isEqualTo("INCONNU");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(EtatLot.class, testInfo);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatLot.valueOf(EtatLot.ABANDONNE.toString())).isEqualTo(EtatLot.ABANDONNE);
    }
}
