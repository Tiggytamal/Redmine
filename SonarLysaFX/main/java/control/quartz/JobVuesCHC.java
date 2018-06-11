package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import control.task.JobForTask;
import javafx.application.Platform;
import model.enums.CHCouCDM;
import model.enums.TypePlan;

public class JobVuesCHC extends JobForTask
{
    
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECHC.toString(), context);
            startTask(new CreerVueCHCCDMTask(liste, CHCouCDM.CHC), context.getJobDetail().getKey().getName());
        });
    }
}