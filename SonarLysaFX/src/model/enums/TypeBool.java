package model.enums;

import java.io.Serializable;

public enum TypeBool implements Serializable,TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VUESSUIVI("Création des vues de suivi");

    private final String string;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeBool(String string)
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
