package model.enums;

/**
 * Enumeration regroupant les resolutions possibles des défauts dans SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public enum TypeResolution
{
    /*---------- ATTRIBUTS ----------*/

    WONTFIX,
    FIXED,
    FALSEPOSITIVE,
    REMOVED;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    public static TypeResolution from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new IllegalArgumentException("model.enums.TypeResolution.from - argument vide ou nul.");

        switch (valeur)
        {
            case "WONTFIX":
                return WONTFIX;

            case "FIXED":
                return FIXED;

            case "FALSE-POSITIVE":
                return FALSEPOSITIVE;
                
            case "REMOVED":
                return REMOVED;

            default:
                throw new IllegalArgumentException("model.enums.TypeResolution.from - valeur inconnue : " + valeur);
        }
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
