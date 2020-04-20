package dao;

import model.bdd.Produit;
import model.bdd.Solution;

/**
 * DAO pour la classe {@link model.bdd.Solution}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public class DaoSolution extends AbstractMySQLDao<Solution, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "solutions";

    /*---------- CONSTRUCTEURS ----------*/

    DaoSolution()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(Solution t)
    {
        persistSousObjet(Produit.class, t.getProduit());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
