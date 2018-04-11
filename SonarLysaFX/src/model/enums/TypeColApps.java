package model.enums;

import java.io.Serializable;

/**
 * Colonnes du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeColApps implements Serializable, TypeCol 
{
    CODEAPPS("Code Application", "colApps"),
    ACTIF("Actif", "colActif");
    
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