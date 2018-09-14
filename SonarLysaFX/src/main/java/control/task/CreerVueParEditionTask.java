package control.task;

import java.util.List;
import java.util.Map;

import model.ComposantSonar;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.Statics;

/**
 * T�che de cr�ation des vues Sonar par �dition (E30,E31,E32...)
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class CreerVueParEditionTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final short ETAPES = 2;
    private static final String TITRE = "Cr�ation Vues par Edition";
    
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
        // R�cup�ration des composants par �dition
        Map<String, List<ComposantSonar>> map = recupererComposantsSonarVersion(Matiere.JAVA);
        
        // Cr�ation des vues
        etapePlus();

        for (Map.Entry<String, List<ComposantSonar>> entry : map.entrySet())
        {
            String nom = "Edition - " + entry.getKey() + "C";
            String base = "Vue " + nom + Statics.NL;
            updateMessage(base);
            updateProgress(0, 1);
            Vue vueParent = creerVue(entry.getKey() + "Key", nom, Statics.EMPTY, true);
            
            int i = 0;
            int size = entry.getValue().size();
            for (ComposantSonar compo : entry.getValue())
            {
                i++;
                updateProgress(i, size);
                updateMessage(base + "Ajout : " + compo.getNom());
                api.ajouterProjet(compo, vueParent);
            }
        }
        return true;
    }
    
    /*---------- ACCESSEURS ----------*/
}
