package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import junit.AutoDisplayName;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEtatAnoRTCProduit extends TestAbstractEnum<EtatAnoRTCProduit>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTCProduit.valueOf("NOUVELLE")).isEqualTo(EtatAnoRTCProduit.NOUVELLE);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTCProduit.values().length).isEqualTo(12);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test toutes les valeurs possibles
        assertThat(EtatAnoRTCProduit.from("1.Nouvelle")).isEqualTo(EtatAnoRTCProduit.NOUVELLE);
        assertThat(EtatAnoRTCProduit.from("2.Affectée")).isEqualTo(EtatAnoRTCProduit.AFFECTEE);
        assertThat(EtatAnoRTCProduit.from("3.Analyse")).isEqualTo(EtatAnoRTCProduit.ANALYSE);
        assertThat(EtatAnoRTCProduit.from("4.En cours de correction")).isEqualTo(EtatAnoRTCProduit.CORRECTION);
        assertThat(EtatAnoRTCProduit.from("5.Corrigée MOE")).isEqualTo(EtatAnoRTCProduit.CORRIGEE);
        assertThat(EtatAnoRTCProduit.from("6. Livrée MOE")).isEqualTo(EtatAnoRTCProduit.VMOE);
        assertThat(EtatAnoRTCProduit.from("7. Validée MOE")).isEqualTo(EtatAnoRTCProduit.VERIFIEE);
        assertThat(EtatAnoRTCProduit.from("8.Livrée MOA")).isEqualTo(EtatAnoRTCProduit.VMOA);
        assertThat(EtatAnoRTCProduit.from("9.Validée")).isEqualTo(EtatAnoRTCProduit.CLOSE);
        assertThat(EtatAnoRTCProduit.from("Abandonnée")).isEqualTo(EtatAnoRTCProduit.ABANDONNEE);
        assertThat(EtatAnoRTCProduit.from("En attente")).isEqualTo(EtatAnoRTCProduit.ENATTENTE);
        assertThat(EtatAnoRTCProduit.from("Rejeté")).isEqualTo(EtatAnoRTCProduit.REJETE);

        // Test hashcode et equals différents
        List<String> listeValeur = Arrays.asList(new String[]
        { "\0" + "1.Nouvelle", "\02.Affectée", "\03.Analyse", "\04.En cours de correction", "\05.Corrigée MOE", "\06. Livrée MOE", "\07. Validée MOE", "\08.Livrée MOA", "\09.Validée", "\0Abandonnée",
                "\0En attente", null, Statics.EMPTY });

        for (String valeur : listeValeur)
        {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> EtatAnoRTCProduit.from(valeur));
            assertThat(e.getMessage()).isEqualTo("model.enums.EtatAnoRTCProduit.from - état envoyé inconnu : " + valeur);
        }
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
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTCProduit.NOUVELLE.getValeur()).isEqualTo("1.Nouvelle");
        assertThat(EtatAnoRTCProduit.AFFECTEE.getValeur()).isEqualTo("2.Affectée");
        assertThat(EtatAnoRTCProduit.ANALYSE.getValeur()).isEqualTo("3.Analyse");
        assertThat(EtatAnoRTCProduit.CORRECTION.getValeur()).isEqualTo("4.En cours de correction");
        assertThat(EtatAnoRTCProduit.CORRIGEE.getValeur()).isEqualTo("5.Corrigée MOE");
        assertThat(EtatAnoRTCProduit.VMOE.getValeur()).isEqualTo("6. Livrée MOE");
        assertThat(EtatAnoRTCProduit.VERIFIEE.getValeur()).isEqualTo("7. Validée MOE");
        assertThat(EtatAnoRTCProduit.VMOA.getValeur()).isEqualTo("8.Livrée MOA");
        assertThat(EtatAnoRTCProduit.CLOSE.getValeur()).isEqualTo("9.Validée");
        assertThat(EtatAnoRTCProduit.ABANDONNEE.getValeur()).isEqualTo("Abandonnée");
        assertThat(EtatAnoRTCProduit.ENATTENTE.getValeur()).isEqualTo("En attente");
    }

    @Test
    public void testGetAction(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatAnoRTCProduit.NOUVELLE.getAction()).isEqualTo("Affecter");
        assertThat(EtatAnoRTCProduit.AFFECTEE.getAction()).isEqualTo("Analyser");
        assertThat(EtatAnoRTCProduit.ANALYSE.getAction()).isEqualTo("Commencer la correction");
        assertThat(EtatAnoRTCProduit.CORRECTION.getAction()).isEqualTo("Correction terminée");
        assertThat(EtatAnoRTCProduit.CORRIGEE.getAction()).isEqualTo("Livrer MOE");
        assertThat(EtatAnoRTCProduit.VMOE.getAction()).isEqualTo("Valider MOE");
        assertThat(EtatAnoRTCProduit.VERIFIEE.getAction()).isEqualTo("Livrer à la MOA");
        assertThat(EtatAnoRTCProduit.VMOA.getAction()).isEqualTo("Valider OK");
        assertThat(EtatAnoRTCProduit.CLOSE.getAction()).isEqualTo(Statics.EMPTY);
        assertThat(EtatAnoRTCProduit.ABANDONNEE.getAction()).isEqualTo(Statics.EMPTY);
        assertThat(EtatAnoRTCProduit.ENATTENTE.getAction()).isEqualTo("Affecter");
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(EtatAnoRTCProduit.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
