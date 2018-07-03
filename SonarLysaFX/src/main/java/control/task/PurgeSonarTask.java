package control.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.PRIVATE_MEMBER;

import application.Main;
import model.ProprietesXML;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

public class PurgeSonarTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    public static final String TITRE = "Purge des composants SonarQube";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public PurgeSonarTask()
    {
        super(1);
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
        List<Projet> suppression = calculPurge();
               
        // Suppression de tous les composants de la liste
        for (Projet projet : suppression)
        {
            api.supprimerProjet(projet.getKey(), true);
            api.supprimerVue(projet.getKey(), true);
        }
        return true;
    }

    /**
     * Cr�ation de a liste des composants � supprimer dans SonarQube
     * @return
     */
    private List<Projet> calculPurge()
    {
        // Variables
        List<Projet> retour = new ArrayList<>();
        int supp = 0;
        int solo = 0;
        int total = 0;
        
        // R�cup�ration des composants Sonar
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "d:\\composants.ser", () -> api.getComposants());
        
        // Pr�paration map
        Map<String, List<Projet>> mapProjets = compileMap(projets);

        // It�ration sur la map pour calcul des composants � supprimer. On ne garde les deux derni�res versions de chaque composant r�cent,
        // ou la derni�re pour les composant plus ancients, ou les 3 derni�re pour la version la plus r�cente selon le param�trage.
        for (Map.Entry<String, List<Projet>> entry : mapProjets.entrySet())
        {
            // ----- 1. Variables -----
            List<Projet> liste = entry.getValue();
            
            // Premier �l�ment
            Projet premier = liste.get(0);
            
            // Calcul nombre total de composants
            int size = liste.size();
            total += size;
            
            // R�cup�ration de la liste des versions des composants � garder et rangement de la plus recente � la plus ancienne.
            List<String> listeVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSCOMPOSANTS).split(";"));
            Collections.sort(listeVersion, (s1,s2) -> Integer.valueOf(s2).compareTo(Integer.valueOf(s1)));
            
            
            // ----- 2. Suppression des composants 9999 pour ne pas les prendre en compte -----
            if (Pattern.compile("9999$").matcher(premier.getKey()).find())
                liste.remove(premier);        
            
            // Incr�mentation indice pour les composants qui n'ont qu'une seule version
            if (size == 1)
                solo++;
            
            // ----- 3. Boucle de purge pour les composants qui ont plus d'une version -----
            if (size > 1)
            {
                // ----- a. On ne garde que les 2 derni�res versions de chaque composant -----
                int i = 2;
                
                // ----- b. On ne garde qu'une version pour les composants qui ne sont pas param�tr�s -----
                
                // Calcul du pattern � partir du param�trage
                StringBuilder pattern = new StringBuilder("[");
                for (String string : listeVersion)
                {
                    pattern.append("(").append(string).append(")|");                   
                }
                pattern.replace(pattern.length() - 1, pattern.length(), "");
                pattern.append("]$");
                
                // Compilation et matching
                if (!Pattern.compile(pattern.toString()).matcher(premier.getKey()).find())
                    i = 1;
                
                // ----- c. Selon param�trage, on va garder les 3 versions des composants les plus r�cents
                if (Pattern.compile("[(" + listeVersion.get(0) + ")]$").matcher(premier.getKey()).find() && Statics.proprietesXML.getMapParamsBool().get(ParamBool.SUPPSONAR))
                    i = 3;
                
                // ----- d. boucle pour cr�er la liste des composants � supprimer
                // De fait que le remove reduit la taille de la liste, il ne faut pas incr�menter l'indice
                while (i < liste.size())
                {
                    Projet projet = entry.getValue().get(i);
                    Statics.LOGGER.info("SUPPRESSION : " + projet.getNom());
                    retour.add(projet);
                    entry.getValue().remove(projet);
                    supp++;
                }
            }
        }
        
        
        // ----- 5. Logs des r�sultats -----
        Statics.LOGGER.info("Composants uniques : " + mapProjets.size());
        Statics.LOGGER.info("composants solo : " + solo);
        Statics.LOGGER.info("total composants sonar : " + total);
        Statics.LOGGER.info("suppressions : " + supp);

        return retour;
    }
    
    private Map<String, List<Projet>> compileMap(List<Projet> projets)
    {
        if (projets == null || projets.isEmpty())
            throw new TechnicalException("Argument null ou vide, m�thode control.sonar.PurgeSonarTask.compileMap", null);
        
        // Triage ascendant de la liste par nom de projet d�croissants
        projets.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));

        // Cr�ation de la regex pour retirer les num�ros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Cr�ation de la map et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caract�res cr��es par la regex comme clef dans la map.
        // Les compossant �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de
        // version obsol�te.
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
