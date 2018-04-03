package model.enums;

import java.io.Serializable;

public enum TypeColSuivi implements Serializable, TypeCol
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
    DATEDETECTION("Date de d�tection", "colDateDetec"),
    DATERELANCE("Date de relance", "colDateRel"), 
    MATIERE("Mati�re", "colMatiere");

    private String string;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColSuivi(String string, String nomCol)
    {
        this.string = string;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    
    @Override
    public String getNomCol()
    {
        return nomCol;
    }
}