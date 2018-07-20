package model.enums;

import java.io.Serializable;

/**
 * Colonnes du fichier des projets Clarity
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeColClarity implements Serializable, TypeColR
{
    /*---------- ATTRIBUTS ----------*/

    ACTIF("Actif","colActif"),
    CLARITY("Code projet","colClarity"),
    LIBELLE("Libellé projet","colLib"),
    CPI("Chef de projet","colCpi"),
    EDITION("Edition","colEdition"),
    DIRECTION("Direction","colDir"),
    DEPARTEMENT("Département","colDepart"),
    SERVICE("Service","colService");
        
    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColClarity(String valeur, String nomCol)
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