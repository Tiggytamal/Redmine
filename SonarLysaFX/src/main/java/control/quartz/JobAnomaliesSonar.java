package control.quartz;

import org.quartz.JobExecutionContext;

import control.task.MajSuiviExcelTask;
import javafx.application.Platform;
import model.enums.TypeMajSuivi;

/**
 * Classe g�n�rique des Jobs pour le planificateur des t�ches
 * 
 * @author ETP8137 - Gr�goire Mathon
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
