package control.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import control.statistique.ControlStatistique;
import javafx.application.Platform;

public class JobStatistiques extends AbstractJobForTask
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        Platform.runLater(() -> {
            ControlStatistique controlStat = new ControlStatistique();
            controlStat.majStatsAnosEnCours();
            controlStat.majStatsComposKO();
            controlStat.majStatsComposSansDefaut();
            controlStat.majStatsLdcTU();
        });
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
