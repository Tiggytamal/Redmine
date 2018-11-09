package control.task;

import static utilities.Statics.NL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.bdd.ComposantSonar;
import model.bdd.Edition;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.TypeEdition;
import model.rest.sonarapi.Vue;
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
    private static final int NBRESEMAINES = 52;
    private static final String TITRE = "création vues Maintenance";
    
    private List<String> annees;
    private TypeEdition chccdm;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueCHCCDMTask()
    {
        super(ETAPES, TITRE);
        startTimers();
    }

    public CreerVueCHCCDMTask(List<String> annees, TypeEdition chccdm)
    {
        this();
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
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueCHCouCDM()
    {
        // Traitement depuis le fichier XML
        suppressionVuesMaintenance(chccdm, annees);

        Map<String, Set<String>> mapVues = preparerMapVuesMaintenance();

        creerVuesMaintenance(mapVues);

        return true;
    }

    /**
     * @param cdm
     * @param annees
     */
    private void suppressionVuesMaintenance(TypeEdition chccdm, List<String> annees)
    {
        String baseClef;
        baseMessage = "Suppression des vues existantes :" + NL;
        int j = 1;
        long debut = System.currentTimeMillis();
        int size = NBRESEMAINES * annees.size();
        
        // On itère sur chacune des annèes
        for (String annee : annees)
        {
            // préparation de la base de la clef
            if (chccdm == TypeEdition.CDM)
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
     * Prépare la liste des vues CHC ou CDM depuis le fichier XML en filtrant selon le type d'édition choisi.
     * 
     * @param mapEditions
     *            Map des éditions provenant de récupérer Edition <br/>
     *         <b>clé</b> = numéro édition XX.YY.ZZ.AA - <b>valeur</b> = CHC(_CDM)YYYY-Sww
     *         
     * @return La map des vues sous forme de HashSet pour ne pas avoir de doublon de lot. <br>
     *          <b>clé</b> : Edition - <b>valeur</b> : set des lot de l'édition 
     */
    private Map<String, Set<String>> preparerMapVuesMaintenance()
    {
        Map<String, Set<String>> retour = new HashMap<>();

        // Récupératoin des composants en base
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA, InstanceSonar.LEGACY);

        etapePlus();
        baseMessage = "Traitement des composants :\n";

        for (int i = 0; i < composants.size(); i++)
        {
            ComposantSonar compo = composants.get(i);

            // MAJ progression
            updateMessage(compo.getNom());
            updateProgress(i, composants.size());

            // Vérification qu'on a bien un numéro de lot et que dans le fichier XML, l'édition du composant est présente
            if (compo.getLotRTC() != null && compo.getLotRTC().getEdition() != null )
            {
                Edition edition = compo.getLotRTC().getEdition();
                
                // Contrôle pour ne prendre que les CHC ou CDM selon le booléen
                if (edition.getTypeEdition() != chccdm)
                    continue;

                // AJout à la map et initialisation HashSet au besoin
                retour.computeIfAbsent(edition.getNom(), k -> new HashSet<>()).add(compo.getLotRTC().getLot());
            }
        }

        return retour;
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
            baseMessage = "Création des vues :\n" + entry.getKey();
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

    /*---------- ACCESSEURS ----------*/
}
