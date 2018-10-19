package control.task;

import javafx.concurrent.Task;

/**
 * Timer des tâches executées
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

    @Override
    protected Boolean call() throws Exception
    {
        return decompteSecondes();
    }
    
    public void annuler()
    {
        stop = true;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean decompteSecondes()
    {
        long debut = System.currentTimeMillis();
        
        while (!stop)
        {
            if (tacheParente == null)
                return false;    
            
                try
                {
                    tacheParente.setTempsEcoule(System.currentTimeMillis() - debut); 
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
