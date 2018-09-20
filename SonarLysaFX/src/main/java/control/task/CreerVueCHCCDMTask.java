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
 * T�che permettant de cr�er les vues CHC et CDM depuis Sonar
 * 
 * @author ETP8137 - Gr�goire MAthon
 * @since 1.0
 * 
 */
public class CreerVueCHCCDMTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final short ETAPES = 3;
    private static final long NBRESEMAINES = 52;
    private static final String TITRE = "cr�ation vues Maintenance";
    
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
            throw new FunctionalException(Severity.ERROR, "Cr�ation task CreerVueCHCCDMTask sans liste d'ann�es");
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
        // On it�re sur chacune des ann�es
        for (String annee : annees)
        {
            // pr�paration de la base de la clef
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
     * Pr�pare la liste des vues CHC ou CDM depuis le fichier XML en filtrant selon le type d'�dition choisi.
     * 
     * @param mapEditions
     *            Map des �ditions provenant de r�cup�rer Edition <br/>
     *         <b>cl�</b> = num�ro �dition XX.YY.ZZ.AA - <b>valeur</b> = CHC(_CDM)YYYY-Sww
     *         
     * @return La map des vues sous forme de HashSet pour ne pas avoir de doublon de lot. <br>
     *          <b>cl�</b> : Edition - <b>valeur</b> : set des lot de l'�dition 
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

            // V�rification qu'on a bien un num�ro de lot et que dans le fichier XML, l'�dition du composant est pr�sente
            if (!compo.getLot().isEmpty() && mapEditions.containsKey(compo.getEdition()))
            {
                String keyCHC = mapEditions.get(compo.getEdition()).getNom();

                // Contr�le pour ne prendre que les CHC ou CDM selon le bool�en
                if (!controle(chccdm, keyCHC))
                    continue;

                // AJout � la map et initialisation HashSet au besoin
                retour.computeIfAbsent(keyCHC, k -> new HashSet<>()).add(compo.getLot());
            }
        }

        return retour;
    }

    /**
     * 
     * @param chccdm
     *          Type d'�dition � prendre en compte
     * @param keyCHC
     *          Valeur de l'�dition - CHC(_CDM)YYYY-Sww
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
        String base = "Cr�ation des vues :" + NL;
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
     * R�cup�ration des �ditions CDM et CHC depuis les fichiers Excel, selon le type de vue et les ann�es
     * 
     * @param annees <br/>
     *            Liste des ann�es � prendre en compte (ann�e en cours et/ou ann�e suivante et/ou ann�e pr�cedente
     *            
     * @return La map provenant du fichier Excel avec suppression des ann�e non d�sir�es. <br/>
     *         <b>cl�</b> = num�ro �dition XX.YY.ZZ.AA - <b>valeur</b> = CHC(_CDM)YYYY-Sww
     */
    private Map<String, Edition> recupererEditions(List<String> annees)
    {
        Map<String, Edition> retour = new DaoEdition().readAllMap();

        // On it�re sur la HashMap pour retirer tous les �l�ments qui ne sont pas des ann�es selectionn�es
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
