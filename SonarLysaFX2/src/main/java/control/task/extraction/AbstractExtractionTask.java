package control.task.extraction;

import control.excel.AbstractControlExcelWrite;
import control.task.AbstractTask;

@SuppressWarnings("rawtypes")
public abstract class AbstractExtractionTask<C extends AbstractControlExcelWrite> extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/
    
    protected C control;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected AbstractExtractionTask(int fin, String titre)
    {
        super(fin, titre);
        startTimers();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/
    
    /**
     * Sauvegarde du fichier
     * 
     * @return
     *         true si le fichier a été sauvegardé.
     */
    protected boolean sauvegarde()
    {
        updateProgress(1, 1);
        return control.write();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
