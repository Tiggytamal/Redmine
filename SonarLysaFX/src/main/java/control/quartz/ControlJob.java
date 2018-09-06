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
import utilities.TechnicalException;

/**
 * Permet de g�rer le planificateur des t�ches.
 * 
 * @author ETP137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class ControlJob
{
    /*---------- ATTRIBUTS ----------*/

    public static final Scheduler scheduler = initScheduler();
    private static final String GROUP = "group";

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Cr�ation des jobs. Mise � jour de la map des donn�es et d�marrage du planificateur
     * 
     * @return
     *      Le planificateur cr��eS
     * @throws SchedulerException
     *      Exception lors de la cr�ation du planificateur
     */
    public Scheduler creationJobsSonar() throws SchedulerException
    {
        // R�cup�ration de tous les planifictauers depuis les param�tres XML
        Map<TypePlan, Planificateur> mapPlans = proprietesXML.getMapPlans();
        
        // Cr�ation et mise en place des jobs
        for (Map.Entry<TypePlan, Planificateur> entry : mapPlans.entrySet())
        {
            // On saute les planificateurs non activ�s
            if (!entry.getValue().isActive())
                continue;
            JobDetail job = creerJob(entry.getKey());
            
            //Rajout dans la DataMap de la liste des ann�es
            job.getJobDataMap().put(AbstractJobForTask.CLEFANNEES + entry.getKey().getValeur(), entry.getValue().getAnnees());
            
            // Enregistrement des jobs
            scheduler.deleteJob(job.getKey());
            scheduler.scheduleJob(job, creerTrigger(entry)); 
        }

        // D�marrage du planificateur
        scheduler.start();
        return scheduler;
    }

    public void fermeturePlanificateur() throws SchedulerException
    {
        scheduler.standby();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation du planificateur
     * @return
     */
    private static Scheduler initScheduler()
    {
        try
        {
            return StdSchedulerFactory.getDefaultScheduler();
        }
        catch (SchedulerException e)
        {
            throw new TechnicalException("Impossible de lancer le planificateur", e);
        }
    }
    
    /**
     * Permet de cr�er un trigger � partir d'une entry de la map
     * 
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
     * 
     * @param typePlan
     * @return
     */
    private JobDetail creerJob(TypePlan typePlan)
    {
        return newJob(typePlan.getClassJob()).withIdentity(typePlan.toString(), GROUP).build();
    }
    
    /*---------- ACCESSEURS ----------*/
}
