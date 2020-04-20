package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Branche;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Message;
import model.rest.sonarapi.Retour;
import model.rest.sonarapi.StatutQGProjet;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestRetour extends TestAbstractModele<Retour>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetComposant(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.getComposant()).isNull();

        // Test après introspection
        ComposantSonar compo = new ComposantSonar();
        compo.setId(KEY);
        setField("composant", compo);
        assertThat(objetTest.getComposant()).isEqualTo(compo);
    }

    @Test
    public void testGetErreurs(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.getErreurs()).isNotNull();
        assertThat(objetTest.getErreurs()).hasSize(0);

        // Test après introspection
        Message mess = new Message();
        List<Message> liste = new ArrayList<>();
        liste.add(mess);
        setField("erreurs", liste);
        assertThat(objetTest.getErreurs()).isEqualTo(liste);

        // Test protection introspection null
        setField("erreurs", null);
        assertThat(objetTest.getErreurs()).isNotNull();
        assertThat(objetTest.getErreurs()).hasSize(0);
    }

    @Test
    public void testGetBranches(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.getBranches()).isNotNull();
        assertThat(objetTest.getBranches()).hasSize(0);

        // Test après introspection
        Branche branche = new Branche();
        List<Branche> liste = new ArrayList<>();
        liste.add(branche);
        setField("branches", liste);
        assertThat(objetTest.getBranches()).isEqualTo(liste);

        // Test protection introspection null
        setField("branches", null);
        assertThat(objetTest.getBranches()).isNotNull();
        assertThat(objetTest.getBranches()).hasSize(0);
    }

    @Test
    public void testGetStatutProjet(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.getStatutProjet()).isNull();

        // Test après introspection
        StatutQGProjet sqgp = new StatutQGProjet();
        setField("statutProjet", sqgp);
        assertThat(objetTest.getStatutProjet()).isEqualTo(sqgp);
    }

    @Test
    public void testIsExist(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.isExist()).isFalse();

        // Test après introspection
        setField("exist", true);
        assertThat(objetTest.isExist()).isTrue();
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
