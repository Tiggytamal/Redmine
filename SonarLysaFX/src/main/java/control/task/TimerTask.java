package control.task;

/**
 * Timer des tâches executées
 * 
 * @author ETP8137 - Gregoire Mathon
 * @since 1.0
 *
 */
public class TimerTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPE = 1;
    private static final String TITRE = "TIMER";
    
    private boolean stop;

    /*---------- CONSTRUCTEURS ----------*/

    protected TimerTask()
    {
        super(ETAPE, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        stop = true;
    }

    @Override
    public Boolean call() throws Exception
    {
        return decompteSecondes();
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
        }
        return true;
    }

    /*---------- ACCESSEURS ----------*/

}
