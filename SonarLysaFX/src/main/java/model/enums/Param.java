package model.enums;

/**
 * Enumération des différents paramètres de l'application
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum Param implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    FILTREDATASTAGE("Filtre DataStage"), 
    FILTRECOBOL("Filtre COBOL"),
    ABSOLUTEPATH("Chemin des fichiers"), 
    NOMFICHIERJAVA("Nom fichier de suivi"), 
    NOMFICHIERDATASTAGE("Nom fichier de suivi DataStage"),
    NOMFICHIERCOBOL("Nom fichier de suivi COBOL"),
    NOMFICHIERAPPLI("Nom fichier d'extraction des applications"),
    ABSOLUTEPATHHISTO("Chemin vers fichier d'historique"), 
    LIENSLOTS("Hyperliens vers lots Sonar"), 
    LIENSANOS("Hyperliens vers anomalie RTC"), 
    NOMQGDATASTAGE("Nom QualityGate Datastage"), 
    URLSONAR("Url serveur Sonar"), 
    URLRTC("Url serveur RTC Jazz"),
    RTCLOTCHC("Edition RTC pour lots CHC"), 
    IPMAIL("Adresse IP du serveur mail"),
    PORTMAIL("Port du serveur mail"),
    AQPMAIL("Adresse mail groupe AQP"),
    NBREPURGE("Nbre de versions à garder purge");

    private final String nom;

    /*---------- CONSTRUCTEURS ----------*/

    private Param(String nom)
    {
        this.nom = nom;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public String getNom()
    {
        return nom;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
