package model.enums;

import java.io.Serializable;

public enum TypeColSuivi implements Serializable, TypeCol
{
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
    TRAITE("Traité", "colTraite"), 
    FILIERE("Filière", "colFil"), 
    MANAGER("Manager", "colManager"), 
    MATIERE("Matière", "colMatiere");

    private String string;
    private String nomCol;

    private TypeColSuivi(String string, String nomCol)
    {
        this.string = string;
        this.nomCol = nomCol;
    }

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