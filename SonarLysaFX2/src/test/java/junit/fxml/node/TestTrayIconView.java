package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxToolkit;

import fxml.node.TrayIconView;
import javafx.stage.Stage;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTrayIconView extends TestAbstractFXML<TrayIconView>
{
    /*---------- ATTRIBUTS ----------*/

    private SystemTray tray;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws InterruptedException, TimeoutException
    {
        FxToolkit.setupFixture(() -> objetTest = new TrayIconView());
        tray = SystemTray.getSystemTray();
    }

    @AfterEach
    public void afterEach()
    {
        // Sorti du mode icône pour tests suivants.
        stage.setIconified(false);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAddToTray(TestInfo testInfo) throws AWTException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.addToTray();
        assertThat(tray.getTrayIcons()).asList().hasSize(1);
    }

    @Test
    public void testRemoveFromTray(TestInfo testInfo) throws AWTException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.addToTray();
        objetTest.removeFromTray();
        assertThat(tray.getTrayIcons()).asList().doesNotContain(objetTest);
    }

    @Test
    public void testHideStage(TestInfo testInfo) throws IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setStage(stage);
        FxToolkit.setupFixture(() -> objetTest.hideStage());
        assertThat(((Stage) Whitebox.getField(TrayIconView.class, "stage").get(objetTest)).isShowing()).isFalse();
    }

    @Test
    public void testChangeImage(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test méthode
        objetTest.changeImage(TrayIconView.imageBase);
        assertThat(((TrayIcon) getField("trayIcon")).getImage()).isEqualTo(TrayIconView.imageBase);
        objetTest.changeImage(TrayIconView.imageRed);
        assertThat(((TrayIcon) getField("trayIcon")).getImage()).isEqualTo(TrayIconView.imageRed);

        // Test protection null
        objetTest.changeImage(null);
        assertThat(((TrayIcon) getField("trayIcon")).getImage()).isEqualTo(TrayIconView.imageRed);
    }

    @Test
    public void testSetStage(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test méthode
        objetTest.setStage(stage);
        assertThat(getField("stage")).isEqualTo(stage);

        // Test protection null
        objetTest.setStage(null);
        assertThat(getField("stage")).isEqualTo(stage);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
