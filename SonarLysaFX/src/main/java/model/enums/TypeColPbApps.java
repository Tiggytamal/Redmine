package model.enums;

/**
 * Colonnes du fichier des problèmes des codes application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeColPbApps implements TypeColW
{
    CODE("Code Composant", "colCode"),
    APPLI("Code Application", "colAppli"),
    ETATAPPLI("Etat Application", "colEtatAppli"),
    LOT("Lot RTC", "colLot"),
    CPILOT("Cpi lot", "colCpiLot"),
    DEP("Département", "colDep"),
    SERVICE("Service", "colService"),
    CHEFSERV("Chef de Service", "colChefServ");
    
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColPbApps(String valeur, String nomCol)
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
