package model.enums;

/**
 * Types des donn�es utlis�es par l'applications
 * 
 * @author ETP8137 - Gr�goire Mathon
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
    DEFAULTQUALITE("Defaults qualit�"),
    DATEMAJ("Dates de mise � jour des tables"),
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
