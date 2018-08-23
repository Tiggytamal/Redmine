package control.view;

import java.awt.AWTException;
import java.awt.Image;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import control.rtc.ControlRTC;
import control.xml.ControlXML;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utilities.Statics;
import utilities.TechnicalException;
import view.TrayIconView;

/**
 * Ecran principal de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class MainScreen extends Application
{
    /*---------- ATTRIBUTS ----------*/

    /* Attibuts généraux */

    /** Affichage général de l'application */
    private static final BorderPane root = new BorderPane();
    /** Icône de la barre des tâches */
    private static final TrayIconView trayIcon = new TrayIconView();
    /** Fenêtre de top niveau de l'application */
    private Stage stage;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void start(final Stage primaryStage) throws IOException, InterruptedException, JAXBException, AWTException
    {
        stage = primaryStage;

        // Menu de l'application
        final HBox menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

        // Ajout au panneau principal
        root.setTop(menu);

        // Affichage de l'interface
        final Scene scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(Statics.CSS);
        trayIcon.setStage(stage);
        stage.setTitle("Sonar Lysa");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.iconifiedProperty().addListener(new IconifiedListener());
        stage.setOnCloseRequest(new CloseEventHandler());
        trayIcon.addToTray();
        stage.show();
        new ControlXML().createAlert();
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    /**
     * Accès au panneau principal depuis les autres contrôleurs.
     * 
     * @return
     */
    public static BorderPane getRoot()
    {
        return root;
    }

    public static void changeImageTray(Image image)
    {
        trayIcon.changeImage(image);
    }

    /**
     * Listener privé pour réduire l'application dans la barre des tâches.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private static class IconifiedListener implements ChangeListener<Boolean>
    {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
        {
            if (newValue)
            {
                Platform.setImplicitExit(false);
                trayIcon.hideStage();
            }
        }
    }

    /**
     * EventHandler privé pour la gestion de la fermeture du programme.<br>
     * Supression Icône de la barre de notification, fermeture des planificateurs et de l'accès RTC
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class CloseEventHandler implements EventHandler<WindowEvent>
    {
        @Override
        public void handle(WindowEvent event)
        {
            if (!stage.isIconified())
            {
                trayIcon.removeFromTray();
                ControlRTC.INSTANCE.shutdown();
                try
                {
                    ControlJob.scheduler.shutdown();
                }
                catch (SchedulerException e)
                {
                    throw new TechnicalException("Impossible de fermer le planificateur", e);
                }
            }
        }
    }
}
