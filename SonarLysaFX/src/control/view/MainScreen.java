package control.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainScreen extends Application
{
	/*---------- ATTRIBUTS ----------*/

	/* Attibuts g�n�raux */

	private static BorderPane root = new BorderPane();

	/* Attributs FXML */

	/*---------- CONSTRUCTEURS ----------*/

	@FXML
	public void initialize()
	{

	}

	/*---------- METHODES PUBLIQUES ----------*/

	@Override
	public void start(final Stage stage) throws Exception
	{
		// Menu de l'application
		final MenuBar menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
		// Ajout au panneau principal
		root.setTop(menu);

		// Affichage de l'interface
		final Scene scene = new Scene(root, 640, 480);
		stage.setTitle("Sonar Lysa");
		stage.setResizable(true);
		stage.setScene(scene);
		stage.show();
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
}