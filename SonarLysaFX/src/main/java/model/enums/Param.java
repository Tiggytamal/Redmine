package model.enums;

import java.io.Serializable;

/**
 * Enumération des différents paramètres de l'application
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum Param implements Serializable, TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    FILTREDATASTAGE("Filtre DataStage"), 
    ABSOLUTEPATH("Chemin des fichiers"), 
    NOMFICHIER("Nom fichier de suivi"), 
    NOMFICHIERDATASTAGE("Nom fichier de suivi DataStage"),
    NOMFICHIERCOBOL("Nom fichier de suivi COBOL"),
    ABSOLUTEPATHHISTO("Chemin vers fichier d'historique"), 
    LIENSLOTS("Hyperliens vers lots Sonar"), 
    LIENSANOS("Hyperliens vers anomalie RTC"), 
    NOMQGDATASTAGE("Nom QualityGate Datastage"), 
    URLSONAR("Url serveur Sonar"), 
    URLRTC("Url serveur RTC Jazz"),
    RTCLOTCHC("Edition RTC pour lots CHC"), 
    IPMAIL("Adresse IP du serveur mail"),
    PORTMAIL("Port du serveur mail"),
    AQPMAIL("Adresse mail groupe AQP");

    private final String string;

    /*---------- CONSTRUCTEURS ----------*/

    private Param(String string)
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
