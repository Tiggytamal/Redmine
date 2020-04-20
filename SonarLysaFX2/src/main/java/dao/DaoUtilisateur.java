package dao;

import model.bdd.Utilisateur;

/**
 * Dao pour la classe {@link model.bdd.Utilisateur}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DaoUtilisateur extends AbstractMySQLDao<Utilisateur, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "utilisateurs";

    /*---------- CONSTRUCTEURS ----------*/

    DaoUtilisateur()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(Utilisateur t)
    {
        // Pas de traitement particulier
    }

    /**
     * Désactive un utilisateur en base pour la création des mails.
     * 
     * @param u
     *          Utilisateur à traiter.
     * @return
     *         Vrai si la persistance est ok.
     */
    public boolean desactiver(Utilisateur u)
    {
        u.setActive(false);
        return persist(u);
    }

    /**
     * Désactive un utilisateur en base pour la création des mails.
     * 
     * @param u
     *          Utilisateur à traiter.
     * @return
     *         Vrai si la persistance est ok.
     */
    public boolean activer(Utilisateur u)
    {
        u.setActive(true);
        return persist(u);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
