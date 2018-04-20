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
public class TrayIconViewTest
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
    public void addToTray() throws AWTException
    {
        tray.addToTray();
        tray.removeFromTray();
    }

    @Test
    public void changeImage()
    {
        tray.changeImage(TrayIconView.imageBase);
        tray.changeImage(null);
    }

    @Test
    public void setStage() throws IllegalArgumentException, IllegalAccessException, InterruptedException
    {
        tray.setStage(stage);
        assertTrue(Whitebox.getField(TrayIconView.class, "stage").get(tray).equals(stage));
        tray.setStage(null);
    }
    
    @Test
    public void hideStage() throws IllegalArgumentException, IllegalAccessException, InterruptedException
    {
        tray.setStage(stage);
        Platform.runLater(() -> stage.hide());
        assertTrue(!((Stage) Whitebox.getField(TrayIconView.class, "stage").get(tray)).isShowing());
    }
    
    @Test
    public void openStage() throws InterruptedException
    {
        tray.openStage();
    }
}
