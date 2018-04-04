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

    private String valeur;
    private String nomCol;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeColChefServ(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String getValeur()
    {
        return valeur;
    }
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
}