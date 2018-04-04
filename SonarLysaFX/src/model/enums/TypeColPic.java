package model.enums;

import java.io.Serializable;

public enum TypeColPic implements Serializable, TypeCol
{

    LOT ("Lot", "colLot"),
    LIBELLE ("Libellé", "colLibelle"),
    CLARITY ("code Clarity", "colClarity"),
    CPI ("Chef de projet", "colCpi"),
    EDITION ("Edition", "colEdition"),
    NBCOMPOS ("Nbre Composants", "colNbCompos"),
    NBPAQUETS ("Nbre Paquets", "colNbpaquets"),
    BUILD ("Date 1er build", "colBuild"),
    DEVTU ("Livraison DEVTU", "colDevtu"),
    TFON ("Livraison TFON", "colTfon"),
    VMOE ("Livraison VMOE", "colVmoe"),
    VMOA ("Livraison VMOA", "colVmoa"),
    LIV ("Livraison édition", "colLiv");
    
    private String valeur;
    private String nomCol;
    
    /*---------- CONSTRUCTEURS ----------*/

    private TypeColPic(String valeur, String nomCol)
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
