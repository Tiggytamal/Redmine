package model.enums;

/**
 * Liste des param�tres sp�ciaux de l'application
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
public enum ParamSpec implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    RECAPDEFECT("R�capitulatif Defect RTC", TypeParamSpec.TEXTAREA),
    TEXTERELANCE("Texte de relance des anomalies", TypeParamSpec.TEXTAREA),
    TEXTEAPPLI("Texte des anomlies sur les applis", TypeParamSpec.TEXTAREA),
    TEXTENEWAPPLI("Texte remontant le bon nom de l'appli", TypeParamSpec.TEXTAREA),
    TEXTEDEFECT("Description Defect RTC", TypeParamSpec.TEXTAREA),
    TEXTEDEFECTAPPLI("Description Defect appli RTC", TypeParamSpec.TEXTAREA),
    TEXTESECURITE("Texte S�curit� RTC", TypeParamSpec.TEXTAREA),
    VERSIONS("Versions", TypeParamSpec.LISTVIEWVERSION),
    VERSIONSVIEUXCOMPOS("Versions des composants anciens", TypeParamSpec.LISTVIEWCOMPO),
    MEMBRESJAVA("Responsables  JAVA", TypeParamSpec.LISTVIEWNOM),
    MEMBRESDATASTAGE("Responsables DATASTAGE", TypeParamSpec.LISTVIEWNOM),
    MEMBRESMAIL("Groupe de reception des rapports", TypeParamSpec.LISTVIEWNOM);
    
    // Nom du param�tre
    private final String nom;
    
    // Type de param�tre
    private final TypeParamSpec type;

    /*---------- CONSTRUCTEURS ----------*/

    private ParamSpec(String nom, TypeParamSpec type)
    {
        this.nom = nom;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getNom()
    {
        return nom;
    }
    
    public TypeParamSpec getType()
    {
        return type;
    }
}
