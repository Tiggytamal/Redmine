package control;

import static utilities.Statics.fichiersXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.parent.SonarTask;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class CreerVueCHCCDMTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private List<String> annees;
    private boolean cdm;
    private File file;

    /*---------- CONSTRUCTEURS ----------*/
    
    public CreerVueCHCCDMTask(List<String> annees, File file)
    {
        super();
        initAnnees(annees);
        initFile(file);

    }
    
    public CreerVueCHCCDMTask(String pseudo, String mdp, List<String> annees, File file)
    {
        super(pseudo, mdp);
        initAnnees(annees);
        initFile(file);
    }
    
    public CreerVueCHCCDMTask(List<String> annees, boolean cdm)
    {
        super();
        initAnnees(annees);
        this.cdm = cdm;
    }
    
    public CreerVueCHCCDMTask(String pseudo, String mdp, List<String> annees, boolean cdm)
    {
        super(pseudo, mdp);
        initAnnees(annees);
        this.cdm = cdm;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueCHCouCDM();
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private void initAnnees(List<String> annees)
    {
        if (annees == null || annees.isEmpty())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Création task CreerVueCHCCDMTask sans liste d'années");
        this.annees = annees;
    }
    
    private void initFile(File file)
    {
        if (file == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Création task CreerVueCHCCDMTask sans fichier de réference");
        this.file = file;
    }
    
    private boolean creerVueCHCouCDM() throws InvalidFormatException, IOException
    {
        if (file == null)
        {
            // Traitement depuis le fichier XML
            suppressionVuesMaintenance(cdm, annees);
            creerVueMaintenance(cdm);
        }
        else
        {
            suppressionVuesMaintenance(true, annees);
            
            // récupération des informations du fichier Excel
            ControlPic control = new ControlPic(file);
            Map<String, List<Vue>> map = control.recupLotsCHCCDM();

            // Création des nouvelles vues
            for (Map.Entry<String, List<Vue>> entry : map.entrySet())
            {
                String key = entry.getKey();
                Vue vue = creerVue(key, key, "Vue de l'edition " + key, true);
                api.ajouterSousVues(entry.getValue(), vue);
            }
        }


        return true;
    }
    
    /**
     * @param cdm
     * @param annees
     */
    private void suppressionVuesMaintenance(boolean cdm, List<String> annees)
    {
        String base;
        
        // On itère sur chacune des annèes
        for (String annee : annees)
        {
            if (isCancelled())
                break;
            
            // préparation de la base de la clef
            if (cdm)
                base = "CHC_CDM" + annee;
            else
                base = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i < 53; i++)
            {
                api.supprimerProjet(new StringBuilder(base).append("-S").append(String.format("%02d", i)).append("Key").toString(), false);
            }
        }
    }
    
    /**
     * Crée les vues CHC ou CDM depuis Sonar et le fichier XML. {@code true} pour les vues CDM et {@code false} pour les
     * vues CHC
     * 
     * @param cdm
     */
    private void creerVueMaintenance(boolean cdm)
    {
        // Récupération du fichier XML des editions
        Map<String, String> mapEditions;
        if (cdm)
            mapEditions = fichiersXML.getMapCDM();
        else
            mapEditions = fichiersXML.getMapCHC();

        Map<String, Set<String>> mapVuesACreer = new HashMap<>();

        Map<String, List<Projet>> mapProjets = recupererComposantsSonarVersion(null);

        // Transfert de la map en une liste avec tous ls projets
        List<Projet> tousLesProjets = new ArrayList<>();
        for (List<Projet> projets : mapProjets.values())
        {
            tousLesProjets.addAll(projets);
        }

        for (Projet projet : tousLesProjets)
        {
            // Récupération de l'édition du composant sou forme numérique xx.yy.zz.tt et du numéro de lot
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "edition", "lot" });

            // Récupération depuis la map des métriques du numéro de lot et du status de la Quality Gate
            Map<String, String> metriques = composant.getMapMetriques();
            String lot = metriques.get("lot");
            String edition = metriques.get("edition");

            // Vérification qu'on a bien un numéro de lot et que dans le fichier XML, l'édition du composant est
            // présente
            if (lot != null && !lot.isEmpty() && edition != null && mapEditions.keySet().contains(edition))
            {
                String keyCHC = mapEditions.get(edition);
                if (!mapVuesACreer.keySet().contains(keyCHC))
                    mapVuesACreer.put(keyCHC, new HashSet<>());
                mapVuesACreer.get(keyCHC).add(lot);
            }
        }

        for (Map.Entry<String, Set<String>> entry : mapVuesACreer.entrySet())
        {
            Vue parent = new Vue(entry.getKey() + "Key", entry.getKey());
            api.creerVue(parent);
            for (String lot : entry.getValue())
            {
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), parent);
            }
        }
    }
    
    /*---------- ACCESSEURS ----------*/
    
}
