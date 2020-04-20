package junit.model.fxml;

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
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        Utilisateur u = ModelFactory.build(Utilisateur.class);
        u.setActive(true);
        u.setEmail(TESTMANUEL);
        u.setIdentifiant(KEY);
        u.setNom(NOM);

        // Appel constructeur
        objetTest = UtilisateurFXML.build(u);

        // Tests
        assertThat(objetTest.isActive()).isTrue();
        assertThat(objetTest.getEmail()).isEqualTo(TESTMANUEL);
        assertThat(objetTest.getIdentifiant()).isEqualTo(KEY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
    }

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(UtilisateurFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getIndex()).isEmpty();
        objetTest.setIdentifiant(TESTMANUEL);
        assertThat(objetTest.getIndex()).isEqualTo(TESTMANUEL);
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetIdentifiant(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getIdentifiant(), s -> objetTest.setIdentifiant(s));
    }

    @Test
    public void testGetEmail(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEmail(), s -> objetTest.setEmail(s));
    }

    @Test
    public void testisActive(TestInfo testInfo)
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
