package view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import application.Main;
import javafx.application.Platform;
import javafx.stage.Stage;
import utilities.Statics;

/**
 * Gestion de l'affichage de l'icône dans la barre de notification
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TrayIconView
{

    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    public static final Image imageBase = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/sonar.jpg"));
    public static final Image imageRed = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/sonarRed.png"));
    
    private SystemTray tray;
    private TrayIcon trayIcon;
    private Stage stage;

    /*---------- CONSTRUCTEURS ----------*/

    public TrayIconView()
    {
        // Récupération de la barre de notifications depuis le système
        tray = SystemTray.getSystemTray();

        // Initialisation de l'icone
        trayIcon = new TrayIcon(imageBase, Statics.NOMAPPLI);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Tooltip");
        trayIcon.displayMessage(Statics.NOMAPPLI, "SonarLyza", MessageType.INFO);

        // Création du menu
        PopupMenu menu = new PopupMenu(Statics.NOMAPPLI);
        MenuItem fermer = new MenuItem("fermer");
        fermer.addActionListener(e -> System.exit(0));
        MenuItem ouvrir = new MenuItem("ouvrir");
        ouvrir.addActionListener(e -> openStage());
        menu.add(ouvrir);
        menu.add(fermer);
        trayIcon.setPopupMenu(menu);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Ajoute l'icone à la barre de notifications
     * 
     * @throws AWTException
     */
    public void addToTray() throws AWTException
    {
        tray.add(trayIcon);
    }

    /**
     * Retire l'icone à la barre de notifications
     */
    public void removeFromTray()
    {
        tray.remove(trayIcon);
    }

    public void hideStage()
    {
        stage.hide();
    }

    /**
     * Retire l'icone de la barre de notifications et affiche la fenêtre de l'application
     */
    public void openStage()
    {
        Platform.runLater(() -> {
            stage.setIconified(false);
            stage.show();
            stage.toFront();
        });
        Platform.setImplicitExit(true);
    }

    public void changeImage(Image image)
    {
        if (image != null)
            trayIcon.setImage(image);
    }

    public void setStage(Stage stage)
    {
        if (stage != null)
            this.stage = stage;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
