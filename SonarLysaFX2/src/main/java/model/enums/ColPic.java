package model.enums;

/**
 * Colonnes des extractions de la Pic
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 2.0
 * 
 */
public enum ColPic implements ColR
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
    
    private final String valeur;
    private final String nomCol;
    
    /*---------- CONSTRUCTEURS ----------*/

    ColPic(String valeur, String nomCol)
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
