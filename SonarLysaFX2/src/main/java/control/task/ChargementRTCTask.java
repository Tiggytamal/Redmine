package control.task;

import org.eclipse.core.runtime.IProgressMonitor;

import control.rtc.ControlRTC;

/**
 * Tâche permettant de charger tous les zones de projets RTC en arrière plan.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ChargementRTCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Nombre d'etapes du traitement */
    private static final short ETAPES = 1;
    /** Titre de la tâche */
    private static final String TITRE = "Chargement Projets RTC";

    /*---------- CONSTRUCTEURS ----------*/

    public ChargementRTCTask()
    {
        super(ETAPES, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        IProgressMonitor monitor = new IProgressMonitor() {

            @Override
            public void beginTask(String arg0, int arg1)
            {
                updateMessage("Chargement...");
            }

            @Override
            public void done()
            {
                updateMessage("Chargement...OK");
                updateProgress(1, 1);
            }

            @Override
            public void internalWorked(double arg0)
            {
                // Pas de traitement
            }

            @Override
            public boolean isCanceled()
            {
                return false;
            }

            @Override
            public void setCanceled(boolean arg0)
            {
                // Pas de traitement
            }

            @Override
            public void setTaskName(String arg0)
            {
                // Pas de traitement
            }

            @Override
            public void subTask(String arg0)
            {
                // Pas de traitement
            }

            @Override
            public void worked(int arg0)
            {
                // Pas de traitement
            }

        };

        ControlRTC.getInstance().recupTousLesProjets(monitor);
        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
