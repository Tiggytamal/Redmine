package control.task;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.ComposantSonar;
import model.enums.Param;
import model.sonarapi.Vue;
import utilities.Statics;

public class CreerVuePatrimoineTask extends AbstractSonarTask
{

    /*---------- ATTRIBUTS ----------*/

    private String key;
    public static final String TITRE = "Vue Patrimoine";
    private final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVuePatrimoineTask()
    {
        super(3);
        annulable = true;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVuePatrimoine();
    }

    @Override
    public void annuler()
    {
        if (key != null && !key.isEmpty())
            api.supprimerProjet(key, true);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Cr�ation de la vue patrimoine pour la semaine en cours.
     * 
     * @return {@code true} Si la vue a bien �t� cr��e.<br>
     *         {@code false} Si la task a �t� int�rompue ou s'il y a eu une erreur.
     */
    private boolean creerVuePatrimoine()
    {
        // Date pour r�cup�rer l'ann�e et le num�ro de semaine
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        // Clef et nom de la vue
        key = "vue_patrimoine_" + today.getYear() + "_S" + today.get(woy);
        String nom = "Vue patrimoine " + today.getYear() + " S" + today.get(woy);

        // R�cup�ration des composants
        List<ComposantSonar> composants = new ArrayList<>(recupererComposantsSonar().values());

        if (isCancelled())
            return false;

        // Cr�ation de la vue
        StringBuilder builder = new StringBuilder("Cr�ation vue ");
        etapePlus();
        updateMessage(builder.append(nom).toString());
        Vue vue = creerVue(key, nom, null, true);
        String baseMessage = builder.append(" OK.").append(Statics.NL).append("Ajout : ").toString();

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
            updateProgress(i, size);
            updateMessage(baseMessage + projet.getNom());
        }
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
