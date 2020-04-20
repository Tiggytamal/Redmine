package junit.model;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import model.Info;
import model.bdd.Utilisateur;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestInfo2 extends TestAbstractModele<Info>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testControle(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String valeur = "pasvide ";

        // Test après initialisation
        assertThat(objetTest.controle()).isFalse();

        // Test mot de passe vide
        objetTest.setMotDePasse(EMPTY);
        assertThat(objetTest.controle()).isFalse();

        // Test pseudo vide
        objetTest.setPseudo(EMPTY);
        objetTest.setMotDePasse(null);
        assertThat(objetTest.controle()).isFalse();

        // Test les deux vides
        objetTest.setPseudo(EMPTY);
        objetTest.setMotDePasse(EMPTY);
        assertThat(objetTest.controle()).isFalse();

        // Test mot de passe rempli
        objetTest.setMotDePasse("28H02m89");
        assertThat(objetTest.controle()).isFalse();

        // Test pseudo rempli
        objetTest.setMotDePasse(EMPTY);
        objetTest.setPseudo(valeur);
        assertThat(objetTest.controle()).isFalse();

        // Test les deux remplis
        objetTest.setPseudo(valeur);
        objetTest.setMotDePasse("28H02m89");
        assertThat(objetTest.controle()).isTrue();
    }

    @Test
    public void testGetFile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        File file = objetTest.getFile();
        assertThat(file).isEqualTo(new File(Statics.JARPATH + Whitebox.getInternalState(Info.class, "NOMFICHIER")));
    }

    @Test
    public void testControleDonnees(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test mdp null
        objetTest.setPseudo("a");
        assertThat(objetTest.controleDonnees()).isEqualTo("Fichier infos de connexion Connexion vide");

        // Test pseudo null
        objetTest.setPseudo(null);
        objetTest.setMotDePasse(Statics.info.getMotDePasse());
        assertThat(objetTest.controleDonnees()).isEqualTo("Fichier infos de connexion Connexion vide");

        // test ok
        objetTest.setPseudo("a");
        assertThat(objetTest.controleDonnees()).isEmpty();
    }

    @Test
    public void testGetMotDePasse(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec getString
        assertWithMessage("Retour getter null : ").that(objetTest.getMotDePasse()).isNotNull();
        assertThat(objetTest.getMotDePasse()).isEmpty();

        // Test getter et setter
        objetTest.setMotDePasse("motdepâsse");;
        assertThat(objetTest.getMotDePasse()).isEqualTo("motdepâsse");

        // Test protection setter null
        objetTest.setMotDePasse(null);
        assertWithMessage("Retour getter null : ").that(objetTest.getMotDePasse()).isNotNull();
        assertThat(objetTest.getMotDePasse()).isEmpty();
    }

    @Test
    public void testGetPseudo(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getPseudo(), s -> objetTest.setPseudo(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetUsers(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getUser()).isNull();

        // Test getter et setter
        initDataBase();
        Utilisateur user = databaseXML.getUsers().get(0);
        objetTest.setUser(user);
        assertThat(objetTest.getUser()).isEqualTo(user);

        // Protection setter null
        objetTest.setUser(null);
        assertThat(objetTest.getUser()).isEqualTo(user);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
