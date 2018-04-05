package model.enums;

import java.io.Serializable;

public enum TypeColApps implements Serializable, TypeCol 
{
    CODEAPPS("Code Application", "colApps"),
    ACTIF("Actif", "colActif");
    
    private String valeur;
    private String nomCol;

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