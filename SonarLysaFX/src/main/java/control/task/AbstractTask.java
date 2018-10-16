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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import control.sonar.SonarAPI;
import dao.DaoFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.OptionRecupCompo;
import model.enums.Param;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

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

    protected static final String RECUPCOMPOSANTS = "Récupération des composants Sonar";
    private static final String ECOULE = "Temps écoulé : ";
    private static final String RESTANT = "\nTemps restant : ";

    protected SonarAPI api;
    protected String baseMessage;
    protected int debut;
    protected int fin;
    protected boolean annulable;
    protected AbstractTask tacheParente;
    protected Map<String, ComposantSonar> mapCompos;

    private StringProperty etape = new SimpleStringProperty(this, "etape", EMPTY);
    private StringProperty tempsEcoule = new SimpleStringProperty(this, "ecoule", EMPTY);
    private StringProperty tempsRestant = new SimpleStringProperty(this, "restant", EMPTY);
    private StringProperty affTimer = new SimpleStringProperty(this, "affTimer", EMPTY);
    private String titre;
    private TimerTask timerTask;
    private AffichageTempsTask affTimerTask;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant les données de l'utilisateur. Initialisation des étapes de traitement
     */
    protected AbstractTask(int fin, String titre)
    {
        if (!info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = SonarAPI.INSTANCE;
        initEtape(fin);
        this.titre = titre;
        baseMessage = "";
        mapCompos = DaoFactory.getDao(ComposantSonar.class).readAllMap();
        setTempsRestant(0);
        timerTask = new TimerTask(this);
        affTimerTask = new AffichageTempsTask(this);
        new Thread(timerTask).start();
        new Thread(affTimerTask).start();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Implémentation des classes filles de la méthode annuler, s'il y a beosin de rajouter des traitements.
     */
    public abstract void annulerImpl();

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public final void updateMessage(String message)
    {
        if (tacheParente != null)
            tacheParente.updateMessage(baseMessage + message);
        else
            super.updateMessage(baseMessage + message);
    }

    @Override
    public void updateProgress(double effectue, double total)
    {
        if (tacheParente != null)
            tacheParente.updateProgress(effectue, total);
        else
            super.updateProgress(effectue, total);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Utilisée pour permettre le retour arrière si possible du traitement
     */
    public final void annuler()
    {
        timerTask.annuler();
        affTimerTask.annuler();
        annulerImpl();
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet de récupérer la dernière version de chaque composants créés dans Sonar qui a été au moins envoyé à l'édition. Rajoute les plus anciennes versions des
     * composants qui n'ont pas de version livrèe à l'édition (due à la purge).
     *
     * @return
     */
    protected final Map<String, ComposantSonar> recupererComposantsSonar(OptionRecupCompo option)
    {
        updateMessage(RECUPCOMPOSANTS);
        updateProgress(-1, -1);

        // Triage ascendant de la liste par nom de projet
        List<ComposantSonar> compos = new ArrayList<>(mapCompos.values());
        compos.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        Map<String, ComposantSonar> retour = null;

        switch (option)
        {
            case INCONNU:
                retour = recupererComposInconnus(compos, pattern);
                break;

            case NONPROD:
                retour = recupererComposNonProd(compos, pattern);
                break;

            case PATRIMOINE:
                retour = recupererPatrimoine(compos, pattern);
                break;

            case TERMINE:
                retour = recupererComposTermines(compos, pattern);
                break;

            case DERNIERE:
                retour = recupererComposDerniereVersion(compos, pattern);
                break;

            default:
                throw new TechnicalException("Option \"" + option + "\" inconnue : control.task.AbstractSonarTask.recupererComposantsSonar.", null);
        }

        updateMessage(RECUPCOMPOSANTS + " OK");

        return retour;
    }

    /**
     * Permet de récupérer les composants de Sonar par matière
     *
     * @return
     */
    protected final List<ComposantSonar> recupererComposantsSonar(Matiere matiere)
    {
        updateMessage(RECUPCOMPOSANTS);

        // Liste de retour avec initialement tous les composants SonarQube
        List<ComposantSonar> retour = new ArrayList<>(mapCompos.values());

        // Itération pour retiter les composants qui ne sont pas de la bonne matière
        for (Iterator<ComposantSonar> iter = retour.iterator(); iter.hasNext();)
        {
            if (!testMatiereCompo(matiere, iter.next()))
                iter.remove();
        }

        updateMessage(RECUPCOMPOSANTS + "OK");
        return retour;
    }

    /**
     * Remonte la valeur d'une metrique en protegeant des nullPointeur avec une valeur par défault.
     * 
     * @param compo
     *            Composant.
     * @param type
     *            type du métrique.
     * @param value
     *            valeur par défault à remonter en cas de valeur nulle.
     * @return
     */
    protected String getValueMetrique(Composant compo, TypeMetrique type, String value)
    {
        return compo.getMapMetriques().computeIfAbsent(type, t -> new Metrique(type, value)).getValue();
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

    /**
     * Initilisa le compteur d'étapes (début et fin).
     * 
     * @param fin
     */
    protected final void initEtape(int fin)
    {
        debut = 1;
        this.fin = fin;
        setEtape(debut, fin);
    }

    /**
     * Incrémente le compteur d'étapes de la tâche.
     */
    protected void etapePlus()
    {
        debut++;
        setEtape(debut, fin);
    }

    /**
     * Affiche le temps restant et le temps écoulé dans les fenêtres des tâches
     * 
     * @param debut
     *            Heure de début.
     * @param i
     *            Index de l'élément en cours
     * @param size
     *            Taille de la liste parcourue.
     * @return
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

    /**
     * Teste un composant et retourne la matière de celui-ci
     * 
     * @param compo
     * @return
     */
    protected Matiere testMatiereCompo(String nom)
    {
        String filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
        String filtreCobol = proprietesXML.getMapParams().get(Param.FILTRECOBOL);

        if (nom.startsWith(filtreDataStage))
            return Matiere.DATASTAGE;

        if (nom.startsWith(filtreCobol))
            return Matiere.COBOL;

        return Matiere.JAVA;
    }

    /**
     * Permet de lier cette tâche à une tâche parente pour mettre à jour le message et la progression.
     * 
     * @param parent
     */
    protected void affilierTache(AbstractTask parent)
    {
        tacheParente = parent;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Contrôle la matière d'un composant en utilisant les filtres paramétrés.
     * 
     * @param matiere
     * @param compo
     * @return
     */
    private boolean testMatiereCompo(Matiere matiere, ComposantSonar compo)
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
                return compo.getNom().startsWith(filtreCobol);

            default:
                throw new FunctionalException(Severity.ERROR, "Nouvelle matière pas prise en compte");
        }
    }

    private Map<String, ComposantSonar> recupererComposInconnus(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map des composants qui ont une version
        Map<String, ComposantSonar> composOK = new HashMap<>();

        // Map de retour des composants qui n'ont pas de lot RTC
        Map<String, ComposantSonar> retour = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
            {
                LotRTC lotRTC = compo.getLotRTC();

                // On vérfie qu'on trouve bien le lotRTC dans la map
                if (lotRTC != null)
                    composOK.put(matcher.group(0), compo);

                // Sinon on rajoute le composant dans la map temporaire.
                else
                    retour.put(matcher.group(0), compo);
            }
        }

        // On supprime de la map de retour les composants qui ont une version avec un lotRTC
        Iterator<String> iter = retour.keySet().iterator();
        while (iter.hasNext())
        {
            String key = iter.next();
            if (composOK.containsKey(key))
                iter.remove();
        }
        return retour;
    }

    private Map<String, ComposantSonar> recupererComposNonProd(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map des composants qui ont bien un lot terminé
        Map<String, ComposantSonar> compoOK = new HashMap<>();

        // Map de retour des composants qui ont un lot RTC mais pas terminé
        Map<String, ComposantSonar> retour = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
            {
                LotRTC lotRTC = compo.getLotRTC();

                // On saute les composants sans lot RTC
                if (lotRTC == null)
                    continue;

                EtatLot etatLot = lotRTC.getEtatLot();

                // AJout des composants avec lots terminés
                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    compoOK.put(matcher.group(0), compo);

                // Sinon on rajoute dans la map de retour les projets qui n'ont pas au moins une version dans la map de retour
                else if (!compoOK.containsKey(matcher.group(0)))
                    retour.put(matcher.group(0), compo);
            }
        }

        // Comparaison des deux maps pour enlever les lots qui ont une entrée dans la map des compos OK.
        Iterator<String> iter = retour.keySet().iterator();
        while (iter.hasNext())
        {
            String key = iter.next();
            if (compoOK.containsKey(key))
                iter.remove();
        }

        return retour;
    }

    private Map<String, ComposantSonar> recupererPatrimoine(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les composants étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de version obsolète.
        Map<String, ComposantSonar> retour = new HashMap<>();

        // Liste temporaire des composants qui n'ont pas une version en production
        Map<String, List<ComposantSonar>> temp = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
            {
                LotRTC lotRTC = compo.getLotRTC();
                EtatLot etatLot = null;

                if (lotRTC != null)
                    etatLot = lotRTC.getEtatLot();

                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    retour.put(matcher.group(0), compo);

                // Sinon on rajoute dans la map temporaire les projets qui n'ont pas au moins une version dans la map de retour
                else if (!retour.containsKey(matcher.group(0)))
                    temp.computeIfAbsent(matcher.group(0), l -> new ArrayList<>()).add(compo);
            }
        }

        // Comparaison des deux maps pour rajouter les composants qui ne sont pas dans la map de retour, pour être sûr d'avoir tous les composants.
        // En effet certains composants n'ont plus la dernière version mise en production dans SonarQube
        for (Map.Entry<String, List<ComposantSonar>> entry : temp.entrySet())
        {
            if (!retour.containsKey(entry.getKey()))
                retour.put(entry.getKey(), entry.getValue().get(0));
        }

        return retour;
    }

    private Map<String, ComposantSonar> recupererComposTermines(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map de retour des composants avec un lots terminé
        Map<String, ComposantSonar> retour = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
            {
                LotRTC lotRTC = compo.getLotRTC();
                EtatLot etatLot = null;

                if (lotRTC != null)
                    etatLot = lotRTC.getEtatLot();

                // On ajoute à la map tous les composants avec un lot à l'état terminé ou envoyé à l'édition
                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    retour.put(matcher.group(0), compo);
            }
        }

        return retour;
    }

    private Map<String, ComposantSonar> recupererComposDerniereVersion(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map de retour des composants avec un lots terminé
        Map<String, ComposantSonar> retour = new HashMap<>();

        for (ComposantSonar compo : composantsSonar)
        {
            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
                retour.put(matcher.group(0), compo);
        }

        return retour;
    }

    /*---------- ACCESSEURS ----------*/

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

    public void setEtape(int debut, int fin)
    {
        Platform.runLater(() -> etape.set("Etape " + debut + " / " + fin));
    }

    public void setTempsEcoule(long millis)
    {
        Platform.runLater(() -> tempsEcoule.set(ECOULE + LocalTime.ofSecondOfDay(millis / Statics.MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME)));
    }

    public void setTempsRestant(long millis)
    {
        Platform.runLater(() -> {
            if (millis == 0)
                tempsRestant.set("");
            else
                tempsRestant.set(RESTANT + LocalTime.ofSecondOfDay(Math.abs(millis) / Statics.MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME));
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
        this.baseMessage = baseMessage;
    }

    public String getBaseMessage()
    {
        return baseMessage == null ? "" : baseMessage;
    }
}
