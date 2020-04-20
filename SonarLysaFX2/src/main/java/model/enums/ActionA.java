package model.enums;

/**
 * Represente les actions possibles sur une anomalie non créée par l'application
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public enum ActionA implements Action
{
    /*---------- ATTRIBUTS ----------*/

    SUPPRIMER(Valeur.SUPPRIMER),
    MODIFIER(Valeur.MODIFIER);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionA(String valeur)
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

    public static ActionA from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionA.from - argument vide ou nul.");

        switch (typeAction)
        {
            case Valeur.SUPPRIMER:
                return SUPPRIMER;
                
            case Valeur.MODIFIER:
                return MODIFIER;

            default:
                throw new IllegalArgumentException("model.enums.ActionA.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String SUPPRIMER = "Supprimer";
        private static final String MODIFIER = "Modifier";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.ActionA$Valeur.");
        }
    }
}
