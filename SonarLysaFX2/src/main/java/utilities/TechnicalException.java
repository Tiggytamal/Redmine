package utilities;

import model.enums.Severity;

/**
 * Erreur fonctionnelle de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class TechnicalException extends RuntimeException
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final Severity severity = Severity.ERROR;

    /*---------- CONSTRUCTEURS ----------*/

    public TechnicalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TechnicalException(String message)
    {
        this(message, null);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public Severity getSeverity()
    {
        return severity;
    }
}
