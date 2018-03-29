package control;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    
    public CreerVueCHCCDMTask(File file)
    {
        super();
        initFile(file);
    }
    
    public CreerVueCHCCDMTask(String pseudo, String mdp, File file)
    {
        super(pseudo, mdp);
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
        if (file == null)
            suppressionVuesMaintenance(cdm, annees);
        else
            suppressionVuesMaintenance(true, annees);

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
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Cr�ation task CreerVueCHCCDMTask sans liste d'ann�es");
        this.annees = annees;
    }
    
    private void initFile(File file)
    {
        if (file == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Cr�ation task CreerVueCHCCDMTask sans fichier de r�ference");
        this.file = file;
    }
    
    private boolean creerVueCHCouCDM() throws InvalidFormatException, IOException
    {
        if (file == null)
        {
            // Traitement depuis le fichier XML
            suppressionVuesMaintenance(cdm, annees);
            creerVueMaintenance(recupererEditions(cdm, annees));
        }
        else
        {
            suppressionVuesMaintenance(true, annees);
            
            // r�cup�ration des informations du fichier Excel
            ControlPic control = new ControlPic(file);
            Map<String, List<Vue>> map = control.recupLotsCHCCDM();

            // Cr�ation des nouvelles vues
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
        String baseMessage = "Suppression des vues existantes :" + NL;
        
        // On it�re sur chacune des ann�es
        for (String annee : annees)
        {
            if (isCancelled())
                break;
            
            // pr�paration de la base de la clef
            if (cdm)
                base = "CHC_CDM" + annee;
            else
                base = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i < 53; i++)
            {
                if (isCancelled())
                    break;
                StringBuilder builder = new StringBuilder(base).append("-S").append(String.format("%02d", i));
                String message = builder.toString();
                api.supprimerProjet(builder.append("Key").toString(), false);
                updateMessage(baseMessage + message);
                updateProgress(i, 52);
            }
        }
    }
    
    /**
     * Cr�e les vues CHC ou CDM depuis Sonar et le fichier XML. {@code true} pour les vues CDM et {@code false} pour les vues CHC
     * 
     * @param cdm
     */
    private void creerVueMaintenance(Map<String, String> mapEditions)
    {
        Map<String, Set<String>> mapVuesACreer = new HashMap<>();

        Map<String, List<Projet>> mapProjets = recupererComposantsSonarVersion(null);

        // Transfert de la map en une liste avec tous les projets
        List<Projet> tousLesProjets = new ArrayList<>();
        for (List<Projet> projets : mapProjets.values())
        {
            tousLesProjets.addAll(projets);
        }

        for (int i = 0; i < tousLesProjets.size(); i++)
        {
            Projet projet = tousLesProjets.get(i);
            // R�cup�ration de l'�dition du composant sous forme num�rique xx.yy.zz.tt et du num�ro de lot
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "edition", "lot" });

            //MAJ progression
            updateMessage("Traitement des composants :" + NL + composant.getNom());
            updateProgress(i, tousLesProjets.size());
            
            // R�cup�ration depuis la map des m�triques du num�ro de lot et du status de la Quality Gate
            Map<String, String> metriques = composant.getMapMetriques();
            String lot = metriques.get("lot");
            String edition = metriques.get("edition");

            // V�rification qu'on a bien un num�ro de lot et que dans le fichier XML, l'�dition du composant est pr�sente
            if (lot != null && !lot.isEmpty() && edition != null && mapEditions.keySet().contains(edition))
            {
                String keyCHC = mapEditions.get(edition);
                if (!mapVuesACreer.keySet().contains(keyCHC))
                    mapVuesACreer.put(keyCHC, new HashSet<>());
                mapVuesACreer.get(keyCHC).add(lot);
            }
        }

        String base = "Cr�ation des vues :" + NL;
        
        // Calcul du nombere total d'objets dans la map
        int sizeComplete = 0;
        for (Set<String> lots : mapVuesACreer.values())
        {
            sizeComplete+= lots.size();
        }
        
        int i = 0;
        for (Map.Entry<String, Set<String>> entry : mapVuesACreer.entrySet())
        {
            Vue parent = new Vue(entry.getKey() + "Key", entry.getKey());
            api.creerVue(parent);
            String baseVue = base + entry.getKey();
            updateMessage(baseVue);
            for (String lot : entry.getValue())
            {
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), parent);
                i++;
                
                //MAJ progression
                updateMessage(baseVue + NL + "ajout lot " + lot);
                updateProgress(i, sizeComplete);
            }
        }
    }
    
    /**
     * R�cup�ration des �ditions CDM et CHC depuis les fichiers Excel, selon le type de vue et les ann�es
     * 
     * @param cdm
     * @param annees
     * @return
     */
    private Map<String, String> recupererEditions(boolean cdm, List<String> annees)
    {
        Map<String, String> retour;
        
        if (cdm)
            retour = fichiersXML.getMapCDM();
        else
            retour = fichiersXML.getMapCHC();
        
        // On it�re sur la HashMap pour retirer tous les �l�ments qui ne sont pas des ann�es selectionn�es
        for (Iterator<Map.Entry<String, String>>  iter = retour.entrySet().iterator(); iter.hasNext();)
        {
            boolean ok = false;
            for (String annee : annees)
            {
                if (iter.next().getValue().contains(annee))
                    ok = true;
            }
            if (!ok)
                iter.remove();
        }
        return retour;
    }
    
    /*---------- ACCESSEURS ----------*/    
}