package application;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.NotLoggedInException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Entrée du programme avec la gestion des erreurs.
 *
 * @author ETP137 - Grégoire Mathon
 * @since 1.0
 */
public final class Main extends Application
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application. */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /** The Constant DETAILLENGTH. */
    private static final int DETAILLENGTH = 384;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Lancement de l'application. Pas d'argument nécessaire.
     *
     * @param args arguments
     */
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    /*
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(final Stage stage) throws Exception
    {
        // Permet de contrôler toutes les erreurs remontées par l'application
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> gestionException(e));

        // Création fenêtre principale
        MainScreen main = new MainScreen();
        main.start(stage);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Gestion des erreurs du Thread. Utilisation de la recursivité pour tester toutes les exceptions précédentes.
     * 
     * @param e
     *          Exception à gérer
     */
    public static void gestionException(Throwable e)
    {
        // Gestion de la non connexion à RTC
        if (e.getCause() instanceof NotLoggedInException && e.getCause().getMessage().contains("Not logged in to the repository"))

            createAlertForFX(Severity.INFO, null, "Vous n'êstes pas connecté à RTC.\n Merci d'utiliser le bouton de connexion.");

        else if (e instanceof FunctionalException)
        {
            // Affichage informations de l'erreur fonctionnelle
            FunctionalException ex1 = (FunctionalException) e;
            createAlertForFX(ex1.getSeverity(), null, ex1.getMessage());
        }
        else if (e instanceof TechnicalException)
        {
            // Affichage informations de l'erreur technique
            TechnicalException ex1 = (TechnicalException) e;
            createAlertForFX(ex1.getSeverity(), ex1.getCause(), ex1.getMessage());
        }
        else if (e instanceof FileNotFoundException && e.getMessage().contains("Le processus ne peut pas accéder au fichier car ce fichier est utilisé par un autre processus"))
        {
            // gestion des fichiers utilisés par un autre utilisateur
            createAlertForFX(Severity.ERROR, null, "Fichier utilisé par un autre utilisateur : \n" + e.getMessage().split("\\(")[0]);
        }
        else if (e.getCause() != null)
        {
            // Recursivité pour tester la cause de cette exception
            gestionException(e.getCause());
        }
        else
            createAlertForFX(Severity.ERROR, e, e.getClass().getSimpleName() + e.getMessage());
    }

    /**
     * Permet de d'afficher un message d'erreur en créant un Thread JavaFX au besoin.
     *
     * @param severity Sévérité de l'anomalie
     * @param ex       Throwable d'où provient le plantage
     * @param detail   message à afficher dans la fenêtre de l'exception
     */
    public static void createAlertForFX(Severity severity, Throwable ex, String detail)
    {
        if (Platform.isFxApplicationThread())
            createAlert(severity, ex, detail);
        else
            Platform.runLater(() -> createAlert(severity, ex, detail));
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de créer un message d'erreur. Et enregistre celui-ci dans la LOG.
     *
     * @param severity the severity
     * @param ex       the ex
     * @param detail   the detail
     */
    private static void createAlert(Severity severity, Throwable ex, String detail)
    {
        LOGPLANTAGE.error(detail, ex);
        Alert alert;

        // Switch sur les sévérités pour récupérer le type d'alerte
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
        if (detail.length() > DETAILLENGTH)
            detail = detail.substring(0, DETAILLENGTH);
        alert.setContentText(detail);

        // Création du message d'exception si celle-ci est fournie.
        if (ex != null)
        {
            // Création de la trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            // Logging de l'erreur
            LOGPLANTAGE.error(sw.toString());

            // Préparation de l'affichage dans la fenêtre
            Label label = new Label("Stacktrace :");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Ajout de la fenêtre au Dialog
            alert.getDialogPane().setExpandableContent(expContent);
            pw.close();
        }

        alert.setResizable(true);
        alert.showAndWait();
    }

    /*---------- ACCESSEURS ----------*/
}
