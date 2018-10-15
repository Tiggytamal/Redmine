package control.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.util.AssertException;

import application.Main;
import control.excel.ControlExtractVul;
import model.Vulnerabilite;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.OptionRecupCompo;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Issue;
import model.sonarapi.Parametre;
import utilities.Statics;
import utilities.Utilities;

/**
 * Tâche d'extraction des vulnérabilités Sonar du patrimoine
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerExtractVulnerabiliteTask extends AbstractTask
{

    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    private static final String TITRE = "Extraction vulnérabilités";

    private ControlExtractVul control;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerExtractVulnerabiliteTask(File file)
    {
        super(TypeVulnerabilite.values().length, TITRE);
        control = new ControlExtractVul(file);
    }

    /**
     * Constructeur pour les test à ne pas utiliser, lance une exception
     */
    public CreerExtractVulnerabiliteTask()
    {
        super(TypeVulnerabilite.values().length, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        creerExtract();
        return sauvegarde();
    }

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private void creerExtract()
    {
        // Création liste des noms des composants du patrimoine

        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            @SuppressWarnings("unchecked")
            List<Vulnerabilite> vulnerabilites = Utilities.recuperation(Main.DESER, List.class, "vulnera" + type.toString() + ".ser", () -> recupVulnerabilitesSonar(type));

            // Création de la feuille excel
            updateMessage("Traitement fichier Excel");
            updateProgress(0, 1);
            control.ajouterExtraction(vulnerabilites, type, this);
            etapePlus();
        }

        // Ecriture du fichier
        updateProgress(1, 1);
    }

    /**
     * Transforme une Issue de Sonar en objet Vulnerabilite pour Excel
     * 
     * @param issue
     * @param composant
     * @return
     */
    private Vulnerabilite convertIssueToVul(Issue issue, ComposantSonar composant)
    {
        Vulnerabilite retour = new Vulnerabilite();
        
        retour.setComposant(composant.getNom());
        retour.setStatus(issue.getStatus());
        retour.setDateCreation(issue.getCreationDate());
        retour.setSeverite(issue.getSeverity());
        retour.setMessage(issue.getMessage());
        retour.setAppli(composant.getAppli().getCode());
        retour.setLib(extractLib(retour.getMessage()));
        
        LotRTC lotRTC = composant.getLotRTC();

        if (lotRTC != null)
        {
            retour.setClarity(lotRTC.getProjetClarity().getCode());
            retour.setLot(lotRTC.getLot());
        }

        return retour;
    }

    /**
     * Récupère toutes les vulnérabilitès du patrimoine depuis Sonar pour un type particulier
     * 
     * @param type
     * @param composants
     * @return
     */
    private List<Vulnerabilite> recupVulnerabilitesSonar(TypeVulnerabilite type)
    {
        // Liste des composants du patrimoine
        Map<String, ComposantSonar> composants = recupererComposantsSonar(OptionRecupCompo.PATRIMOINE);
        
        // Affichage avancée
        baseMessage = "Vulnérabilitès " + type.getNomSheet() + Statics.NL;
        updateMessage("récupération des vulnérabilités dans SonarQube.");
        updateProgress(-1, -1);

        // Variables
        List<Vulnerabilite> retour = new ArrayList<>();
        int i = 0;

        // Paramètres
        List<Parametre> params = new ArrayList<>();
        params.add(new Parametre("severities", "CRITICAL, BLOCKER"));
        params.add(new Parametre("resolved", type.getBooleen()));
        params.add(new Parametre("tags", "cve"));
        params.add(new Parametre("types", "VULNERABILITY"));

        // Appel Sonar pour récupération données vulnérabilitès
        List<Issue> liste = api.getIssuesGenerique(params);
        int size = liste.size();

        String base = "Traitement des composants :\n";
        // Traitement de chaque issue pour récupérer le numéro de lot, et l'appli.
        for (Issue issue : liste)
        {
            // Nom du projet
            String clefProjet = issue.getProjet();

            // Affichage avancée
            updateMessage(base + clefProjet);
            updateProgress(i, size);
            i++;

            ComposantSonar composant = composants.get(clefProjet);

            if (composant == null)
            {
                LOGGER.warn(clefProjet + " n'existe pas dans la liste des composants.");
                continue;
            }

            // Conversion dans le format pour créer le fichier Excel
            retour.add(convertIssueToVul(issue, composant));
        }

        return retour;
    }

    /**
     * Extrait le nom de la bibliothèque depuis le message de l'anomalie
     * 
     * @param message
     * @return
     */
    private String extractLib(String message)
    {
        String retour = message.split(" \\||/")[0].replace("Filename: ", Statics.EMPTY);
        if (retour.contains(":"))
            return retour.split(":")[1];
        return retour;
    }

    /**
     * Sauvegarde du fichier
     * 
     * @return
     */
    private boolean sauvegarde()
    {
        return control.write();
    }
    /*---------- ACCESSEURS ----------*/
}
