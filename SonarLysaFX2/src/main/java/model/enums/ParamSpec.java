package model.enums;

/**
 * Liste des paramètres speciaux de l'application
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public enum ParamSpec implements TypeKey, EnumParam
{
    /*---------- ATTRIBUTS ----------*/

    TEXTEPANORECAP("Recapitulatif anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTEANORELANCE("Texte de relance des anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTEANOOBSOLETE("Texte des anos RTC obsolètes", TypeParamSpec.TEXTAREA, false),
    TEXTEANOCLOSE("Texte des anos RTC closes", TypeParamSpec.TEXTAREA, false),
    TEXTEANOABANDONNEE("Texte des anos RTC abandonnées", TypeParamSpec.TEXTAREA, false),
    TEXTEANOAPPLI("Texte des anos RTC sur les applis", TypeParamSpec.TEXTAREA, false),
    TEXTEANOAPPLIFAUX("Texte des anos RTC avec codes appli faux", TypeParamSpec.TEXTAREA, false),
    TEXTEANO("Description anomalie", TypeParamSpec.TEXTAREA, false),
    TEXTEANOSECURITE("Texte des anos RTC avec défauts sécurité", TypeParamSpec.TEXTAREA, false),
    TITREASSIGNATIONANO("Titre des mails des assignations", TypeParamSpec.TEXTAREA, false),
    TEXTEASSIGNATIONANO("Texte des mails des assignations", TypeParamSpec.TEXTAREA, false),
    TITRECREERANORTC("Titre des mails de création des anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTECREERANORTC("Texte des mails de création des anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTECREERANORTCAPPLI("Texte des mails de création des anos RTC (code appli)", TypeParamSpec.TEXTAREA, false),
    TITRERELANCEANORTC("Titre des mails de relance des anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTERELANCEANORTC("Texte des mails de relance des anos RTC", TypeParamSpec.TEXTAREA, false),
    TEXTERELANCEANORTCAPPLI("Texte des mails de relance des anos RTC (code appli)", TypeParamSpec.TEXTAREA, false),
    MEMBRESAQP("Responsables AQP", TypeParamSpec.LISTVIEWNOM, false),
    USERSMAIL("Personnes en copie des mails", TypeParamSpec.LISTVIEWNOM, true),
    SIGNATURE("Signature E-Mail", TypeParamSpec.TEXTAREA, true);

    // Nom du paramètre
    private final String nom;

    // Type de paramètre
    private final TypeParamSpec type;
    
    // Paramètre personnel ou non
    private boolean perso;

    /*---------- CONSTRUCTEURS ----------*/

    ParamSpec(String nom, TypeParamSpec type, boolean perso)
    {
        this.nom = nom;
        this.type = type;
        this.perso = perso;
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
    
    public boolean isPerso()
    {
        return perso;
    }
}
