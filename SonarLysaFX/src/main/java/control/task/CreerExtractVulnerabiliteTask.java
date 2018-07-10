package control.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import application.Main;
import control.excel.ControlExtract;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.Vulnerabilite;
import model.enums.TypeMetrique;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Composant;
import model.sonarapi.Issue;
import model.sonarapi.Metrique;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.Utilities;

public class CreerExtractVulnerabiliteTask extends SonarTask
{

    /*---------- ATTRIBUTS ----------*/
    
    private File file;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerExtractVulnerabiliteTask(File file)
    {
        super(TypeVulnerabilite.values().length);
        this.file = file;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerExtract();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerExtract() throws InvalidFormatException, IOException
    {
        ControlExtract control = new ControlExtract(new File(file.getPath()));
        
        // Création liste des noms des composants du patrimoine
        List<String> nomsComposPatrimoine = new ArrayList<>();       
        for (Projet projet : recupererComposantsSonar().values())
        {
            nomsComposPatrimoine.add(projet.getNom());
        }

        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            @SuppressWarnings("unchecked")
            List<Vulnerabilite> vulnerabilites = Utilities.recuperation(Main.DESER, List.class, "vulnera" + type.toString() + ".ser", () -> recupVulnerabilitesSonar(type, nomsComposPatrimoine));
            
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
    private Vulnerabilite convertIssueToVul(Issue issue, Composant composant)
    {
        Vulnerabilite retour = new Vulnerabilite();
        retour.setComposant(composant.getNom());
        retour.setStatus(issue.getStatus());
        retour.setDateCreation(issue.getCreationDate());
        retour.setSeverite(issue.getSeverity());
        retour.setMessage(issue.getMessage());
        retour.setLot(composant.getMapMetriques().computeIfAbsent(TypeMetrique.LOT, t -> new Metrique(TypeMetrique.LOT, null)).getValue());
        retour.setAppli(composant.getMapMetriques().computeIfAbsent(TypeMetrique.APPLI, t -> new Metrique(TypeMetrique.APPLI, null)).getValue());
        retour.setClarity(Statics.fichiersXML.getLotsRTC().computeIfAbsent(retour.getLot(), s -> ModelFactory.getModel(LotSuiviRTC.class)).getProjetClarity());
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
        Map<String, Composant> cacheComposants = new HashMap<>();
        Composant composant;
        


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
            updateProgress(i++, size);
            
            // recherche si le composant est déjà dans le cache, sinon on fait un appel au webService Sonar puis on l'ajoute au cache. 
            if (cacheComposants.keySet().contains(clefProjet))
                composant = cacheComposants.get(clefProjet);
            else
            {
                composant = api.getMetriquesComposant(issue.getProjet(), new String[] { TypeMetrique.LOT.toString(), TypeMetrique.APPLI.toString() });
                cacheComposants.put(clefProjet, composant);
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
        String retour =  message.split(" \\||/")[0].replace("Filename: ", "");
        if (retour.contains(":"))
            return retour.split(":")[1];
        return retour;
    }
    /*---------- ACCESSEURS ----------*/
}