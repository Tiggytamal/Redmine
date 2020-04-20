package model.enums;

/**
 * Represente les actions possibles sur un utilisateur RTC
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public enum ActionU implements Action
{
    /*---------- ATTRIBUTS ----------*/

    DESACTIVER(Valeur.DESACTIVER),
    ACTIVER(Valeur.ACTIVER);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionU(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public String getValeur()
    {
        return valeur;
    }

    @Override
    public String toString()
    {
        return valeur;
    }

    public static ActionU from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionU.from - argument vide ou nul.");

        switch (typeAction)
        {               
            case Valeur.DESACTIVER:
                return DESACTIVER;
                
            case Valeur.ACTIVER:
                return ACTIVER;

            default:
                throw new IllegalArgumentException("model.enums.ActionU.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String DESACTIVER = "Désactiver";
        private static final String ACTIVER = "Activer";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.ActionU$Valeur.");
        }
    }
}
