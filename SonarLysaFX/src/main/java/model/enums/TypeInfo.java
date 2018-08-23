package model.enums;

import static utilities.Statics.EMPTY;
import static utilities.Statics.LIENANO;
import static utilities.Statics.LIENAPP;

/**
 * Enum�ration regroupant toutes les informations possibles sur l'�xecution du traitement du fichier de suivi.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public enum TypeInfo 
{
    /*---------- ATTRIBUTS ----------*/

    ANOSRTCCREES("Lots avec anomalies RTC cr��es :\n", LIENANO),
    ANONEW("Lots avec nouvelles anomalies :\n", EMPTY),
    ANOABANDON("Lots avec anomalies ferm�es (abandonn�es ou cl�tur�es) :\n", LIENANO),
    ANOABANDONRATE("Lots avec anomalies non ferm�es :\n", LIENANO),
    ANOARELANCER("Lots avec anomalies � relancer :\n", LIENANO),
    ANOMAJ("Anomalies RTC mises � jour :\n", " - Nouvel �tat : "),
    LOTMAJ("Lots mis � jour :\n", " - Nouvel �tat : "),
    LOTNONRTC("Lots inconnus dans l'extraction RTC:\n", EMPTY),
    CLARITYINCONNU("Lots avec Clarity inconnu :\n", "- Clarity : "),
    SERVICESSANSRESP("Lots avec services sans responsable :\n", "- Service : "),
    APPLIOBSOLETE("Liste des composants avec un code application obsol�te :\n", LIENAPP),
    APPLINONREF("Liste des composants avec une application non list�e dans le r�f�rentiel :\n", LIENAPP),
    COMPOSANSAPP("Liste des composants sans application :\n", EMPTY),
    COMPOPURGE("Liste des composants purg�s :\n", EMPTY);   
    
    private String titre;
    private String liens;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeInfo(String titre, String liens)
    {
        this.titre = titre;
        this.liens = liens;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getTitre()
    {
        return titre;
    }
    
    public String getLiens()
    {
        return liens;
    }
}
