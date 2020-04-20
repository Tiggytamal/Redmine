package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.bdd.Utilisateur;
import model.fxml.UtilisateurFXML;
import model.fxml.UtilisateurFXML.UtilisateurFXMLGetters;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestUtilisateurFXML extends TestAbstractModele<UtilisateurFXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(UtilisateurFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        objetTest.setIdentifiant("123456");
        assertThat(objetTest.getIndex()).isEqualTo((objetTest.getIdentifiant()));
    }

    @Test
    public void testBuild(TestInfo testInfo)
    {
        // Initialisation paramètre méthode build
        Utilisateur u = ModelFactory.build(Utilisateur.class);
        u.setNom(NOM);
        u.setIdentifiant(KEY);
        u.setEmail(TESTMANUEL);
        u.setActive(true);

        // Appel méthode
        UtilisateurFXML ufxml = UtilisateurFXML.build(u);

        // Contrôles
        assertThat(ufxml.getNom()).isEqualTo(NOM);
        assertThat(ufxml.getIdentifiant()).isEqualTo(KEY);
        assertThat(ufxml.getEmail()).isEqualTo(TESTMANUEL);
        assertThat(ufxml.isActive()).isTrue();
    }

    @Test
    void testGetTitre(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    void testGetEtatAno(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getIdentifiant(), s -> objetTest.setIdentifiant(s));
    }

    @Test
    void testGetCommentaire(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEmail(), s -> objetTest.setEmail(s));
    }

    @Test
    void testGetActive(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.isActive()).isTrue();
        objetTest.setActive(false);
        assertThat(objetTest.isActive()).isFalse();
    }

    @Test
    public void testUtilisateurFXMLGettersValues(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(UtilisateurFXMLGetters.NOM.getGroupe()).isEqualTo("Informations");
        assertThat(UtilisateurFXMLGetters.NOM.getAffichage()).isEqualTo("Nom");
        assertThat(UtilisateurFXMLGetters.NOM.getNomParam()).isEqualTo("nom");
        assertThat(UtilisateurFXMLGetters.NOM.getNomMethode()).isEqualTo("getNom");
        assertThat(UtilisateurFXMLGetters.NOM.getStyle()).isEqualTo("tableYellow");
        assertThat(UtilisateurFXMLGetters.NOM.isAffParDefaut()).isTrue();
        assertThat(UtilisateurFXMLGetters.NOM.toString()).isEqualTo("Nom");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
