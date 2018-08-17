package control.task;

import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.sonar.SonarAPI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.Utilities;
import utilities.enums.Severity;

public abstract class AbstractSonarTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    protected SonarAPI api;
    protected static final String RECUPCOMPOSANTS = "Récupération des composants Sonar";
    public static final String TITRE = "Tâche Sonar";
    private StringProperty etape = new SimpleStringProperty(this, "etape", EMPTY);
    protected int debut;
    protected int fin;
    protected boolean annulable;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant les données de l'utilisateur. Initialisation des étapes de traitement
     */
    protected AbstractSonarTask(int fin)
    {
        if (!info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = SonarAPI.INSTANCE;
        initEtape(fin);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Utilisée pour permettre le retour arrière si possible du traitement
     */
    public void annuler()
    {
        // Pas de traitement par default
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de récupérer la dernière version de chaque composants créés dans Sonar qui a été au moins envoyé à l'édition. Rajoute les plus anciennes versions des
     * composants qui n'ont pas de version livrèe à l'édition (dûe à la purge).
     *
     * @return
     */
    protected final Map<String, ComposantSonar> recupererComposantsSonar()
    {
        updateMessage(RECUPCOMPOSANTS);
        updateProgress(-1, -1);

        // Récupération des composants Sonar depuis le fichier XML
        List<ComposantSonar> composantsSonar = new ArrayList<>();
        composantsSonar.addAll(Statics.fichiersXML.getMapComposSonar().values());

        // Triage ascendant de la liste par nom de projet
        composantsSonar.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de version obsolète.
        Map<String, ComposantSonar> retour = new HashMap<>();

        // Liste temporaire des composants qui n'ont pas une version en production
        Map<String, List<ComposantSonar>> temp = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
            {
                EtatLot etatLot = Statics.fichiersXML.getLotsRTC().computeIfAbsent(compo.getLot(), l -> ModelFactory.getModel(LotSuiviRTC.class)).getEtatLot();

                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    retour.put(matcher.group(0), compo);

                // Sinon on rajoute dans la map temporaire les projets qui n'ont pas au moins une version dans la map de retour
                else if (!retour.containsKey(matcher.group(0)))
                {
                    // Création de la liste si la clef n'est pas encore présente dans la map
                    if (!temp.containsKey(matcher.group(0)))
                        temp.put(matcher.group(0), new ArrayList<>());

                    // Ajout du projet
                    List<ComposantSonar> liste = temp.get(matcher.group(0));
                    liste.add(compo);
                }
            }
        }

        // Comparaison des deux maps pour rajouter les composants qui ne sont pas dans la map de retour, pour être sûr d'avoir tous les composants.
        // En effet certains composants n'ont plus la dernière version mise en production dans SonarQube
        for (Map.Entry<String, List<ComposantSonar>> entry : temp.entrySet())
        {
            if (!retour.containsKey(entry.getKey()))
            {
                retour.put(entry.getKey(), entry.getValue().get(0));
            }
        }

        updateMessage(RECUPCOMPOSANTS + " OK");

        return retour;
    }

    /**
     * Permet de récupérer les composants de Sonar triés par version avec séparation des composants datastage
     *
     * @return
     */
    protected final Map<String, List<ComposantSonar>> recupererComposantsSonarVersion(Matiere matiere)
    {
        updateMessage(RECUPCOMPOSANTS);

        // Récupération des versions en paramètre
        String[] versions = proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONS).split(";");

        // Récupération composants depuis fichier XML
        List<ComposantSonar> compos = Statics.fichiersXML.getListComposants();

        // Création de la map de retour en utilisant les versions données
        Map<String, List<ComposantSonar>> retour = new HashMap<>();

        for (String version : versions)
        {
            retour.put(version, new ArrayList<>());
        }

        // Itération sur les projets pour remplir la liste de retour
        for (ComposantSonar compo : compos)
        {
            String versionCompo = EMPTY;
            for (String version : versions)
            {
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans version E32
                if (compo.getNom().endsWith(Utilities.transcoEdition(version)))
                    versionCompo = version;
            }

            // Si le composant ne fait pas partie des versions paramétrée, on passe au suivant.
            if (versionCompo.isEmpty())
                continue;

            // Test de la matiere du composant et des filtres des composants pour rajouter à la map de retour.
            if (testFiltreEtMatiere(matiere, compo))
                retour.get(versionCompo).add(compo);

        }

        updateMessage(RECUPCOMPOSANTS + "OK");
        return retour;
    }

    /**
     * Contrôle la matière et les filtres paramétrés
     * 
     * @param matiere
     * @param compo
     * @return
     */
    private boolean testFiltreEtMatiere(Matiere matiere, ComposantSonar compo)
    {
        String filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
        String filtreCobol = proprietesXML.getMapParams().get(Param.FILTRECOBOL);

        // Switch de contrôle selon la type de matière en utilisant les filtres
        switch (matiere)
        {
            case DATASTAGE:
                return compo.getNom().startsWith(filtreDataStage);

            case JAVA:
                return !compo.getNom().startsWith(filtreDataStage) && !compo.getNom().startsWith(filtreCobol);

            case COBOL:
                throw new FunctionalException(Severity.ERROR, "COBOL pas pris en compte!");

            default:
                throw new FunctionalException(Severity.ERROR, "Nouvelle matière pas prise en compte");
        }
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
    protected final Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contrôle
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");

        // Création de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valorisée
        if (description != null)
            vue.setDescription(description);

        // Suppression de la vue précedente. Utilisation supression projet et vue pour éviter la recréation de la vue
        if (suppression)
        {
            api.supprimerProjet(vue, false);
            api.supprimerVue(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }

    protected final void updateMessage(String message, int etape, int nbreEtapes)
    {
        StringBuilder base = new StringBuilder("Etape ").append(etape).append("/").append(nbreEtapes).append(Statics.NL);
        updateMessage(base.append(message).toString());
    }

    protected final void initEtape(int fin)
    {
        debut = 1;
        this.fin = fin;
        setEtape(debut, fin);
    }

    protected void etapePlus()
    {
        debut++;
        setEtape(debut, fin);
    }

    /*---------- ACCESSEURS ----------*/

    public String getEtape()
    {
        return etape.get();
    }

    public void setEtape(int debut, int fin)
    {
        Platform.runLater(() -> etape.set("Etape " + debut + " / " + fin));
    }

    public StringProperty etapeProperty()
    {
        return etape;
    }

    public boolean isAnnulable()
    {
        return annulable;
    }
}
