package junit.control.task;

import org.junit.Test;
import org.junit.runner.RunWith;

import control.task.CreerVueParEditionTask;
import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import view.ProgressDialog;

@RunWith(JfxRunner.class)
public class CreerVueParEditionTaskTest
{
    @Test
    public void task()
    {
        Platform.runLater(() -> {
            CreerVueParEditionTask task = new CreerVueParEditionTask("ETP8137", "28H02m89,;:!");
            new ProgressDialog(task, "test");
        });
    }
}
