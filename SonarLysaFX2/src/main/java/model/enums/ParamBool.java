package model.enums;

/**
 * Enumeration représentant les paramètre de type booleen.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum ParamBool implements TypeKey, EnumParam 
{
    /*---------- ATTRIBUTS ----------*/

    FICHIERPICAUTO("Traitement auto des fichiers PIC");

    private final String nom;

    /*---------- CONSTRUCTEURS ----------*/

    ParamBool(String nom)
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
