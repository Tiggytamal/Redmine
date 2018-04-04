package control.parent;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Impl�mentaion g�n�rique pour un Job de traitement planifi�.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public abstract class JobForTask extends LaunchTask implements Job
{
    public static final String CLEFANNEES = "annees";
    
    protected <T> T getObjectFromJobMap(Class<T> retour, String clef, JobExecutionContext context)
    {
        Object objet = context.getJobDetail().getJobDataMap().get(clef);
        if (retour.isAssignableFrom(objet.getClass()) )
            return retour.cast(objet);
        throw new FunctionalException(Severity.SEVERITY_ERROR, "Mauvais format de donn�e");
    }
}