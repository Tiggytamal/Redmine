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
import model.enums.EtatAnoRTC;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEtatAnoRTC extends TestAbstractEnum<EtatAnoRTC>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTC.values().length).isEqualTo(14);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTC.from("Nouvelle")).isEqualTo(EtatAnoRTC.NOUVELLE);
        assertThat(EtatAnoRTC.from("1.Nouvelle")).isEqualTo(EtatAnoRTC.NOUVELLE);
        assertThat(EtatAnoRTC.from("Ouverte")).isEqualTo(EtatAnoRTC.OUVERTE);
        assertThat(EtatAnoRTC.from("En cours")).isEqualTo(EtatAnoRTC.ENCOURS);
        assertThat(EtatAnoRTC.from("Résolue")).isEqualTo(EtatAnoRTC.RESOLUE);
        assertThat(EtatAnoRTC.from("En attente vérification MOE")).isEqualTo(EtatAnoRTC.VMOE);
        assertThat(EtatAnoRTC.from("En attente validation/homologation MOA")).isEqualTo(EtatAnoRTC.VMOA);
        assertThat(EtatAnoRTC.from("Vérifiée")).isEqualTo(EtatAnoRTC.VERIFIEE);
        assertThat(EtatAnoRTC.from(Statics.ANOCLOSE)).isEqualTo(EtatAnoRTC.CLOSE);
        assertThat(EtatAnoRTC.from("Réouverte")).isEqualTo(EtatAnoRTC.REOUVERTE);
        assertThat(EtatAnoRTC.from("Rejetée")).isEqualTo(EtatAnoRTC.REJETEE);
        assertThat(EtatAnoRTC.from("Correctif éditeur testé")).isEqualTo(EtatAnoRTC.EDITEUR);
        assertThat(EtatAnoRTC.from("En attente correctif éditeur")).isEqualTo(EtatAnoRTC.ATTEDITEUR);
        assertThat(EtatAnoRTC.from("En attente de dénouement")).isEqualTo(EtatAnoRTC.DENOUEMENT);
        assertThat(EtatAnoRTC.from("Abandonnée")).isEqualTo(EtatAnoRTC.ABANDONNEE);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings =
    { "\0Nouvelle", "\01.Nouvelle", "\0Ouverte", "\0En cours", "\0Résolue", "\0En attente vérification MOE", "\0En attente validation/homologation MOA", "\0Vérifiée", "\0Close", "\0Réouverte",
            "\0Rejetée", "\0En attente de dénouement", "\0Abandonnée", "\0En attente correctif éditeur", "\0Correctif éditeur testé" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> EtatAnoRTC.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.EtatAnoRTC.from - état envoyé inconnu : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> EtatAnoRTC.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.EtatAnoRTC.from - état envoyé nul ou vide.");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        assertThat(EtatAnoRTC.NOUVELLE.getValeur()).isEqualTo("Nouvelle");
        assertThat(EtatAnoRTC.OUVERTE.getValeur()).isEqualTo("Ouverte");
        assertThat(EtatAnoRTC.ENCOURS.getValeur()).isEqualTo("En cours");
        assertThat(EtatAnoRTC.RESOLUE.getValeur()).isEqualTo("Résolue");
        assertThat(EtatAnoRTC.VMOE.getValeur()).isEqualTo("En attente vérification MOE");
        assertThat(EtatAnoRTC.VMOA.getValeur()).isEqualTo("En attente validation/homologation MOA");
        assertThat(EtatAnoRTC.VERIFIEE.getValeur()).isEqualTo("Vérifiée");
        assertThat(EtatAnoRTC.CLOSE.getValeur()).isEqualTo(Statics.ANOCLOSE);
        assertThat(EtatAnoRTC.REOUVERTE.getValeur()).isEqualTo("Réouverte");
        assertThat(EtatAnoRTC.REJETEE.getValeur()).isEqualTo("Rejetée");
        assertThat(EtatAnoRTC.EDITEUR.getValeur()).isEqualTo("Correctif éditeur testé");
        assertThat(EtatAnoRTC.ATTEDITEUR.getValeur()).isEqualTo("En attente correctif éditeur");
        assertThat(EtatAnoRTC.DENOUEMENT.getValeur()).isEqualTo("En attente de dénouement");
        assertThat(EtatAnoRTC.ABANDONNEE.getValeur()).isEqualTo("Abandonnée");
    }

    @Test
    public void testGetAction(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTC.NOUVELLE.getAction()).isEqualTo("Accepter");
        assertThat(EtatAnoRTC.OUVERTE.getAction()).isEqualTo("Commencer à travailler");
        assertThat(EtatAnoRTC.ENCOURS.getAction()).isEqualTo("Terminer le travail");
        assertThat(EtatAnoRTC.RESOLUE.getAction()).isEqualTo("Clore");
        assertThat(EtatAnoRTC.VMOE.getAction()).isEqualTo("Vérifier OK");
        assertThat(EtatAnoRTC.VERIFIEE.getAction()).isEqualTo("Clore");
        assertThat(EtatAnoRTC.VMOA.getAction()).isEqualTo("Valider et clore");
        assertThat(EtatAnoRTC.CLOSE.getAction()).isEqualTo("");
        assertThat(EtatAnoRTC.REOUVERTE.getAction()).isEqualTo("Commencer à travailler");
        assertThat(EtatAnoRTC.REJETEE.getAction()).isEqualTo("");
        assertThat(EtatAnoRTC.EDITEUR.getAction()).isEqualTo("Terminer le travail");
        assertThat(EtatAnoRTC.ATTEDITEUR.getAction()).isEqualTo("Tester correctif");
        assertThat(EtatAnoRTC.DENOUEMENT.getAction()).isEqualTo("Reprendre");
        assertThat(EtatAnoRTC.CLOSE.getAction()).isEqualTo("");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        testValeurInstanciation(EtatAnoRTC.class, testInfo);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTC.valueOf(EtatAnoRTC.NOUVELLE.toString())).isEqualTo(EtatAnoRTC.NOUVELLE);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
