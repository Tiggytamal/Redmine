package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.excel.ControlSuiviApps;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import dao.DaoDefaultAppli;
import dao.DaoDefaultQualite;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.enums.EtatAnoRTC;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeColSuiviApps;
import model.enums.TypeDefaut;

public class MajSuiviAppsTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Création Vue Datastage";
    private static final int ETAPES = 2;

    private DaoDefaultAppli dao;
    private DaoDefaultQualite daoDq;
    private ControlSuiviApps control;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviAppsTask()
    {
        super(ETAPES, TITRE);
        dao = DaoFactory.getDao(DefautAppli.class);
        daoDq = DaoFactory.getDao(DefautQualite.class);
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA);
        control = ExcelFactory.getReader(TypeColSuiviApps.class, new File(name));
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement pour cette classe
    }

    @Override
    protected Boolean call() throws Exception
    {
        return majSuiviApps();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majSuiviApps() throws TeamRepositoryException
    {
        Map<String, DefautAppli> mapDefaults = dao.readAllMap();

        etapePlus();
        List<DefautAppli> das = control.recupDonneesDepuisExcel();

        int i = 0;
        int size = das.size();
        long debut = System.currentTimeMillis();
        baseMessage = "Traitement Défaults Qualité :";
        updateMessage("");

        // Récupération eds informations depuis le fichier Excel pour le traitement
        for (DefautAppli da : das)
        {
            DefautAppli daBase = mapDefaults.get(da.getMapIndex());
            if (daBase != null)
            {
                daBase.setAction(da.getAction());
                gestionAction(daBase);
            }
            i++;
            calculTempsRestant(debut, i, size);
            updateProgress(i, size);
        }

        control.majFeuilleDefaultsAppli(new ArrayList<>(mapDefaults.values()), control.resetFeuilleDA());
        control.write();
        control.close();
        dao.persist(mapDefaults.values());
        return false;
    }

    private void gestionAction(DefautAppli da) throws TeamRepositoryException
    {
        if (da.getAction() == TypeAction.CREER && creerAnoRTC(da))
        {
            da.setAction(TypeAction.VIDE);
            da.setEtatDefaut(EtatDefaut.TRAITE);
        }
    }

    private boolean creerAnoRTC(DefautAppli da) throws TeamRepositoryException
    {
        DefautQualite dq = daoDq.recupEltParIndex(da.getCompo().getLotRTC().getLot());
        ControlRTC controlRTC = ControlRTC.INSTANCE;
        boolean retour = false;

        // Action si pas de défaut Qualite : On crée le DefautQualité puis l'anomalie
        if (dq == null)
        {
            dq = ModelFactory.getModel(DefautQualite.class);
            dq.setSecurite(false);
            dq.setDateDetection(da.getDateDetection());
            dq.setTypeDefaut(TypeDefaut.APPLI);
            dq.setLotRTC(da.getCompo().getLotRTC());

            int numeroAno = controlRTC.creerAnoRTC(dq);
            if (numeroAno != 0)
            {
                dq.setAction(TypeAction.VIDE);
                dq.setNumeroAnoRTC(numeroAno);
                dq.setDateCreation(LocalDate.now());
                dq.calculTraitee();
                controlRTC.ajoutAppliAnoRTC(da);
                retour = true;
            }
        }

        // Action si défaut déjà créée mais pas d'anomalie RTC : On crée l'anomalie
        else if (dq.getNumeroAnoRTC() == 0)
        {
            dq.setNomCompoAppli(da.getCompo().getNom());
            int numeroAno = controlRTC.creerAnoRTC(dq);
            if (numeroAno != 0)
            {
                dq.setAction(TypeAction.VIDE);
                dq.setNumeroAnoRTC(numeroAno);
                dq.setDateCreation(LocalDate.now());
                dq.calculTraitee();
                controlRTC.ajoutAppliAnoRTC(da);
                retour = true;
            }
        }

        // Action si l'anomalie RTC n'est pas déjà close : On rajoute un commentaire pour le code application
        else if (!dq.getEtatRTC().equals(EtatAnoRTC.CLOSE.getValeur()) && controlRTC.ajoutAppliAnoRTC(da))
            retour = true;

        // Action si l'anomalie est close : On rouvre l'anomalie et on rajoute le commentaire
        else if (controlRTC.reouvrirAnoRTC(dq.getNumeroAnoRTC()) && controlRTC.ajoutAppliAnoRTC(da))
            retour = true;

        // Traitement général à chaque cas
        if (dq.getTypeDefaut() != TypeDefaut.APPLI)
            dq.setTypeDefaut(TypeDefaut.MIXTE);
        da.setDefautQualite(dq);
        dq.setEtatDefaut(EtatDefaut.TRAITE);
        dq.setNomCompoAppli(da.getCompo().getNom());

        // Si tout est bon, on enlève l'action de création
        if (retour)
            da.setAction(TypeAction.VIDE);

        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
