package control.task.action;

import java.util.List;

import dao.Dao;
import dao.DaoFactory;
import dao.DaoUtilisateur;
import model.bdd.Utilisateur;
import model.enums.ActionU;
import model.enums.Severity;
import utilities.FunctionalException;

/**
 * Classe de traitement des actions pour les anomalies RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionUtilisateurTask extends AbstractTraiterActionTask<Utilisateur, ActionU, Dao<Utilisateur, String>, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionUtilisateurTask(ActionU action, Utilisateur dq)
    {
        super(action, dq, ETAPES, TITRE, Utilisateur::getNom);
        dao = DaoFactory.getMySQLDao(Utilisateur.class);
    }

    public TraiterActionUtilisateurTask(ActionU action, List<Utilisateur> dqs)
    {
        super(action, dqs, ETAPES, TITRE, Utilisateur::getNom);
        dao = DaoFactory.getMySQLDao(Utilisateur.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void traitementAction(Utilisateur user)
    {
        switch (action)
        {
            case DESACTIVER:
                desactiver(user);
                break;

            case ACTIVER:
                activer(user);
                break;

            default:
                throw new FunctionalException(Severity.ERROR, "Action non prise en charge sur traitement composant : " + action);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Désactivation de l'utilisateur pour l'envoi de mails.
     * 
     * @param u
     *          Utilisateur à traiter.
     */
    private void desactiver(Utilisateur u)
    {
        ((DaoUtilisateur) dao).desactiver(u);
    }

    /**
     * Activation de l'utilisateur pour l'envoi de mails.
     * 
     * @param u
     *          Utilisateur à traiter.
     */
    private void activer(Utilisateur u)
    {
        ((DaoUtilisateur) dao).activer(u);
    }

    /*---------- ACCESSEURS ----------*/

}
