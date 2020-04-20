package control.task;

import static utilities.Statics.EMPTY;
import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import control.rest.SonarAPI;
import dao.Dao;
import dao.DaoComposantBase;
import dao.DaoEdition;
import dao.DaoFactory;
import dao.DaoProjetClarity;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import model.bdd.AbstractBDDModele;
import model.bdd.ComposantBase;
import model.bdd.Edition;
import model.bdd.ProjetClarity;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.OptionGestionErreur;
import model.enums.Param;
import model.enums.Severity;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.FunctionalException;
import utilities.Statics;

/**
 * Classe abstraite des tâches de traitement de l'application
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 *
 */
public abstract class AbstractTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    // Attributs statiques
    protected static final String RECUPCOMPOSANTS = "Récupération des composants Sonar";
    private static final String ECOULE = "Temps ecoule : ";
    private static final String RESTANT = "\nTemps restant : ";
    private static final int DAYINMILLI = 86_399_000;
    private static final Pattern PATTERNNAMECOMPO = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

    // Attributs protected
    protected SonarAPI api;
    protected String baseMessage;
    protected int etapeDebut;
    protected int etapeFin;
    protected boolean annulable;
    protected AbstractTask tacheParente;
    protected DaoComposantBase daoCompo;
    protected DaoEdition daoEdition;
    protected DaoProjetClarity daoClarity;

    // Attributs privés
    private Map<String, ComposantBase> mapCompo;
    private Map<String, Edition> mapEdition;
    private Map<String, ProjetClarity> mapClarity;
    private StringProperty etape = new SimpleStringProperty(this, "etape", EMPTY);
    private StringProperty tempsEcoule = new SimpleStringProperty(this, "ecoule", EMPTY);
    private StringProperty tempsRestant = new SimpleStringProperty(this, "restant", EMPTY);
    private StringProperty affTimer = new SimpleStringProperty(this, "affTimer", EMPTY);
    private String titre;
    private TimerTask timerTask;
    private AffichageTempsTask affTimerTask;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractTask(int fin, String titre)
    {
        initEtape(fin);
        this.titre = titre;
        if (!info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = SonarAPI.build();
        setTempsRestant(0);
        timerTask = new TimerTask(this);
        affTimerTask = new AffichageTempsTask(this);
        baseMessage = Statics.EMPTY;
        daoCompo = DaoFactory.getMySQLDao(ComposantBase.class);
        daoEdition = DaoFactory.getMySQLDao(Edition.class);
        daoClarity = DaoFactory.getMySQLDao(ProjetClarity.class);
        mapCompo = new HashMap<>();
        mapEdition = new HashMap<>();
        mapClarity = new HashMap<>();
    }

    protected AbstractTask(int fin, String titre, AbstractTask tacheParente)
    {
        this(fin, titre);
        if (tacheParente != null)
        {
            this.tacheParente = tacheParente;
            baseMessage = tacheParente.getBaseMessage();
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Implementation des classes filles de la methode annuler, s'il y a besoin de rajouter des traitements.
     */
    public abstract void annulerImpl();

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public final void updateMessage(String message)
    {
        // Si le message est null, on ne fait rien
        if (message == null)
            return;

        if (tacheParente != null)
            tacheParente.updateMessage(getBaseMessage() + message);
        else
            super.updateMessage(getBaseMessage() + message);
    }

    @Override
    public void updateProgress(double effectue, double total)
    {
        if (tacheParente != null)
            tacheParente.updateProgress(effectue, total);
        super.updateProgress(effectue, total);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Utilisée pour permettre le retour arrière si possible du traitement.
     */
    public final void annuler()
    {
        timerTask.annuler();
        affTimerTask.annuler();
        annulerImpl();
    }

    /**
     * Lance les timers pour calculer les temps restant et écoulé de la tâche.
     */
    public final void startTimers()
    {
        new Thread(timerTask).start();
        new Thread(affTimerTask).start();
    }

    /**
     * Affiche le temps restant et le temps ecoulé dans les fenêtres des tâches
     * 
     * @param debut
     *              Heure de debut.
     * @param i
     *              Index de l'élément en cours
     * @param size
     *              Taille de la liste parcourue.
     */
    public final void calculTempsRestant(long debut, int i, int size)
    {
        long actuel = System.currentTimeMillis();
        long millisEcoulees = actuel - debut;
        long prevu = millisEcoulees * size / i;
        if (tacheParente != null)
            tacheParente.setTempsRestant(prevu - millisEcoulees);
        else
            setTempsRestant(prevu - millisEcoulees);
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet de fermer les sous-tâches de gestion du temps en cas de reussite de la tâche
     */
    @Override
    protected void succeeded()
    {
        timerTask.cancel(true);
        affTimerTask.cancel(true);
    }

    /**
     * Permet de fermer les sous-tâches de gestion du temps en cas d'annulation de la tâche
     */
    @Override
    protected void cancelled()
    {
        succeeded();
    }

    /**
     * Permet de fermer les sous-tâches de gestion du temps en cas d'echec de la tâche
     */
    @Override
    protected void failed()
    {
        succeeded();
    }

    /**
     * Permet de récupérer la dernière version de chaque composant crée dans SonarQube.
     *
     * @return
     *         La map des composants classées par nom.
     */
    protected final Map<String, ComposantBase> recupererComposantsSonar()
    {
        baseMessage = RECUPCOMPOSANTS;
        updateMessage(Statics.EMPTY);
        updateProgress(-1, -1);

        // Triage ascendant de la liste par nom de projet
        List<ComposantBase> compos = new ArrayList<>(getMapCompo().values());
        compos.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Map de retour des composants avec un lots terminf
        Map<String, ComposantBase> retour = new HashMap<>();

        for (ComposantBase compo : compos)
        {
            Matcher matcher = PATTERNNAMECOMPO.matcher(compo.getNom());

            if (matcher.find())
                retour.put(matcher.group(0), compo);
        }

        updateMessage(" OK");

        return retour;
    }

    /**
     * Permet de récupérer les composants de Sonar par matière.
     * 
     * @param matiere
     *                 Matière des composants voulus.
     * @param instance
     *                 Instance des composants voulus.
     * @return
     *         Liste des composants.
     */
    protected final List<ComposantBase> recupererComposantsSonar(Matiere matiere, InstanceSonar instance)
    {
        updateMessage(RECUPCOMPOSANTS);

        // Liste de retour avec initialement tous les composants SonarQube
        List<ComposantBase> retour = new ArrayList<>(getMapCompo().values());

        // Itération pour retiter les composants qui ne sont pas de la bonne matiere
        for (Iterator<ComposantBase> iter = retour.iterator(); iter.hasNext();)
        {
            if (!testMatiereInstanceCompo(instance, matiere, iter.next()))
                iter.remove();
        }

        updateMessage(RECUPCOMPOSANTS + "OK");
        return retour;
    }

    /**
     * Crée d'un objet dans Sonar (portfolio/appli) avec suppression ou non de l'objet précédent.
     *
     * @param key
     *                    Clef de l'objet.
     * @param name
     *                    Nom de l'objet.
     * @param description
     *                    Description de l'objet.
     * @param type
     *                    Type de l'objet Sonar (applicaiton/appli)
     * @param suppression
     *                    Supression ou non de l'ancien objet dans SonarQube.
     * @return
     *         L'objet créé dans SonarQube
     */
    protected final ObjetSonar creerObjetSonar(String key, String name, String description, TypeObjetSonar type, boolean suppression)
    {
        // Contrôle
        if (key == null || key.isEmpty() || name == null || name.isEmpty() || type == null)
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");

        // Création de la vue
        ObjetSonar pf = new ObjetSonar(key, name, description, type);

        // Ajout de la description si elle est valorisée
        if (description != null)
            pf.setDescription(description);

        // Suppression de la vue precedente. Utilisation supression projet et vue pour éviter la recreation de la vue
        if (suppression)
            api.supprimerObjetSonar(pf, OptionGestionErreur.NON);

        // Appel de l'API Sonar
        api.creerObjetSonar(pf);

        return pf;
    }

    /**
     * Initialise le compteur d'etapes (début et fin).
     * 
     * @param fin
     *            Valeur de l'étape de fin.
     */
    protected final void initEtape(int fin)
    {
        etapeDebut = 1;
        this.etapeFin = fin;
        setEtape(etapeDebut, fin);
    }

    /**
     * Incremente le compteur d'etapes de la tâche.
     */
    protected void etapePlus()
    {
        etapeDebut++;
        setEtape(etapeDebut, etapeFin);
    }

    /**
     * Encapsulation de la persistance des données pour permettre les mocks.
     * 
     * @param dao
     *          Dao utilisé pour sauvegarder les données
     * @param object
     *          Objet à sauvegarder
     * @return
     */
    protected <T extends AbstractBDDModele<I>, I> boolean sauvegarde(Dao<T, I> dao, T object)
    {
        return dao.persist(object);
    }
    
    /**
     * Encapsulation de la persistance des données sous forme de liste pour permettre les mocks.
     * 
     * @param dao
     *          Dao utilisé pour sauvegarder les données
     * @param object
     *          Objet à sauvegarder
     * @return
     */
    protected <T extends AbstractBDDModele<I>, I> int sauvegarde(Dao<T, I> dao, Iterable<T> object)
    {
        return dao.persist(object);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Contrôle la matière d'un composant en utilisant les filtres paramétrés.
     * 
     * @param instance
     *                 Instance à tester par rapport à celle du composant.
     * @param matiere
     *                 Matière à tester par rapport à celle du composant.
     * @param compo
     *                 Composant à tester.
     * @return
     *         Vrai si le composant est de la bonne instance et matière.
     */
    private boolean testMatiereInstanceCompo(InstanceSonar instance, Matiere matiere, ComposantBase compo)
    {
        // On ne prend que les composants de la bonne instance
        if (compo.getInstance() != instance)
            return false;

        // Filtres et variables
        String filtreDataStage;
        String filtreCobol;
        String filtreIOS;
        String filtreAndroid;
        String nom = compo.getNom();

        // Switch de contrôle selon la type de matière en utilisant les filtres
        switch (matiere)
        {
            case DATASTAGE:
                filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
                return nom.startsWith(filtreDataStage);

            case JAVA:
                filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
                filtreCobol = proprietesXML.getMapParams().get(Param.FILTRECOBOL);
                return !nom.startsWith(filtreDataStage) && !compo.getNom().startsWith(filtreCobol);

            case COBOL:
                filtreCobol = proprietesXML.getMapParams().get(Param.FILTRECOBOL);
                return nom.startsWith(filtreCobol);

            case IOS:
                filtreIOS = proprietesXML.getMapParams().get(Param.FILTREIOS);
                return nom.contains(filtreIOS);

            case ANDROID:
                filtreAndroid = proprietesXML.getMapParams().get(Param.FILTREANDROID);
                return nom.contains(filtreAndroid);

            default:
                throw new FunctionalException(Severity.ERROR, "Nouvelle matière pas prise en compte");
        }
    }

    /*---------- ACCESSEURS ----------*/

    protected Map<String, ComposantBase> getMapCompo()
    {
        if (mapCompo.isEmpty())
            mapCompo = daoCompo.readAllMap();
        return mapCompo;
    }

    protected Map<String, Edition> getMapEdition()
    {
        if (mapEdition.isEmpty())
            mapEdition = daoEdition.readAllMap();
        return mapEdition;
    }

    protected Map<String, ProjetClarity> getMapClarity()
    {
        if (mapClarity.isEmpty())
            mapClarity = daoClarity.readAllMap();
        return mapClarity;
    }

    public String getEtape()
    {
        return etape.get();
    }

    public String getTitre()
    {
        return titre;
    }

    public String getTempsEcoule()
    {
        return tempsEcoule.get();
    }

    public String getTempsRestant()
    {
        return tempsRestant.get();
    }

    public String getAffTimer()
    {
        return affTimer.get();
    }

    public final void setEtape(int debut, int fin)
    {
        Platform.runLater(() -> etape.set("Etape " + debut + " / " + fin));
    }

    public final void setTempsEcoule(long millis)
    {
        Platform.runLater(() -> {
            if (millis > DAYINMILLI)
                tempsEcoule.set(RESTANT + " > 1j.");
            else
                tempsEcoule.set(ECOULE + LocalTime.ofSecondOfDay(millis / Statics.MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME));
        });
    }

    public final void setTempsRestant(long millis)
    {
        Platform.runLater(() -> {
            if (millis == 0)
                tempsRestant.set("");
            else if (millis < 0)
                tempsRestant.set(LocalTime.ofSecondOfDay(0).format(DateTimeFormatter.ISO_LOCAL_TIME));
            else if (millis > DAYINMILLI)
                tempsRestant.set(RESTANT + " > 1j.");
            else
                tempsRestant.set(RESTANT + LocalTime.ofSecondOfDay(millis / Statics.MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME));
        });
    }

    public void setAffTimer(String ecoule, String restant)
    {
        Platform.runLater(() -> affTimer.set(ecoule + restant));
    }

    public StringProperty etapeProperty()
    {
        return etape;
    }

    public StringProperty tempsEcouleProperty()
    {
        return tempsEcoule;
    }

    public StringProperty tempsRestantProperty()
    {
        return tempsRestant;
    }

    public StringProperty affTimerProperty()
    {
        return affTimer;
    }

    public boolean isAnnulable()
    {
        return annulable;
    }

    public void setBaseMessage(String baseMessage)
    {
        // Pas de mise à jour si le message est null
        if (baseMessage == null)
            return;

        if (tacheParente != null)
            tacheParente.setBaseMessage(baseMessage);
        else
            this.baseMessage = baseMessage;
    }

    public String getBaseMessage()
    {
        if (tacheParente != null)
            return tacheParente.getBaseMessage();
        return baseMessage == null ? Statics.EMPTY : baseMessage;
    }
}
