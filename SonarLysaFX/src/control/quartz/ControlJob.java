package control.quartz;

import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import model.Planificateur;

/**
 * Permet de gérer le planificateur des tâches.
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public class ControlJob
{
    private Scheduler scheduler;

    public ControlJob() throws SchedulerException
    {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    public void creationJobsSonar() throws SchedulerException
    {
        // Définition des jobs
        JobDetail jobAnomalies = newJob(JobAnomaliesSonar.class).withIdentity("jobAnomaliesSonar", "group").build();
        JobDetail jobVuesCHC = newJob(JobVuesCHC.class).withIdentity("jobVuesCHC", "group").build();
        JobDetail jobVuesCDM = newJob(JobVuesCDM.class).withIdentity("jobVuesCDM", "group").build();

        // Création des triggers
        Planificateur plan = new Planificateur();
        Trigger triggerAnomalies = newTrigger().withIdentity("trigger", "group").startNow().withSchedule(atHourAndMinuteOnGivenDaysOfWeek(23, 00, DateBuilder.MONDAY,
                DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY)).build();
        Trigger triggerVuesCHC = newTrigger().withIdentity("trigger", "group").startNow().withSchedule(atHourAndMinuteOnGivenDaysOfWeek(23, 00, DateBuilder.MONDAY,
                DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY)).build();
        Trigger triggerVuesCDM = newTrigger().withIdentity("trigger", "group").startNow().withSchedule(atHourAndMinuteOnGivenDaysOfWeek(23, 00, DateBuilder.MONDAY,
                DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY)).build();

        // Mise en place du job.
        scheduler.deleteJob(jobAnomalies.getKey());
        scheduler.scheduleJob(jobAnomalies, triggerAnomalies);
        scheduler.start();
    }

    public void fermeturePlanificateur() throws SchedulerException
    {
        scheduler.standby();
    }
}