package junit.control.task;

import org.junit.Test;
import org.junit.runner.RunWith;

import control.task.CreerVueParEditionTask;
import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestCreerVueParEditionTask extends JunitBase
{
    @Test
    public void testTask()
    {
        Platform.runLater( CreerVueParEditionTask::new);
    }
}
