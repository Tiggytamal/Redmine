package dao;

import model.bdd.Produit;

/**
 * DAO pour la classe {@link model.bdd.Produit}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public class DaoProduit extends AbstractMySQLDao<Produit, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "produits";

    /*---------- CONSTRUCTEURS ----------*/

    DaoProduit()
    {
        super(TABLE);
    }

    @Override
    protected void persistImpl(Produit t)
    {
        // Pas d'implementation necessaire
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
