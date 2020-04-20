package model.enums;

/**
 * Colonnes du fichier des projets Clarity
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ColClarity implements ColR
{
    /*---------- ATTRIBUTS ----------*/

    ACTIF("Actif","colActif"),
    CLARITY("Code projet","colClarity"), 
    LIBELLE("Libelle projet","colLib"),
    CPI("Chef de projet","colCpi"),
    EDITION("Edition","colEdition"),
    DIRECTION("Direction","colDir"),
    DEPARTEMENT("Departement","colDepart"),
    SERVICE("Service","colService");
        
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    ColClarity(String valeur, String nomCol)
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
