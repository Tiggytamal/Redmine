package control.quartz;

import org.quartz.JobExecutionContext;

import control.parent.JobForTask;
import control.task.MajSuiviExcelTask;
import control.task.MajSuiviExcelTask.TypeMaj;
import javafx.application.Platform;

public class JobAnomaliesSonar extends JobForTask
{
	@Override
	public void execute(JobExecutionContext context)
	{	
		 Platform.runLater(() -> startTask(new MajSuiviExcelTask(TypeMaj.DOUBLE), context.getJobDetail().getKey().getName()));
	}
}
