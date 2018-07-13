package control.task;

import java.util.List;
import java.util.Map;

import model.ComposantSonar;
import model.enums.Matiere;
import model.sonarapi.Vue;
import utilities.Statics;

public class CreerVueParEditionTask extends SonarTask
{

    public CreerVueParEditionTask()
    {
        super(2);
        annulable = false;
    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParEdition();
    }

    private Boolean creerVueParEdition()
    {
        // Récupération des composants par édition
        Map<String, List<ComposantSonar>> map = recupererComposantsSonarVersion(Matiere.JAVA);
        
        // Création des vues
        etapePlus();

        for (Map.Entry<String, List<ComposantSonar>> entry : map.entrySet())
        {
            String nom = "Edition - " + entry.getKey() + "C";
            String base = "Vue " + nom + Statics.NL;
            updateMessage(base);
            updateProgress(0, 1);
            Vue vueParent = creerVue(entry.getKey() + "Key", nom, "", true);
            
            int i = 0;
            int size = entry.getValue().size();
            for (ComposantSonar compo : entry.getValue())
            {
                updateProgress(++i, size);
                updateMessage(base + "Ajout : " + compo.getNom());
                api.ajouterProjet(compo, vueParent);
            }
        }
        return true;
    }

}
