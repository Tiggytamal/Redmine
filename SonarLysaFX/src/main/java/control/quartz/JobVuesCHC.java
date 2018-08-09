package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import javafx.application.Platform;
import model.enums.CHCouCDM;
import model.enums.TypePlan;

public class JobVuesCHC extends AbstractJobForTask
{

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECHC.getValeur(), context);
            startTask(new CreerVueCHCCDMTask(liste, CHCouCDM.CHC), context.getJobDetail().getKey().getName());
        });
    }
}
