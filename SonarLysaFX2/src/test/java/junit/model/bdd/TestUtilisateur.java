package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import dao.Dao;
import dao.DaoFactory;
import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.Utilisateur;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestUtilisateur extends TestAbstractBDDModele<Utilisateur, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        ControlRTC.build().connexionSimple();
        ControlRTC.getInstance().recupTousLesProjets();
        IContributor contrib = ControlRTC.getInstance().recupContributorDepuisNom("PRUDENT Alain");
        Utilisateur user = Utilisateur.build(contrib);
        assertThat(user.getNom()).isEqualTo(contrib.getName());
        assertThat(user.getIdentifiant()).isEqualTo(contrib.getUserId());
        assertThat(user.getEmail()).isEqualTo(contrib.getEmailAddress());

        // Test avec utilisateur pas dans la base de donnée
        contrib = ControlRTC.getInstance().recupContributorDepuisNom("ABDELHEDI Louay");
        user = Utilisateur.build(contrib);

        Dao<Utilisateur, String> dao = DaoFactory.getMySQLDao(Utilisateur.class);

        // Test utilisateur créé
        Utilisateur userTest = dao.recupEltParIndex("ETP8088");
        dao.delete(user);
        assertThat(userTest).isNotNull();
        assertThat(userTest.getNom()).isEqualTo("ABDELHEDI Louay");
        assertThat(userTest.getIdentifiant()).isEqualTo("ETP8088");
        assertThat(userTest.getEmail()).isNotEmpty();
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setIdentifiant(KEY);
        assertThat(objetTest.getMapIndex()).isEqualTo(KEY);
    }

    @Test
    public void testEquals(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setEmail("mail@mail.com");
        objetTest.setIdentifiant(KEY);
        objetTest.setNom(NOM);
        setField("idBase", 12345);

        Utilisateur user = ModelFactory.build(Utilisateur.class);

        testSimpleEquals(user);

        // test autres paramètres
        Whitebox.getField(Utilisateur.class, "idBase").set(user, user.getIdBase());
        assertThat(objetTest.equals(user)).isFalse();

        user.setEmail(objetTest.getEmail());
        assertThat(objetTest.equals(user)).isFalse();

        user.setIdentifiant(objetTest.getIdentifiant());
        assertThat(objetTest.equals(user)).isFalse();

        user.setNom(objetTest.getNom());
        assertThat(objetTest.equals(user)).isTrue();
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetIdentifiant(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getIdentifiant(), s -> objetTest.setIdentifiant(s));
    }

    @Test
    public void testGetEmail(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEmail(), s -> objetTest.setEmail(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
