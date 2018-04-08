package control.view;

import java.awt.AWTException;
import java.awt.Image;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import control.xml.ControlXML;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.TrayIconView;

/**
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class MainScreen extends Application
{
    /*---------- ATTRIBUTS ----------*/

    /* Attibuts g�n�raux */

    /** Affichage g�n�ral de l'applicaiton */
    private static final BorderPane root = new BorderPane();
    /** Ic�ne de la barre des t�ches */
    private static final TrayIconView trayIcon = new TrayIconView();

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void start(final Stage stage) throws IOException, InterruptedException, JAXBException, AWTException
    {
        // Menu de l'application
        final HBox menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

        // Ajout au panneau principal
        root.setTop(menu);

        // Affichage de l'interface
        final Scene scene = new Scene(root, 640, 480);
        trayIcon.setStage(stage);
        stage.setTitle("Sonar Lysa");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.iconifiedProperty().addListener(new IconifiedListener());
        trayIcon.addToTray();
        stage.show();
        new ControlXML().createAlert();
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    /**
     * Acc�s au panneau principal depuis les autres contr�leurs.
     * 
     * @return
     * 
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
     * Listener rpiv� pour r�duire l'application dans la barre des t�ches.
     * 
     * @author ETP8137 - Gr�goire Mathon
     */
    private class IconifiedListener implements ChangeListener<Boolean>
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
}