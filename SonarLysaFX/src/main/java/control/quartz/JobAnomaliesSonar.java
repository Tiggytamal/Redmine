package control.quartz;

import org.quartz.JobExecutionContext;

import control.task.AbstractJobForTask;
import control.task.MajSuiviExcelTask;
import javafx.application.Platform;
import model.enums.TypeMajSuivi;

public class JobAnomaliesSonar extends AbstractJobForTask
{
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() -> startTask(new MajSuiviExcelTask(TypeMajSuivi.MULTI), context.getJobDetail().getKey().getName()));
    }
}
