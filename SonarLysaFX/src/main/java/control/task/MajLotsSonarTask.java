package control.task;

import java.util.List;

import model.sonarapi.Vue;

/**
 * Tâche permettant de mettre à jour les vues Sonar avec une valeur mauelle égale au numéro du lot.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajLotsSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 1;
    private static final String TITRE = "Mise à jour des lots SonarQube";

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
        return majLotsSonar();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majLotsSonar()
    {
        // variables
        List<Vue> liste = api.getVues();
        String base = "Mise à jour des vues Sonar.\n";
        boolean ok = true;
        int size = liste.size();
        int i = 0;
        long debut = System.currentTimeMillis();
        
        for (Vue vue : liste)
        {
            i++;
            if (vue.getName().startsWith("Lot "))
            {
                if (!api.setManualMesureView(vue.getKey()))
                    ok = false;
                
                // Affichage
                updateProgress(i, size);
                updateMessage(base + "vue " + i + " / " + size + affichageTemps(debut, i, size));
            }
        }

        return ok;

    }

    /*---------- ACCESSEURS ----------*/
}
