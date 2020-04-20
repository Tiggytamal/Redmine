package model.enums;

/**
 * Represente les actions possible sur un défaut qualité historisé.
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ActionDqH implements Action
{
    /*---------- ATTRIBUTS ----------*/

    MAJ(Valeur.MAJ);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionDqH(String valeur)
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

    public static ActionDqH from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionDqH.from - argument vide ou nul.");
        
        switch (typeAction)
        {                
            case Valeur.MAJ:
                return MAJ;

            default:
                throw new IllegalArgumentException("model.enums.ActionDqH.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String MAJ = "Mise à jour";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeActionDqH$Valeur.");
        }
    }
}
