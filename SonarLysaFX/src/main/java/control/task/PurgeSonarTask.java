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

import application.Main;
import control.mail.ControlMail;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypeInfoMail;
import model.enums.TypeMail;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.Utilities;

public class PurgeSonarTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    public static final String TITRE = "Purge des composants SonarQube";
    private ControlMail controlMail;

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");

    /*---------- CONSTRUCTEURS ----------*/

    public PurgeSonarTask()
    {
        super(2);
        annulable = true;
        controlMail = new ControlMail();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return purgeVieuxComposants();
    }

    /*---------- METHODES PRIVEES ----------*/

    private Boolean purgeVieuxComposants()
    {
        updateMessage("Calcul des composants à purger.");
        List<Projet> suppression = calculPurge();

        etapePlus();

        String base = "Purge en cours :\n";
        int size = suppression.size();
        int i = 0;

        // Suppression de tous les composants de la liste
        for (Projet projet : suppression)
        {
            i++;
            updateMessage(base + projet.getNom() + "\n" + i + " sur" + size);
            updateProgress(i, size);
            api.supprimerProjet(projet.getKey(), true);
            api.supprimerVue(projet.getKey(), true);
            controlMail.addInfo(TypeInfoMail.COMPOPURGE, projet.getNom(), null);
        }

        updateMessage("Fin du traitement.");
        updateProgress(1, 1);

        controlMail.envoyerMail(TypeMail.PURGESONAR);
        return true;
    }

    /**
     * Création de a liste des composants à supprimer dans SonarQube
     * 
     * @return
     */
    private List<Projet> calculPurge()
    {
        // Variables
        List<Projet> retour = new ArrayList<>();
        int supp = 0;
        int solo = 0;
        int total = 0;

        // Nombre de versions de chaque composant à garder.
        int nbreVersion = Integer.parseInt(Statics.proprietesXML.getMapParams().get(Param.NBREPURGE));

        // Récupération de la liste des versions des composants à garder et rangement de la plus recente à la plus ancienne.
        List<String> listeVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSCOMPOSANTS).split(";"));
        Collections.sort(listeVersion, (s1, s2) -> Integer.valueOf(s2).compareTo(Integer.valueOf(s1)));

        // Préparation map
        Map<String, List<Projet>> mapProjets = compileMap();

        // Itération sur la map pour calcul des composants à supprimer. On ne garde les deux dernières versions de chaque composant récent,
        // ou la dernière pour les composant plus ancients, ou les 3 dernière pour la version la plus récente selon le paramètrage.
        for (Map.Entry<String, List<Projet>> entry : mapProjets.entrySet())
        {
            // ----- 1. Variables -----
            List<Projet> liste = entry.getValue();

            // Premier élément
            Projet premier = liste.get(0);

            // Calcul nombre total de composants
            int size = liste.size();
            total += size;

            // ----- 2. Suppression des composants 9999 pour ne pas les prendre en compte -----
            if (premier.getKey().endsWith("9999") && liste.size() > 1)
            {
                liste.remove(premier);
                premier = liste.get(0);
            }

            // Incrémentation indice pour les composants qui n'ont qu'une seule version
            if (size == 1)
                solo++;

            // ----- 3. Boucle de purge pour les composants qui ont plus d'une version -----
            if (size > 1)
            {
                // ----- a. On ne garde que les x dernières versions de chaque composant selon le paramétrage -----
                int i = nbreVersion;

                // Calcul du pattern à partir du paramètrage pour obtenir : [(X)|...|(Z)]$
                StringBuilder pattern = new StringBuilder("[");
                for (String string : listeVersion)
                {
                    pattern.append("(").append(string).append(")|");
                }
                // On enlève la dernière |
                pattern.replace(pattern.length() - 1, pattern.length(), "");
                pattern.append("]$");

                // Compilation et matching
                if (!Pattern.compile(pattern.toString()).matcher(premier.getKey()).find())
                    i = 1;

                // ----- c. Selon paramétrage, on va garder les 3 versions des composants les plus récents
                if (Pattern.compile("[(" + listeVersion.get(0) + ")]$").matcher(premier.getKey()).find() && Statics.proprietesXML.getMapParamsBool().get(ParamBool.SUPPSONAR))
                    i = 3;

                // ----- d. boucle pour créer la liste des composants à supprimer
                // De fait que le remove reduit la taille de la liste, il ne faut pas incrémenter l'indice
                while (i < liste.size())
                {
                    Projet projet = entry.getValue().get(i);
                    LOGGER.info("SUPPRESSION : " + projet.getNom());
                    retour.add(projet);
                    entry.getValue().remove(projet);
                    supp++;
                }
            }
        }

        // ----- 5. Logs des résultats -----
        String extraDonnees = "Composants uniques (hors versions) : " + mapProjets.size() + "\nComposants solo (une seule version) : " + solo
                + "\nTotal composants sonar (toutes versions comprises) : " + total + "\nSuppressions : " + supp + "\n";
        controlMail.addExtra(extraDonnees);

        return retour;
    }

    private Map<String, List<Projet>> compileMap()
    {
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "composants.ser", () -> api.getComposants());

        // Triage ascendant de la liste par nom de projet décroissants
        projets.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Création de la map et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de
        // version obsolète.
        Map<String, List<Projet>> retour = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getKey());
            if (matcher.find())
            {
                if (retour.get(matcher.group(0)) == null)
                    retour.put(matcher.group(0), new ArrayList<>());

                retour.get(matcher.group(0)).add(projet);
            }
        }

        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
