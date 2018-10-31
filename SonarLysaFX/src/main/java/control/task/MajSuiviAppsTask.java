package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import control.excel.ControlSuiviApps;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import dao.DaoDefaultAppli;
import dao.DaoDefaultQualite;
import dao.DaoFactory;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
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

    private boolean majSuiviApps()
    {
        Map<String, DefautAppli> mapDefaults = dao.readAllMap();

        etapePlus();
        List<DefautAppli> das = control.recupDonneesDepuisExcel();
        
        int i = 0;
        int size= das.size();
        baseMessage = "Traitement Défaults Qualité :";

        for (DefautAppli da : das)
        {
            DefautAppli daBase = mapDefaults.get(da.getMapIndex());
            if (daBase != null)
            {
                daBase.setAction(da.getAction());
                gestionAction(da);
            }
            i++;
            calculTempsRestant(debut, i, size);
            updateProgress(i, size);
        }
        

        control.majFeuilleDefaultsAppli(new ArrayList<>(mapDefaults.values()), control.resetFeuilleDA());
        control.write();
        control.close();
        return false;
    }

    private void gestionAction(DefautAppli da)
    {
        if (da.getAction() == TypeAction.CREER && creerAnoRTC(da))
        {
            da.setAction(TypeAction.VIDE);
            da.setEtatDefaut(EtatDefaut.TRAITEE);
        }
    }

    private boolean creerAnoRTC(DefautAppli da)
    {

        DefautQualite dq = daoDq.recupEltParIndex(da.getCompo().getLotRTC().getLot());
        ControlRTC controlRTC = ControlRTC.INSTANCE;
        if (dq == null)
        {
//            dq = ModelFactory.getModel(DefaultQualite.class);
//            dq.setEtatDefault(EtatDefault.TRAITEE);
//            dq.setSecurite(false);
//            dq.setDateDetection(da.getDateDetection());
//            dq.setTypeDefault(TypeDefault.APPLI);
//            dq.setLotRTC(da.getCompo().getLotRTC());
//            da.setDefaultQualite(dq);
//
//            int numeroAno = controlRTC.creerAnoRTC(dq);
//            if (numeroAno != 0)
//            {
//                dq.setAction(TypeAction.VIDE);
//                dq.setNumeroAnoRTC(numeroAno);
//                dq.setDateCreation(LocalDate.now());
//                dq.calculTraitee();
//                return true;
//            }
        }
        else if (dq.getNumeroAnoRTC() == 0)
        {
//            dq.setTypeDefault(TypeDefault.MIXTE);
//            da.setDefaultQualite(dq);
//            dq.setNomCompoAppli(da.getCompo().getNom());
//            int numeroAno = controlRTC.creerAnoRTC(dq);
//            if (numeroAno != 0)
//            {
//                dq.setAction(TypeAction.VIDE);
//                dq.setNumeroAnoRTC(numeroAno);
//                dq.setDateCreation(LocalDate.now());
//                dq.calculTraitee();
//                return true;
//            }
        }
        else
        {
            dq.setTypeDefaut(TypeDefaut.MIXTE);
            da.setDefautQualite(dq);
            dq.setNomCompoAppli(da.getCompo().getNom());
            return controlRTC.ajoutAppliAnoRTC(da);

        }
        return false;
    }

    /*---------- ACCESSEURS ----------*/
}
