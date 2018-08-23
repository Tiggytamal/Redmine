package model.enums;

/**
 * Enum�ration repr�sentant les param�tre de type bool�en.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum ParamBool implements TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VUESSUIVI("Cr�ation des vues de suivi"),
    SUPPSONAR("Protection composants r�cents purge");

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
