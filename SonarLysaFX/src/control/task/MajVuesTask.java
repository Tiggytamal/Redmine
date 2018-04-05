package control.task;

/**
 * Tâche permettant la mise à jour des vues dans Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajVuesTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String TITRE = "Mise à jour des vues Sonar";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public MajVuesTask()
    {
        super(1);
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public void annuler()
    {
        // Pas d'action possible à l'annulation
    }

    @Override
    protected Boolean call() throws Exception
    {
        return majVues();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Lance la mise à jour des vues dans SonarQube. Indispenssable après la création d'une nouvelle vue.
     */
    private boolean majVues()
    {
        api.majVues();
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
