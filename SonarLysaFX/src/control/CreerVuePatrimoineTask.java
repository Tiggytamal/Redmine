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
     * Création de la vue patrimoine pour la semaine en cours.
     * 
     * @return
     *         {@code true} si la vue a bien été créée.<br>
     *         {@code false} Si la task a été intérompue ou s'il y a ue une erreur.
     */
    private boolean creerVuePatrimoine()
    {
        // Date pour récupérer l'annèe et le numéro de semaine
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        
        // Clef et nom de la vue
        key = "vue_patrimoine_" + TODAY.getYear() + "_S" + TODAY.get(woy);
        String nom = "Vue patrimoine " + TODAY.getYear() + " S" + TODAY.get(woy);
        
        // Récupération des composants
        updateMessage("Récupération des composants...");
        List<Projet> composants = new ArrayList<>(recupererComposantsSonar().values());
        updateMessage("Récupération des composants...OK");
       
        if (isCancelled())
            return false;
        
        // Création de la vue
        StringBuilder builder = new StringBuilder("Création vue ");
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