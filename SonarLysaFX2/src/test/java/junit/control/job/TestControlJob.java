package junit.control.job;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import control.job.AbstractJobForTask;
import control.job.ControlJob;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.Planificateur;
import model.enums.TypePlan;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlJob extends JunitBase<ControlJob>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SCHEDULER = "SCHEDULER";

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init()
    {
        objetTest = new ControlJob();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreationJobsSonar(TestInfo testInfo) throws SchedulerException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        Map<TypePlan, Planificateur> mapPlans = Statics.proprietesXML.getMapPlans();
        List<String> planKey = new ArrayList<>();
        for (Map.Entry<TypePlan, Planificateur> entry : mapPlans.entrySet())
        {
            if (entry.getValue().isActive())
            {
                planKey.add(entry.getKey().getValeur());
            }
        }

        // Test 1 - nombre de jobs
        objetTest.creationJobsSonar();
        Scheduler scheduler = ((Scheduler) Whitebox.getField(ControlJob.class, SCHEDULER).get(objetTest));
        for (String clef : planKey)
        {
            JobDetail job = scheduler.getJobDetail(new JobKey(clef, "group"));

            // Verificaton que le job est bien enregistre
            assertThat(job).isNotNull();

            Trigger trigger = scheduler.getTrigger(new TriggerKey(clef, "group"));

            // Verificaton que le trigger est bien enregistre
            assertThat(trigger).isNotNull();

            // Verification que la map est bien remplie
            assertThat(job.getJobDataMap().get(AbstractJobForTask.CLEFANNEES + clef)).isNotNull();
        }
    }

    @Test
    public void testCreerTrigger(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Parcours de tous les planificateur et verification que les infos sont bonnes après création des triggers
        for (Map.Entry<TypePlan, Planificateur> entry : Statics.proprietesXML.getMapPlans().entrySet())
        {
            Trigger trigger = Whitebox.invokeMethod(objetTest, "creerTrigger", entry);
            Planificateur plan = entry.getValue();
            assertThat(trigger).isInstanceOf(CronTrigger.class);

            // Récupération de l'expression du trigger
            String cron = ((CronTrigger) trigger).getCronExpression().replace(" ", EMPTY);

            // Récupération de l'heure depuis le planificateur
            String heure = plan.getHeure().format(new DateTimeFormatterBuilder().appendPattern("mmH").toFormatter());

            // Récupération des jours actifs depuis le planificateur
            StringBuilder builder = new StringBuilder(plan.isLundi() ? "2," : EMPTY);
            String jour = builder.append(plan.isMardi() ? "3," : EMPTY).append(plan.isMercredi() ? "4," : EMPTY).append(plan.isJeudi() ? "5," : EMPTY).append(plan.isVendredi() ? "6" : EMPTY)
                    .toString();
            if (jour.endsWith(","))
                jour = jour.substring(0, jour.length() - 1);

            // Contrôle des donnees
            assertWithMessage("Mauvaise heure d'un trigger").that(cron).contains(heure);
            assertWithMessage("Mauvaises dates d'un trigger").that(cron).contains(jour);
        }
    }

    @Test
    public void testCreationJobAnomaliesSonar(TestInfo testInfo) throws SchedulerException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.creationJobsSonar();
        assertThat(((Scheduler) Whitebox.getField(ControlJob.class, SCHEDULER).get(objetTest)).isStarted()).isTrue();
    }

    @Test
    public void testFermeturePlanificateur(TestInfo testInfo) throws IllegalAccessException, SchedulerException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test si le planificateur se ferme bien
        objetTest.fermeturePlanificateur();
        assertThat(((Scheduler) Whitebox.getField(ControlJob.class, SCHEDULER).get(objetTest)).isInStandbyMode()).isTrue();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
