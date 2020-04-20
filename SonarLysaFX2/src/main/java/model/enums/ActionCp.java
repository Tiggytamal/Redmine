package model.enums;

/**
 * Represente les actions possible sur un composant en erreur
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public enum ActionCp implements Action
{
    /*---------- ATTRIBUTS ----------*/

    PURGER(Valeur.PURGER),
    CLOTURER(Valeur.CLOTURER);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionCp(String valeur)
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

    public static ActionCp from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionCp.from - argument vide ou nul.");

        switch (typeAction)
        {
            case Valeur.PURGER:
                return PURGER;

            case Valeur.CLOTURER:
                return CLOTURER;

            default:
                throw new IllegalArgumentException("model.enums.ActionCp.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String PURGER = "Purger";
        private static final String CLOTURER = "Clôturer";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.ActionCp$Valeur.");
        }
    }
}
