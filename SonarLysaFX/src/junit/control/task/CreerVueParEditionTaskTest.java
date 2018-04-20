package junit.control.task;

import org.junit.Test;
import org.junit.runner.RunWith;

import control.task.CreerVueParEditionTask;
import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;

@RunWith(JfxRunner.class)
public class CreerVueParEditionTaskTest
{
    @Test
    public void task()
    {
        Platform.runLater(() -> {
            new CreerVueParEditionTask();
        });
    }
}
