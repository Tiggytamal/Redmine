package model.enums;

import java.io.Serializable;

/**
 * 
 * @author ETP8137 - Grégoire mathon
 *
 */
public enum TypeParamSpec implements Serializable, TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VERSIONS("Version"),
    TEXTEDEFECT("Description Defect RTC"),
    TEXTESECURITE("Texte Defect"),
    MEMBRESJAVA("Responsables anomalies JAVA"),
    MEMBRESDTATSTAGE("Responsables anomalies DATASTAGE");
    
    
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
