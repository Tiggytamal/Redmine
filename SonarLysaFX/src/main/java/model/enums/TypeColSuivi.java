package model.enums;

/**
 * Colonnes du fichier de Suivi des d�faults de qualit�
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public enum TypeColSuivi  implements TypeColR
{
    /*---------- ATTRIBUTS ----------*/

    DIRECTION("Direction", "colDir"), 
    DEPARTEMENT("D�partement", "colDepart"),
    SERVICE("Service", "colService"), 
    RESPSERVICE("Responsable Service", "colResp"),
    CLARITY("Code Clarity", "colClarity"),
    LIBELLE("Libell� projet", "colLib"), 
    CPI("Chef de projet du lot", "colCpi"), 
    EDITION("Edition", "colEdition"), 
    LOT("Num�ro du lot", "colLot"), 
    ENV("Etat du lot", "colEnv"), 
    ANOMALIE("Num�ro anomalie", "colAno"), 
    ETAT("Etat de l'anomalie", "colEtat"),
    SECURITE("S�curit�", "colSec"), 
    REMARQUE("Remarque", "colRemarque"), 
    VERSION("Version", "colVer"), 
    DATECREATION("Date de cr�ation", "colDateCrea"), 
    DATEREOUV("Date de r�ouverture", "colDateReouv"),
    DATEMEPPREV("Date MEP prev.", "colDateMepPrev"),
    DATEDETECTION("Date de d�tection", "colDateDetec"),
    DATERELANCE("Date de relance", "colDateRel"),
    DATERESOLUTION("Date de r�solution", "colDateRes"),
    DUREEANO("Dur�e de l'anomalie", "colDureeAno"),
    DATEMAJETAT("Date de mise � jour de l'�tat", "colDateMajEtat"),
    MATIERE("Mati�re", "colMatiere"),
    TYPE("Type", "colType"),
    PROJETRTC("Projet RTC", "colProjetRTC"),
    ACTION("Action", "colAction"),
    PRODUIT("Produit", "colProduit");

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
