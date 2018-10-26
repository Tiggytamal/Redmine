package control.task;

/**
 * Tâche d'extraction eds données de la base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ExtractionBDDTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 3;
    private static final String TITRE = "Extraction base de données";

    /*---------- CONSTRUCTEURS ----------*/

    public ExtractionBDDTask()
    {
        super(ETAPES, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement pour le moment.
    }

    @Override
    protected Boolean call() throws Exception
    {
        return extractionDonnees();
    }

    /*---------- METHODES PRIVEES ----------*/

    private Boolean extractionDonnees()
    {
        return true;
    }

    /*---------- ACCESSEURS ----------*/

}
