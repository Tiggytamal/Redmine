package control.task;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.bdd.ComposantSonar;
import model.enums.OptionRecupCompo;
import model.enums.Param;
import model.sonarapi.Vue;
import utilities.Statics;

/**
 * Tâche de création de la vue Sonar du patrimoine
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerVuePatrimoineTask extends AbstractTask
{

    /*---------- ATTRIBUTS ----------*/

    public static final String TITRE = "Création Vue Patrimoine";
    
    private static final int ETAPES = 3;
    
    private String key;
    private final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVuePatrimoineTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVuePatrimoine();
    }

    @Override
    public void annulerImpl()
    {
        cancel();
        if (key != null && !key.isEmpty())
            api.supprimerProjet(key, true);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création de la vue patrimoine pour la semaine en cours.
     * 
     * @return {@code true} Si la vue a bien été créée.<br>
     *         {@code false} Si la task a été intérompue ou s'il y a eu une erreur.
     */
    private boolean creerVuePatrimoine()
    {
        // Date pour récupérer l'annèe et le numéro de semaine
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        // Clef et nom de la vue
        key = "vue_patrimoine_" + today.getYear() + "_S" + today.get(woy);
        String nom = "Vue patrimoine " + today.getYear() + " S" + today.get(woy);

        if (isCancelled())
            return false;

        // Création de la vue
        
        // Affichage plus variables
        StringBuilder builder = new StringBuilder("Création vue ");
        etapePlus();
        updateMessage(builder.append(nom).toString());
        Vue vue = creerVue(key, nom, null, true);
        baseMessage = builder.append(" OK.\n").append("Ajout : ").toString();
        long debut = System.currentTimeMillis();

        // Récupération des composants
        List<ComposantSonar> composants = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.PATRIMOINE).values());
        
        // Ajout des composants
        int size = composants.size();
        etapePlus();
        for (int i = 0; i < size; i++)
        {
            if (isCancelled())
                return false;
            ComposantSonar projet = composants.get(i);

            // Suppression composants COBOL
            if (projet.getNom().startsWith(Statics.proprietesXML.getMapParams().get(Param.FILTRECOBOL)))
                continue;

            api.ajouterProjet(projet, vue);
            
            // Affichage
            updateProgress(i, size);
            calculTempsRestant(debut, i, size);
            updateMessage(projet.getNom());
        }
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
