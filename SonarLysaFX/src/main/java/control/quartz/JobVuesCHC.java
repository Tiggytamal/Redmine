package control.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.CreerVueCHCCDMTask;
import javafx.application.Platform;
import model.enums.TypeEdition;
import model.enums.TypePlan;

/**
 * Job de création des vues CHC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class JobVuesCHC extends AbstractJobForTask
{

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECHC.getValeur(), context);
            startTask(new CreerVueCHCCDMTask(liste, TypeEdition.CHC), context.getJobDetail().getKey().getName());
        });
    }
}
