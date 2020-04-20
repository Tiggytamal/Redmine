package control.task.action;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import control.rest.SonarAPI;
import dao.Dao;
import dao.DaoFactory;
import fxml.Menu;
import javafx.application.Platform;
import model.bdd.ComposantBase;
import model.enums.ActionC;
import model.enums.OptionGestionErreur;
import model.enums.Severity;
import model.enums.TypeObjetSonar;
import utilities.FunctionalException;

/**
 * Classe de traitement des actions pour les composants SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionCompoTask extends AbstractTraiterActionTask<ComposantBase, ActionC, Dao<ComposantBase, String>, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";
    private static final String IDQUALITYGATESANSAPPLI = "7";
    private static final String IDQUALITYGATESANSDETTE = "7";

    private SonarAPI sonarAPI;

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionCompoTask(ActionC action, ComposantBase dq)
    {
        super(action, dq, ETAPES, TITRE, ComposantBase::getNom);
        dao = DaoFactory.getMySQLDao(ComposantBase.class);
        sonarAPI = SonarAPI.build();
    }

    public TraiterActionCompoTask(ActionC action, List<ComposantBase> dqs)
    {
        super(action, dqs, ETAPES, TITRE, ComposantBase::getNom);
        dao = DaoFactory.getMySQLDao(ComposantBase.class);
        sonarAPI = SonarAPI.build();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void traitementAction(ComposantBase compo)
    {
        switch (action)
        {
            case PURGER:
                purger(compo);
                break;

            case SUPPCONTROLEAPPLI:
                supprimerControleAppli(compo);
                break;

            case SUPPCONTROLEDETTE:
                supprimerControleDette(compo);
                break;

            default:
                throw new FunctionalException(Severity.ERROR, "Action non prise en charge sur traitement composant : " + action);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Purge du composant dans la base de données et dans SonarQube.
     * 
     * @param compo
     *              Composant à traiter.
     */
    private void purger(ComposantBase compo)
    {
        Status status = sonarAPI.supprimerObjetSonar(compo.getKey(), TypeObjetSonar.PROJECT, OptionGestionErreur.OUI);
        if (status == Status.NO_CONTENT || status == Status.NOT_FOUND)
            dao.delete(compo);
        Platform.runLater(Menu::updateNbreLignes);
    }

    /**
     * Suppression du contrôle du code application pour le composant. (Changement de Quality Gate)
     * 
     * @param compo
     *              Composant à traiter.
     */
    private void supprimerControleAppli(ComposantBase compo)
    {
        compo.setControleAppli(false);
        sonarAPI.associerQualitygate(compo, IDQUALITYGATESANSAPPLI);
        sauvegarde(dao, compo);
    }

    /**
     * Suppression du contrôle de la dette technique pour le composant. (Changement de Quality Gate)
     * 
     * @param compo
     *              Composant à traiter.
     */
    private void supprimerControleDette(ComposantBase compo)
    {
        sonarAPI.associerQualitygate(compo, IDQUALITYGATESANSDETTE);
        sauvegarde(dao, compo);
    }

    /*---------- ACCESSEURS ----------*/

}
