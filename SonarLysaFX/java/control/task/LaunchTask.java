package control.task;

import java.util.concurrent.ExecutionException;

import application.Main;
import javafx.application.Platform;
import view.ProgressDialog;

public class LaunchTask
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/    
    
    protected LaunchTask() {}
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    protected void startTask(AbstractSonarTask task, String titre)
    {
        ProgressDialog dialog;
        if (titre != null)
            dialog = new ProgressDialog(task, titre);
        else
            dialog = new ProgressDialog(task);
        dialog.show();
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
        new Thread(task).start();
    }
}
