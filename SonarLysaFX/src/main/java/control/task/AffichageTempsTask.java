package control.task;

import javafx.concurrent.Task;

public class AffichageTempsTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

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

    public void terminer()
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
                stop = true;
                Thread.currentThread().interrupt();
            }
        }

        return true;
    }

    /*---------- ACCESSEURS ----------*/

}
