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
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.fxml.DefautQualiteFXML;
import fxml.dialog.RemarqueDialog;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestRemarqueDialog extends TestAbstractFXML<RemarqueDialog>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualiteFXML dq;
    private Button buttonOK;
    private Button buttonCancel;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        dq = new DefautQualiteFXML();
        dq.setRemarque(TESTSTRING);
        FxToolkit.setupFixture(() -> { 
            objetTest = new RemarqueDialog(dq); 
            objetTest.show(); 
            });
        buttonOK = (Button) objetTest.getDialogPane().lookupButton(objetTest.getDialogPane().getButtonTypes().get(1));
        buttonCancel = (Button) objetTest.getDialogPane().lookupButton((ButtonType) getField("close"));
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

        // Contrôle des données du dialogue
        assertThat(objetTest.getTitle()).isEqualTo("Remarques");
        assertThat(objetTest.getHeaderText()).isEmpty();
        assertThat(objetTest.getDialogPane().getStylesheets()).containsExactly(CSS);

        // Contrôle des boutons
        assertThat(objetTest.getDialogPane().getButtonTypes()).contains(getField("close"));
        assertThat(objetTest.getDialogPane().getButtonTypes().get(1).getButtonData()).isEqualTo(ButtonData.OK_DONE);
        assertThat(objetTest.getDialogPane().getButtonTypes().get(1).getText()).isEqualTo("Sauvegarder");

        // Contrôle du texte provenant du défaut
        assertThat(objetTest.getDialogPane().getContent()).isInstanceOf(TextArea.class);
        TextArea area = (TextArea) objetTest.getDialogPane().getContent();
        assertThat(area.getText()).isEqualTo(dq.getRemarque());
    }

    @Test
    public void testBouton_Cancel(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test de non sauvegarde du nouveau texte
        robot.clickOn(objetTest.getDialogPane().getContent());
        robot.write(TESTSTRING);
        robot.clickOn(buttonCancel);
        assertThat(objetTest.getResult()).isEqualTo(dq.getRemarque());
    }

    @Test
    public void testBouton_OK(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Modification du texte de la remarque
        robot.clickOn(objetTest.getDialogPane().getContent());
        robot.write(TESTSTRING);
        robot.clickOn(buttonOK);

        // Contrôle qu'on envoie bien le nouveau texte
        assertThat(objetTest.getResult()).isEqualTo(dq.getRemarque() + TESTSTRING);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
