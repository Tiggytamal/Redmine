package control.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.concurrent.Task;

/**
 * Timer des t�ches execut�es
 * 
 * @author ETP8137 - Gregoire Mathon
 * @since 1.0
 *
 */
public class TimerTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/
    
    /** logger plantage */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
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
            
            tacheParente.setTempsEcoule(System.currentTimeMillis() - debut); 
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
