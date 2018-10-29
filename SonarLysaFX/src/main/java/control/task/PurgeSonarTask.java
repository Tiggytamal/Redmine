package control.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.word.ControlRapport;
import dao.DaoFactory;
import model.bdd.ComposantSonar;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Tâche permetant d'effectuer la purge des composants SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class PurgeSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    private static final String TITRE = "Purge des composants SonarQube";
    private static final short ETAPES = 2;
    private static final short MAXVERSION = 3;
    private ControlRapport controlRapport;

    /*---------- CONSTRUCTEURS ----------*/

    public PurgeSonarTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
        controlRapport = new ControlRapport(TypeRapport.PURGESONAR);
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return purgeVieuxComposants();
    }
    
    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation        
    }

    /*---------- METHODES PRIVEES ----------*/

    private Boolean purgeVieuxComposants()
    {
        updateMessage("Calcul des composants à purger.");
        List<ComposantSonar> suppression = calculPurge();

        etapePlus();

        String base = "Purge en cours :\n";
        int size = suppression.size();
        int i = 0;

        // Suppression de tous les composants de la liste
        for (ComposantSonar compo : suppression)
        {
            api.supprimerProjet(compo.getKey(), true);
            api.supprimerVue(compo.getKey(), true);
            controlRapport.addInfo(TypeInfo.COMPOPURGE, compo.getNom(), null);
            
            // Affichage
            i++;
            calculTempsRestant(i, i, size);
            updateProgress(i, size);
            updateMessage(new StringBuilder(base).append(compo.getNom()).append("\n").append(i).append(" sur").append(size).toString());
        }

        updateMessage("Fin du traitement.");
        updateProgress(1, 1);

        return controlRapport.creerFichier();
    }

    /**
     * Création de a liste des composants à supprimer dans SonarQube
     * 
     * @return
     */
    private List<ComposantSonar> calculPurge()
    {
        // Variables
        List<ComposantSonar> retour = new ArrayList<>();
        int supp = 0;
        int solo = 0;
        int total = 0;

        // Nombre de versions de chaque composant à garder.
        int nbreVersion = Integer.parseInt(Statics.proprietesXML.getMapParams().get(Param.NBREPURGE));

        // Récupération de la liste des versions des composants à garder et rangement de la plus recente à la plus ancienne.
        List<String> listeVersion = Arrays.asList("15", "14", "13");
        Collections.sort(listeVersion, (s1, s2) -> Integer.valueOf(s2).compareTo(Integer.valueOf(s1)));

        // Calcul du pattern à partir du paramètrage pour obtenir : [(X)|...|(Z)]$
        StringBuilder builder = new StringBuilder("[");
        for (String string : listeVersion)
        {
            builder.append("(").append(string).append(")|");
        }

        // On enlève la dernière |
        builder.replace(builder.length() - 1, builder.length(), Statics.EMPTY);
        builder.append("]$");
        String pattern = builder.toString();

        // Préparation map
        Map<String, List<ComposantSonar>> mapCompos = compileMap();

        // Itération sur la map pour calcul des composants à supprimer. On ne garde les deux dernières versions de chaque composant récent,
        // ou la dernière pour les composant plus ancients, ou les 3 dernière pour la version la plus récente selon le paramètrage.
        for (Map.Entry<String, List<ComposantSonar>> entry : mapCompos.entrySet())
        {
            // ----- 1. Variables -----
            List<ComposantSonar> liste = entry.getValue();

            // Premier élément
            ComposantSonar premier = liste.get(0);

            // Calcul nombre total de composants
            int size = liste.size();
            total += size;

            // ----- 2. Suppression des composants 9999 pour ne pas les prendre en compte -----
            if (premier.getKey().endsWith("9999") && liste.size() > 1)
            {
                liste.remove(premier);
                premier = liste.get(0);
            }

            // Incrémentation indice pour les composants qui n'ont qu'une seule version et sortie du traitement
            if (size == 1)
            {
                solo++;
                continue;
            }

            // ----- 3. Composants qui ont plus d'une version -----
            // ----- a. On ne garde que les x dernières versions de chaque composant selon le paramétrage -----
            int i = nbreVersion;

            // Compilation et matching
            if (!Pattern.compile(pattern).matcher(premier.getKey()).find())
                i = 1;

            // ----- c. Selon paramétrage, on va garder les 3 versions des composants les plus récents
            if (Pattern.compile("[(" + listeVersion.get(0) + ")]$").matcher(premier.getKey()).find() && Statics.proprietesXML.getMapParamsBool().get(ParamBool.SUPPSONAR))
                i = MAXVERSION;

            // ----- d. boucle pour créer la liste des composants à supprimer
            // De fait que le remove reduit la taille de la liste, il ne faut pas incrémenter l'indice
            while (i < liste.size())
            {
                ComposantSonar projet = entry.getValue().get(i);
                LOGGER.info("SUPPRESSION : " + projet.getNom());
                retour.add(projet);
                entry.getValue().remove(projet);
                supp++;
            }

        }

        // ----- 5. Logs des résultats -----
        String extraDonnees = "Composants uniques (hors versions) : " + mapCompos.size() + "\nComposants solo (une seule version) : " + solo + "\nTotal composants sonar (toutes versions comprises) : "
                + total + "\nSuppressions : " + supp + "\n";
        controlRapport.addExtra(extraDonnees);

        return retour;
    }

    private Map<String, List<ComposantSonar>> compileMap()
    {
        // Récupération composants depuis fichier XML
        List<ComposantSonar> compos = DaoFactory.getDao(ComposantSonar.class).readAll();

        // Contrôle si la liste des composants est vide.
        if (compos.isEmpty())
            throw new TechnicalException("Attention la liste des composants est vide - control.task.PurgeSonarTask.compileMap", null);

        // Triage ascendant de la liste par nom de projet décroissants
        compos.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Création de la map et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de
        // version obsolète.
        Map<String, List<ComposantSonar>> retour = new HashMap<>();

        for (ComposantSonar compo : compos)
        {
            Matcher matcher = pattern.matcher(compo.getKey());
            if (matcher.find())
            {
                if (retour.get(matcher.group(0)) == null)
                    retour.put(matcher.group(0), new ArrayList<>());

                retour.get(matcher.group(0)).add(compo);
            }
        }

        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
