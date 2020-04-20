package model.enums;

/**
 * Regroupe les type de statistiques calculées par l'application sur les composants
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 2.0
 */
public enum StatistiqueCompoEnum
{
    /*---------- ATTRIBUTS ----------*/

    NEWLDCTOCOVER("Nouvelles Lignes de code à couvrir"),
    NEWLDCNOCOVER("Nouvelles lignes de code sans couverture");

    private String label;

    StatistiqueCompoEnum(String label)
    {
        this.label = label;
    }

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLabel()
    {
        return label;
    }
}
