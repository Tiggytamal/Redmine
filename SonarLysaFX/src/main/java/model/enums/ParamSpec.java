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

    RECAPDEFECT("Récapitulatif Defect RTC", TypeParamSpec.TEXTAREA),
    TEXTEDEFECT("Description Defect RTC", TypeParamSpec.TEXTAREA),
    TEXTESECURITE("Texte Sécurité RTC", TypeParamSpec.TEXTAREA),
    VERSIONS("Versions", TypeParamSpec.LISTVIEWVERSION),
    VERSIONSCOMPOSANTS("Versions des composants pour la purge", TypeParamSpec.LISTVIEWCOMPO),
    MEMBRESJAVA("Responsables  JAVA", TypeParamSpec.LISTVIEWNOM),
    MEMBRESDATASTAGE("Responsables DATASTAGE", TypeParamSpec.LISTVIEWNOM),
    MEMBRESMAIL("Groupe de reception des rapports", TypeParamSpec.LISTVIEWNOM);
    
    // Nom du paramètre
    private final String string;
    
    // Type de paramètre
    private final TypeParamSpec type;

    /*---------- CONSTRUCTEURS ----------*/

    private ParamSpec(String string, TypeParamSpec type)
    {
        this.string = string;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public TypeParamSpec getType()
    {
        return type;
    }
}