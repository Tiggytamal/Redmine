package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import control.task.AbstractJobForTask;
import javafx.application.Platform;
import model.enums.CHCouCDM;
import model.enums.TypePlan;

public class JobVuesCDM extends AbstractJobForTask
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECDM.toString(), context);
            startTask(new CreerVueCHCCDMTask(liste, CHCouCDM.CDM), context.getJobDetail().getKey().getName());
        });
    }
}
