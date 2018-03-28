package control;

import static utilities.Statics.TODAY;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.Statics;

public class CreerVuePatrimoineTask extends SonarTask
{
    
    /*---------- ATTRIBUTS ----------*/
    
    private String key;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public CreerVuePatrimoineTask()
    {
        super();
    }
    
    public CreerVuePatrimoineTask(String pseudo, String mdp)
    {
        super(pseudo, mdp);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        creerVuePatrimoine();
        return true;
    }
    
    
    @Override
    public void annuler()
    {
        api.supprimerProjet(key, true);   
    }
    
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Cr�ation de la vue patrimoine pour la semaine en cours.
     * 
     * @return
     *         {@code true} si la vue a bien �t� cr��e.<br>
     *         {@code false} Si la task a �t� int�rompue ou s'il y a ue une erreur.
     */
    private boolean creerVuePatrimoine()
    {
        // Date pour r�cup�rer l'ann�e et le num�ro de semaine
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        
        // Clef et nom de la vue
        key = "vue_patrimoine_" + TODAY.getYear() + "_S" + TODAY.get(woy);
        String nom = "Vue patrimoine " + TODAY.getYear() + " S" + TODAY.get(woy);
        
        // R�cup�ration des composants
        updateMessage("R�cup�ration des composants...");
        List<Projet> composants = new ArrayList<>(recupererComposantsSonar().values());
        updateMessage("R�cup�ration des composants...OK");
       
        if (isCancelled())
            return false;
        
        // Cr�ation de la vue
        StringBuilder builder = new StringBuilder("Cr�ation vue ");
        updateMessage(builder.append(nom).toString());
        Vue vue = creerVue(key, nom, null, true);
        String baseMessage =  builder.append(" OK.").append(Statics.NL).append("Ajout : ").toString();

        // Ajout des composants
        int size = composants.size();
        for (int i = 0; i < size; i++)
        {
            if (isCancelled())
                return false;
            Projet projet = composants.get(i);
            api.ajouterProjet(projet, vue);
            updateProgress(i, size);
            updateMessage(baseMessage + projet.getNom());
        }
        return true;
    }
    
    /*---------- ACCESSEURS ----------*/
}