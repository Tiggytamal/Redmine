package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import javafx.application.Platform;
import model.enums.CHCouCDM;
import model.enums.TypePlan;

/**
 * Job de création des vues CDM
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class JobVuesCDM extends AbstractJobForTask
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECDM.getValeur(), context);
            startTask(new CreerVueCHCCDMTask(liste, CHCouCDM.CDM), context.getJobDetail().getKey().getName());
        });
    }
}
