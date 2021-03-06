package control.task;

import java.util.List;
import java.util.Map;

import model.ComposantSonar;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.Statics;

public class CreerVueParEditionTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final short ETAPES = 2;
    
    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueParEditionTask()
    {
        super(ETAPES);
        annulable = false;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParEdition();
    }

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
            Vue vueParent = creerVue(entry.getKey() + "Key", nom, EMPTY, true);
            
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

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
