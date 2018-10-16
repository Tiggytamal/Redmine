package control.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.concurrent.Task;

public class AffichageTempsTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantage */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private boolean stop;
    private AbstractTask tacheParente;

    /*---------- CONSTRUCTEURS ----------*/

    protected AffichageTempsTask(AbstractTask tacheParente)
    {
        this.tacheParente = tacheParente;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majAffTemps();
    }

    public void annuler()
    {
        stop = true;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majAffTemps()
    {
        while (!stop)
        {
            if (tacheParente == null)
                return false;

            tacheParente.setAffTimer(tacheParente.getTempsEcoule(), tacheParente.getTempsRestant());
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                LOGPLANTAGE.error(e);
                stop = true;
                Thread.currentThread().interrupt();
            }
        }

        return true;
    }

    /*---------- ACCESSEURS ----------*/

}
