package control.task;

import java.util.List;

import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Projet;
import model.sonarapi.Vue;
import utilities.Statics;

public class MajLotsSonarTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final int ETAPES = 1;
    private static final String TITRE = "Mise � jour des lots SonarQube";
    
    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsSonarTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        cancel();        
    }

    @Override
    protected Boolean call() throws Exception
    {
        majLotsSonar();
        return true;
    }
    
    /*---------- METHODES PRIVEES ----------*/

    private void majLotsSonar()
    {
        // R�cup�ration des composants Sonar
        List<Projet> composants = api.getComposants();
        
        // It�ration sur ceux-ci pour r�cup�rer le lot puis lui ajouter le le composant.
        for (Projet projet : composants)
        {
            // Appel pour r�cup�rer la m�trique LOT.
            Composant compo = api.getMetriquesComposant(projet.getKey(), new String[] { TypeMetrique.LOT.getValeur() });
            String lot = getValueMetrique(compo, TypeMetrique.LOT, Statics.EMPTY);
            
            if (isCancelled())
                break;
            
            // Ajout du projet � la vue du lot.
            if (!lot.isEmpty())
                api.ajouterProjet(projet, new Vue("view_lot_" + lot, "Lot " + lot));
        }      
    }
    
    /*---------- ACCESSEURS ----------*/
}
