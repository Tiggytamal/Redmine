package control.quartz;

import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static utilities.Statics.proprietesXML;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import model.Planificateur;
import model.enums.TypePlan;

/**
 * Permet de g�rer le planificateur des t�ches.
 * 
 * @author ETP137 - Gr�goire Mathon
 */
public class ControlJob
{
    /*---------- ATTRIBUTS ----------*/

    private Scheduler scheduler;
    private static final String GROUP = "group";

    /*---------- CONSTRUCTEURS ----------*/

    public ControlJob() throws SchedulerException
    {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void creationJobsSonar() throws SchedulerException
    {
        Map<TypePlan, Planificateur> mapPlans = proprietesXML.getMapPlans();
        
        // Cr�ation et mise ne place des jobs
        for (Map.Entry<TypePlan, Planificateur> entry : mapPlans.entrySet())
        {
            JobDetail job = creerJob(entry.getKey());
            scheduler.deleteJob(job.getKey());
            scheduler.scheduleJob(job, creerTrigger(entry));            
        }

        // D�marrage du planificateur
        scheduler.start();
    }

    public void fermeturePlanificateur() throws SchedulerException
    {
        scheduler.standby();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de cr�er un trigger � partir d'une entry de la map
     * @param entry
     * @return
     */
    private Trigger creerTrigger(Map.Entry<TypePlan, Planificateur> entry)
    {
        //Initialisation des variables
        Planificateur plan = entry.getValue();
        List<Integer> listeJour = new ArrayList<>();
        
        // R�cup�ration des donn�es du planificateur
        if (plan.isLundi())
            listeJour.add(DateBuilder.MONDAY);
        if (plan.isMardi())
            listeJour.add(DateBuilder.TUESDAY);
        if (plan.isMercredi())
            listeJour.add(DateBuilder.WEDNESDAY);
        if (plan.isJeudi())
            listeJour.add(DateBuilder.THURSDAY);
        if (plan.isVendredi())
            listeJour.add(DateBuilder.FRIDAY);
        LocalTime heure = plan.getHeure();
        
        // Cr�ation du trigger
        return newTrigger().withIdentity(entry.getKey().toString(), GROUP).startNow()
                .withSchedule(atHourAndMinuteOnGivenDaysOfWeek(heure.getHour(), heure.getMinute(), listeJour.toArray(new Integer[listeJour.size()]))).build();
    }
    
    /**
     * Permet de cr�er un job � partir de l'�num�ration du type de planificateur
     * @param typePlan
     * @return
     */
    private JobDetail creerJob(TypePlan typePlan)
    {
        return newJob(typePlan.getClazz()).withIdentity(typePlan.toString(), GROUP).build();
    }
    
    /*---------- ACCESSEURS ----------*/
}