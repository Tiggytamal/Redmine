package model.enums;

import java.io.Serializable;

public enum TypeColEdition implements Serializable, TypeCol  
{
    LIBELLE ("Libellé", "colLib"),
    VERSION ("Numero de version", "colVersion");

    private String valeur;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColEdition(String valeur, String nomCol)
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