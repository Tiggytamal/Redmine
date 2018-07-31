package utilities;

import utilities.enums.Severity;

/**
 * Erreur fonctionnelle de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class TechnicalException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private static final Severity severity = Severity.ERROR;

    /**
     * Constructeur des erreurs fonctionnelles.<br>
     * Choix de la sévérité (INFO ou ERROR), du message afficher, ainsi que de l'affichage ou non de la fenêtre du stacktrace
     */
    public TechnicalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @return the severity
     */
    public Severity getSeverity()
    {
        return severity;
    }
}
