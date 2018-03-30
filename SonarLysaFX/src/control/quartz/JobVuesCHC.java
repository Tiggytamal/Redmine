package control.quartz;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import control.CreerVueCHCCDMTask;
import javafx.application.Platform;
import view.ProgressDialog;

public class JobVuesCHC implements Job
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    {
        Platform.runLater(() ->{
                Object objet = context.getJobDetail().getJobDataMap().get("annees");
                List<String> liste = new ArrayList<>();
                if (objet instanceof List)
                    liste = (List<String>) objet;
                CreerVueCHCCDMTask task = new CreerVueCHCCDMTask(liste, false);
                ProgressDialog dialog = new ProgressDialog(task, "Vues CHC");
                dialog.show();
                dialog.startTask();
            }
        );
    }
}