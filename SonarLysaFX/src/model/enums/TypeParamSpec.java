package model.enums;

import java.io.Serializable;

/**
 * 
 * @author ETP8137 - Gr�goire mathon
 *
 */
public enum TypeParamSpec implements Serializable, TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VERSIONS("Version"),
    TEXTEDEFECT("Description Defect RTC"),
    TEXTESECURITE("Texte S�curit� RTC"),
    RECAPDEFECT("R�capitulatif Defect RTC"),
    MEMBRESJAVA("Responsables  JAVA"),
    MEMBRESDTATSTAGE("Responsables DATASTAGE");
    
    
    private final String string;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeParamSpec(String string)
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
