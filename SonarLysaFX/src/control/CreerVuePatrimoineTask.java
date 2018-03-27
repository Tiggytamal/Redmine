package control;

import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.concurrent.Task;
import junit.control.ControlSonarTest;
import model.enums.TypeParam;
import sonarapi.SonarAPI;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.FunctionalException;
import utilities.Utilities;
import utilities.enums.Severity;

public class CreerVuePatrimoineTask extends Task<Object>
{
    private SonarAPI api;

    public CreerVuePatrimoineTask()
    {
        if (!info.controle())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), info.getPseudo(), info.getMotDePasse());
    }

    @Override
    protected Object call() throws Exception
    {
        return methode();
    }
    
    private boolean methode()
    {
        // Récupération des composants
        List<Projet> composants = new ArrayList<>(recupererComposantsSonar().values());

        // Date pour récupérer l'annèe et la semaine
        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        String nom = "Vue patrimoine " + date.getYear() + " S" + date.get(woy);
        updateMessage("Création " + nom);
        // Création de la vue
        Vue vue = creerVue("vue_patrimoine_" + date.getYear() + "_S" + date.get(woy), nom, null, true);

        // Ajout des composants
        int size = composants.size();
        for (int i = 0; i < size; i++)
        {
            Projet projet = composants.get(i);
            api.ajouterProjet(projet, vue);
            updateProgress(i, size);
            updateMessage(projet.getNom());
        }
        return true;
    }

    /**
     * Permet de récupérer la dernière version de chaque composants créés dans Sonar
     *
     * @return
     */
    @SuppressWarnings ("unchecked")
    private Map<String, Projet> recupererComposantsSonar()
    {
        // Appel du webservice pour remonter tous les composants
        List<Projet> projets;

        if (ControlSonarTest.deser)
        {
            projets = Utilities.deserialisation("d:\\composants.ser", List.class);
        }
        else
        {
            projets = api.getComposants();
            Utilities.serialisation("d:\\composants.ser", projets);
        }

        // Triage ascendant de la liste par nom de projet
        projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D*");

        // Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de
        // version obsolète.
        Map<String, Projet> retour = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getNom());
            if (matcher.find())
            {
                retour.put(matcher.group(0), projet);
            }
        }
        return retour;
    }

    /**
     * Crée une vue dans Sonar avec suppression ou non de la vue précédente.
     *
     * @param key
     * @param name
     * @param description
     * @param suppression
     * @return
     */
    private Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contrôle
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
        }

        // Création de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valorisée
        if (description != null)
        {
            vue.setDescription(description);
        }

        // Suppresison de la vue précedente
        if (suppression)
        {
            api.supprimerProjet(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }
}