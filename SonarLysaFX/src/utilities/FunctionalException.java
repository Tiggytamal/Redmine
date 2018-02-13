package utilities;

/**
 * Erreur fonctionnelle de l'application
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class FunctionalException extends Exception
{
    private static final long serialVersionUID = 1L;    
    private final Severity severity;  
    private final boolean showException;

    /**
     * Constructeur des erreurs fonctionnelles.<br>
     * Choix de la s�v�rit� (INFO ou ERROR), du message afficher, ainsi que de l'affichage ou non de la fen�tre du stacktrace
     */
    public FunctionalException(Severity severity, String message, boolean show)
    {
        super(message);
        this.severity = severity;
        this.showException = show;
    }
    
    /**
     * @return the severity
     */
    public Severity getSeverity()
    {
        return severity;
    }

    /**
     * @return the showException
     */
    public boolean isShowException()
    {
        return showException;
    }   
}