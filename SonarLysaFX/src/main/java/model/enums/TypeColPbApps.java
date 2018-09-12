package model.enums;

/**
 * Colonnes du fichier des probl�mes des codes application
 * 
 * @author ETP8137 - Gr�goire Mathon
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
    DEP("D�partement", "colDep"),
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
