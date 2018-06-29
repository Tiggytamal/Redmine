package control.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

public class PurgeSonarTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    public static final String TITRE = "Prge des composants SonarQube";
    
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

    private List<Projet> calculPurge()
    {
        // Variables
        List<Projet> retour = new ArrayList<>();
        int supp = 0;
        int solo = 0;
        int total = 0;
        
        // Récupération des composants Sonar
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "d:\\composants.ser", () -> api.getComposants());
        
        // Préparation map
        Map<String, List<Projet>> mapProjets = compileMap(projets);

        // Itération sur la map pour calcul des composants à supprimer. On ne garde les deux dernières versions de chaque composant récent,
        // ou la dernière pour les composant plus ancients.
        for (Map.Entry<String, List<Projet>> entry : mapProjets.entrySet())
        {
            List<Projet> liste = entry.getValue();
            
            // Calcul nombre total de composants
            int size = liste.size();
            total += size;
            
            // Suppression des composants 9999 pour ne pas les prendre ne compte
            if (Pattern.compile("9999$").matcher(entry.getValue().get(0).getKey()).find())
                liste.remove(entry.getValue().get(0));
            
            
            // Incrémentaiotn indice pour les composants qui n'ont qu'une seule version
            if (size == 1)
                solo++;
            
            // Suppression de toutes les versions après les deux dernières
            if (size > 1)
            {
                // On ne garde que les deux dernières versions de chaque composant
                int i = 2;
                
                // On ne garde qu'une version pour les composants plus vieux que 14
                if (!Pattern.compile("[(14)|(15)]$").matcher(entry.getValue().get(0).getKey()).find())
                    i = 1;
                
                // De fait que le remove reduit la taille de la liste, il ne faut pas incrémenter l'indice
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
        
        Statics.LOGGER.info("Composants uniques : " + mapProjets.size());
        Statics.LOGGER.info("composants solo : " + solo);
        Statics.LOGGER.info("total composants sonar : " + total);
        Statics.LOGGER.info("suppressions : " + supp);

        return retour;
    }
    
    private Map<String, List<Projet>> compileMap(List<Projet> projets)
    {
        if (projets == null || projets.isEmpty())
            throw new TechnicalException("Argument null ou vide, méthode control.sonar.PurgeSonarTask.compileMap", null);
        
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
