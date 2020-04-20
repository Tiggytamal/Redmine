package model.enums;

/**
 * Enumeration représentant le type d'édition d'un lot (CHC, CDM ou autre).
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeEdition 
{
    /*---------- ATTRIBUTS ----------*/

    CHC,
    CDM,
    FDL,
    INCONNUE,
    MAJEURE,
    MEDIANE,
    CU;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Création du type d'édition depuis la colonne du fichier. Retourne une édition inconnue si la valeur n'est pas reconnue.
     * 
     * @param edition
     *                Edition à traiter.
     * @return
     *         L'Enumération correspondante.
     */
    public static TypeEdition getTypeEdition(String edition)
    {
        try
        {
            return TypeEdition.valueOf(edition);
        }
        catch (IllegalArgumentException e)
        {
            return TypeEdition.INCONNUE;
        }
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
