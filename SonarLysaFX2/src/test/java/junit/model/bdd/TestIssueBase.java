package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.IssueBase;
import model.bdd.Utilisateur;
import model.rest.sonarapi.Issue;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestIssueBase extends TestAbstractBDDModele<IssueBase, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test constructor depuis Issue
        Issue issue = new Issue();
        issue.setKey(KEY);
        assertThat(IssueBase.build(issue).getKey()).isEqualTo(issue.getKey());
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test retour getKey
        objetTest.setKey(KEY);
        assertThat(objetTest.getMapIndex()).isEqualTo(KEY);
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setAnalyseId(TESTSTRING);
        objetTest.setAssignee(true);
        objetTest.setDefautQualite(ModelFactory.build(DefautQualite.class));
        objetTest.setKey(KEY);
        objetTest.setUtilisateur(Statics.USERINCONNU);

        // Test simple
        IssueBase issue = ModelFactory.build(IssueBase.class);
        testSimpleEquals(issue);

        // Test autres paramètres
        issue.setAnalyseId(TESTSTRING);
        assertThat(objetTest.equals(issue)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(issue.hashCode());

        issue.setAssignee(true);
        assertThat(objetTest.equals(issue)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(issue.hashCode());

        issue.setDefautQualite(ModelFactory.build(DefautQualite.class));
        assertThat(objetTest.equals(issue)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(issue.hashCode());

        issue.setKey(KEY);
        assertThat(objetTest.equals(issue)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(issue.hashCode());

        issue.setUtilisateur(Statics.USERINCONNU);
        assertThat(objetTest.equals(issue)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(issue.hashCode());
    }

    @Test
    public void testGetUtilisateur(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getUtilisateur()).isNotNull();
        assertThat(objetTest.getUtilisateur()).isEqualTo(Statics.USERINCONNU);

        // Protection setter null
        objetTest.setUtilisateur(null);
        assertThat(objetTest.getUtilisateur()).isEqualTo(Statics.USERINCONNU);

        // Test getter et setter
        ControlRTC.build().connexionSimple();
        Utilisateur user = (Utilisateur.build(ControlRTC.getInstance().recupContributorDepuisNom("PRUDENT Alain")));
        objetTest.setUtilisateur(user);
        assertThat(objetTest.getUtilisateur()).isEqualTo(user);
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetAnalyseId(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getAnalyseId(), s -> objetTest.setAnalyseId(s));
    }

    @Test
    public void testGetDefautQualite(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getDefautQualite()).isNull();

        // Test getter et setter
        initDataBase();
        DefautQualite dq = databaseXML.getDqs().get(0);
        objetTest.setDefautQualite(dq);
        assertThat(objetTest.getDefautQualite()).isEqualTo(dq);

        // Test protection null
        objetTest.setDefautQualite(null);
        assertThat(objetTest.getDefautQualite()).isEqualTo(dq);
    }

    @Test
    public void testIsAssignee(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isAssignee(), b -> objetTest.setAssignee(b));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
