package control.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.excel.ControlExtractVul;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.Vulnerabilite;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Issue;
import model.sonarapi.Parametre;
import utilities.Statics;
import utilities.Utilities;

public class CreerExtractVulnerabiliteTask extends AbstractSonarTask
{

    /*---------- ATTRIBUTS ----------*/

    private ControlExtractVul control;
    private static final Logger LOGGER = LogManager.getLogger("complet-log");

    /*---------- CONSTRUCTEURS ----------*/

    public CreerExtractVulnerabiliteTask(File file)
    {
        super(TypeVulnerabilite.values().length);
        control = new ControlExtractVul(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerExtract();
    }    

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation        
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerExtract()
    {
        // Création liste des noms des composants du patrimoine
        List<String> nomsComposPatrimoine = new ArrayList<>();
        for (ComposantSonar compo : recupererComposantsSonar().values())
        {
            nomsComposPatrimoine.add(compo.getNom());
        }

        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            @SuppressWarnings("unchecked")
            List<Vulnerabilite> vulnerabilites = Utilities.recuperation(Main.DESER, List.class, "vulnera" + type.toString() + ".ser",
                    () -> recupVulnerabilitesSonar(type, nomsComposPatrimoine));

            // Création de la feuille excel
            updateMessage("Traitement fichier Excel");
            updateProgress(-1, -1);
            control.ajouterExtraction(vulnerabilites, type);
            etapePlus();
        }

        // Ecriture du fichier
        updateProgress(1, 1);
        control.write();
        return true;
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
        retour.setLot(composant.getLot());
        retour.setAppli(composant.getAppli());
        retour.setClarity(Statics.fichiersXML.getMapLotsRTC().computeIfAbsent(retour.getLot(), s -> ModelFactory.getModel(LotSuiviRTC.class)).getProjetClarity());
        retour.setLib(extractLib(retour.getMessage()));
        return retour;
    }

    /**
     * Récupère toutes les vulnérabilitès du patrimoine depuis Sonar pour un type particulier
     * 
     * @param type
     * @param nomsComposPatrimoine
     * @return
     */
    private List<Vulnerabilite> recupVulnerabilitesSonar(TypeVulnerabilite type, List<String> nomsComposPatrimoine)
    {
        // Affichage avancée
        String basetype = "Vulnérabilitès " + type.getNomSheet() + Statics.NL;
        updateMessage(basetype + "récupération des vulnérabilités dans SonarQube.");
        updateProgress(-1, -1);

        // Variables
        List<Vulnerabilite> retour = new ArrayList<>();
        int i = 0;
        Map<String, ComposantSonar> composants = Statics.fichiersXML.getMapComposSonar();

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
            updateMessage(basetype + base + clefProjet);
            updateProgress(i, size);
            i++;

            ComposantSonar composant = composants.get(clefProjet);

            if (composant == null)
            {
                LOGGER.warn(clefProjet + " n'existe pas dans la liste des composants.");
                continue;
            }

            // Vérification que le lot est bien dans les composants du patrimoine puis conversion dans le format pour créer le fichier Excel
            if (nomsComposPatrimoine.contains(composant.getNom()))
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
        String retour = message.split(" \\||/")[0].replace("Filename: ", "");
        if (retour.contains(":"))
            return retour.split(":")[1];
        return retour;
    }
    /*---------- ACCESSEURS ----------*/
}
