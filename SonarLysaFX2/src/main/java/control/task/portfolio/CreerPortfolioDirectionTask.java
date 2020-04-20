package control.task.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response.Status;

import control.task.AbstractTask;
import dao.Dao;
import dao.DaoFactory;
import model.bdd.Application;
import model.enums.OptionGestionErreur;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.Statics;

/**
 * Tâche de création du portfolio regroupant les composants par code application par direction.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class CreerPortfolioDirectionTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final short ETAPES = 3;
    private static final String TITRE = "creation Portfolios Direction";

    private static final String DIRNAME = "Direction ";
    private static final String DIRKEY = "direction_";
    private static final String APPLINAME = "Application ";
    private static final String APPLIKEY = "view_application_";

    private static final Pattern PATTERNE = Pattern.compile("[éêè]");

    private Dao<Application, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioDirectionTask()
    {
        super(ETAPES, TITRE);
        startTimers();
        dao = DaoFactory.getMySQLDao(Application.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*----------- METHODES RPOTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Suppresion des anciens portfolios
        baseMessage = "Suppression portfolios éxistants :\n";
        updateMessage("");
        suppressionPortfolios();

        etapePlus();

        // Préparation de la liste des portfolios
        updateMessage("Prépration nouveaux portfolios.");
        Map<String, List<String>> map = preparerPortfolios();

        etapePlus();

        // Création des portfolios
        baseMessage = "Création nouveaux portfolios :\n";
        updateMessage(Statics.EMPTY);
        return creerPortfoliosDirection(map);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Suppression des portfolios existants.
     * 
     * @return
     *         Vrai si les portfolios ont bien été supprimés.
     */
    private boolean suppressionPortfolios()
    {
        List<ComposantSonar> liste = api.getObjetSonarParNomOuType("Direction", TypeObjetSonar.PORTFOLIO);

        int size = liste.size();
        int i = 0;
        boolean retour = true;

        for (ComposantSonar compo : liste)
        {
            if (api.supprimerObjetSonar(compo.getKey(), TypeObjetSonar.PORTFOLIO, OptionGestionErreur.OUI) != Status.NO_CONTENT)
                retour = false;

            // Affichage
            i++;
            updateMessage(compo.getNom());
            updateProgress(i, size);
        }

        return retour;
    }

    /**
     * Création de la map des code applications avec un code direction référencé.
     * 
     * @return
     *         La map des code application.
     */
    private Map<String, List<String>> preparerPortfolios()
    {
        // Map de retour
        Map<String, List<String>> retour = new HashMap<>();

        // Récupération liste des applications
        List<Application> liste = dao.readAll();

        for (Application appli : liste)
        {
            // On ne prend ne compte que les applications qui ont un code direction
            if (appli.getDirection().isEmpty())
                continue;
            retour.computeIfAbsent(appli.getDirection(), k -> new ArrayList<>()).add(appli.getCode());
        }

        return retour;
    }

    /**
     * Création des portfolios par Direction avec les portfolios des applications liées
     * 
     * @param map
     *            Map regroupant les noms de composants par code appli.
     * @return
     *         Vrai si les portfolios ont bien été créés.
     */
    private boolean creerPortfoliosDirection(Map<String, List<String>> map)
    {
        // Affichage
        int size = 0;
        int i = 0;
        for (List<String> liste : map.values())
        {
            size += liste.size();
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet())
        {
            // Préparation de la clef pour éviter les plantages des caratères spéciaux et création du portfolio
            String key = DIRKEY + entry.getKey().toLowerCase(Locale.FRANCE).replace(" ", "_").replace("ô", "o").replace("&", "et");
            key = PATTERNE.matcher(key).replaceAll("e");
            String directionString = DIRNAME + entry.getKey();
            ObjetSonar direction = new ObjetSonar(key, directionString, "Portfolio des applications de la direction " + entry.getKey(), TypeObjetSonar.PORTFOLIO);
            api.creerObjetSonar(direction);

            // Ajout des portfolios des applications
            for (String appli : entry.getValue())
            {
                api.ajouterSousPortfolio(direction, new ObjetSonar(APPLIKEY + appli, APPLINAME + appli, Statics.EMPTY, TypeObjetSonar.PORTFOLIO));

                // Affichage
                i++;
                updateMessage(directionString + " :\n" + APPLINAME + appli);
                updateProgress(i, size);
            }

            // Lancement du calcul du portfolio
            api.calculObjetSonar(direction);
        }

        return true;
    }
    /*---------- ACCESSEURS ----------*/
}
