package model.enums;

/**
 * Colonnes du fichier des vuln�rabilit�s
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum TypeColCompo implements TypeColW 
{
    PATRIMOINE("Patrimoine", "colPat"),
    INCONNU("Sans lots RTC", "colInco"),
    NONPROD("Lots non livr�s", "colNonProd"),
    TERMINE("Lots termin�s", "colTermine");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColCompo(String valeur, String nomCol)
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
