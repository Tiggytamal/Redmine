package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import fxml.dialog.ProgressDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.WindowEvent;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.EmptyTaskForTest;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestProgressDialog extends TestAbstractFXML<ProgressDialog>
{
    /*---------- ATTRIBUTS ----------*/

    private EmptyTaskForTest task;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        task = new EmptyTaskForTest(true);
        FxToolkit.setupFixture(() -> objetTest = new ProgressDialog(task));
    }

    @AfterEach
    public void close() throws TimeoutException
    {
        FxToolkit.setupFixture(() -> objetTest.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        task = new EmptyTaskForTest(false);
        FxToolkit.setupFixture(() -> { objetTest = new ProgressDialog(task); });

        // Test initialisation de la fenêtre
        assertThat(objetTest.getTitle()).isEqualTo("TEST");
        assertThat(objetTest.getHeaderText()).isEmpty();
        DialogPane pane = objetTest.getDialogPane();
        assertThat(pane.getStylesheets()).containsExactly(CSS);
        List<ButtonType> liste = pane.getButtonTypes();
        assertThat(liste).hasSize(1);
        assertThat(liste.get(0).getText()).isEqualTo("Fermer");
        assertThat(liste.get(0).getButtonData()).isEqualTo(ButtonData.CANCEL_CLOSE);

        // Affichage et lancement tâche.
        FxToolkit.setupFixture(() -> objetTest.show());
        task.run();

        // Vérification bouton annuler désactivé
        Button cancel = robot.fromAll().lookup("#cancel").query();
        assertThat(cancel.isDisabled()).isTrue();

        // Test activation du bouton ok
        Button ok = robot.from(pane).lookup("#ok").query();
        robot.clickOn(ok);
    }

    @Test
    public void testAnnuler(TestInfo testInfo, FxRobot robot) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Affichage et lancement tâche.
        FxToolkit.setupFixture(() -> objetTest.show());
        new Thread(task).start();

        // Test appuis sur bouton annuler et vérification annulation tâche
        Button cancel = robot.fromAll().lookup("#cancel").query();
        assertThat(cancel.isDisabled()).isFalse();
        robot.clickOn(cancel);
        assertThat(task.isCancelled()).isTrue();
    }

    @Test
    public void testClickCroix(TestInfo testInfo, FxRobot robot) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Affichage et lancement tâche.
        FxToolkit.setupFixture(() -> {
            objetTest.show();
            objetTest.getDialogPane().getScene().getWindow().fireEvent(new WindowEvent(objetTest.getDialogPane().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        assertThat(task.isCancelled()).isFalse();
        assertThat(objetTest.isShowing()).isFalse();
    }

    @Test
    public void testClickCroixCancel(TestInfo testInfo, FxRobot robot) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Affichage et lancement tâche.
        FxToolkit.setupFixture(() -> {
            objetTest.show();
            task.annuler();
            objetTest.getDialogPane().getScene().getWindow().fireEvent(new WindowEvent(objetTest.getDialogPane().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        assertThat(task.isCancelled()).isFalse();
        assertThat(objetTest.isShowing()).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
