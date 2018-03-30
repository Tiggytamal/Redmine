package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.CreerVueCHCCDMTask;
import control.parent.JobForTask;
import javafx.application.Platform;

public class JobVuesCDM extends JobForTask
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES, context);  
            startTask(new CreerVueCHCCDMTask(liste, true), context.getJobDetail().getKey().getName());
        });
    }
}