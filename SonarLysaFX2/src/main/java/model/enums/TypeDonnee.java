package model.enums;

/**
 * Types des données utlisees par l'applications
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
    ISSUE("Issue SonarQube"),
    COMPOSANT("Composants"),
    LOTSRTC("Lots RTC"),
    PRODUIT("Produits"),
    SOLUTION("Solutions"),
    DEFAULTQUALITE("Defaults qualite"),
    DATEMAJ("Dates de mise à jour des tables"),
    DEFAULTAPPLI("Defaults application"),
    COMPOERR("Composants en erreurs"),
    PROJETMC("Projets Mobile Center"),
    USER("Utilisateurs RTC/Sonar"),
    ANO("Anomalies RTC");
    
    private String valeur;
    
    /*---------- CONSTRUCTEURS ----------*/

    TypeDonnee(String valeur)
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
