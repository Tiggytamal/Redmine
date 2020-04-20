package fxml.dialog;

import java.time.LocalDate;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.rtc.ControlRTC;
import control.task.maj.MajLotsRTCTask;
import dao.DaoComposantBase;
import dao.DaoFactory;
import dao.DaoLotRTC;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.ModelFactory;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import utilities.DateHelper;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Fenêtre permettant de créer et d'neregistrer un DefautQualité
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class AjouterDefautDialog extends AbstractAjouterDialog<DefautQualite, String>
{
    /*---------- ATTRIBUTS ----------*/

    // Nodes
    private TextField numeroAnoField;
    private TextField numeroLotField;
    private TextField nomComposant;
    private TextField remarques;
    private DatePicker dateDetection;
    private DatePicker dateRelance;
    private DatePicker dateMEPField;

    // Données
    private LotRTC lot;
    private ComposantBase compo;
    private String etatAnoRTC;
    private LocalDate dateCreation;
    private LocalDate dateReso;
    private LocalDate dateMEP;

    // Controlleurs
    private ControlRTC controlRTC;
    private DaoComposantBase daoCompo;
    private DaoLotRTC daoLot;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Initialisation de la fenêtre avec ajout de chaque Node nécessaire.
     */
    public AjouterDefautDialog()
    {
        super("Créer Nouveau Défaut Qualité");

        etatAnoRTC = Statics.EMPTY;

        // Numéro d'anomalie
        numeroAnoField = creerIntegerField("numeroAnoField", "6 chiffres", "Numéro de l'anomalie RTC", "Numéro à 6 chiffres de l'anomalie RTC");

        // Numéro du lot
        numeroLotField = creerIntegerField("numeroLotField", "6 chiffres", "Numéro du lot RTC", "Numéro à 6 chiffres du lot RTC");

        // Nom du composant
        nomComposant = creerTextField("nomComposant", "Nom du composant");

        // Remarques
        remarques = creerTextField("remarques", "Remarques");

        // Date détection
        dateDetection = creerDateField("dateDetection", "Date détection");

        // Date relance
        dateRelance = creerDateField("dateRelance", "Date relance");

        // Date mise en production
        dateMEPField = creerDateField("dateMiseEnProd", "Date mise en production");

        controlRTC = ControlRTC.build();
        daoCompo = DaoFactory.getMySQLDao(ComposantBase.class);
        daoLot = DaoFactory.getMySQLDao(LotRTC.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected DefautQualite retourObjet()
    {
        DefautQualite retour = ModelFactory.build(DefautQualite.class);
        retour.setNumeroAnoRTC(Integer.parseInt(numeroAnoField.getText()));
        retour.setLotRTC(lot);
        retour.setCompo(compo);
        retour.setRemarque(remarques.getText());
        retour.setDateDetection(dateDetection.getValue());
        retour.setDateRelance(dateRelance.getValue());
        retour.setDateMepPrev(dateMEP);
        retour.setDateReso(dateReso);
        retour.setDateCreation(dateCreation);
        retour.setEtatAnoRTC(etatAnoRTC);
        retour.initMapIndex();

        return retour;
    }

    @Override
    protected boolean controle()
    {
        boolean retour = true;
        donnesIncorrectes = Statics.EMPTY;

        // Contrôle lot RTC
        if (numeroLotField.getText().isEmpty())
        {
            retour = false;
            donnesIncorrectes += "Le lot n'est pas renseigné.\n";
        }
        else if (!controleLotRTC())
            retour = false;
        else
        {
            // Pas de traitement dans les autres cas
        }

        // Contrôle anomalie
        if (!controleAno())
            retour = false;
        else
        {
            // Pas de traitement dans les autres cas
        }

        // Contrôle composant
        if (nomComposant.getText().isEmpty())
        {
            retour = false;
            donnesIncorrectes += "Le composant n'est pas renseigné.\n";
        }
        else
        {
            compo = daoCompo.recupCompoByNom(nomComposant.getText());

            if (compo == null)
            {
                retour = false;
                donnesIncorrectes += "Le composant est inconnu.\n";
            }
        }

        // Contrôle dates, il faut que les dates de création de de détection ne soient pas nulles et que celle de détection soit avant la création
        if (dateCreation == null || dateDetection == null || dateCreation.isBefore(dateDetection.getValue()))
        {
            retour = false;
            donnesIncorrectes += "La date de détection doit renseignée être avant la date de création.\n";
        }

        // Si la date de mise ne production est inconnue, on met 2099 par défaut.
        if (dateMEPField.getValue() == null)
            dateMEP = Statics.DATEINCO2099;
        else
            dateMEP = dateMEPField.getValue();

        return retour;
    }

    private boolean controleLotRTC()
    {
        lot = daoLot.recupEltParIndex(numeroLotField.getText());

        // Si le lot existe pas dans la base de donnée, on va chercher dans RTC
        if (lot == null)
        {
            IWorkItem wi;
            try
            {
                wi = controlRTC.recupWorkItemDepuisId((Integer) numeroLotField.getTextFormatter().getValue());
                
                if (wi == null)
                {
                    donnesIncorrectes += "Le lot est inconnu.\n";
                    return false;
                }
                else
                {
                    lot = controlRTC.creerLotSuiviRTCDepuisItem(wi);
                    new MajLotsRTCTask(null, null).majLotRTC(lot);
                }
            }
            catch (TeamRepositoryException e)
            {
                throw new TechnicalException("Méthode view.dialog.AjouterDefautDialog.controleLotRTC : Erreur appel RTC", e);                
            }
        }
        return true;
    }

    private boolean controleAno()
    {
        if (numeroAnoField.getText().isEmpty())
        {
            donnesIncorrectes += "Le numéro de l'anomalie n'est pas renseigné.\n";
            return false;
        }
        else
        {
            IWorkItem wi;
            try
            {
                wi = controlRTC.recupWorkItemDepuisId((Integer) numeroAnoField.getTextFormatter().getValue());

                if (wi != null)
                {

                    etatAnoRTC = controlRTC.recupEtatElement(wi);
                    dateCreation = DateHelper.convert(LocalDate.class, wi.getCreationDate());
                    dateReso = DateHelper.convert(LocalDate.class, wi.getResolutionDate());
                    return true;
                }
                else
                {
                    donnesIncorrectes += "Le numéro de l'anomalie est inconnu.\n";
                    return false;
                }

            }
            catch (TeamRepositoryException e)
            {
                throw new TechnicalException("Méthode view.dialog.AjouterDefautDialog.controleAno : Erreur appel RTC", e);
            }
        }
    }

    /*---------- ACCESSEURS ----------*/
}
