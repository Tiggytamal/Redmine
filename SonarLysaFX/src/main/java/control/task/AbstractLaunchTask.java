package control.task;

import java.util.concurrent.ExecutionException;

import application.Main;
import javafx.application.Platform;
import view.ProgressDialog;

/**
 * Classe abstraite de lancement des t�ches
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public abstract class AbstractLaunchTask
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractLaunchTask() {}

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Permet de lancer une t�che avec le titre choisi.
     * 
     * @param task
     *            La t�che � lancer. Impl�mentation concr�te de {@code AbstractSonarTask}.
     * @param titre
     *            Le titre affich� sur la f�n�tre.
     */
    protected void startTask(AbstractTask task, String titre)
    {
        ProgressDialog dialog;
        
        // Si le titre est null on prend celui du dialogue.
        if (titre != null)
            dialog = new ProgressDialog(task, titre);
        else
            dialog = new ProgressDialog(task);
        dialog.show();
        
        // Lancement du thread de contr�le. Celui-ci va attendre le fin du thread de la t�che et propag�e les erreurs au besoin.
        new Thread(() -> {
            try
            {
                task.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                Platform.runLater(() -> {
                    dialog.close();
                    Main.gestionException(e);
                });
            }
        }).start();
        
        // Lancement du thread de la t�che.
        new Thread(task).start();
    }
    
    /**
     * Permet de lancer une t�che avec le titre par d�fault de la t�che.
     * 
     * @param task
     *            La t�che � lancer. Impl�mentation concr�te de {@code AbstractSonarTask}.
     */
    protected void startTask(AbstractTask task)
    {
        startTask(task, null);
    }
}
