package dao;

import model.bdd.ProjetMobileCenter;

/**
 * DAO pour la classe {@link model.bdd.ProjetMobileCenter}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public class DaoProjetMobileCenter extends AbstractMySQLDao<ProjetMobileCenter, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "projets_mobile";

    /*---------- CONSTRUCTEURS ----------*/

    DaoProjetMobileCenter()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(ProjetMobileCenter t)
    {
        // Pas d'implementation necessaire
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
