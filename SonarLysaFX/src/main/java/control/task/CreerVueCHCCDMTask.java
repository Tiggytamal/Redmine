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

import model.ComposantSonar;
import model.enums.CHCouCDM;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * T�che permettant de cr�er les vues CHC et CDM depuis Sonar
 * 
 * @author ETP8137 - Gr�goire MAthon
 * @since  1.0
 */
public class CreerVueCHCCDMTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private List<String> annees;
    private CHCouCDM chccdm;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueCHCCDMTask(List<String> annees, CHCouCDM chccdm)
    {
        super(3);
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

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueCHCouCDM()
    {
        // Traitement depuis le fichier XML
        suppressionVuesMaintenance(chccdm, annees);

        creerVueMaintenance(recupererEditions(annees));
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
     * Cr�e les vues CHC ou CDM depuis Sonar et le fichier XML. {@code true} pour les vues CDM et {@code false} pour les vues CHC
     * 
     * @param cdm
     */
    private void creerVueMaintenance(Map<String, String> mapEditions)
    {
        Map<String, Set<String>> mapVuesACreer = new HashMap<>();

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
            if (!compo.getLot().isEmpty() && mapEditions.keySet().contains(compo.getEdition()))
            {
                String keyCHC = mapEditions.get(compo.getEdition());

                // Contr�le pour ne prendre que les CHC ou CDM selon le bool�en
                if (!controle(chccdm, keyCHC))
                    continue;

                // AJout � la map et cr�ation de la clef au besoin
                if (!mapVuesACreer.keySet().contains(keyCHC))
                    mapVuesACreer.put(keyCHC, new HashSet<>());
                mapVuesACreer.get(keyCHC).add(compo.getLot());
            }
        }

        creerVues(mapVuesACreer);
    }

    private boolean controle(CHCouCDM chccdm, String keyCHC)
    {
        return chccdm == CHCouCDM.CDM && keyCHC.contains("CDM") || chccdm == CHCouCDM.CHC  && !keyCHC.contains("CDM");
    }

    private void creerVues(Map<String, Set<String>> mapVuesACreer)
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
     * @param cdm
     * @param annees
     * @return
     */
    private Map<String, String> recupererEditions(List<String> annees)
    {
        Map<String, String> retour = fichiersXML.getMapEditions();

        // On it�re sur la HashMap pour retirer tous les �l�ments qui ne sont pas des ann�es selectionn�es
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