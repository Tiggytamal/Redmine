package utilities;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Classe de m�thode utilitaires statiques
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Utilities
{
    /**
     * R�cup�re une expression du bundle de langue
     * 
     * @param El
     * valeur de l'expression language
     * @return
     * le texte correspondant
     */
    public static String getEl(String El)
    {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getApplication().evaluateExpressionGet(ctx, El, String.class);
    }

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
            severity = FacesMessage.SEVERITY_INFO;

            // Cr�e le FaceMessage
            FacesMessage fm = new FacesMessage(severity, message, detail);

            // Ajoute le message au contexte
            FacesContext.getCurrentInstance().addMessage(null, fm);
    }
}
