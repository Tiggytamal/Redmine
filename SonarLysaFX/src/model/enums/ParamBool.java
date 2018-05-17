package model.enums;

import java.io.Serializable;

public enum ParamBool implements Serializable,TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VUESSUIVI("Création des vues de suivi");

    private final String string;

    /*---------- CONSTRUCTEURS ----------*/

    private ParamBool(String string)
    {
        this.string = string;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/    
}
