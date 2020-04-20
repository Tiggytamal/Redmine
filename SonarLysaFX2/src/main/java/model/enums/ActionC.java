package model.enums;

/**
 * Represente les actions possibles sur un composant.
 * Attention la methode toString() retourne la valeur de la donnée et pas la représentation String de l'énumération.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ActionC implements Action
{
    /*---------- ATTRIBUTS ----------*/

    PURGER(Valeur.PURGER),
    SUPPCONTROLEAPPLI(Valeur.SUPPCONTROLEAPPLI),
    SUPPCONTROLEDETTE(Valeur.SUPPCONTROLEDETTE);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    ActionC(String valeur)
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

    public static ActionC from(String typeAction)
    {
        if (typeAction == null || typeAction.isEmpty())
            throw new IllegalArgumentException("model.enums.ActionC.from - argument vide ou nul.");
        
        switch (typeAction)
        {
            case Valeur.PURGER:
                return PURGER;
                
            case Valeur.SUPPCONTROLEAPPLI:
                return SUPPCONTROLEAPPLI;
                
            case Valeur.SUPPCONTROLEDETTE:
                return SUPPCONTROLEDETTE;

            default:
                throw new IllegalArgumentException("model.enums.ActionC.from - action inconnue : " + typeAction);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private static final class Valeur
    {
        private static final String SUPPCONTROLEAPPLI = "Supp. contrôle app.";
        private static final String PURGER = "Purger";
        private static final String SUPPCONTROLEDETTE = "Supp. contrôle dette tech.";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.ActionC$Valeur.");
        }
    }
}
