package control.task;

import javafx.concurrent.Task;
import utilities.Statics;

/**
 * Timer des tâches executées. Permet de mettre à jour toutes les secondes le temps écoulé depuis le début de la tâche.
 * 
 * @author ETP8137 - Gregoire Mathon
 * @since 1.0
 *
 */
public class TimerTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    private boolean stop;
    private AbstractTask tacheParente;

    /*---------- CONSTRUCTEURS ----------*/

    protected TimerTask(AbstractTask tacheParente)
    {
        this.tacheParente = tacheParente;
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
        long debut = System.currentTimeMillis();

        while (!stop)
        {
            if (tacheParente == null)
                return Boolean.FALSE;

            try
            {
                tacheParente.setTempsEcoule(System.currentTimeMillis() - debut);
                Thread.sleep(Statics.SECOND);
            }
            catch (InterruptedException e)
            {
                stop = true;
                Thread.currentThread().interrupt();
            }
        }

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
