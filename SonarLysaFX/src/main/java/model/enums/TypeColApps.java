package model.enums;

/**
 * Colonnes du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeColApps implements TypeColR, TypeColW
{
    CODEAPPS("Code Application", "colCode"),
    ACTIF("Actif", "colActif"),
    LIB("Libellé", "colLib"),
    OPEN("Top appli open", "colOpen"),
    MAINFRAME("Top appli MainFrame", "colMainFrame");
    
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColApps(String valeur, String nomCol)
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
