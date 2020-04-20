package model.enums;

/**
 * Enumeration des différents paramètres de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum Param implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    FILTREDATASTAGE("Filtre DataStage", false), 
    FILTRECOBOL("Filtre COBOL", false),
    FILTREANDROID("Filtre Androed", false),
    FILTREIOS("Filtre iOS", false),
    FILTREANGULAR("Filtre Angular", false),
    ABSOLUTEPATHRAPPORT("Chemin vers rapports de traitement", false),
    REPSONAR("Chemin du quai de depet Sonar", false),
    LIENSCOMPOS("Suffixe url composants RTC", false), 
    LIENSRTC("Url vers projets jazz RTC", false),  
    URLSONAR("Url serveur Sonar", false), 
    URLSONARMC("Url serveur Sonar Mobile Center", false),
    URLREPACK("URL Webservice Repack", false),
    URLRTC("Url serveur RTC Jazz", false),
    URLAPIAPPCATS("Url API App CATS", false),
    URLAPPCATSAPPLI("Url Appli App CATS", false),
    URLMAPS("Url serveur MAPS", false),
    URLMAPSPRODUIT("Url MAPS Produits", false),
    NOMQGDATASTAGE("Nom QualityGate Datastage", false),
    RTCLOTCHC("Edition RTC pour lots CHC", false),
    FICHIERVERSION("Nom de fichier de gestion des versions", false);

    private final String nom;
    private boolean perso;

    /*---------- CONSTRUCTEURS ----------*/

    Param(String nom, boolean perso)
    {
        this.nom = nom;
        this.perso = perso;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public String getNom()
    {
        return nom;
    }
    
    public boolean isPerso()
    {
        return perso;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
