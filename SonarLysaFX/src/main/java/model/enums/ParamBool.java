package model.enums;

import java.io.Serializable;

public enum ParamBool implements Serializable,TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VUESSUIVI("Cr�ation des vues de suivi"),
    SUPPSONAR("Non purge des 3 derni�res versions des composants r�cents");

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
