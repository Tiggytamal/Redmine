package model.enums;

import java.io.Serializable;

/**
 * Colonnes du fichiers des éditions Pic
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeColEdition implements Serializable, TypeColR  
{
    LIBELLE ("Libellé", "colLib"),
    VERSION ("Numero de version", "colVersion");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColEdition(String valeur, String nomCol)
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
