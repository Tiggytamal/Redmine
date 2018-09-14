package control.task;

/**
 * Tâche permettant la mise à jour des vues dans Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajVuesTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final String TITRE = "Mise à jour des vues Sonar";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public MajVuesTask()
    {
        super(1, TITRE);
        annulable = false;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majVues();
    }
    
    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Lance la mise à jour des vues dans SonarQube. Indispenssable après la création d'une nouvelle vue.
     */
    private boolean majVues()
    {
        updateProgress(0, 1);
        api.majVues();
        updateProgress(1, 1);
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
