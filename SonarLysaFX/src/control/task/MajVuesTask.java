package control.task;

/**
 * T�che permettant la mise � jour des vues dans Sonar
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class MajVuesTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String TITRE = "Mise � jour des vues Sonar";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public MajVuesTask()
    {
        super(1);
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public void annuler()
    {
        // Pas d'action possible � l'annulation
    }

    @Override
    protected Boolean call() throws Exception
    {
        return majVues();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Lance la mise � jour des vues dans SonarQube. Indispenssable apr�s la cr�ation d'une nouvelle vue.
     */
    private boolean majVues()
    {
        api.majVues();
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
