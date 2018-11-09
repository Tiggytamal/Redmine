package model.enums;

/**
 * Enumération des différents paramètres de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum Param implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    FILTREDATASTAGE("Filtre DataStage"), 
    FILTRECOBOL("Filtre COBOL"),
    FILTREANDROID("Filtre Androïd"),
    FILTREIOS("Filtre iOS"),
    ABSOLUTEPATH("Chemin des fichiers"), 
    NOMFICHIERJAVA("Nom fichier de suivi"), 
    NOMFICHIERANDROID("Nom fichier de suivi Android"),
    NOMFICHIERIOS("Nom fichier de suivi iOS"),
    NOMFICHIERDATASTAGE("Nom fichier de suivi DataStage"),
    NOMFICHIERCOBOL("Nom fichier de suivi COBOL"),
    NOMFICHIERAPPLI("Nom fichier d'extraction des applis"),
    NOMFICHIERPBAPPLI("Nom fichier pbs des codes appli"),
    ABSOLUTEPATHHISTO("Chemin vers fichiers d'historique"), 
    ABSOLUTEPATHRAPPORT("Chemin vers rapports de traitement"),
    LIENSLOTS("Hyperliens vers lots Sonar"), 
    LIENSANOS("Hyperliens vers anomalie RTC"), 
    NOMQGDATASTAGE("Nom QualityGate Datastage"), 
    URLSONAR("Url serveur Sonar"), 
    URLREPACK("URL Webservice Repack"),
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
