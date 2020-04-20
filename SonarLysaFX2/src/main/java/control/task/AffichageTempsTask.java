package control.task;

import javafx.concurrent.Task;
import utilities.Statics;

/**
 * Tâche gérant la l'affichage du temps passé pour les traitements.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class AffichageTempsTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    private boolean stop;
    private AbstractTask tacheParente;

    /*---------- CONSTRUCTEURS ----------*/

    protected AffichageTempsTask(AbstractTask tacheParente)
    {
        this.tacheParente = tacheParente;
        stop = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void annuler()
    {
        stop = true;
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majAffTemps();
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
                Thread.sleep(Statics.SECOND);
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
