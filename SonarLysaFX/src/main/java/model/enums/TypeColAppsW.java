package model.enums;

/**
 * Colonnes du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeColAppsW implements TypeColR, TypeColW
{
    CODEAPPS("Code Application", "colCode"),
    ACTIF("Actif", "colActif"),
    LIB("Libellé", "colLib"),
    OPEN("Top appli open", "colOpen"),
    MAINFRAME("Top appli MainFrame", "colMainFrame"),
    CRITICITE("Criticité", "colCrit"),
    VULN("Vulnérabilitès", "colVuln"),
    LDCSONAR("LDC SonarQube", "colLDCSonar"),
    LDCMAIN("LDC MainFrame", "colLDCMain");
    
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColAppsW(String valeur, String nomCol)
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
