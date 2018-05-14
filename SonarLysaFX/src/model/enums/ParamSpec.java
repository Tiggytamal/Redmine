package model.enums;

import java.io.Serializable;

/**
 * Liste des paramètres spéciaux de l'application
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public enum ParamSpec implements Serializable, TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    VERSIONS("Version", TypeParamSpec.LISTVIEWVERSION),
    TEXTEDEFECT("Description Defect RTC", TypeParamSpec.TEXTAREA),
    TEXTESECURITE("Texte Sécurité RTC", TypeParamSpec.TEXTAREA),
    RECAPDEFECT("Récapitulatif Defect RTC", TypeParamSpec.TEXTAREA),
    MEMBRESJAVA("Responsables  JAVA", TypeParamSpec.LISTVIEWNOM),
    MEMBRESDTATSTAGE("Responsables DATASTAGE", TypeParamSpec.LISTVIEWNOM);
        
    private final String string;
    private final TypeParamSpec type;

    /*---------- CONSTRUCTEURS ----------*/

    private ParamSpec(String string, TypeParamSpec type)
    {
        this.string = string;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public TypeParamSpec getType()
    {
        return type;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
