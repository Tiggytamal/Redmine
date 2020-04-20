package application;

import static utilities.Statics.proprietesXML;

import java.awt.AWTException;
import java.awt.Image;
import java.io.IOException;

import org.quartz.SchedulerException;

import control.job.ControlJob;
import control.rtc.ControlRTC;
import fxml.node.TrayIconView;
import javafx.application.Application;
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
import utilities.Utilities;

/**
 * Ecran principal de l'application. Affiche la fenêtre principale et le menu.
 * Gère aussi le mode icône de l'application.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class MainScreen extends Application
{
    /*---------- ATTRIBUTS ----------*/

    /** Affichage général de l'application */
    public static final BorderPane ROOT = new BorderPane();
    /** Icône de la barre des tâches */
    private static final TrayIconView trayIcon = new TrayIconView();
    /** Fenêtre de top niveau de l'application */
    private Stage stage;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void start(final Stage primaryStage) throws IOException, AWTException
    {
        stage = primaryStage;

        // Menu de l'application
        final HBox menu = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));

        // Ajout au panneau principal
        ROOT.setTop(menu);

        // Affichage de l'interface
        final Scene scene = new Scene(ROOT, 1024, 768);
        scene.getStylesheets().add(Statics.CSS);
        trayIcon.setStage(stage);
        stage.setTitle(Statics.NOMAPPLI);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.iconifiedProperty().addListener(new IconifiedListener());
        stage.setOnCloseRequest(new CloseEventHandler());
        trayIcon.addToTray();
        stage.show();
        Utilities.createAlert(proprietesXML.controleDonnees());
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    /**
     * Change l'image dans la barre de notifications.
     * 
     * @param image
     *              Image à changer.
     */
    public static void changeImageTray(Image image)
    {
        trayIcon.changeImage(image);
    }

    /**
     * Change le titre de l'application pour afficher le menu actuel
     * 
     * @param node
     *              Node ou le titre sera affcihé.
     * @param titre
     *              Titre à afficher.
     */
    public static void majTitre(String titre)
    {
        ((Stage) ROOT.getScene().getWindow()).setTitle(Statics.NOMAPPLI + "/" + titre);
    }

    /**
     * Listener privé pour reduire l'application dans la barre des tâches.
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
                trayIcon.hideStage();
        }
    }

    /**
     * EventHandler privé pour la gestion de la fermeture du programme.<br>
     * Supression Icône de la barre de notification, fermeture des planificateurs et de l'accés RTC.
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

                if (ControlRTC.getInstance() != null)
                    ControlRTC.getInstance().shutdown();

                try
                {
                    ControlJob.SCHEDULER.shutdown();
                }
                catch (SchedulerException e)
                {
                    throw new TechnicalException("Impossible de fermer le planificateur", e);
                }

                System.exit(0);
            }
        }
    }
}
