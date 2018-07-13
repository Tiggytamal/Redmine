package control.task;

import java.util.ArrayList;
import java.util.List;

import model.ComposantSonar;
import model.sonarapi.Vue;
import utilities.Statics;

public class CreerVueDataStageTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    public static final String TITRE = "Vue Datastage";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private Vue vue;

    public CreerVueDataStageTask()
    {
        super(2);
        annulable = true;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        if (vue != null)
        {
            api.supprimerProjet(vue, true);
            api.supprimerVue(vue, true);
        }
    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueDatastage();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Crée une vue avec tous les composants Datastage
     */
    private boolean creerVueDatastage()
    {
        // Appel du webservice pour remonter tous les composants
        updateMessage(RECUPCOMPOSANTS);
        
        // Récupération composants depuis fichier XML
        List<ComposantSonar> compos = Statics.fichiersXML.getListComposants();
        
        if (isCancelled())
            return false;
        
        // Création de la vue avec maj du message
        etapePlus();
        String nom = "Composants Datastage";
        StringBuilder builder = new StringBuilder("Création vue ");
        updateMessage(builder.append(nom).toString());
        vue = creerVue("DSDataStageListeKey", nom, "Vue regroupant tous les composants Datastage", true);
        String baseMessage =  builder.append(" OK.").append(Statics.NL).append("Ajout : ").toString();
        
        // Itération sur les projets pour ajouter tous les composants DataStage, puis itération sur la nouvelle liste pour traitement et affichage progression
        List<ComposantSonar> listeDS = new ArrayList<>();
        for (ComposantSonar compo : compos)
        {
            if (compo.getNom().startsWith("Composant DS_"))
                listeDS.add(compo);
        }
        
        int i = 0;
        int size = listeDS.size();

        for (ComposantSonar compo : listeDS)
        {
            if (isCancelled())
                return false;
            
            api.ajouterProjet(compo, vue);
            updateProgress(++i, size);
            updateMessage(baseMessage + compo.getNom());
        }
        return true;
    }
}
