package model.enums;

/**
 * Colonnes du fichier des chefs de services
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ColChefServ implements ColR 
{
    /*---------- ATTRIBUTS ----------*/
    
    DIRECTION("Direction", "colDir"), 
    DEPARTEMENT("Departement", "colDepart"), 
    SERVICE("Service", "colService"), 
    FILIERE("Filiere", "colFil"), 
    MANAGER("Manager", "colManager");

    private final String valeur;
    private final String nomCol;
    
    /*---------- CONSTRUCTEURS ----------*/

    ColChefServ(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String getValeur()
    {
        return valeur;
    }
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
}
