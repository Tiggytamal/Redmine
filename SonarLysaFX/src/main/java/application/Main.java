package application;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.view.MainScreen;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.enums.OptionDeser;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Entr�e du programme avec la gestion des erreurs
 * 
 * @author ETP137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public final class Main extends Application
{
    /** Boolean pour g�rer la d�srialisation ou non des objets JAVA - A ACTIVER UNIQUEMENT EN TEST */
    public static final OptionDeser DESER = OptionDeser.AUCUNE;

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    public static void main(final String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception
    {
        // Permet de controler toutes les erreurs remont�es par l'application
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> gestionException(e));

        // Cr�ation fen�tre principale
        MainScreen main = new MainScreen();
        main.start(stage);
    }

    /**
     * Gestion des erreurs du Thread. Utilisation de la r�cursivit� pour tester toutes les Exceptions pr�cedentes.
     * 
     * @param e
     *            Exception � g�rer
     */
    public static void gestionException(Throwable e)
    {
        if (e instanceof FunctionalException)
        {
            // Affichage informations de l'erreur fonctionnelle
            FunctionalException ex1 = (FunctionalException) e;
            createAlert(ex1.getSeverity(), null, ex1.getMessage());
        }
        else if (e instanceof TechnicalException)
        {
            // Affichage informations de l'erreur technique
            TechnicalException ex1 = (TechnicalException) e;
            createAlert(ex1.getSeverity(), ex1.getCause(), ex1.getMessage());
        }
        else if (e instanceof FileNotFoundException && e.getMessage().contains("Le processus ne peut pas acc�der au fichier car ce fichier est utilis� par un autre processus"))
        {
            // gestion des fichiers utilis�s par un autre utilisateur
            createAlert(Severity.ERROR, null, "Fichier utilis� par un autre utilisateur : \n" + e.getMessage().split("\\(")[0]);
        }
        else if (e.getCause() != null)
        {
            // R�cursivit� pour tester la cause de cette exception
            gestionException(e.getCause());
        }
        else
        {
            createAlert(Severity.ERROR, e, e.getClass().getSimpleName() + e.getMessage());
        }

        LOGPLANTAGE.error(e);
    }

    /**
     * M�thode permettant de cr�er un message d'erreur
     * 
     * @param severity
     *            S�v�rit� de l'anomalie
     * @param ex
     *            Throwable d'ou provient le plantage
     * @param detail
     *            message � afficher dans la fenetre de l'exception
     */
    private static void createAlert(Severity severity, Throwable ex, String detail)
    {

        Alert alert;

        // Switch sur les s�v�rit�s pour r�cup�rer le type d'alerte
        if (severity == Severity.ERROR)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
        }
        else
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
        }
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.setHeaderText(null);
        alert.setContentText(detail);

        // Cr�ation du message d'exception si celle-ci est fournie.
        if (ex != null)
        {
            // Cr�ation de la trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            // Logging de l'erreur
            LOGPLANTAGE.error(sw.toString());

            // Pr�paration de l'affichage dans al fen�tre
            Label label = new Label("Stacktrace :");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Ajout de la fen�tre au Dialog
            alert.getDialogPane().setExpandableContent(expContent);
        }
        alert.showAndWait();
    }
}
