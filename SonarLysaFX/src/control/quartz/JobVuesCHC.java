package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import control.task.JobForTask;
import javafx.application.Platform;

public class JobVuesCHC extends JobForTask
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES, context);
            startTask(new CreerVueCHCCDMTask(liste, false), context.getJobDetail().getKey().getName());
        });
    }
}