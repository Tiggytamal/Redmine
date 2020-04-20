package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.TextMatchers;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.sun.javafx.scene.control.skin.LabeledText;

import control.rtc.ControlRTC;
import dao.DaoLotRTC;
import fxml.dialog.AjouterDefautDialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import utilities.Statics;
import utilities.TechnicalException;

@SuppressWarnings("restriction")
@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestAjouterDefautDialog extends TestAbstractFXML<AjouterDefautDialog>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        // @formatter:off
        FxToolkit.setupFixture(() -> { 
            objetTest = new AjouterDefautDialog();
            objetTest.show(); 
            });
        // @formatter:on
    }

    @AfterEach
    public void close() throws TimeoutException
    {
        FxToolkit.setupFixture(() -> objetTest.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test des données génériques du Dialog
        assertThat(objetTest.getTitle()).isEqualTo("Créer Nouveau Défaut Qualité");
        assertThat(objetTest.getHeaderText()).isEmpty();
        assertThat(objetTest.getDialogPane().getStylesheets()).containsExactly(CSS);
        assertThat(objetTest.getDialogPane().getButtonTypes()).containsExactly(ButtonType.OK, getField("close"));
    }

    @Test
    public void testRetourObjet_et_controle_OK(TestInfo testInfo, FxRobot robot) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Connexion RTC
        ControlRTC.build().connexionSimple();

        // Remplissage des champs
        robot.clickOn(lookup(robot, "numeroAnoField"));
        robot.write("423989");
        robot.clickOn(lookup(robot, "numeroLotField"));
        robot.write("423737");
        robot.clickOn(lookup(robot, "nomComposant"));
        robot.write("WebApp_GreenWeb");
        robot.clickOn(lookup(robot, "remarques"));
        robot.write(TESTSTRING);
        robot.clickOn(lookup(robot, "dateDetection"));
        robot.write("10/01/2019");
        robot.clickOn(lookup(robot, "dateRelance"));
        robot.write("23/09/2019");
        robot.clickOn(lookup(robot, "dateMiseEnProd"));
        robot.write("01/01/2020");
        robot.clickOn((LabeledText) robot.from(robot.lookup("#pane")).lookup(TextMatchers.hasText("OK")).query());

        // Vérification du retour du Dialog
        DefautQualite dq = objetTest.getResult();
        assertThat(dq).isNotNull();
        assertThat(dq.getNumeroAnoRTC()).isEqualTo(423989);
        assertThat(dq.getLotRTC()).isNotNull();
        assertThat(dq.getLotRTC().getNumero()).isEqualTo("423737");
        assertThat(dq.getCompo()).isNotNull();
        assertThat(dq.getCompo().getNom()).isEqualTo("Composant WebApp_GreenWeb");
        assertThat(dq.getRemarque()).isEqualTo(TESTSTRING);
        assertThat(dq.getDateDetection()).isEqualTo(LocalDate.of(2019, 1, 10));
        assertThat(dq.getDateRelance()).isEqualTo(LocalDate.of(2019, 9, 23));
        assertThat(dq.getDateCreation()).isEqualTo(LocalDate.of(2019, 9, 20));
        assertThat(dq.getDateReso()).isEqualTo(LocalDate.of(2019, 9, 27));
        assertThat(dq.getDateMepPrev()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(dq.getEtatAnoRTC()).isEqualTo(Statics.ANOCLOSE);
    }

    @Test
    public void testControle_KO(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Connexion RTC
        ControlRTC.getInstance().connexionSimple();

        // On va appeler la méthode sans passer par le robot, car iil n'y a pas de moyen de faire un catch de l'exception

        // Test initial sans champs remplis
        assertThat(appelControle()).isFalse();
        assertThat(getField("donnesIncorrectes")).isEqualTo("Le lot n'est pas renseigné.\nLe numéro de l'anomalie n'est pas renseigné.\n"
                + "Le composant n'est pas renseigné.\nLa date de détection doit renseignée être avant la date de création.\n");

        // Test avec numéros lot et ano mauvais
        robot.clickOn(lookup(robot, "numeroAnoField"));
        robot.write("1");
        robot.clickOn(lookup(robot, "numeroLotField"));
        robot.write("1");
        robot.clickOn(lookup(robot, "nomComposant"));
        robot.write("inconnu");
        assertThat(appelControle()).isFalse();
        assertThat(getField("donnesIncorrectes"))
                .isEqualTo("Le lot est inconnu.\nLe numéro de l'anomalie est inconnu.\n" + "Le composant est inconnu.\nLa date de détection doit renseignée être avant la date de création.\n");
    }

    @Test
    public void testControleLot(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock pour faire un retour à null de puis la base de données
        DaoLotRTC mock = mock(DaoLotRTC.class);
        when(mock.recupEltParIndex(Mockito.anyString())).thenReturn(null);
        setField("daoLot", mock);

        // Mock pour faire remonter une exception
        ControlRTC mockRTC = mock(ControlRTC.class);
        when(mockRTC.recupWorkItemDepuisId(Mockito.anyInt())).thenThrow(TeamRepositoryException.class);
        setField("controlRTC", mockRTC);

        // Enregistrement donnée
        ((TextField) getField("numeroLotField")).setText("423737");

        // Premier appel, retour de l'erreur technique
        TechnicalException e = assertThrows(TechnicalException.class, () -> appelControleLotRTC());
        assertThat(e.getMessage()).isEqualTo("Méthode view.dialog.AjouterDefautDialog.controleLotRTC : Erreur appel RTC");

        // Connexion RTC
        ControlRTC.getInstance().connexionSimple();
        setField("controlRTC", ControlRTC.getInstance());

        // Duexième appel, retour lot
        appelControleLotRTC();
        LotRTC lot = (LotRTC) getField("lot");
        assertThat(lot).isNotNull();
        assertThat(lot.getNumero()).isEqualTo("423737");
    }

    @Test
    public void testControleAno(TestInfo testInfo) throws IllegalAccessException, TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Enregistrement donnée
        ((TextField) getField("numeroAnoField")).setText("423989");

        // Mock pour faire remonter une exception
        ControlRTC mockRTC = mock(ControlRTC.class);
        when(mockRTC.recupWorkItemDepuisId(Mockito.anyInt())).thenThrow(TeamRepositoryException.class);
        setField("controlRTC", mockRTC);

        // Premier appel, retour de l'erreur technique
        TechnicalException e = assertThrows(TechnicalException.class, () -> invokeMethod(objetTest, "controleAno"));
        assertThat(e.getMessage()).isEqualTo("Méthode view.dialog.AjouterDefautDialog.controleAno : Erreur appel RTC");
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean appelControle() throws Exception
    {
        return invokeMethod(objetTest, "controle");
    }

    private boolean appelControleLotRTC() throws Exception
    {
        return invokeMethod(objetTest, "controleLotRTC");
    }

    /*---------- ACCESSEURS ----------*/
}
