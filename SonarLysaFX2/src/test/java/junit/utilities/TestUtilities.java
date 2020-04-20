package junit.utilities;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxToolkit;

import application.Main;
import application.MainScreen;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.Utilities;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestUtilities extends JunitBase<Utilities>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        // Pas d'instanciation
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThrows(AssertionError.class, () -> Whitebox.invokeConstructor(Utilities.class));
    }

    @Test
    public void testGetLocation(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test url classe
        URL url = Utilities.getLocation(Main.class);
        assertThat(url.toString()).contains("file:");
    }

    @Test
    public void testGetLocation_Null(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test exception avec paramètre nul
        TechnicalException e = assertThrows(TechnicalException.class, () -> Utilities.getLocation(null));
        assertThat(e.getMessage()).isEqualTo("Utilities.getLocation - paramètre null");
    }

    @Test
    public void testUrlToFile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec fichier
        File file = Utilities.urlToFile(Utilities.getLocation(Main.class));
        assertThat(file).isNotNull();
        assertThat(file.getPath()).contains("SonarLysaFX2");
    }

    @Test
    public void testUrlToFile_Null(TestInfo testInfo)
    {
        // test exception avec paramètre nul
        URL url = null;
        TechnicalException e = assertThrows(TechnicalException.class, () -> Utilities.urlToFile(url));
        assertThat(e.getMessage()).isEqualTo("Utilities.urlToFile - paramètre null");
    }

    @Test
    public void testTranscoEdition(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test ok
        assertThat(Utilities.transcoEdition("E33")).isEqualTo("16");
        assertThat(Utilities.transcoEdition("E29")).isEqualTo("12");

        // Test exception apramètre null
        assertThrows(FunctionalException.class, () -> Utilities.transcoEdition(null));

        // Test exception paraètre incohérent
        assertThrows(FunctionalException.class, () -> Utilities.transcoEdition("33"));
    }

    @Test
    public void testCreateAlert(TestInfo testInfo) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel alert et contrôle des données
        Stage stage = FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> {
            if (MainScreen.ROOT.getScene() == null)
                new Scene(MainScreen.ROOT, 1024, 768);
            stage.setScene(MainScreen.ROOT.getScene());
            Alert alert = Utilities.createAlert(proprietesXML.controleDonnees());
            assertThat(alert.getAlertType()).isEqualTo(AlertType.INFORMATION);
            assertThat(alert.isResizable()).isTrue();
            assertThat(alert.getHeaderText()).isNull();
            assertThat(alert.getContentText()).contains("Chargement paramètres :");
            assertThat(alert.getContentText()).contains("Nom colonnes OK");
            assertThat(alert.getContentText()).contains("Paramètres OK");
            assertThat(alert.getContentText()).contains("Planificateurs OK");
        });
        FxToolkit.setupFixture(() -> stage.close());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
