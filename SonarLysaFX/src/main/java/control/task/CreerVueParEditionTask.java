package control.task;

import java.util.List;
import java.util.Map;

import model.bdd.ComposantSonar;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.Statics;

/**
 * Tâche de création des vues Sonar par édition (E30,E31,E32...)
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerVueParEditionTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final short ETAPES = 2;
    private static final String TITRE = "Création Vues par Edition";
    
    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueParEditionTask()
    {
        super(ETAPES, TITRE);
        annulable = false;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParEdition();
    }
    
    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation        
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private Boolean creerVueParEdition()
    {
        // Récupération des composants par édition
        Map<String, List<ComposantSonar>> map = recupererComposantsSonarVersion(Matiere.JAVA);
        
        // Création des vues
        etapePlus();

        for (Map.Entry<String, List<ComposantSonar>> entry : map.entrySet())
        {
            // Affichage et variables
            String nom = "Edition - " + entry.getKey() + "C";
            String base = "Vue " + nom + Statics.NL;
            updateMessage(base);
            updateProgress(0, 1);
            Vue vueParent = creerVue(entry.getKey() + "Key", nom, Statics.EMPTY, true);
            
            int i = 0;
            int size = entry.getValue().size();
            long debut = System.currentTimeMillis();
            
            // Itération sur chaque composant pour les ajouter à la vue Parent
            for (ComposantSonar compo : entry.getValue())
            {
                api.ajouterProjet(compo, vueParent);
                
                // Affichage
                i++;
                updateProgress(i, size);
                updateMessage(base + "Ajout : " + compo.getNom() + affichageTemps(debut, i, size));
            }
        }
        return true;
    }
    
    /*---------- ACCESSEURS ----------*/
}
