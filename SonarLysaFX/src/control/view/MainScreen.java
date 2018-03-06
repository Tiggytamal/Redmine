package control.view;

import java.awt.AWTException;
import java.awt.Image;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import control.ControlXML;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.FichiersXML;
import model.ProprietesXML;
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
    /** Controleur de gestion des fichier XML */
    private static final ControlXML controlXML = new ControlXML();
    /** Ic�ne de la barre des t�ches */
    private static final TrayIconView trayIcon = new TrayIconView();
    /** Sauvegarde des fichiers Excel de param�tre */
    public static final FichiersXML fichiersXML = (FichiersXML) controlXML.recuprerXML(FichiersXML.class);
    /** Sauvegarde des fichiers Excel de param�tre */
    public static final ProprietesXML proprietesXML = (ProprietesXML) controlXML.recuprerXML(ProprietesXML.class);

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void start(final Stage stage) throws IOException, InterruptedException, JAXBException, AWTException
    {
        // Menu de l'application
        final MenuBar menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

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
        controlXML.createAlert();
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

    /**
     * Acc�es au fichier de param�tre de sauvegarde des fichiers Excel
     * 
     * @return
     */
    public static FichiersXML getParamFichier()
    {
        return fichiersXML;
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