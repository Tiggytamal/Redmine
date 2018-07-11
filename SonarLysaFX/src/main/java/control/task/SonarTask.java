package control.task;

import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.sonar.SonarAPI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
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
    private static final Logger LOGGER = LogManager.getLogger("console-log");

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
     * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar qui a �t� au moins envoy� � l'�dition.
     * Rajoute les plus anciennes versions des composants qui n'ont pas de version livr�e � l'�dition d�e � la purge.
     *
     * @return
     */
    protected final Map<String, Projet> recupererComposantsSonar()
    {
        updateMessage(RECUPCOMPOSANTS);
        updateProgress(0, 1);
        
        // Appel du webservice pour remonter tous les composants
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "composants.ser", () -> api.getComposants());

        // Triage ascendant de la liste par nom de projet
        projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Cr�ation de la regex pour retirer les num�ros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Message
        int size = projets.size();
        int i = 0;

        // Cr�ation de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caract�res cr��es par la regex comme clef dans la map.
        // Les compossant �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de
        // version obsol�te.
        Map<String, Projet> retour = new HashMap<>();

        // Liste temporaire des composants qui n'ont pas une version en production
        Map<String, List<Projet>> temp = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getNom());

            // Message
            updateMessage(RECUPCOMPOSANTS + "\n Traitement : " + projet.getKey() + "\n" + i++ + "sur" + size);
            updateProgress(i, size);
            LOGGER.debug(i + " - " + size);

            if (matcher.find())
            {
                // On r�cup�re l'�tat du lot et si l'etat est au moins � envoyer � l'�dition, on prend la nouvelle version
                Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { TypeMetrique.LOT.toString() });
                String lot = composant.getMapMetriques().computeIfAbsent(TypeMetrique.LOT, (m) -> new Metrique(TypeMetrique.LOT)).getValue();
                EtatLot etatLot = Statics.fichiersXML.getLotsRTC().computeIfAbsent(lot, (l) -> ModelFactory.getModel(LotSuiviRTC.class)).getEtatLot();

                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                {
                    LOGGER.debug("add : " + composant.getNom());
                    retour.put(matcher.group(0), projet);
                }
                // Sinon on rajoute dans la map temporaire les projets qui n'ont pas au moins une version dans la map de retour
                else if (!retour.containsKey(matcher.group(0)))
                {
                    LOGGER.debug("lot : " + etatLot + " - " + composant.getNom());
                    
                    // Cr�ation de la liste si la clef n'est pas encore pr�sente dans la map
                    if (!temp.containsKey(matcher.group(0)))
                        temp.put(matcher.group(0), new ArrayList<>());
                    
                    // Ajout du projet
                    List<Projet> liste = temp.get(matcher.group(0));
                    liste.add(projet);
                }
            }
        }
        

        // Comparaison des deux maps pour rajouter les composants qui ne sont pas dans la map de retour, pour �tre s�r d'avoir tous les composants.
        // En effet certains composants n'ont plus la derni�re version mise en production dans SonarQube
        for (Map.Entry<String, List<Projet>> entry : temp.entrySet())
        {
            if (!retour.containsKey(entry.getKey()))
            {
                retour.put(entry.getKey(), entry.getValue().get(0));
                LOGGER.debug("rajout : " + entry.getValue().get(0).getNom());
            }
        }

        updateMessage(RECUPCOMPOSANTS + " OK");
        return retour;
    }

    /**
     * Permet de r�cup�rer les composants de Sonar tri�s par version avec s�paration des composants datastage
     *
     * @return
     */
    protected final Map<String, List<Projet>> recupererComposantsSonarVersion(Matiere matiere)
    {
        updateMessage(RECUPCOMPOSANTS);

        // R�cup�ration des versions en param�tre
        String[] versions = proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONS).split(";");

        // Appel du webservice pour remonter tous les composants
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "composants.ser", () -> api.getComposants());

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
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans version E32
                if (projet.getNom().endsWith(Utilities.transcoEdition(version)))
                {
                    // Switch de contr�le selon la type de mati�re
                    // utilisant le filtre en param�tre. Si le Boolean est nul, on prend tous les composants
                    String filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
                    switch (matiere)
                    {
                        case DATASTAGE:
                            if (projet.getNom().startsWith(filtreDataStage))
                                retour.get(version).add(projet);
                            break;

                        case JAVA:
                            if (!projet.getNom().startsWith(filtreDataStage))
                                retour.get(version).add(projet);
                            break;

                        case COBOL:
                            throw new FunctionalException(Severity.ERROR, "COBOL pas pris en compte!");

                        default:
                            throw new FunctionalException(Severity.ERROR, "Nouvelle mati�re pas prise en compte");
                    }
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
    protected final Vue creerVue(String key, String name, String description, boolean suppression)
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