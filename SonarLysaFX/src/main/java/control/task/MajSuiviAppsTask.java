package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import control.excel.ControlSuiviApps;
import control.excel.ExcelFactory;
import dao.DaoFactory;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.TypeColSuiviApps;

public class MajSuiviAppsTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Création Vue Datastage";
    private static final int ETAPES = 1;

    private ControlSuiviApps control;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviAppsTask()
    {
        super(ETAPES, TITRE);
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
        // Récupération des défauts qualités
        List<DefautQualite> dqs = DaoFactory.getDao(DefautQualite.class).readAll();
        Set<DefautAppli> dasATraiter = new HashSet<>();

        setBaseMessage("Mise à jour du Suivi des défauts applicatifs :");
        updateMessage("");

        for (DefautQualite dq : dqs)
        {
            // On ajoute à la liste de traitements tous les defauts applicatifs venant de défauts qualité non fermés
            if ((dq.getEtatDefaut() != EtatDefaut.ABANDONNE || dq.getEtatDefaut() != EtatDefaut.CLOS) && !dq.getDefautsAppli().isEmpty())
                dasATraiter.addAll(dq.getDefautsAppli());
        }

        control.majFeuilleDefaultsAppli(new ArrayList<>(dasATraiter), control.resetFeuilleDA());
        control.write();
        control.close();
        updateProgress(1, 1);
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
