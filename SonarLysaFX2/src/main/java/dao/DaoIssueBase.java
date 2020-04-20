package dao;

import model.bdd.IssueBase;
import model.bdd.Utilisateur;

/**
 * DAO pour la classe {@link model.bdd.IssueBase}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
public class DaoIssueBase extends AbstractMySQLDao<IssueBase, String>
{

    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "issues_sonar";

    /*---------- CONSTRUCTEURS ----------*/

    DaoIssueBase()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(IssueBase user)
    {
        // Presistance application liee
        persistSousObjet(Utilisateur.class, user.getUtilisateur());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
