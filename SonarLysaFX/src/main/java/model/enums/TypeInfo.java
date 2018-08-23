package model.enums;

import static utilities.Statics.EMPTY;
import static utilities.Statics.LIENANO;
import static utilities.Statics.LIENAPP;

/**
 * Enumération regroupant toutes les informations possibles sur l'éxecution du traitement du fichier de suivi.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeInfo 
{
    /*---------- ATTRIBUTS ----------*/

    ANOSRTCCREES("Lots avec anomalies RTC créées :\n", LIENANO),
    ANONEW("Lots avec nouvelles anomalies :\n", EMPTY),
    ANOABANDON("Lots avec anomalies fermées (abandonnées ou clôturées) :\n", LIENANO),
    ANOABANDONRATE("Lots avec anomalies non fermèes :\n", LIENANO),
    ANOARELANCER("Lots avec anomalies à relancer :\n", LIENANO),
    ANOMAJ("Anomalies RTC mises à jour :\n", " - Nouvel état : "),
    LOTMAJ("Lots mis à jour :\n", " - Nouvel état : "),
    LOTNONRTC("Lots inconnus dans l'extraction RTC:\n", EMPTY),
    CLARITYINCONNU("Lots avec Clarity inconnu :\n", "- Clarity : "),
    SERVICESSANSRESP("Lots avec services sans responsable :\n", "- Service : "),
    APPLIOBSOLETE("Liste des composants avec un code application obsolète :\n", LIENAPP),
    APPLINONREF("Liste des composants avec une application non listée dans le référentiel :\n", LIENAPP),
    COMPOSANSAPP("Liste des composants sans application :\n", EMPTY),
    COMPOPURGE("Liste des composants purgés :\n", EMPTY);   
    
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
