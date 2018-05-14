package control.task;

import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import control.sonar.SonarAPI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import model.enums.Param;
import model.enums.ParamSpec;
import model.sonarapi.Projet;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.Utilities;
import utilities.enums.Severity;

public abstract class SonarTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    protected SonarAPI api;
    protected static final String RECUPCOMPOSANTS = "R�cup�ration des composants Sonar";
    public static final String TITRE = "T�che Sonar";
    private StringProperty etape = new SimpleStringProperty(this, "etape", "");
    protected int debut;
    protected int fin;
    protected boolean annulable;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant les donn�es de l'utilisateur. Initialisation des �tapes de traitement
     */
    protected SonarTask(int fin)
    {
        if (!info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = SonarAPI.INSTANCE;
        initEtape(fin);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Utilis�e pour permettre le retour arri�re si possible du traitement
     */
    public void annuler()
    {
        // Pas de traitement par default
    }

    /**
     * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar
     *
     * @return
     */
    protected Map<String, Projet> recupererComposantsSonar()
    {
        updateMessage(RECUPCOMPOSANTS);
        updateProgress(-1, 1);
        // Appel du webservice pour remonter tous les composants

        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "d:\\composants.ser", () -> api.getComposants());

        // Triage ascendant de la liste par nom de projet
        projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Cr�ation de la regex pour retirer les num�ros de version des composants
        Pattern pattern = Pattern.compile("^\\D*");

        // Cr�ation de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caract�res cr��es par la regex comme clef dans la map.
        // Les compossant �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de
        // version obsol�te.
        Map<String, Projet> retour = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getNom());
            if (matcher.find())
            {
                retour.put(matcher.group(0), projet);
            }
        }
        updateMessage(RECUPCOMPOSANTS + " OK");
        return retour;
    }

    /**
     * Permet de r�cup�rer les composants de Sonar tri�s par version avec s�pration des composants datastage
     *
     * @return
     */
    protected Map<String, List<Projet>> recupererComposantsSonarVersion(Boolean datastage)
    {
        updateMessage(RECUPCOMPOSANTS);
        
        // R�cup�ration des versions en param�tre
        String[] versions = proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONS).split(";");

        // Appel du webservice pour remonter tous les composants
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "d:\\composants.ser", () -> api.getComposants());

        // Cr�ation de la map de retour en utilisant les versions donn�es
        Map<String, List<Projet>> retour = new HashMap<>();

        for (String version : versions)
        {
            retour.put(version, new ArrayList<>());
        }

        // It�ration sur les projets pour remplir la liste de retour
        for (Projet projet : projets)
        {
            for (String version : versions)
            {
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans
                // version E32
                if (projet.getNom().endsWith(Utilities.transcoEdition(version)))
                {
                    // Selon que l'on regarde les composants datastage ou non, on remplie la liste en cons�quence en
                    // utilisant le filtre en param�tre. Si le Boolean est nul, on prend tous les composants
                    String filtre = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
                    if (datastage == null || datastage && projet.getNom().startsWith(filtre) || !datastage && !projet.getNom().startsWith(filtre))
                        retour.get(version).add(projet);
                }
            }
        }
        updateMessage(RECUPCOMPOSANTS + "OK");
        return retour;
    }

    /**
     * Cr�e une vue dans Sonar avec suppression ou non de la vue pr�c�dente.
     *
     * @param key
     * @param name
     * @param description
     * @param suppression
     * @return
     */
    protected Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contr�le
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");

        // Cr�ation de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valoris�e
        if (description != null)
            vue.setDescription(description);

        // Suppression de la vue pr�cedente. Utilisation supression projet et vue pour �viter la recr�ation de la vue
        if (suppression)
        {
            api.supprimerProjet(vue, false);
            api.supprimerVue(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }

    protected void updateMessage(String message, int etape, int nbreEtapes)
    {
        StringBuilder base = new StringBuilder("Etape ").append(etape).append("/").append(nbreEtapes).append(Statics.NL);
        updateMessage(base.append(message).toString());
    }

    protected void initEtape(int fin)
    {
        debut = 1;
        this.fin = fin;
        setEtape(debut, fin);
    }

    protected void etapePlus()
    {
        setEtape(++debut, fin);
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