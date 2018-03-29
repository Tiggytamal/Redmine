package control.quartz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import control.CreerVueCHCCDMTask;
import view.ProgressDialog;

public class JobVuesCHC implements Job
{
    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context)
    { 
        Object objet = context.get("annee");
        List<String> liste = new ArrayList<>();
        if (objet instanceof List)
           liste = (List<String>)objet;
        CreerVueCHCCDMTask task = new CreerVueCHCCDMTask(liste, false);
        ProgressDialog dialog;
        try
        {
            dialog = new ProgressDialog(task, "Vues CHC");
            dialog.show();
            dialog.startTask();
        } catch (IOException e)
        {
            // nothing
        }

    }
}