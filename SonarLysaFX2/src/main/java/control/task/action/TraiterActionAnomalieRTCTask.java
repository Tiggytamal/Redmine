package control.task.action;

import java.util.List;

import dao.Dao;
import dao.DaoFactory;
import model.bdd.AnomalieRTC;
import model.enums.ActionA;
import model.enums.Severity;
import utilities.FunctionalException;

/**
 * Classe de traitement des actions pour les anomalies RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionAnomalieRTCTask extends AbstractTraiterActionTask<AnomalieRTC, ActionA, Dao<AnomalieRTC, Integer>, Integer>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionAnomalieRTCTask(ActionA action, AnomalieRTC ano)
    {
        super(action, ano, ETAPES, TITRE, AnomalieRTC::getMapIndex);
        dao = DaoFactory.getMySQLDao(AnomalieRTC.class);
    }

    public TraiterActionAnomalieRTCTask(ActionA action, List<AnomalieRTC> anos)
    {
        super(action, anos, ETAPES, TITRE, AnomalieRTC::getMapIndex);
        dao = DaoFactory.getMySQLDao(AnomalieRTC.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void traitementAction(AnomalieRTC objet)
    {
        switch (action)
        {
            case MODIFIER:
                sauvegarde(dao, objet);
                break;

            case SUPPRIMER:
                dao.delete(objet);
                break;

            default:
                throw new FunctionalException(Severity.ERROR, "Action non prise en charge sur traitement anomalie RTC : " + action);

        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
