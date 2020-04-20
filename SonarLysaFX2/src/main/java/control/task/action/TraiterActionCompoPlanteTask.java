package control.task.action;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import control.rest.SonarAPI;
import dao.Dao;
import dao.DaoFactory;
import model.bdd.ComposantErreur;
import model.enums.ActionCp;
import model.enums.OptionGestionErreur;
import model.enums.Severity;
import model.enums.TypeObjetSonar;
import utilities.FunctionalException;

/**
 * Classe de traitement des actions pour les composant en erreur dans SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionCompoPlanteTask extends AbstractTraiterActionTask<ComposantErreur, ActionCp, Dao<ComposantErreur, String>, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";

    private SonarAPI sonarAPI;

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionCompoPlanteTask(ActionCp action, ComposantErreur dq)
    {
        super(action, dq, ETAPES, TITRE, ComposantErreur::getNom);
        dao = DaoFactory.getMySQLDao(ComposantErreur.class);
        sonarAPI = SonarAPI.build();
    }

    public TraiterActionCompoPlanteTask(ActionCp action, List<ComposantErreur> dqs)
    {
        super(action, dqs, ETAPES, TITRE, ComposantErreur::getNom);
        dao = DaoFactory.getMySQLDao(ComposantErreur.class);
        sonarAPI = SonarAPI.build();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void traitementAction(ComposantErreur compo)
    {
        switch (action)
        {
            case PURGER:
                purger(compo);
                break;

            case CLOTURER:
                cloturer(compo);
                break;

            default:
                throw new FunctionalException(Severity.ERROR, "Action non prise en charge sur traitement composant erreur : " + action);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Purge d'un composant (dans SonarQube et dans la base de données).
     * 
     * @param compo
     *              Composant à purger.
     */
    private void purger(ComposantErreur compo)
    {
        if (sonarAPI.supprimerObjetSonar(compo.getKey(), TypeObjetSonar.PROJECT, OptionGestionErreur.OUI) == Status.NO_CONTENT)

        {
            compo.setaPurger(false);
            compo.setExiste(false);
            compo.ajouterPurge();
            sauvegarde(dao, compo);
        }
    }

    /**
     * Fermeture du suivi d'un composant.
     * 
     * @param compo
     *              Composant à traiter.
     */
    private void cloturer(ComposantErreur compo)
    {
        dao.delete(compo);
    }

    /*---------- ACCESSEURS ----------*/
}
