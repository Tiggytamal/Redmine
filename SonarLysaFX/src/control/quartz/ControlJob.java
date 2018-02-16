package control.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.time.LocalDateTime;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import utilities.DateConvert;

public class ControlJob
{
	Scheduler scheduler;
	
	public ControlJob() throws SchedulerException
	{
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
	}
	
	public void creationJobAnomaliesSonar() throws SchedulerException
	{
		// D�finition du job
		JobDetail job = newJob(JobAnomaliesSonar.class).withIdentity("jobAnomaliesSonar", "group").build();

		// Cr�ation d'un trigger qui d�marre le soir � 23h et se r�pete tous les jours.
		LocalDateTime date = LocalDateTime.now();
		LocalDateTime soir = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 00);
		Trigger trigger = newTrigger().withIdentity("trigger", "group").startAt(DateConvert.convertToOldDate(soir)).withSchedule(simpleSchedule().withIntervalInHours(24).repeatForever()).build();

		// Mise en place du job.
		scheduler.scheduleJob(job, trigger);
	}
}