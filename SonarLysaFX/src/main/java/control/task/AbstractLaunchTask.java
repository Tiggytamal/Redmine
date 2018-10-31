package control.task;

import java.util.concurrent.ExecutionException;

import application.Main;
import javafx.application.Platform;
import view.ProgressDialog;

/**
 * Classe abstraite de lancement des tâches
 * 
 * @author ETP8137 - Grégoire Mathon
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
     * Permet de lancer une tâche avec le titre choisi.
     * 
     * @param task
     *            La tâche à lancer. Implémentation concrète de {@code AbstractSonarTask}.
     * @param titre
     *            Le titre affiché sur la fénêtre.
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
        
        // Lancement du thread de contrôle. Celui-ci va attendre le fin du thread de la tâche et propagée les erreurs au besoin.
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
        
        // Lancement du thread de la tâche.
        new Thread(task).start();
    }
    
    /**
     * Permet de lancer une tâche avec le titre par défault de la tâche.
     * 
     * @param task
     *            La tâche à lancer. Implémentation concrète de {@code AbstractSonarTask}.
     */
    protected void startTask(AbstractTask task)
    {
        startTask(task, null);
    }
}
