package model.enums;

/**
 * Colonnes du fichier des vulnérabilitès
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeColVul implements TypeColW 
{
    SEVERITE("Severité", "colSeverity"), 
    STATUS("Status", "colStatus"),
    MESSAGE("Message", "colMess"),
    DATECREA("Date création", "colDateCrea"),
    LOT("Lot", "colLot"),
    CLARITY("Code Clarity", "colClarity"),
    APPLI("Appli", "colAppli"),
    COMPOSANT("Composant", "colComp"),
    LIB("Bibliothèque", "colLib");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColVul(String valeur, String nomCol)
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
