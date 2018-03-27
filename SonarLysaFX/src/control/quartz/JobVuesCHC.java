package control.quartz;

import static utilities.Statics.info;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import control.ControlSonar;

public class JobVuesCHC implements Job
{
    @Override
    public void execute(JobExecutionContext context)
    {
        ControlSonar control = new ControlSonar();
        control.creerVueCHC();       
    }
}