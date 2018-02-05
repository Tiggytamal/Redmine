package application;

import control.view.MainScreen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entr�e du programme avec la gestion des erreurs
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class Main extends Application
{
	public static void main(final String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception
	{
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
			e.printStackTrace();
		});
		MainScreen main = new MainScreen();
		main.start(stage);
	}
}
