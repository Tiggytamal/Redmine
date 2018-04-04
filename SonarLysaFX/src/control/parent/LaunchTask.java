package control.parent;

import java.util.concurrent.ExecutionException;

import application.Main;
import javafx.application.Platform;
import view.ProgressDialog;

abstract class LaunchTask
{
    protected void startTask(SonarTask task, String titre)
    {
        ProgressDialog dialog;
        if (titre != null)
            dialog = new ProgressDialog(task, titre);
        else
            dialog = new ProgressDialog(task);
        dialog.show();
        new Thread(task).start();

        new Thread(() -> {
            try
            {
                task.get();
            } catch (InterruptedException | ExecutionException e)
            {
                Platform.runLater(() -> Main.gestionException(e));
            }
        }).start();
    }
}