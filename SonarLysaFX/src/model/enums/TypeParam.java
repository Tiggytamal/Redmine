package model.enums;

import java.io.Serializable;

/**
 * Enumération des différents paramètres de l'application
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeParam implements Serializable, TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    VERSIONS("Version"), 
    FILTREDATASTAGE("Filtre DataStage"), 
    ABSOLUTEPATH("Chemin des fichiers"), 
    NOMFICHIER("Nom fichier de suivi"), 
    NOMFICHIERDATASTAGE("Nom fichier de suivi datastage"), 
    ABSOLUTEPATHHISTO("Chemin vers fichier d'historique"), 
    LIENSLOTS("Hyperliens vers lots Sonar"), 
    LIENSANOS("Hyperliens vers anomalie RTC"), 
    NOMQGDATASTAGE("Nom QualityGate Datastage"), 
    URLSONAR("Url serveur Sonar"), 
    URLSONARTEST("Url Serveur Sonar de test"),
    URLRTC("Url serveur RTC Jazz");

    private final String string;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeParam(String string)
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
