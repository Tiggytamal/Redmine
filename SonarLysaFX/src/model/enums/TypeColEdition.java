package model.enums;

import java.io.Serializable;

public enum TypeColEdition implements Serializable, TypeCol  
{
    LIBELLE ("Libellé", "colLib"),
    VERSION ("Numero de version", "colVersion");

    private String string;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColEdition(String string, String nomCol)
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