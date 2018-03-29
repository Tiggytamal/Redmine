package control.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import control.ControlSonar;

public class JobVuesCDM implements Job
{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        ControlSonar control = new ControlSonar();
        control.creerVueCHC();         
    }
}