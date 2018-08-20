package model.enums;

/**
 * Colonnes du fichier de Suivi des anomalies
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeColSuivi  implements TypeColR
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
    DATERESOLUTION("Date de résolution", "colDateRes"),
    DATEMAJETAT("Date de mise à jour de l'état", "colDateMajEtat"),
    MATIERE("Matière", "colMatiere"),
    PROJETRTC("Projet RTC", "colProjetRTC"),
    ACTION("Action", "colAction"),
    NPC("Projet NPC", "colNpc");

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
