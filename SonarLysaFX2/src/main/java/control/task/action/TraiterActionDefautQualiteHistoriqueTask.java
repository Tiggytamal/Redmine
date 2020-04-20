package control.task.action;

import static utilities.Statics.EMPTY;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rest.ControlAppCATS;
import control.rest.SonarAPI;
import control.rtc.ControlRTC;
import control.task.maj.MajLotsRTCTask;
import dao.Dao;
import dao.DaoFactory;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.enums.ActionDqH;
import model.enums.EtatCodeAppli;
import model.enums.Metrique;
import model.enums.Severity;
import model.rest.sonarapi.ComposantSonar;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de traitement des actions pour les défaut qualité.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionDefautQualiteHistoriqueTask extends AbstractTraiterActionTask<DefautQualite, ActionDqH, Dao<DefautQualite, String>, String>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantage */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";
    private ControlRTC controlRTC;

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionDefautQualiteHistoriqueTask(ActionDqH action, DefautQualite dq)
    {
        super(action, dq, ETAPES, TITRE, t -> t.getCompo().getNom());
        init();
    }

    public TraiterActionDefautQualiteHistoriqueTask(ActionDqH action, List<DefautQualite> dqs)
    {
        super(action, dqs, ETAPES, TITRE, t -> t.getCompo().getNom());
        init();
    }

    private void init()
    {
        controlRTC = ControlRTC.getInstance();
        dao = DaoFactory.getMySQLDao(DefautQualite.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    protected final void traitementAction(DefautQualite dq)
    {
        switch (action)
        {
            case MAJ:
                majDq(dq);
                break;

            default:
                throw new FunctionalException(Severity.ERROR, "Action non prise en charge sur traitement défaut qualité : " + action);
        }

        sauvegarde(dao, dq);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Mise à jour des informations du Défaut Qualité.
     * Maj du lot RTC, de sinformations SonarQube et PIC.
     * 
     * @param dq
     *           DefautQualite à traiter.
     */
    private void majDq(DefautQualite dq)
    {
        // Mise à jour depuis RTC
        try
        {
            controlRTC.majDQ(dq);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("Impossible de mettre à jour le défaut qualité : " + dq.getIdBase(), e);
        }

        // Mise à jour de l'édition et du projet Clarity
        new MajLotsRTCTask(null).majLotRTC(dq.getLotRTC());

        // Mise à jour depuis SonarQube - Lignes de code - Sécurité - QualityGate
        ComposantBase compo = dq.getCompo();
        SonarAPI api = SonarAPI.build(dq.getCompo().getInstance());
        ComposantSonar compoMetriques = api.getMesuresComposant(compo.getKey(), compo.getBranche(), new String[]
        { Metrique.LDC.getValeur(), Metrique.SECURITY.getValeur(), Metrique.QG.getValeur() });
        if (compoMetriques != null)
        {
            compo.setLdc(compoMetriques.getValueMetrique(Metrique.LDC, "0"));
            compo.setSecurityRatingDepuisSonar(compoMetriques.getValueMetrique(Metrique.SECURITY, "0"));
            compo.setQualityGate(compoMetriques.getValueMetrique(Metrique.QG, "NONE"));
        }

        // Vérification de l'état du code appli s'il est en erreur
        if (dq.getEtatCodeAppli() == EtatCodeAppli.ERREUR && ControlAppCATS.INSTANCE.testApplicationExiste(compo.getAppli().getCode()))
        {
            compo.getAppli().setReferentiel(true);
            dq.setEtatCodeAppli(EtatCodeAppli.OK);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
