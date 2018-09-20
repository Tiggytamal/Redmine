package control.task;

import static utilities.Statics.NL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.DaoEdition;
import model.ComposantSonar;
import model.Edition;
import model.enums.CHCouCDM;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Tâche permettant de créer les vues CHC et CDM depuis Sonar
 * 
 * @author ETP8137 - Grégoire MAthon
 * @since 1.0
 * 
 */
public class CreerVueCHCCDMTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final short ETAPES = 3;
    private static final long NBRESEMAINES = 52;
    private static final String TITRE = "création vues Maintenance";
    
    private List<String> annees;
    private CHCouCDM chccdm;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueCHCCDMTask()
    {
        super(ETAPES, TITRE);
    }

    public CreerVueCHCCDMTask(List<String> annees, CHCouCDM chccdm)
    {
        super(ETAPES, TITRE);
        annulable = false;
        if (annees == null || annees.isEmpty())
            throw new FunctionalException(Severity.ERROR, "Création task CreerVueCHCCDMTask sans liste d'années");
        this.annees = annees;
        this.chccdm = chccdm;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueCHCouCDM();
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueCHCouCDM()
    {
        // Traitement depuis le fichier XML
        suppressionVuesMaintenance(chccdm, annees);

        Map<String, Edition> editions = recupererEditions(annees);

        Map<String, Set<String>> mapVues = preparerMapVuesMaintenance(editions);

        creerVuesMaintenance(mapVues);

        return true;
    }

    /**
     * @param cdm
     * @param annees
     */
    private void suppressionVuesMaintenance(CHCouCDM chccdm, List<String> annees)
    {
        String base;
        String baseMessage = "Suppression des vues existantes :" + NL;
        int j = 1;
        // On itère sur chacune des annèes
        for (String annee : annees)
        {
            // préparation de la base de la clef
            if (chccdm == CHCouCDM.CDM)
                base = "CHC_CDM" + annee;
            else
                base = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i <= NBRESEMAINES; i++)
            {
                StringBuilder builder = new StringBuilder(base).append("-S").append(String.format("%02d", i));
                String message = builder.toString();
                api.supprimerProjet(builder.append("Key").toString(), false);
                updateMessage(baseMessage + message);
                updateProgress(j, NBRESEMAINES * annees.size());
                j++;
            }
        }
    }

    /**
     * Prépare la liste des vues CHC ou CDM depuis le fichier XML en filtrant selon le type d'édition choisi.
     * 
     * @param mapEditions
     *            Map des éditions provenant de récupérer Edition <br/>
     *         <b>clé</b> = numéro édition XX.YY.ZZ.AA - <b>valeur</b> = CHC(_CDM)YYYY-Sww
     *         
     * @return La map des vues sous forme de HashSet pour ne pas avoir de doublon de lot. <br>
     *          <b>clé</b> : Edition - <b>valeur</b> : set des lot de l'édition 
     */
    private Map<String, Set<String>> preparerMapVuesMaintenance(Map<String, Edition> mapEditions)
    {
        Map<String, Set<String>> retour = new HashMap<>();

        Map<String, List<ComposantSonar>> mapProjets = recupererComposantsSonarVersion(Matiere.JAVA);

        // Transfert de la map en une liste avec tous les projets
        List<ComposantSonar> tousLesProjets = new ArrayList<>();
        for (List<ComposantSonar> compos : mapProjets.values())
        {
            tousLesProjets.addAll(compos);
        }

        etapePlus();

        for (int i = 0; i < tousLesProjets.size(); i++)
        {
            ComposantSonar compo = tousLesProjets.get(i);

            // MAJ progression
            updateMessage("Traitement des composants :" + NL + compo.getNom());
            updateProgress(i, tousLesProjets.size());

            // Vérification qu'on a bien un numéro de lot et que dans le fichier XML, l'édition du composant est présente
            if (!compo.getLot().isEmpty() && mapEditions.containsKey(compo.getEdition()))
            {
                String keyCHC = mapEditions.get(compo.getEdition()).getNom();

                // Contrôle pour ne prendre que les CHC ou CDM selon le booléen
                if (!controle(chccdm, keyCHC))
                    continue;

                // AJout à la map et initialisation HashSet au besoin
                retour.computeIfAbsent(keyCHC, k -> new HashSet<>()).add(compo.getLot());
            }
        }

        return retour;
    }

    /**
     * 
     * @param chccdm
     *          Type d'édition à prendre en compte
     * @param keyCHC
     *          Valeur de l'édition - CHC(_CDM)YYYY-Sww
     * @return
     *         vrai si la clef contient CDM pour le CDM ou si la clef ne contient pas CDM pour les CHC.
     *      
     */
    private boolean controle(CHCouCDM chccdm, String keyCHC)
    {
        return (chccdm == CHCouCDM.CDM && keyCHC.contains("CDM")) || (chccdm == CHCouCDM.CHC && !keyCHC.contains("CDM"));
    }

    /**
     * 
     * @param mapVuesACreer
     */
    private void creerVuesMaintenance(Map<String, Set<String>> mapVuesACreer)
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
     * @param annees <br/>
     *            Liste des annèes à prendre en compte (année en cours et/ou annèe suivante et/ou annèe précedente
     *            
     * @return La map provenant du fichier Excel avec suppression des annèe non désirées. <br/>
     *         <b>clé</b> = numéro édition XX.YY.ZZ.AA - <b>valeur</b> = CHC(_CDM)YYYY-Sww
     */
    private Map<String, Edition> recupererEditions(List<String> annees)
    {
        Map<String, Edition> retour = new DaoEdition().readAllMap();

        // On itère sur la HashMap pour retirer tous les éléments qui ne sont pas des annèes selectionnées
        for (Iterator<Edition> iter = retour.values().iterator(); iter.hasNext();)
        {
            boolean ok = false;
            Edition edition = iter.next();
            for (String annee : annees)
            {
                if (edition.getNom().contains(annee))
                    ok = true;
            }
            if (!ok)
                iter.remove();
        }
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
