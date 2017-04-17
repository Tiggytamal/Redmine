package utilities;

import utilities.enums.Severity;

/**
 * Classe de m�thode utilitaires statiques
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Utilities
{
    /**
     * Permet de mettre � jour un message dans une page JSF. La s�v�rit� et le d�tail sont optionnels.<br>
     * Si le detail est null, renvoie un message sans d�tail.<br>
     * Si la severit� est null, renvoie un message de type {@code SEVERITY_INFO}.<br>
     * Renvoie un {@code IllegalArgumentException} si jamais le message est null ou vide.
     * 
     * @param severity
     * @param message
     */
    public static void updateGrowl(String message, Severity severity, String detail)
    {
        if (message == null || message.isEmpty())
            throw new IllegalArgumentException("Growl avec un message null");
        
        if (severity == null)
            severity = Severity.SEVERITY_INFO;

    }
}
