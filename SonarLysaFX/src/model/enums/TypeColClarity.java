package model.enums;

import java.io.Serializable;

public enum TypeColClarity implements Serializable, TypeCol
{
    /*---------- ATTRIBUTS ----------*/

    ACTIF("Actif","colActif"),
    CLARITY("Code projet","colClarity"),
    LIBELLE("Libell� projet","colLib"),
    CPI("Chef de projet","colCpi"),
    EDITION("Edition","colEdition"),
    DIRECTION("Direction","colDir"),
    DEPARTEMENT("D�partement","colDepart"),
    SERVICE("Service","colService");
        
    private String string;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColClarity(String string, String nomCol)
    {
        this.string = string;
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
    public String toString()
    {
        return string;
    }
}