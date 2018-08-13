package control.task;

import java.util.concurrent.ExecutionException;

import application.Main;
import javafx.application.Platform;
import view.ProgressDialog;

public abstract class AbstractLaunchTask
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractLaunchTask() { }

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
    protected void startTask(AbstractSonarTask task, String titre)
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
}
