package model.enums;

/**
 * Colonnes du fichiers des projets NPC
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum TypeColProduit implements TypeColR 
{
    /*---------- ATTRIBUTS ----------*/

    NOM("Nom Projet", "colNom"),
    GROUPE("Groupe", "colGroupe");

    private String valeur;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColProduit(String valeur, String nomCol)
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
