package model.enums;

/**
 * Types des données utlisées par l'applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeDonnee implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    APPS ("Applications"), 
    CLARITY("Projets Clarity"), 
    RESPSERVICE("Chefs de Service"), 
    EDITION("Editions"),
    COMPOSANT("Composants"),
    LOTSRTC("Lots RTC"),
    GROUPE("Groupement de Projets"),
    DEFAULTQUALITE("Defaults qualité"),
    DATEMAJ("Dates de mise à jour des tables"),
    DEFAULTAPPLI("Defaults application");
    
    private String valeur;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeDonnee(String valeur)
    {
        this.valeur = valeur;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
}
