package model.enums;

/**
 * Colonnes du fichiers des editions Pic
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum ColEdition implements ColR
{
    LIBELLE ("Libelle", "colLib"),
    VERSION ("Numero de version", "colNumero"),
    COMMENTAIRE("Commentaire", "colComment"),
    SEMAINE("Semaine", "colSemaine"),
    TYPE("Type de changement", "colType"),
    EDITION("Edition majeure", "colEdition");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    ColEdition(String valeur, String nomCol)
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
