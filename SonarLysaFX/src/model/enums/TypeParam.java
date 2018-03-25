package model.enums;

import java.io.Serializable;

public enum TypeParam implements Serializable, TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    VERSIONS("Version"), FILTREDATASTAGE("Filtre DataStage"), ABSOLUTEPATH("Chemin des fichiers"), NOMFICHIER("Nom fichier de suivi"), NOMFICHIERDATASTAGE(
            "Nom fichier de suivi datastage"), ABSOLUTEPATHHISTO("chemin vers fichier d'historique"), LIENSLOTS("hyperliens vers lots Sonar"), LIENSANOS(
                    "hyperliens vers anomalie RTC"), NOMQGDATASTAGE("nom QualityGate Datastage"), URLSONAR("url serveur Sonar"), URLSONARTEST("url Serveur Sonar de test");

    private String string;

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
