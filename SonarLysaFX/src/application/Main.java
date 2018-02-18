package application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import control.view.MainScreen;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.enums.Severity;

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
	    // Permet de controler toutes les erreurs remont�es par l'application
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> 
		{ 
		    // Dewrapping de la targetInvocationException cr��e depuis les m�thodes de l'application
            if (e.getCause() instanceof InvocationTargetException)
            {
		        InvocationTargetException ex0 = (InvocationTargetException) e.getCause();
		        if (ex0.getTargetException() instanceof FunctionalException)
		        {
		            // Affichage informations de l'erreur fonctionnelle
                    FunctionalException ex1 = (FunctionalException) ex0.getTargetException();
                    createAlert(ex1.getSeverity(), null, ex1.getMessage());
		        }
		        else if (ex0.getTargetException() instanceof TechnicalException)
		        {
		         // Affichage informations de l'erreur fonctionnelle
		            TechnicalException ex1 = (TechnicalException) ex0.getTargetException();
                    createAlert(ex1.getSeverity(), ex1.getCause(), ex1.getMessage());
		        }
		        else
		        {
		            // Affichage de la classe de l'Exception et du message d'erreur
		            Throwable t1 = ex0.getTargetException();
		            createAlert(Severity.SEVERITY_ERROR, ex0, t1.getClass().getSimpleName() + t1.getMessage());
		        }
            }            
            else
            {
                createAlert(Severity.SEVERITY_ERROR, e, e.getMessage());
            }
        });
		
		MainScreen main = new MainScreen();
		main.start(stage);
	}
	
    /**
     * M�thode permettant de cr�er un message d'erreur
     * 
     * @param severity
     * @param ex
     * @param detail
     */
    public static void createAlert(Severity severity, Throwable ex, String detail)
    {
        // instance de l'alerte
        Alert alert;

        // Switch sur les s�v�rit�s pour r�cup�rer le type d'alerte
        switch (severity)
        {
            case SEVERITY_ERROR :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            case SEVERITY_INFO :
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            default :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Une erreur est survenue");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
        }

        // Cr�ation du message d'exception si celle-ci est fournie.
        if (ex != null)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Stacktrace :");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
        }

        alert.showAndWait();
    }
}
