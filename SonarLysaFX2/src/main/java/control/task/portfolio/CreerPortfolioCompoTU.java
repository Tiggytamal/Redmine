package control.task.portfolio;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import control.task.AbstractTask;
import dao.DaoFactory;
import dao.DaoStatistiqueCompo;
import model.bdd.StatistiqueCompo;
import model.enums.OptionGestionErreur;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.ObjetSonar;

public class CreerPortfolioCompoTU extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    public static final String KEY = "couvertureGlobaleKey";
    public static final String NOM = "Couverture Globale";
    
    private static final String TITRE = "Création Portfolio Couverture Globale";
    private static final int ETAPES = 2;

    private DaoStatistiqueCompo dao;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioCompoTU()
    {
        super(ETAPES, TITRE);
        dao = DaoFactory.getMySQLDao(StatistiqueCompo.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'implémentation
    }

    @Override
    protected Boolean call() throws Exception
    {
        // Suppresion des anciens portfolios
        baseMessage = "Suppression de l'ancien portfolio...";
        updateMessage("");
        if (!suppressionPortfolio())
        {
            updateMessage("Plantage ! - Voir Logs");
            return Boolean.FALSE;
        }

        updateMessage("OK");
        etapePlus();

        ObjetSonar portfolio = new ObjetSonar(KEY, NOM, "Portfolio regroupant tous les composant avec une couveture de TU", TypeObjetSonar.PORTFOLIO);
        api.creerObjetSonar(portfolio);

        // Affichage
        List<String> compos = dao.recupTousCompos();
        int i = 0;
        int size = compos.size();
        baseMessage = "Ajouts des composants dans le nouveau portfolio :\n";

        // Ajout des portfolios des applications
        for (String compo : compos)
        {
            api.ajouterSousProjet(portfolio, compo);

            // Affichage
            i++;
            updateMessage(compo);
            updateProgress(i, size);
        }

        // Lancement du calcul du portfolio
        api.calculObjetSonar(portfolio);

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean suppressionPortfolio()
    {
        List<ComposantSonar> liste = api.getObjetSonarParNomOuType(KEY, TypeObjetSonar.PORTFOLIO);

        for (ComposantSonar compo : liste)
        {
            if (api.supprimerObjetSonar(compo.getKey(), TypeObjetSonar.PORTFOLIO, OptionGestionErreur.OUI) != Status.NO_CONTENT)
                return false;
        }
        return true;
    }
    /*---------- ACCESSEURS ----------*/
}
