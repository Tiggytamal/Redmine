package control.task;

import java.util.ArrayList;
import java.util.List;

import control.parent.SonarTask;
import junit.control.ControlSonarTest;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.Statics;
import utilities.Utilities;

public class CreerVueDataStageTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    public static final String TITRE = "Vue Datastage";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private Vue vue;

    public CreerVueDataStageTask()
    {
        super(2);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        if (vue != null)
            api.supprimerProjet(vue, true);
    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVuesDatastage();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Cr�e une vue avec tous les composants Datastage
     */
    private boolean creerVuesDatastage()
    {
        // Appel du webservice pour remonter tous les composants
        updateMessage(RECUPCOMPOSANTS);
        
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(ControlSonarTest.deser, List.class, "d:\\composants.ser", () -> api.getComposants());
        
        if (isCancelled())
            return false;
        
        // Cr�ation de la vue avec maj du message
        etapePlus();
        String nom = "Composants Datastage";
        StringBuilder builder = new StringBuilder("Cr�ation vue ");
        updateMessage(builder.append(nom).toString());
        vue = creerVue("DSDataStageListeKey", nom, "Vue regroupant tous les composants Datastage", true);
        String baseMessage =  builder.append(" OK.").append(Statics.NL).append("Ajout : ").toString();
        
        // It�ration sur les projets pour ajouter tous les composants DataStage, puis it�ration sur la nouvelle liste pour traitement et affichage progression
        List<Projet> listeDS = new ArrayList<>();
        for (Projet projet : projets)
        {
            if (projet.getNom().startsWith("Composant DS_"))
                listeDS.add(projet);
        }
        
        int i = 0;
        int size = listeDS.size();

        for (Projet projet : listeDS)
        {
            if (isCancelled())
                return false;
            
            api.ajouterProjet(projet, vue);
            updateProgress(++i, size);
            updateMessage(baseMessage + projet.getNom());
        }
        return true;
    }
}
