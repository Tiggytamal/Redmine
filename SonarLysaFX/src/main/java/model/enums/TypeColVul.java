package model.enums;

/**
 * Colonnes du fichier des vuln�rabilit�s
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum TypeColVul implements TypeColW 
{
    SEVERITE("Severit�", "colSeverity"), 
    STATUS("Status", "colStatus"),
    MESSAGE("Message", "colMess"),
    DATECREA("Date cr�ation", "colDateCrea"),
    LOT("Lot", "colLot"),
    CLARITY("Code Clarity", "colClarity"),
    APPLI("Appli", "colAppli"),
    COMPOSANT("Composant", "colComp"),
    LIB("Biblioth�que", "colLib");

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
