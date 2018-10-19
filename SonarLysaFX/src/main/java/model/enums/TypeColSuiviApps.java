package model.enums;

/**
 * Colonnes du fichier de Suivi des défaults des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeColSuiviApps implements TypeColR 
{
    COMPO("Composant", "colCompo"),
    ACTUEL("Code actuel", "colActuel"),
    NEW("Code à valoriser", "colNew"),
    ACTION("Action", "colAction"),
    ETAT("Etat", "colEtat"),
    ANORTC("Anomalie RTC", "colAnoRTC"),
    CPILOT("Cpi Lot", "colCpiLot"),
    LOT("Lot", "colLot");

    private String valeur;
    private String nomCol;
    
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeColSuiviApps(String valeur, String nomCol)
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
