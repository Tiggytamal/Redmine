package model.enums;

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
