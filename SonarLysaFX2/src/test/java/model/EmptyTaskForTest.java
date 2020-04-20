package model;

import control.task.AbstractTask;

/**
 * Classe vide pour permettre des tests avec une classe concrete vide de AbstractTask
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class EmptyTaskForTest extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private boolean stop;
    /*---------- CONSTRUCTEURS ----------*/

    public EmptyTaskForTest()
    {
        super(2, "TEST");
    }
    
    public EmptyTaskForTest(boolean annulable)
    {
        super(2, "TEST");
        this.annulable = annulable;
    }
    
    public EmptyTaskForTest(boolean annulable, AbstractTask task)
    {
        super(2, "TEST", task);
        this.annulable = annulable;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        stop = true;
    }

    @Override
    protected Boolean call() throws Exception
    {
        for (int i = 0; i < 11; i++)
        {
            if (stop)
                return false;
            Thread.sleep(200);
            updateProgress(i, 10);
            updateMessage("i = " + i);
        }
        etapePlus();

        for (int i = 0; i < 11; i++)
        {
            if (stop)
                return false;
            Thread.sleep(200);
            updateProgress(i, 10);
            updateMessage("Etape 2\ni = " + i);
        }
        return true;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
