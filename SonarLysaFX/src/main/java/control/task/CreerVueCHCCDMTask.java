package control.task;

import static utilities.Statics.NL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.DaoFactory;
import model.bdd.ComposantSonar;
import model.bdd.Edition;
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
    private static final int NBRESEMAINES = 52;
    private static final String TITRE = "cr�ation vues Maintenance";
    
    private List<String> annees;
    private CHCouCDM chccdm;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueCHCCDMTask()
    {
        super(ETAPES, TITRE);
        startTimers();
    }

    public CreerVueCHCCDMTask(List<String> annees, CHCouCDM chccdm)
    {
        this();
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
    public void annulerImpl()
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
        String baseClef;
        baseMessage = "Suppression des vues existantes :" + NL;
        int j = 1;
        long debut = System.currentTimeMillis();
        int size = NBRESEMAINES * annees.size();
        
        // On it�re sur chacune des ann�es
        for (String annee : annees)
        {
            // pr�paration de la base de la clef
            if (chccdm == CHCouCDM.CDM)
                baseClef = "CHC_CDM" + annee;
            else
                baseClef = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i <= NBRESEMAINES; i++)
            {
                StringBuilder builder = new StringBuilder(baseClef).append("-S").append(String.format("%02d", i));
                String message = builder.toString();
                api.supprimerProjet(builder.append("Key").toString(), false);
                
                //Affichage
                j++;
                updateProgress(j, (long) size);
                calculTempsRestant(debut, j, size);
                updateMessage(message);
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

        // R�cup�ratoin des composants en base
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA);

        etapePlus();
        baseMessage = "Traitement des composants :\n";

        for (int i = 0; i < composants.size(); i++)
        {
            ComposantSonar compo = composants.get(i);

            // MAJ progression
            updateMessage(compo.getNom());
            updateProgress(i, composants.size());

            // V�rification qu'on a bien un num�ro de lot et que dans le fichier XML, l'�dition du composant est pr�sente
            if (compo.getLotRTC() != null && mapEditions.containsKey(compo.getEdition()))
            {
                String keyCHC = mapEditions.get(compo.getEdition()).getNom();

                // Contr�le pour ne prendre que les CHC ou CDM selon le bool�en
                if (!controle(chccdm, keyCHC))
                    continue;

                // AJout � la map et initialisation HashSet au besoin
                retour.computeIfAbsent(keyCHC, k -> new HashSet<>()).add(compo.getLotRTC().getLot());
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
        // Calcul du nombere total d'objets dans la map
        int sizeComplete = 0;
        for (Set<String> lots : mapVuesACreer.values())
        {
            sizeComplete += lots.size();
        }

        // Affichage
        etapePlus();
        int i = 0;
        long debut = System.currentTimeMillis();
        
        for (Map.Entry<String, Set<String>> entry : mapVuesACreer.entrySet())
        {
            Vue parent = new Vue(entry.getKey() + "Key", entry.getKey());
            api.creerVue(parent);
            baseMessage = "Cr�ation des vues :\n" + entry.getKey();
            updateMessage("");

            for (String lot : entry.getValue())
            {
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), parent);
                i++;

                // MAJ progression
                calculTempsRestant(debut, i, sizeComplete);
                updateMessage(NL + "ajout lot " + lot);
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
        Map<String, Edition> retour = DaoFactory.getDao(Edition.class).readAllMap();

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
