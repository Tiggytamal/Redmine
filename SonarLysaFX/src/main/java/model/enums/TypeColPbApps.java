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
    CODEAPPS("Code Application", "colCode"),
    ACTIF("Actif", "colActif"),
    LIB("Libellé", "colLib"),
    OPEN("Top appli open", "colOpen"),
    MAINFRAME("Top appli MainFrame", "colMainFrame");
    
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
