package model.enums;

/**
 * Represente les actions possible sur un défaut qualité .
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ActionDq implements Action
{
    /*---------- ATTRIBUTS ----------*/

    CREER(Valeur.CREER),
    CLOTURER(Valeur.CLOTURER),
    ABANDONNER(Valeur.ABANDONNER),
    OBSOLETE(Valeur.OBSOLETE),
    RELANCER(Valeur.RELANCER),
    MAJ(Valeur.MAJ);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionDq(String valeur)
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

    public static ActionDq from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionDq.from - argument vide ou nul.");

        switch (typeAction)
        {
            case Valeur.CREER:
                return CREER;

            case Valeur.CLOTURER:
                return CLOTURER;

            case Valeur.ABANDONNER:
                return ABANDONNER;

            case Valeur.OBSOLETE:
                return OBSOLETE;

            case Valeur.RELANCER:
                return RELANCER;

            case Valeur.MAJ:
                return MAJ;

            default:
                throw new IllegalArgumentException("model.enums.ActionDq.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String CREER = "Créer";
        private static final String CLOTURER = "Clôturer";
        private static final String ABANDONNER = "Abandonner";
        private static final String OBSOLETE = "Rendre obsolète";
        private static final String RELANCER = "Relancer";
        private static final String MAJ = "Mise à jour";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeActionDq$Valeur.");
        }
    }
}
