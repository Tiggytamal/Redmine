package junit.control.quartz;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import control.quartz.ControlJob;
import control.task.JobForTask;
import junit.JunitBase;
import model.Planificateur;
import model.enums.TypePlan;

/**
 * JUnit pour control.quartz.ControlJob
 * 
 * @author ETP137 - Gr�goire Mathon
 */
public class ControlJobTest extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlJob handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws SchedulerException
    {
        handler = new ControlJob();
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void creationJobsSonar() throws Exception
    {
        // Initialisation
        Map<TypePlan, Planificateur> mapPlans = proprietes.getMapPlans();
        List<String> planKey = new ArrayList<>();
        for (Map.Entry<TypePlan, Planificateur> entry : mapPlans.entrySet())
        {
            if (entry.getValue().isActive())
            {
                planKey.add(entry.getKey().toString());
            }
        }

        // Test 1 - nombre de jobs
        handler.creationJobsSonar();
        Scheduler scheduler = ((Scheduler) Whitebox.getField(ControlJob.class, "scheduler").get(handler));
        for (String clef : planKey)
        {
            JobDetail job = scheduler.getJobDetail(new JobKey(clef, "group"));

            // V�rificaton que le job est bien enregistr�
            assertNotNull(job);

            Trigger trigger = scheduler.getTrigger(new TriggerKey(clef, "group"));

            // V�rificaton que le trigger est bien enregistr�
            assertNotNull(trigger);

            // V�rification que la map est bien remplie
            assertNotNull(job.getJobDataMap().get(JobForTask.CLEFANNEES + clef));
        }
    }

    @Test
    public void creerTrigger() throws Exception
    {
        // Parcours de tous les planificateur et v�rification que les infos sont bonnes apr�s cr�ation des triggers
        for (Map.Entry<TypePlan, Planificateur> entry : proprietes.getMapPlans().entrySet())
        {
            Trigger trigger = Whitebox.invokeMethod(handler, "creerTrigger", entry);
            Planificateur plan = entry.getValue();
            assertTrue(trigger instanceof CronTrigger);

            // R�cup�ration de l'expression du trigger
            String cron = ((CronTrigger) trigger).getCronExpression().replace(" ", "");

            // R�cup�ration de l'heure depuis le planificateur
            String heure = plan.getHeure().format(new DateTimeFormatterBuilder().appendPattern("mmH").toFormatter());

            // R�cup�ration des jours actifs depuis le planificateur
            StringBuilder builder = new StringBuilder(plan.isLundi() ? "2," : "");
            String jour = builder.append(plan.isMardi() ? "3," : "").append(plan.isMercredi() ? "4," : "").append(plan.isJeudi() ? "5," : "").append(plan.isVendredi() ? "6" : "").toString();
            if (jour.endsWith(","))
                jour = jour.substring(0, jour.length() - 1);
            
            // Contr�le des donn�es
            assertTrue("Mauvaise heure d'un trigger", cron.contains(heure));
            assertTrue("Mauvaises dates d'un trigger", cron.contains(jour));
        }
    }

    @Test
    public void creationJobAnomaliesSonar() throws Exception
    {
        handler.creationJobsSonar();
        assertTrue(((Scheduler) Whitebox.getField(ControlJob.class, "scheduler").get(handler)).isStarted());
    }

    @Test
    public void fermeturePlanificateur() throws Exception
    {
        // Test si le planificateur se ferme bien
        handler.fermeturePlanificateur();
        assertTrue(((Scheduler) Whitebox.getField(ControlJob.class, "scheduler").get(handler)).isInStandbyMode());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
