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
 * Permet de gérer le planificateur des tâches.
 * 
 * @author ETP137 - Grégoire Mathon
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
     * Création des jobs. Mise à jour de la map des données et démarrage du planificateur
     * 
     * @return
     *      Le planificateur crééeS
     * @throws SchedulerException
     *      Exception lors de la création du planificateur
     */
    public Scheduler creationJobsSonar() throws SchedulerException
    {
        // Récupération de tous les planifictauers depuis les paramètres XML
        Map<TypePlan, Planificateur> mapPlans = proprietesXML.getMapPlans();
        
        // Création et mise en place des jobs
        for (Map.Entry<TypePlan, Planificateur> entry : mapPlans.entrySet())
        {
            // On saute les planificateurs non activés
            if (!entry.getValue().isActive())
                continue;
            JobDetail job = creerJob(entry.getKey());
            
            //Rajout dans la DataMap de la liste des annèes
            job.getJobDataMap().put(AbstractJobForTask.CLEFANNEES + entry.getKey().getValeur(), entry.getValue().getAnnees());
            
            // Enregistrement des jobs
            scheduler.deleteJob(job.getKey());
            scheduler.scheduleJob(job, creerTrigger(entry)); 
        }

        // Démarrage du planificateur
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
     * Permet de créer un trigger à partir d'une entry de la map
     * 
     * @param entry
     * @return
     */
    private Trigger creerTrigger(Map.Entry<TypePlan, Planificateur> entry)
    {
        //Initialisation des variables
        Planificateur plan = entry.getValue();
        List<Integer> listeJour = new ArrayList<>();
        
        // Récupération des données du planificateur
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
        
        // Création du trigger
        return newTrigger().withIdentity(entry.getKey().toString(), GROUP).startNow()
                .withSchedule(atHourAndMinuteOnGivenDaysOfWeek(heure.getHour(), heure.getMinute(), listeJour.toArray(new Integer[listeJour.size()]))).build();
    }
    
    /**
     * Permet de créer un job à partir de l'énumération du type de planificateur
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
