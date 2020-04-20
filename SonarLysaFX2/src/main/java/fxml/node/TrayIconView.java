package fxml.node;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
    private MenuItem reduire;

    /*---------- CONSTRUCTEURS ----------*/

    public TrayIconView()
    {
        // Récupération de la barre de notifications depuis le système
        tray = SystemTray.getSystemTray();

        // Initialisation de l'icone
        trayIcon = new TrayIcon(imageBase, Statics.NOMAPPLI);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(Statics.NOMAPPLI);
        trayIcon.displayMessage(Statics.NOMAPPLI, "SonarLyza", MessageType.INFO);
        trayIcon.addMouseListener(new DoubleClickListener());

        // Création du menu
        PopupMenu menu = new PopupMenu(Statics.NOMAPPLI);
        MenuItem fermer = new MenuItem("fermer");
        fermer.addActionListener(e -> System.exit(0));
        MenuItem ouvrir = new MenuItem("ouvrir");
        ouvrir.addActionListener(e -> openStage());
        reduire = new MenuItem("réduire");
        reduire.addActionListener(e -> hideStage());
        menu.add(ouvrir);
        menu.add(reduire);
        menu.add(fermer);
        trayIcon.setPopupMenu(menu);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Ajoute l'icone à la barre de notifications
     * 
     * @throws AWTException
     *                      Exception lancée par le système pour la gestion de la barre de tâche.
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
        reduire.setEnabled(false);
        Platform.setImplicitExit(false);
        Platform.runLater(() -> { stage.setIconified(true); stage.hide(); });
    }

    /**
     * Retire l'icone de la barre de notifications et affiche la fenêtre de l'application
     */
    public void openStage()
    {
        reduire.setEnabled(true);
        Platform.runLater(() -> { stage.setIconified(false); stage.show(); stage.toFront(); });
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
    /*---------- CLASSES PRIVEES ----------*/

    /**
     * Permet la gestion ud double click pour réouvrir l'application
     * 
     * @author ETP8137- Grégoire Mathon
     *
     */
    private class DoubleClickListener implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2)
                openStage();
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            // Pas de traitement
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            // Pas de traitement
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            // Pas de traitement
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            // Pas de traitement
        }
    }
}
