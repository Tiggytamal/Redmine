package model.enums;

/**
 * Colonnes du fichier des vulnérabilitès
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeColCompo implements TypeColW 
{
    PATRIMOINE("Patrimoine", "colPat"),
    INCONNU("Sans lots RTC", "colInco"),
    NONPROD("Lots non livrés", "colNonProd"),
    TERMINE("Lots terminés", "colTermine");

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
