package control.quartz;

import org.quartz.JobExecutionContext;

import control.task.MajSuiviExcelTask;
import javafx.application.Platform;
import model.enums.TypeMajSuivi;

/**
 * Classe générique des Jobs pour le planificateur des tâches
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class JobAnomaliesSonar extends AbstractJobForTask
{
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> startTask(new MajSuiviExcelTask(TypeMajSuivi.MULTI), context.getJobDetail().getKey().getName()));
    }
}
