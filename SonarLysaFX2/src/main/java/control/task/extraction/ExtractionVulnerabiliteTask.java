package control.task.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import control.excel.ControlExtractVul;
import control.excel.ExcelFactory;
import model.Vulnerabilite;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.enums.ColVul;
import model.enums.TypeVulnerabilite;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.ParamAPI;
import utilities.Statics;

/**
 * Teche d'extraction des vulnerabilités Sonar du patrimoine
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ExtractionVulnerabiliteTask extends AbstractExtractionTask<ControlExtractVul>
{

    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Extraction vulnerabilites";
    private static final int ETAPES = TypeVulnerabilite.values().length;
    private static final Pattern SPLIT = Pattern.compile(" \\||/");

    /*---------- CONSTRUCTEURS ----------*/

    public ExtractionVulnerabiliteTask(File file)
    {
        super(ETAPES, TITRE);
        control = ExcelFactory.getWriter(ColVul.class, file);
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
        // Création de la liste nulnérabilités par type de vulnérabilité.
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            List<Vulnerabilite> vulnerabilites = recupVulnerabilitesSonar(type);

            // Création de la feuille excel
            updateMessage("Traitement fichier Excel");
            updateProgress(0, 1);
            control.ajouterExtraction(vulnerabilites, type, this);
            etapePlus();
        }

        // Ecriture du fichier
        updateProgress(1, 1);

        return sauvegarde();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Transforme une Issue de Sonar en objet Vulnerabilite pour Excel
     * 
     * @param issue
     *              Issue à convertir.
     * @param compo
     *              Composant lié à l'anomalie.
     * @return
     *         La vulnérabilité avec les informations valorisées.
     */
    private Vulnerabilite convertIssueToVul(Issue issue, ComposantBase compo)
    {
        Vulnerabilite retour = new Vulnerabilite();

        retour.setComposant(compo.getNom());
        retour.setStatus(issue.getStatut());
        retour.setDateCreation(issue.getDateCreation().toString());
        retour.setSeverite(issue.getSeverite());
        retour.setMessage(issue.getMessage());
        retour.setAppli(compo.getAppli().getCode());
        retour.setLib(extractLib(retour.getMessage()));

        LotRTC lotRTC = compo.getLotRTC();

        if (lotRTC != null)
        {
            if (lotRTC.getProjetClarity() != null)
                retour.setClarity(lotRTC.getProjetClarity().getCode());
            retour.setLot(lotRTC.getNumero());
        }

        return retour;
    }

    /**
     * Récupère toutes les vulnerabilités du patrimoine depuis Sonar pour un type particulier
     * 
     * @param type
     *             Type de vulnérabilité : ouverte ou résolue.
     * @return
     *         La liste des vulnérabilités de ce type.
     */
    private List<Vulnerabilite> recupVulnerabilitesSonar(TypeVulnerabilite type)
    {
        // Liste des composants du patrimoine
        Map<String, ComposantBase> composants = recupererComposantsSonar();
        Map<String, ComposantBase> composantsByKey = new HashMap<>((int) (composants.size() * Statics.RATIOLOAD));
        for (ComposantBase compo : composants.values())
        {
            composantsByKey.put(compo.getKey(), compo);
        }

        // Affichage avancee
        baseMessage = "Vulnerabilites " + type.getNomSheet() + Statics.NL;
        updateMessage("recuperation des vulnerabilités dans SonarQube.");
        updateProgress(-1, -1);

        // Paramètres
        List<ParamAPI> params = new ArrayList<>();
        params.add(new ParamAPI("severities", "CRITICAL, BLOCKER"));
        params.add(new ParamAPI("resolved", type.getBooleen()));
        params.add(new ParamAPI("tags", "cve"));
        params.add(new ParamAPI("types", "VULNERABILITY"));

        // Appel Sonar pour récupération données vulnerabilites
        List<Issue> liste = api.getIssuesGenerique(params);
        int size = liste.size();

        // Variables
        List<Vulnerabilite> retour = new ArrayList<>((int) (size * Statics.RATIOLOAD));
        int i = 0;

        String base = "Traitement des composants :\n";

        // Traitement de chaque issue pour récupérer le numero de lot, et l'appli.
        for (Issue issue : liste)
        {
            // Nom du projet
            String clefProjet = issue.getProjet();

            // Affichage avancee
            updateMessage(base + clefProjet);
            updateProgress(i, size);
            i++;

            ComposantBase composant = composantsByKey.get(clefProjet);

            if (composant == null)
                continue;

            // Conversion dans le format pour créer le fichier Excel
            retour.add(convertIssueToVul(issue, composant));
        }

        return retour;
    }

    /**
     * Extrait le nom de la bibliotheque depuis le message de l'anomalie
     * 
     * @param message
     *                Message de l'anomalie à traiter.
     * @return
     *         Le nom de la bibliothèque.
     */
    private String extractLib(String message)
    {
        String retour = SPLIT.split(message)[0].replace("Filename: ", Statics.EMPTY);
        if (retour.contains(":"))
            return retour.split(":")[1];
        return retour;
    }
    /*---------- ACCESSEURS ----------*/
}
