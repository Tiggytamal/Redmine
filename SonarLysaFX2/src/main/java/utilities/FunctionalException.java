package utilities;

import model.enums.Severity;

/**
 * Erreur fonctionnelle de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class FunctionalException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private final Severity severity;

    /**
     * Constructeur des erreurs fonctionnelles.<br>
     * Choix de la severite (INFO ou ERROR), du message afficher, ainsi que de l'affichage ou non de la fenêtre du stacktrace
     * 
     * @param severity
     *            Sévérité de l'exception
     * @param message
     *            Message à afficher dan sla boete de dialogue
     */
    public FunctionalException(Severity severity, String message)
    {
        super(message);
        this.severity = severity;
    }

    public Severity getSeverity()
    {
        return severity;
    }
}
