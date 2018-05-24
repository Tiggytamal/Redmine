package model.enums;

import java.io.Serializable;

/**
 * Colonnes du fichier de Suivi des anomalies
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeColSuivi  implements Serializable, TypeCol
{
    /*---------- ATTRIBUTS ----------*/

    DIRECTION("Direction", "colDir"), 
    DEPARTEMENT("Département", "colDepart"),
    SERVICE("Service", "colService"), 
    RESPSERVICE("Responsable Service", "colResp"),
    CLARITY("Code Clarity", "colClarity"),
    LIBELLE("Libellé projet", "colLib"), 
    CPI("Chef de projet du lot", "colCpi"), 
    EDITION("Edition", "colEdition"), 
    LOT("Numéro du lot", "colLot"), 
    ENV("Etat du lot", "colEnv"), 
    ANOMALIE("Numéro anomalie", "colAno"), 
    ETAT("Etat de l'anomalie", "colEtat"), 
    SECURITE("Sécurité", "colSec"), 
    REMARQUE("Remarque", "colRemarque"), 
    VERSION("Version", "colVer"), 
    DATECREATION("Date de création", "colDateCrea"), 
    DATEDETECTION("Date de détection", "colDateDetec"),
    DATERELANCE("Date de relance", "colDateRel"), 
    MATIERE("Matière", "colMatiere"),
    PROJETRTC("Projet RTC", "colProjetRTC"),
    ACTION("Action", "colAction");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColSuivi(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String getValeur()
    {
        return valeur;
    }
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
}