package control.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.excel.ControlExtract;
import control.sonar.SonarAPI;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.Vulnerabilite;
import model.enums.TypeMetrique;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Composant;
import model.sonarapi.Issue;
import model.sonarapi.Metrique;
import model.sonarapi.Parametre;
import utilities.Statics;

public class CreerExtractVulnerabiliteTask extends SonarTask
{

    /*---------- ATTRIBUTS ----------*/

    private SonarAPI api;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerExtractVulnerabiliteTask()
    {
        super(1);
        api = SonarAPI.INSTANCE;
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
        ControlExtract control = new ControlExtract(new File ("d:\\testExtract.xlsx"));
        
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            // Variables
            List<Vulnerabilite> vulnerabilites = new ArrayList<>();
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

            // Traitement de chaque issue pour récupérer le numéro de lot, et l'appli.
            for (Issue issue : liste)
            {
                // recherche si le composant est déjà dans le cache, sinon on fait un appel au webService Sonar puis on l'ajpoute au cache.
                String clefProjet = issue.getProjet();
                if (cacheComposants.keySet().contains(clefProjet))
                    composant = cacheComposants.get(clefProjet);
                else
                {
                    composant = api.getMetriquesComposant(issue.getProjet(), new String[] { TypeMetrique.LOT.toString(), TypeMetrique.APPLI.toString() });
                    cacheComposants.put(clefProjet, composant);
                }
                
                // Conversion dans le format pour créer le fichier Excel
                System.out.println(composant.getNom());
                vulnerabilites.add(convertIssueToVul(issue, composant));
                System.out.println(i++ + " - " + size);
            }
            
            // Création de la feuille excel
            control.ajouterExtraction(vulnerabilites, type);
        }

        // Ecriture du fichier        
        control.write();
        return true;
    }

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

        return retour;
    }
    /*---------- ACCESSEURS ----------*/
}
