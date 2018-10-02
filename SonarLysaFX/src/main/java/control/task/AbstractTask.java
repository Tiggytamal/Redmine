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
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe abstraite des t�ches de traitement de l'application
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 *
 */
public abstract class AbstractTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    protected static final String RECUPCOMPOSANTS = "R�cup�ration des composants Sonar";
    private static final int MILLITOSECOND = 1000;
    private static final String ECOULE = "\nTemps �coul� : ";
    private static final String RESTANT = "\nTemps restant : ";

    protected SonarAPI api;
    protected String baseMessage;
    protected int debut;
    protected int fin;
    protected boolean annulable;
    
    private StringProperty etape = new SimpleStringProperty(this, "etape", EMPTY);
    private String titre;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant les donn�es de l'utilisateur. Initialisation des �tapes de traitement
     */
    protected AbstractTask(int fin, String titre)
    {
        if (!info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = SonarAPI.INSTANCE;
        initEtape(fin);
        this.titre = titre;
        baseMessage = "";
    }
    
    /*---------- METHODES ABSTRAITES ----------*/
        
    /**
     * Utilis�e pour permettre le retour arri�re si possible du traitement
     */
    public abstract void annuler();

    /*---------- METHODES PUBLIQUES ----------*/


    @Override
    public final void updateMessage(String message)
    {
        super.updateMessage(baseMessage + message);
    }
    
    @Override
    public void updateProgress(double effectue, double total)
    {
        super.updateProgress(effectue, total);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar qui a �t� au moins envoy� � l'�dition. Rajoute les plus anciennes versions des
     * composants qui n'ont pas de version livr�e � l'�dition (due � la purge).
     *
     * @return
     */
    protected final Map<String, ComposantSonar> recupererComposantsSonar(OptionRecupCompo option)
    {
        updateMessage(RECUPCOMPOSANTS);
        updateProgress(-1, -1);

        // R�cup�ration des composants Sonar depuis la base de donn�es
        List<ComposantSonar> composantsSonar = DaoFactory.getDao(ComposantSonar.class).readAll();

        // Triage ascendant de la liste par nom de projet
        composantsSonar.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Cr�ation de la regex pour retirer les num�ros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        Map<String, ComposantSonar> retour = null;

        switch (option)
        {
            case INCONNU:
                retour = recupererComposInconnus(composantsSonar, pattern);
                break;

            case NONPROD:
                retour = recupererComposNonProd(composantsSonar, pattern);
                break;

            case PATRIMOINE:
                retour = recupererPatrimoine(composantsSonar, pattern);
                break;

            case TERMINE:
                retour = recupererComposTermines(composantsSonar, pattern);
                break;

            case DERNIERE:
                retour = recupererComposDerniereVersion(composantsSonar, pattern);
                break;

            default:
                throw new TechnicalException("Option \"" + option + "\" inconnue : control.task.AbstractSonarTask.recupererComposantsSonar.", null);
        }

        updateMessage(RECUPCOMPOSANTS + " OK");

        return retour;
    }

    /**
     * Permet de r�cup�rer les composants de Sonar avec s�paration des composants datastage
     *
     * @return
     */
    protected final Map<String, ComposantSonar> recupererComposantsSonar(Matiere matiere)
    {
        updateMessage(RECUPCOMPOSANTS);
        
        // Map de retour
        Map<String, ComposantSonar> retour = new HashMap<>();

        // It�ration sur les composants issus de la base de donn�es
        for (ComposantSonar compo : DaoFactory.getDao(ComposantSonar.class).readAll())
        {
            // Test de la matiere du composant et des filtres des composants pour rajouter � la map de retour.
            if (testFiltreEtMatiere(matiere, compo))
                retour.put(compo.getLotRTC().getLot(), compo);
        }

        updateMessage(RECUPCOMPOSANTS + "OK");
        return retour;
    }

    /**
     * Remonte la valeur d'une metrique en protegeant des nullPointeur avec une valeur par d�fault.
     * 
     * @param compo
     *            Composant.
     * @param type
     *            type du m�trique.
     * @param value
     *            valeur par d�fault � remonter en cas de valeur nulle.
     * @return
     */
    protected String getValueMetrique(Composant compo, TypeMetrique type, String value)
    {
        return compo.getMapMetriques().computeIfAbsent(type, t -> new Metrique(type, value)).getValue();
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

    /**
     * Initilisa le compteur d'�tapes (d�but et fin).
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
     * Incr�mente le compteur d'�tapes de la t�che.
     */
    protected void etapePlus()
    {
        debut++;
        setEtape(debut, fin);
    }

    /**
     * Affiche le temps restant et le temps �coul� dans les fen�tres des t�ches
     * 
     * @param debut
     *            Heure de d�but.
     * @param i
     *            Index de l'�l�ment en cours
     * @param size
     *            Taille de la liste parcourue.
     * @return
     */
    protected final String affichageTemps(long debut, int i, int size)
    {
        long actuel = System.currentTimeMillis();
        long ecoute = actuel - debut;
        long prevu = ecoute / i * size;
        String ecoule = LocalTime.ofSecondOfDay(ecoute / MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME);
        String restant = LocalTime.ofSecondOfDay(Math.abs((prevu - ecoute)) / MILLITOSECOND).format(DateTimeFormatter.ISO_LOCAL_TIME);
        return new StringBuilder(ECOULE).append(ecoule).append(RESTANT).append(restant).toString();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Contr�le la mati�re et les filtres param�tr�s
     * 
     * @param matiere
     * @param compo
     * @return
     */
    private boolean testFiltreEtMatiere(Matiere matiere, ComposantSonar compo)
    {
        String filtreDataStage = proprietesXML.getMapParams().get(Param.FILTREDATASTAGE);
        String filtreCobol = proprietesXML.getMapParams().get(Param.FILTRECOBOL);

        // Switch de contr�le selon la type de mati�re en utilisant les filtres
        switch (matiere)
        {
            case DATASTAGE:
                return compo.getNom().startsWith(filtreDataStage);

            case JAVA:
                return !compo.getNom().startsWith(filtreDataStage) && !compo.getNom().startsWith(filtreCobol);

            case COBOL:
                throw new FunctionalException(Severity.ERROR, "COBOL pas pris en compte!");

            default:
                throw new FunctionalException(Severity.ERROR, "Nouvelle mati�re pas prise en compte");
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

                // On v�rfie qu'on trouve bien le lotRTC dans la map
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
        // Map des composants qui ont bien un lot termin�
        Map<String, ComposantSonar> compoOK = new HashMap<>();

        // Map de retour des composants qui ont un lot RTC mais pas termin�
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

                // AJout des composants avec lots termin�s
                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    compoOK.put(matcher.group(0), compo);

                // Sinon on rajoute dans la map de retour les projets qui n'ont pas au moins une version dans la map de retour
                else if (!compoOK.containsKey(matcher.group(0)))
                    retour.put(matcher.group(0), compo);
            }
        }

        // Comparaison des deux maps pour enlever les lots qui ont une entr�e dans la map des compos OK.
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
        // Cr�ation de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caract�res cr��es par la regex comme clef dans la map.
        // Les composants �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de version obsol�te.
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

        // Comparaison des deux maps pour rajouter les composants qui ne sont pas dans la map de retour, pour �tre s�r d'avoir tous les composants.
        // En effet certains composants n'ont plus la derni�re version mise en production dans SonarQube
        for (Map.Entry<String, List<ComposantSonar>> entry : temp.entrySet())
        {
            if (!retour.containsKey(entry.getKey()))
                retour.put(entry.getKey(), entry.getValue().get(0));
        }

        return retour;
    }

    private Map<String, ComposantSonar> recupererComposTermines(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map de retour des composants avec un lots termin�
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

                // On ajoute � la map tous les composants avec un lot � l'�tat termin� ou envoy� � l'�dition
                if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
                    retour.put(matcher.group(0), compo);
            }
        }

        return retour;
    }

    private Map<String, ComposantSonar> recupererComposDerniereVersion(List<ComposantSonar> composantsSonar, Pattern pattern)
    {
        // Map de retour des composants avec un lots termin�
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
    
    public String getBaseMessage()
    {
        return baseMessage == null ? "" : baseMessage;
    }
}
