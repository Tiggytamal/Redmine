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
        // Définition du job
        JobDetail jobAnomalies = newJob(JobAnomaliesSonar.class).withIdentity("jobAnomaliesSonar", "group").build();

        // Création d'un trigger qui démarre le soir à 23h et se répete tous les jours sauf le week-end.
        Trigger trigger = newTrigger().withIdentity("trigger", "group").startNow().withSchedule(atHourAndMinuteOnGivenDaysOfWeek(10, 56, DateBuilder.MONDAY,
                DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY)).build();

        // Mise en place du job.
        scheduler.deleteJob(jobAnomalies.getKey());
        scheduler.scheduleJob(jobAnomalies, trigger);
        scheduler.start();
    }

    public void fermeturePlanificateur() throws SchedulerException
    {
        scheduler.standby();
    }
}