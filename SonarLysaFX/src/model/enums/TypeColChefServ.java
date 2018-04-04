package model.enums;

import java.io.Serializable;

public enum TypeColChefServ implements Serializable, TypeCol 
{
    /*---------- ATTRIBUTS ----------*/
    
    DIRECTION("Direction", "colDir"), 
    DEPARTEMENT("D�partement", "colDepart"), 
    SERVICE("Service", "colService"), 
    FILIERE("Fili�re", "colFil"), 
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