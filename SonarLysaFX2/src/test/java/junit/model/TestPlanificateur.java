package junit.model;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.Planificateur;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestPlanificateur extends TestAbstractModele<Planificateur>
{
    /*---------- ATTRIBUTS ----------*/

    private String anneeEnCours;
    private String prec;
    private String suiv;

    /*---------- CONSTRUCTEURS ----------*/

    public TestPlanificateur()
    {
        anneeEnCours = String.valueOf(today.getYear());
        prec = String.valueOf(today.getYear() - 1);
        suiv = String.valueOf(today.getYear() + 1);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testIsLundi(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isLundi(), (b) -> objetTest.setLundi(b));
    }

    @Test
    public void testIsMardi(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isMardi(), (b) -> objetTest.setMardi(b));
    }

    @Test
    public void testIsMercredi(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isMercredi(), (b) -> objetTest.setMercredi(b));
    }

    @Test
    public void testIsJeudi(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isJeudi(), (b) -> objetTest.setJeudi(b));
    }

    @Test
    public void testIsVendredi(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isVendredi(), (b) -> objetTest.setVendredi(b));
    }

    @Test
    public void testGetHeure(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à 00:00
        assertThat(objetTest.getHeure()).isEqualTo(LocalTime.of(0, 0));

        // Test getter et setter
        objetTest.setHeure(time);
        assertThat(objetTest.getHeure()).isEqualTo(time);

        // Test protection setter null
        objetTest.setHeure(null);
        assertThat(objetTest.getHeure()).isNotNull();
        assertThat(objetTest.getHeure()).isEqualTo(time);
    }

    @Test
    public void testIsActive(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isActive(), (b) -> objetTest.setActive(b));
    }

    @Test
    public void testGetAnnees(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test valeur après initialisation
        assertThat(objetTest.getAnnees().get(0)).isEqualTo(anneeEnCours);

        // Test ajout annee precedente
        objetTest.addLastYear();
        assertThat(objetTest.getAnnees()).contains(prec);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);

        // test ajout annee suivante
        objetTest.addNextYear();
        assertThat(objetTest.getAnnees()).contains(suiv);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);

        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);

        // Test ajout annee precedente
        objetTest.addLastYear();
        assertThat(objetTest.getAnnees()).contains(prec);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);

        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);

        // test ajout annee suivante
        objetTest.addNextYear();
        assertThat(objetTest.getAnnees()).contains(suiv);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);
    }

    @Test
    public void testAddLastYear(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test ajout annee precedente
        objetTest.addLastYear();
        assertThat(objetTest.getAnnees()).contains(prec);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);

        // Verification qu'on ajoute pas de doublons d'annee
        objetTest.addLastYear();
        objetTest.addLastYear();

        int compte = 0;
        for (String annee : objetTest.getAnnees())
        {
            if (annee.equals(prec))
                compte++;
        }
        assertWithMessage("Duplication des annees").that(compte).isEqualTo(1);
    }

    @Test
    public void testAddNextYear(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test ajout annee suivante
        objetTest.addNextYear();
        assertThat(objetTest.getAnnees()).contains(suiv);
        assertThat(objetTest.getAnnees()).contains(anneeEnCours);

        // Verification qu'on ajoute pas de doublons d'annee
        objetTest.addNextYear();
        objetTest.addNextYear();

        int compte = 0;
        for (String annee : objetTest.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertWithMessage("Duplication des annees").that(compte).isEqualTo(1);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
