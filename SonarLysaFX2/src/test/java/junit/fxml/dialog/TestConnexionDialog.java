package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;

import fxml.dialog.ConnexionDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestConnexionDialog extends TestAbstractFXML<ConnexionDialog>
{
    /*---------- ATTRIBUTS ----------*/

    private Button buttonLogin;
    private Button buttonCancel;
    private PasswordField password;
    private TextField username;
    private DialogPane pane;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        FxToolkit.setupFixture(() -> { objetTest = new ConnexionDialog(); objetTest.show(); });

        pane = objetTest.getDialogPane();
        assertThat(pane.getButtonTypes()).isNotEmpty();
        buttonLogin = (Button) pane.lookupButton(objetTest.getDialogPane().getButtonTypes().get(1));
        buttonCancel = (Button) pane.lookupButton((ButtonType) getField("close"));
        password = (PasswordField) getField("password");
        username = (TextField) getField("username");
    }

    @AfterEach
    public void close() throws TimeoutException
    {
        FxToolkit.setupFixture(() -> objetTest.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Contrôle des données du dialogue
        assertThat(objetTest.getTitle()).isEqualTo("Connexion");
        assertThat(objetTest.getHeaderText()).isEmpty();
        assertThat(pane.getStylesheets()).containsExactly(CSS);
        assertThat(buttonLogin).isNotNull();
        assertThat(buttonCancel).isNotNull();

        // Contrôle Grid
        assertThat(pane.getContent()).isNotNull();
        assertThat(pane.getContent()).isInstanceOf(GridPane.class);

        // Contrôle TextField
        assertThat(username).isNotNull();
        assertThat(username.getPromptText()).isEqualTo("Pseudo");
        Assertions.assertThat(username).isFocused();

        // Contrôle PasswordField
        assertThat(password).isNotNull();
        assertThat(password.getPromptText()).isEqualTo("Mot de passe");
    }

    @Test
    public void testBouton_Login(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Envoi mot de passe et pseudo
        robot.write("Login");
        robot.clickOn(password);
        robot.write("Password");
        robot.clickOn(buttonLogin);

        // Contrôle qu'on envoie bien le mot de passe et le login
        assertThat(objetTest.getResult()).isEqualTo(new Pair<>("Login", "Password"));
    }

    @Test
    public void testBouton_Cancel(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Envoi mot de passe et pseudo
        robot.write("Login");
        robot.clickOn(password);
        robot.write("Password");
        robot.clickOn(buttonCancel);

        // Contrôle qu'on envoie bien le mot de passe et le login
        assertThat(objetTest.getResult()).isEqualTo(null);
    }

    @Test
    public void testTextListener(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test bouton login grisé
        Assertions.assertThat(buttonLogin).isDisabled();

        // Test un champ rempli
        robot.write("Login");
        Assertions.assertThat(buttonLogin).isDisabled();

        // Test deux champs remplis
        robot.clickOn(password);
        robot.write("Password");
        Assertions.assertThat(buttonLogin).isEnabled();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
