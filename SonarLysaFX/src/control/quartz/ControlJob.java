package control.quartz;

import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Permet de g�rer le planificateur des t�ches.
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class ControlJob
{
    private Scheduler scheduler;

    public ControlJob() throws SchedulerException
    {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    public void creationJobAnomaliesSonar() throws SchedulerException
    {
        // D�finition du job
        JobDetail job = newJob(JobAnomaliesSonar.class).withIdentity("jobAnomaliesSonar", "group").build();

        // Cr�ation d'un trigger qui d�marre le soir � 23h et se r�pete tous les jours sauf le week-end.
        Trigger trigger = newTrigger().withIdentity("trigger", "group").startNow().withSchedule(atHourAndMinuteOnGivenDaysOfWeek(23, 00, DateBuilder.MONDAY,
                DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY)).build();

        // Mise en place du job.
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
    
    public void test() throws SchedulerException
    {
        JobDetail job = newJob(JobJob.class).withIdentity("job1", "group1").build();
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

    public void fermeturePlanificateur() throws SchedulerException
    {
        scheduler.standby();
    }
    
    public static class JobJob implements Job
    {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException
        {
           System.out.println("bouh!");
            
        }
        
    }
}