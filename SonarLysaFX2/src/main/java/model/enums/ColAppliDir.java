package model.enums;

/**
 * Colonnes du fichier des Applications/Départements
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public enum ColAppliDir implements ColR
{
    /*---------- ATTRIBUTS ----------*/

    APPLI("Application","colAppli"),
    DIR("Direction","colDir");
        
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    ColAppliDir(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
    
    @Override
    public String getValeur()
    {
        return valeur;
    }
}
