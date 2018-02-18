package view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javafx.application.Platform;
import javafx.stage.Stage;
import utilities.Statics;

public class TrayIconView
{
	
	/*---------- ATTRIBUTS ----------*/

	private SystemTray tray;
	private TrayIcon trayIcon;
	private Stage stage;
	
	/*---------- CONSTRUCTEURS ----------*/

	public TrayIconView(Stage stage)
	{
		// D�pendance avec l'application
		this.stage = stage;
		
		// R�cup�ration de la barre de notifications depuis le syst�me
		tray = SystemTray.getSystemTray();

		// Image utilis�e pour l'ic�ne
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/sonar.jpg"));

		// Initialisation de l'icone
		trayIcon = new TrayIcon(image, Statics.NOMAPPLI);
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("Tooltip");
		trayIcon.displayMessage(Statics.NOMAPPLI, "demo", MessageType.INFO);
		
		// Cr�ation du menu
		PopupMenu menu = new PopupMenu(Statics.NOMAPPLI);
		MenuItem fermer  = new MenuItem("fermer");
		fermer.addActionListener(e -> System.exit(0));
		MenuItem ouvrir  = new MenuItem("ouvrir");
		ouvrir.addActionListener(e -> removeFromTray());
	    menu.add(ouvrir);
		menu.add(fermer);
		trayIcon.setPopupMenu(menu);
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
	/**
	 * Ajoute l'icone � la barre de notifications et ferme la fenetre de l'application
	 * @throws AWTException
	 */
	public void addToTray() throws AWTException
	{
		stage.hide();
		tray.add(trayIcon);
	}
	
	/**
	 * Retire l'icone de la barre de notifications et affiche la fen�tre de l'application
	 */
	public void removeFromTray()
	{
		tray.remove(trayIcon);
		Platform.runLater(() -> { 
		    stage.setIconified(false);
		    stage.show();
		    stage.toFront();		    
		});
	    Platform.setImplicitExit(true);
	}
	
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}