package dao;

import model.bdd.ComposantErreur;

/**
 * DAO pour la classe {@link model.bdd.ComposantErreur}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public class DaoComposantErreur extends AbstractMySQLDao<ComposantErreur, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "composants_plantes";

    /*---------- CONSTRUCTEURS ----------*/

    DaoComposantErreur()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(ComposantErreur t)
    {
        // Pas d'implementation aprticuliere
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
