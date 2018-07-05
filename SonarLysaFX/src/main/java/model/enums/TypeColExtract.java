package model.enums;

import java.io.Serializable;

public enum TypeColExtract implements Serializable, TypeCol 
{
    SEVERITE("Severité", "colSeverity"), 
    STATUS("Status", "colStatus"),
    MESSAGE("Message", "colMess"),
    DATECREA("Date création", "colDateCrea"),
    LOT("Lot", "colLot"),
    CLARITY("Code Clarity", "colClarity"),
    APPLI("Appli", "colAppli"),
    COMPOSANT("Composant", "colComp");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColExtract(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public String getNomCol()
    {
        return nomCol;
    }

    @Override
    public String getValeur()
    {
        return valeur;
    }
}
