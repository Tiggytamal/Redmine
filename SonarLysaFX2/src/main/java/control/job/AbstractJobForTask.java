package control.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import model.enums.Severity;
import utilities.FunctionalException;

/**
 * Implementaion générique pour un Job de traitement planifié.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class AbstractJobForTask implements Job
{
    /** Clef de la Map pour les années */
    public static final String CLEFANNEES = "annees";

    /**
     * Méthode retournant un objet depuis la map des paramètres d'un Job
     * 
     * @param retour
     *                Classe de retour de l'objet
     * @param clef
     *                Clef de la map pour retrouver l'objet
     * @param context
     *                Contexte d'exécution du Job
     * @param         <T>
     *                Classe de l'objet à récupérer.
     * @return
     *         L'objet récupérer depuis la map. pas de null possible.
     */
    protected <T> T getObjectFromJobMap(Class<T> retour, String clef, JobExecutionContext context)
    {
        // Récupération de l'objet depuis la map
        Object objet = context.getJobDetail().getJobDataMap().get(clef);

        // Contrôle pour éviter un ClassCast et renvoi de l'objet
        if (retour.isAssignableFrom(objet.getClass()))
            return retour.cast(objet);
        throw new FunctionalException(Severity.ERROR, "Mauvais format de données");
    }
}
