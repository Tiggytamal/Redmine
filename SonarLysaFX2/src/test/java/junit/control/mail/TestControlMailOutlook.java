package junit.control.mail;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.powermock.reflect.Whitebox;

import control.mail.ControlMailOutlook;
import control.rtc.ControlRTC;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.bdd.DefautQualite;
import model.bdd.Utilisateur;
import utilities.FunctionalException;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlMailOutlook extends JunitBase<ControlMailOutlook>
{

    /*---------- ATTRIBUTS ----------*/

    private DefautQualite dq;
    private HashSet<Utilisateur> users;
    private Utilisateur user;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        initDataBase();
        objetTest = new ControlMailOutlook();
        dq = databaseXML.getDqs().get(0);
        user = databaseXML.getUsers().get(0);
        users = new HashSet<>();
        users.add(user);
        ControlRTC.build().connexionSimple();
    }
    
    @AfterEach
    public void after()
    {
        ControlRTC.build().logout();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreerMailAssignerDefautSonar(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation
        Utilisateur user = databaseXML.getUsers().get(0);
        Set<Utilisateur> set = new HashSet<>();
        set.add(user);

        // Appel méthode et tests
        assertThat(objetTest.creerMailAssignerDefautSonar(set)).isTrue();

        // Contrôle parties statiques du builder
        String builder = ((StringBuilder) getField("builder")).toString();
        assertThat(builder).contains("mailto:");
        assertThat(builder).contains("?cc=");
        assertThat(builder).contains("&subject=");
        assertThat(builder).contains("&body=");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreerMailAssignerDefautSonar_NUll_Et_Vide(Set<Utilisateur> set, TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test exception
        assertThrows(FunctionalException.class, () -> objetTest.creerMailAssignerDefautSonar(set));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreerMailCreerAnoRTC_FunctionalException(Set<Utilisateur> set, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test exception
        assertThrows(FunctionalException.class, () -> objetTest.creerMailCreerAnoRTC(set, dq));
    }

    @Test
    public void testCreerMailCreerAnoRTC_TechnicalException(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test exception
        assertThrows(TechnicalException.class, () -> objetTest.creerMailCreerAnoRTC(users, null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreerMailRelanceAnoRTC_FunctionalException(Set<Utilisateur> set, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test exception
        assertThrows(FunctionalException.class, () -> objetTest.creerMailRelanceAnoRTC(set, dq));
    }

    @Test
    public void testCreerMailRelanceAnoRTC_TechnicalException(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test exception
        assertThrows(TechnicalException.class, () -> objetTest.creerMailRelanceAnoRTC(users, null));
    }

    @Test
    public void testInitMail_Users_vide(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        Whitebox.invokeMethod(objetTest, "initMail", new HashSet<>());

        // Contrôle
        String builder = ((StringBuilder) getField("builder")).toString();
        assertThat(builder).isEqualTo("mailto:?cc=Alain.PRUDENT@ca-ts.fr;Nicolas.TRICOT@ca-ts.fr;");
    }

    @Test
    public void testInitMail_Users_non_vide(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création set
        objetTest = new ControlMailOutlook();

        // Appel méthode
        Whitebox.invokeMethod(objetTest, "initMail", users);

        // Contrôle
        StringBuilder builder = ((StringBuilder) getField("builder"));
        assertThat(builder.toString()).isEqualTo("mailto:" + user.getEmail() + ";?cc=Alain.PRUDENT@ca-ts.fr;Nicolas.TRICOT@ca-ts.fr;");
    }

    @Test
    public void testCorpsAnoRTC(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
    }

    @Test
    public void testReplacePlus(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test méthode de remplacement des +
        String methode = "replacePlus";
        String string = "+++";
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, string)).isEqualTo("%20%20%20");
        string = "++ ++";
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, string)).isEqualTo("%20%20 %20%20");
        string = "azerty+azert";
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, string)).isEqualTo("azerty%20azert");
        string = " + + +";
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, string)).isEqualTo(" %20 %20 %20");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
