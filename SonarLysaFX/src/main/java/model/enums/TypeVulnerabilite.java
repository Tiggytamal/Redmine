package model.enums;

/**
 * Valeurs possibles des vulnérabilitès SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeVulnerabilite 
{
    /*---------- ATTRIBUTS ----------*/

    OUVERTE ("Ouvertes", "false"),
    RESOLUE ("Résolues", "true");
    
    private String nomSheet;
    private String booleen;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeVulnerabilite(String nomSheet, String booleen)
    {
        this.nomSheet = nomSheet;
        this.booleen = booleen;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getNomSheet()
    {
        return nomSheet;
    }
    
    public String getBooleen()
    {
        return booleen;
    }
}
