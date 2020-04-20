package control.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import control.task.LaunchTask;
import control.task.portfolio.CreerPortfolioCHCCDMTask;
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
        // Lancement depuis une instance FX, récupération de la liste des années, puis lancement de la tâche de traitement.
        Platform.runLater(() -> {
            List<String> liste = getObjectFromJobMap(List.class, CLEFANNEES + TypePlan.VUECHC.getValeur(), context);
            LaunchTask.startTask(new CreerPortfolioCHCCDMTask(liste, TypeEdition.CHC), context.getJobDetail().getKey().getName());
        });
    }
}
