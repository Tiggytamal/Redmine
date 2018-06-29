package junit.view;

import static org.junit.Assert.assertTrue;

import java.awt.AWTException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import javafx.stage.Stage;
import view.TrayIconView;

@RunWith(JfxRunner.class)
public class TestTrayIconView
{
    private TrayIconView tray;
    private Stage stage;

    @Before
    public void init() throws InterruptedException
    {
        tray = new TrayIconView();
        Platform.runLater(() -> stage = new Stage());
        Thread.sleep(500);
    }

    @Test
    public void testAddToTray() throws AWTException
    {
        tray.addToTray();
        tray.removeFromTray();
    }

    @Test
    public void testChangeImage()
    {
        tray.changeImage(TrayIconView.imageBase);
        tray.changeImage(null);
    }

    @Test
    public void testSetStage() throws IllegalAccessException
    {
        tray.setStage(stage);
        assertTrue(Whitebox.getField(TrayIconView.class, "stage").get(tray).equals(stage));
        tray.setStage(null);
    }
    
    @Test
    public void testHideStage() throws IllegalAccessException
    {
        tray.setStage(stage);
        Platform.runLater(() -> stage.hide());
        assertTrue(!((Stage) Whitebox.getField(TrayIconView.class, "stage").get(tray)).isShowing());
    }
    
    @Test
    public void testOpenStage()
    {
        tray.openStage();
    }
}
