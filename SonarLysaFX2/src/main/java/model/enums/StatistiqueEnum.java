package model.enums;

/**
 * Regroupe les type de statistiques calculées par l'application.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 2.0
 */
public enum StatistiqueEnum
{
    /*---------- ATTRIBUTS ----------*/
    
    FICHIERSPARJOUR("Assemblages effectués", TypeStatistique.VALEUR),
    COMPOSKO("Composants QG KO", TypeStatistique.VALEUR),
    DEFAUTSENCOURS("Défauts en cours", TypeStatistique.VALEUR),
    COMPOSPROPRES("Composants sans défauts", TypeStatistique.VALEUR),
    COMPOSKOP("Composants QG KO", TypeStatistique.POURCENTAGE),
    COMPOSPROPRESP("Composants sans défauts", TypeStatistique.POURCENTAGE),
    NEWLDCTUP("Couverture nouvelles lignes de code", TypeStatistique.POURCENTAGE);

    private String label;
    private TypeStatistique type;

    StatistiqueEnum(String label, TypeStatistique type)
    {
        this.label = label;
        this.type = type;
    }

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLabel()
    {
        return label;
    }
    
    public TypeStatistique getType()
    {
        return type;
    }
}
