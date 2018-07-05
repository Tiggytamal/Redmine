package model.enums;

import static utilities.Statics.LIENANO;
import static utilities.Statics.LIENAPP;
/**
 * Enumération regroupant toutes les informations possibles sur l'éxecution du traitement du fichier de suivi.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeInfoMail 
{
    /*---------- ATTRIBUTS ----------*/

    ANOSRTCCREES("Lots avec anomalies RTC créées :\n", LIENANO),
    ANONEW("Lots avec nouvelles anomalies :\n", ""),
    ANOABANDON("Lotas avec anomalies abandonnées :\n", LIENANO),
    ANOABANDONRATE("Lots avec anomalies non abandonnées :\n", LIENANO),
    ANOARELANCER("Lots avec anomalies à relancer :\n", LIENANO),
    ANOMAJ("Anomalies RTC mises à jour :\n", " - Nouvel état : "),
    LOTMAJ("Lots mis à jour :\n", " - Nouvel état : "),
    CLARITYINCONNU("Lots avec Clarity inconnu :\n", "- Clarity : "),
    SERVICESSANSRESP("Lots avec services sans responsable :\n", "- Service : "),
    APPLIOBSOLETE("Liste des composants avec un code application obsolète :\n", LIENAPP),
    APPLINONREF("Liste des composants avec une application non listée dans le référentiel :\n", LIENAPP),
    COMPOSANSAPP("Liste des composants sans application :\n", ""),
    COMPOPURGE("Liste des composants purgés :\n", "");   
    
    private String titre;
    private String liens;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeInfoMail(String titre, String liens)
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