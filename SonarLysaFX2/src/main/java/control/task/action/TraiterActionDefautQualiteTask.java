package control.task.action;

import static utilities.Statics.EMPTY;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.mail.ControlMailOutlook;
import control.rest.ControlAppCATS;
import control.rest.SonarAPI;
import control.rtc.ControlRTC;
import control.task.maj.MajLotsRTCTask;
import dao.Dao;
import dao.DaoFactory;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.Utilisateur;
import model.enums.ActionDq;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import model.enums.EtatCodeAppli;
import model.enums.EtatDefaut;
import model.enums.Metrique;
import model.enums.ParamSpec;
import model.enums.Severity;
import model.rest.sonarapi.ComposantSonar;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de traitement des actions pour les défaut qualité.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterActionDefautQualiteTask extends AbstractTraiterActionTask<DefautQualite, ActionDq, Dao<DefautQualite, String>, String>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantage */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    private static final int ETAPES = 1;
    private static final String TITRE = "Traitement action";
    private ControlRTC controlRTC;

    private Optional<ButtonType> confirmation;
    private final CountDownLatch latch = new CountDownLatch(1);

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterActionDefautQualiteTask(ActionDq action, DefautQualite dq)
    {
        super(action, dq, ETAPES, TITRE, t -> t.getCompo().getNom());
        init();
    }

    public TraiterActionDefautQualiteTask(ActionDq action, List<DefautQualite> dqs)
    {
        super(action, dqs, ETAPES, TITRE, t -> t.getCompo().getNom());
        init();
    }

    private void init()
    {
        controlRTC = ControlRTC.getInstance();
        dao = DaoFactory.getMySQLDao(DefautQualite.class);
        confirmation = Optional.of(ButtonType.OK);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    protected final void traitementAction(DefautQualite dq)
    {
        switch (action)
        {
            case ABANDONNER:
                clotureAno(dq, EtatDefaut.ABANDONNE);
                majDq(dq);
                ajouterCommFermetureAno(dq);
                break;

            case OBSOLETE:
                clotureAno(dq, EtatDefaut.OBSOLETE);
                majDq(dq);
                ajouterCommFermetureAno(dq);
                break;

            case CLOTURER:
                clotureAno(dq, EtatDefaut.CLOS);
                majDq(dq);
                ajouterCommFermetureAno(dq);
                break;

            case CREER:
                creationAno(dq);
                majDq(dq);
                break;

            case RELANCER:
                relancerAno(dq);
                majDq(dq);
                break;

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
     * Clôture d'une anomalie RTC. Pas d'effet si l'anomalie est déjà clôturée ou si le défaut n'a pas d'anomalie.
     * 
     * @param dq
     *             DefautQualite à traiter.
     * @param etat
     *             Etat désiré du défaut.
     */
    private void clotureAno(DefautQualite dq, EtatDefaut etat)
    {
        boolean fermee = false;

        // S'il y a une anomalie et que celle-ci est déjà fermée, on passe le booléen à vrai
        if (dq.getNumeroAnoRTC() == 0 || Statics.ANOCLOSE.equals(dq.getEtatAnoRTC()))
            fermee = true;
        else if (dq.getNumeroAnoRTC() != 0)
            try
            {
                // Fermeture de l'anomalie et mise à jour du booléen selon réussite ou non de l'action
                fermee = controlRTC.fermerAnoRTC(dq.getNumeroAnoRTC());
            }
            catch (TeamRepositoryException e)
            {
                LOGPLANTAGE.error(EMPTY, e);
                fermee = false;
            }
        else
        {
            // Pas de traitement dans les autres cas.
        }

        // Si l'ano est bien fermée, on change l'état du défaut.
        if (fermee)
            dq.setEtatDefaut(etat);
    }

    /**
     * Ajout un commentaire à la fermeture d'une anomalie RTC. Pas d'efet si l'anomalie n'est pas déjà clôturée.
     * Le commentaire dépend de l'état du défaut qualité (obsolète, clos ou abandonné).
     * 
     * @param dq
     *           DefautQualite à traiter.
     */
    private void ajouterCommFermetureAno(DefautQualite dq)
    {
        // Ajout d'un commentaire que sur un défaut avec un anomalie close.
        if (dq.getNumeroAnoRTC() == 0 || (!EtatAnoRTC.CLOSE.getValeur().equals(dq.getEtatAnoRTC()) && !EtatAnoRTCProduit.CLOSE.getValeur().equals(dq.getEtatAnoRTC())))
            return;

        try
        {
            // Ajout du commentaire en cas de fermeture
            if (dq.getEtatDefaut() == EtatDefaut.OBSOLETE)
                controlRTC.ajoutercommentaireAno(dq.getNumeroAnoRTC(), Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOOBSOLETE));
            else if (dq.getEtatDefaut() == EtatDefaut.CLOS)
                controlRTC.ajoutercommentaireAno(dq.getNumeroAnoRTC(), Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOCLOSE));
            else if (dq.getEtatDefaut() == EtatDefaut.ABANDONNE)
                controlRTC.ajoutercommentaireAno(dq.getNumeroAnoRTC(), Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOABANDONNEE));
            else
                throw new TechnicalException(
                        "control.task.action.TraiterActionDefautQualiteTask.ajouterCommFermetureAno - incohérence défaut : numéro " + dq.getNumeroAnoRTC() + "état : " + dq.getEtatDefaut());
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
        }

    }

    /**
     * Création d'une anomalie RTC sur un défaut qualité et lancement d'outlook pour la création du mail.
     * Bloque si une anomalie non close est déjà présente.
     * Demande confirmation si une anomalie close est présente, car une nouvelle sera créée, et il n'y aura plus de lien avec l'ancienne.
     * 
     * @param dq
     *           DefautQaulite à traiter
     */
    private void creationAno(DefautQualite dq)
    {
        // Contrôle si une anomalie est déjà créée
        if (dq.getNumeroAnoRTC() != 0)
        {
            // Retour d'une erreur si l'anomalie n'est pas encore close
            if (!Statics.ANOCLOSE.equals(dq.getEtatAnoRTC()) && !Statics.ANOCLOSEPRODUIT.equals(dq.getEtatAnoRTC()))
                throw new FunctionalException(Severity.INFO,
                        "L'anomalie " + dq.getNumeroAnoRTC() + " (" + dq.getEtatAnoRTC() + ") est déjà présente sur ce défaut. Veuillez la clôturer avant d'en créer une nouvelle.");

            // Demande confirmation pour créer la nouvelle anomalie - Lancement dans un thread FX pour lancer la fenêtre de confirmation - Utilisation
            // CountDownLatch pour arrêter le thread principal
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.getDialogPane().getStylesheets().add(Statics.CSS);
                alert.setHeaderText(null);
                alert.setContentText("Une anomalie close (" + dq.getNumeroAnoRTC() + ") est déjà sur le défaut. Etes-vous sûr de vouloir en créer une nouvelle ?");
                confirmation = alert.showAndWait();
                latch.countDown();
            });
        }
        else
            latch.countDown();

        // Attente de la fin du Thread FX
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            LOGPLANTAGE.error(Statics.EMPTY, e);
            Thread.currentThread().interrupt();
        }

        // Test du retour si un bouton cancel a été pressé.
        if (confirmation.isPresent() && confirmation.get() != ButtonType.OK)
            return;

        // Création de l'anomalie avec réinitialisaiton de la date de relance si jamais une ancienne anomalie avait été relancée.
        int numeroAno = controlRTC.creerAnoRTC(dq);
        if (numeroAno != 0)
        {
            dq.setNumeroAnoRTC(numeroAno);
            dq.setDateCreation(LocalDate.now());
            dq.setDateRelance(null);
            dq.calculTraitee();

            Set<Utilisateur> utilisateurs = dq.getCreateurIssues();
            utilisateurs.add(dq.getLotRTC().getCpiProjet());
            new ControlMailOutlook().creerMailCreerAnoRTC(utilisateurs, dq);
        }
        dq.setEtatDefaut(EtatDefaut.ENCOURS);
    }

    /**
     * Relance d'une anomalie - Ne fait rien si le défaut n'a pas d'anomalie ou que l'anomalie est close.
     * Envoi d'un mail aprés la relance.
     * 
     * @param dq
     *           DefautQualite à traiter.
     */
    private void relancerAno(DefautQualite dq)
    {
        if (dq.getNumeroAnoRTC() == 0 || Statics.ANOCLOSE.equals(dq.getEtatAnoRTC()) || Statics.ANOCLOSEPRODUIT.equals(dq.getEtatAnoRTC()))
            return;

        Utilisateur deteneurAno = null;

        // Relance anomalie
        try
        {
            deteneurAno = controlRTC.relancerAno(dq.getNumeroAnoRTC());
            dq.setDateRelance(LocalDate.now());

        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
        }

        // Création mail

        // Ajout des créateurs d'anomalies
        Set<Utilisateur> utilisateurs = dq.getCreateurIssues();

        // Protection si jamais le détenteur de l'anomalie est nul - dans ce cas, on prend le cpi du lot
        if (deteneurAno == null)
            utilisateurs.add(dq.getLotRTC().getCpiProjet());
        else
            utilisateurs.add(deteneurAno);

        new ControlMailOutlook().creerMailRelanceAnoRTC(utilisateurs, dq);
    }

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
