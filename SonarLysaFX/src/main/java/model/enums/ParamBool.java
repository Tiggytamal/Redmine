package model.enums;

/**
 * Enumération représentant les paramètre de type booléen.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum ParamBool implements TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VUESSUIVI("Création des vues de suivi"),
    SUPPSONAR("Protection composants récents purge");

    private final String nom;

    /*---------- CONSTRUCTEURS ----------*/

    private ParamBool(String nom)
    {
        this.nom = nom;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public String getNom()
    {
        return nom;
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/    
}
