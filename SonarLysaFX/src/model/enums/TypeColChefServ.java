package model.enums;

import java.io.Serializable;

public enum TypeColChefServ implements Serializable, TypeCol 
{
    /*---------- ATTRIBUTS ----------*/
    
    DIRECTION("Direction", "colDir"), 
    DEPARTEMENT("Département", "colDepart"), 
    SERVICE("Service", "colService"), 
    FILIERE("Filière", "colFil"), 
    MANAGER("Manager", "colManager");

    private String string;
    private String nomCol;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeColChefServ(String string, String nomCol)
    {
        this.string = string;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
}