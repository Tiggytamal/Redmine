package control.task.portfolio;

import static control.rest.SonarAPI.KEY;
import static utilities.Statics.NL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import control.task.AbstractTask;
import model.bdd.ComposantBase;
import model.bdd.Edition;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.OptionGestionErreur;
import model.enums.Severity;
import model.enums.TypeEdition;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.FunctionalException;

/**
 * Teche permettant de créer les portfolios CHC et CDM depuis Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class CreerPortfolioCHCCDMTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final short ETAPES = 3;
    private static final int NBRESEMAINES = 52;
    private static final String TITRE = "creation vues Maintenance";

    private List<String> annees;
    private TypeEdition typeEdition;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioCHCCDMTask()
    {
        super(ETAPES, TITRE);
        startTimers();
    }

    public CreerPortfolioCHCCDMTask(List<String> annees, TypeEdition typeEdition)
    {
        this();
        annulable = false;
        if (annees == null || annees.isEmpty())
            throw new FunctionalException(Severity.ERROR, "Création task CreerVueCHCCDMTask sans liste d'annees");
        this.annees = annees;
        this.typeEdition = typeEdition;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        suppressionPortfoliosMaintenance(typeEdition, annees);

        Map<String, Set<String>> mapVues = preparerMapPortfoliosMaintenance();

        creerPortfoliosMaintenance(mapVues);

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Suppression du portfolio initial.
     * 
     * @param typeEdition
     *                    Type d'edition à supprimer.
     * 
     * @param annees
     *                    Liste des années à prendre en compte.
     */
    private void suppressionPortfoliosMaintenance(TypeEdition typeEdition, List<String> annees)
    {
        String baseClef;
        baseMessage = "Suppression des vues existantes :" + NL;
        int j = 1;
        long debut = System.currentTimeMillis();
        int size = NBRESEMAINES * annees.size();

        // On itere sur chacune des annees
        for (String annee : annees)
        {
            // preparation de la base de la clef
            if (typeEdition == TypeEdition.CDM)
                baseClef = "CHC_CDM" + annee;
            else
                baseClef = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i <= NBRESEMAINES; i++)
            {
                StringBuilder builder = new StringBuilder(baseClef).append("-S").append(String.format("%02d", i));
                String message = builder.toString();
                api.supprimerObjetSonar(builder.append(KEY).toString(), TypeObjetSonar.PORTFOLIO, OptionGestionErreur.NON);

                // Affichage
                j++;
                updateProgress(j, (long) size);
                calculTempsRestant(debut, j, size);
                updateMessage(message);
            }
        }
    }

    /**
     * Prepare la liste des vues CHC ou CDM en filtrant selon le type d'édition choisi.
     * 
     * @return La map des vues sous forme de HashSet pour ne pas avoir de doublon de lot. <br>
     *         <b>cle</b> : Edition - <b>valeur</b> : set des lot de l'edition
     */
    private Map<String, Set<String>> preparerMapPortfoliosMaintenance()
    {
        Map<String, Set<String>> retour = new HashMap<>();

        // Recuperatoin des composants en base
        List<ComposantBase> composants = recupererComposantsSonar(Matiere.JAVA, InstanceSonar.LEGACY);

        etapePlus();
        baseMessage = "Traitement des composants :\n";

        for (int i = 0; i < composants.size(); i++)
        {
            ComposantBase compo = composants.get(i);

            // MAJ progression
            updateMessage(compo.getNom());
            updateProgress(i, composants.size());

            // Verification qu'on a bien un numero de lot et que dans le fichier XML, l'édition du composant est presente
            if (compo.getLotRTC() != null && compo.getLotRTC().getEdition() != null)
            {
                Edition edition = compo.getLotRTC().getEdition();

                // Contrôle pour ne prendre que les CHC ou CDM selon le booleen
                if (edition.getTypeEdition() != typeEdition)
                    continue;

                // Ajout à la map et initialisation HashSet au besoin
                retour.computeIfAbsent(edition.getNom(), k -> new HashSet<>()).add(compo.getKey());
            }
        }

        return retour;
    }

    /**
     * Création des portfolios dans SonarQube.
     * 
     * @param mapPortfoliosACreer
     *                      Map des portfolios
     */
    private void creerPortfoliosMaintenance(Map<String, Set<String>> mapPortfoliosACreer)
    {
        // Calcul du nombere total d'objets dans la map
        int sizeComplete = 0;
        for (Set<String> compos : mapPortfoliosACreer.values())
        {
            sizeComplete += compos.size();
        }

        // Affichage
        etapePlus();
        int i = 0;
        long debut = System.currentTimeMillis();

        for (Map.Entry<String, Set<String>> entry : mapPortfoliosACreer.entrySet())
        {
            ObjetSonar parent = new ObjetSonar(entry.getKey() + KEY, entry.getKey(), null, TypeObjetSonar.PORTFOLIO);
            api.creerObjetSonar(parent);
            baseMessage = "Création des vues :\n" + entry.getKey();
            updateMessage("");

            for (String compo : entry.getValue())
            {
                api.ajouterSousProjet(parent, compo);
                i++;

                // MAJ progression
                calculTempsRestant(debut, i, sizeComplete);
                updateMessage(NL + "ajout lot " + compo);
                updateProgress(i, sizeComplete);
            }
            
            api.calculObjetSonar(parent);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
