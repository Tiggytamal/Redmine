package model.enums;

import java.io.Serializable;

public enum TypeColClarity implements Serializable, TypeCol
{
    /*---------- ATTRIBUTS ----------*/

    ACTIF("Actif","colActif"),
    CLARITY("Code projet","colClarity"),
    LIBELLE("Libellé projet","colLib"),
    CPI("Chef de projet","colCpi"),
    EDITION("Edition","colEdition"),
    DIRECTION("Direction","colDir"),
    DEPARTEMENT("Département","colDepart"),
    SERVICE("Service","colService");
        
    private String valeur;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColClarity(String valeur, String nomCol)
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