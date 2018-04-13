package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.enums.TypeMetrique;
import sonarapi.model.Composant;
import sonarapi.model.Metrique;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Tâche permettant de créer les vues CHC et CDM depuis Sonar
 * 
 * @author ETP8137 - Grégoire MAthon
 * @since  1.0
 */
public class CreerVueCHCCDMTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private List<String> annees;
    private boolean cdm;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueCHCCDMTask(List<String> annees, boolean cdm)
    {
        super(3);
        annulable = false;
        if (annees == null || annees.isEmpty())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Création task CreerVueCHCCDMTask sans liste d'années");
        this.annees = annees;
        this.cdm = cdm;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueCHCouCDM();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueCHCouCDM()
    {
        // Traitement depuis le fichier XML
        suppressionVuesMaintenance(cdm, annees);

        creerVueMaintenance(recupererEditions(annees));
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
        int j = 1;
        // On itère sur chacune des annèes
        for (String annee : annees)
        {
            // préparation de la base de la clef
            if (cdm)
                base = "CHC_CDM" + annee;
            else
                base = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i < 53; i++)
            {
                StringBuilder builder = new StringBuilder(base).append("-S").append(String.format("%02d", i));
                String message = builder.toString();
                api.supprimerProjet(builder.append("Key").toString(), false);
                updateMessage(baseMessage + message);
                updateProgress(j++, 52l * annees.size());
            }
        }
    }

    /**
     * Crée les vues CHC ou CDM depuis Sonar et le fichier XML. {@code true} pour les vues CDM et {@code false} pour les vues CHC
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

        etapePlus();

        for (int i = 0; i < tousLesProjets.size(); i++)
        {
            Projet projet = tousLesProjets.get(i);

            // Récupération de l'édition du composant sous forme numérique xx.yy.zz.tt et du numéro de lot
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { TypeMetrique.EDITION.toString(), TypeMetrique.LOT.toString() });

            // MAJ progression
            updateMessage("Traitement des composants :" + NL + composant.getNom());
            updateProgress(i, tousLesProjets.size());

            // Récupération depuis la map des métriques du numéro de lot et du status de la Quality Gate
            Map<TypeMetrique, Metrique> metriques = composant.getMapMetriques();
            String lot = metriques.get(TypeMetrique.LOT).getValue();
            String edition = metriques.get(TypeMetrique.EDITION).getValue();

            // Vérification qu'on a bien un numéro de lot et que dans le fichier XML, l'édition du composant est présente
            if (lot != null && !lot.isEmpty() && edition != null && mapEditions.keySet().contains(edition))
            {
                String keyCHC = mapEditions.get(edition);

                // Contrôle pour ne prendre que les CHC ou CDM selon le booléen
                if (controle(cdm, keyCHC))
                    continue;

                // AJout à la map et création de la clef au besoin
                if (!mapVuesACreer.keySet().contains(keyCHC))
                    mapVuesACreer.put(keyCHC, new HashSet<>());
                mapVuesACreer.get(keyCHC).add(lot);
            }
        }

        creerVues(mapVuesACreer);
    }

    private boolean controle(boolean cdm, String keyCHC)
    {
        return !cdm && keyCHC.contains("CDM") || cdm && !keyCHC.contains("CDM");
    }

    private void creerVues(Map<String, Set<String>> mapVuesACreer)
    {
        String base = "Création des vues :" + NL;
        etapePlus();

        // Calcul du nombere total d'objets dans la map
        int sizeComplete = 0;
        for (Set<String> lots : mapVuesACreer.values())
        {
            sizeComplete += lots.size();
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

                // MAJ progression
                updateMessage(baseVue + NL + "ajout lot " + lot);
                updateProgress(i, sizeComplete);
            }
        }
    }

    /**
     * Récupération des éditions CDM et CHC depuis les fichiers Excel, selon le type de vue et les annèes
     * 
     * @param cdm
     * @param annees
     * @return
     */
    private Map<String, String> recupererEditions(List<String> annees)
    {
        Map<String, String> retour = fichiersXML.getMapEditions();

        // On itère sur la HashMap pour retirer tous les éléments qui ne sont pas des annèes selectionnées
        for (Iterator<String> iter = retour.values().iterator(); iter.hasNext();)
        {
            boolean ok = false;
            String value = iter.next();
            for (String annee : annees)
            {
                if (value.contains(annee))
                    ok = true;
            }
            if (!ok)
                iter.remove();
        }
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}