package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.excel.ControlAppsW;
import control.excel.ControlPbApps;
import control.excel.ExcelFactory;
import control.word.ControlRapport;
import control.xml.ControlXML;
import model.Application;
import model.CompoPbApps;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.CreerVueParAppsTaskOption;
import model.enums.TypeColAppsW;
import model.enums.TypeColPbApps;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import model.sonarapi.Projet;
import model.sonarapi.Vue;
import model.utilities.ControlModelInfo;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Tâche de création des vues par application. Permet aussi de créer le fichier d'extraction des composant traités dans Sonar 
 * ainsi que le fichier des problèmes des codes applicatifs.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerVueParAppsTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/

    public static final String TITRE = "Vues par Application";

    /** logger composants sans applications */
    private static final Logger LOGSANSAPP = LogManager.getLogger("sansapp-log");
    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    /** logger applications non listée dans le référentiel */
    private static final Logger LOGNONLISTEE = LogManager.getLogger("nonlistee-log");

    private static final short ETAPES = 3;
    /** compo sans lot */
    private static final String COMPOSANSLOT = "Composant sans numéro de lot";

    /** Nombre de composants avec application inconnues */
    private int inconnues;
    /** Controleur Mails */
    private ControlRapport controlRapport;
    /** Liste des apllications Dans SonarQube */
    private Set<Application> applisOpenSonar;
    /** Map de toutes les apllications */
    private Map<String, Application> applications;
    /** Liste des composants avec un problème au niveau du cide application */
    private List<ComposantSonar> composPbAppli;
    /** 2numération des options de la tâches */
    private CreerVueParAppsTaskOption option;
    /** Nom du fichier de suavegarde de l'extraction */
    private File file;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueParAppsTask(CreerVueParAppsTaskOption option, File file)
    {
        super(ETAPES);
        annulable = false;
        inconnues = 0;
        applications = fichiersXML.getMapApplis();
        controlRapport = new ControlRapport(TypeRapport.VUEAPPS);
        applisOpenSonar = new HashSet<>();
        composPbAppli = new ArrayList<>();
        this.option = option;
        this.file = file;

        if (option != CreerVueParAppsTaskOption.VUE && file == null)
            throw new TechnicalException("Control.task.CreerVueParAppsTask - Demande de création d'extraction sans fichier", null);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParApplication();
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueParApplication()
    {
        // 1 .Création de la liste des composants par application
        @SuppressWarnings("unchecked")
        Map<String, List<ComposantSonar>> mapApplication = Utilities.recuperation(Main.DESER, Map.class, "mapApplis.ser", this::controlerSonarQube);

        // On ne crée pas les vues avec l'option fichier
        if (option == CreerVueParAppsTaskOption.FICHIERS)
            return false;

        // 2. Suppression des vues existantes

        // Message
        String base = "Suppression des vues existantes :" + NL;
        etapePlus();
        updateMessage(base);
        updateProgress(0, 1);

        // Suppression anciennes vues
        List<Projet> listeVuesExistantes = api.getVuesParNom("APPLI MASTER ");
        for (int i = 0; i < listeVuesExistantes.size(); i++)
        {
            Projet projet = listeVuesExistantes.get(i);
            api.supprimerProjet(projet.getKey(), false);
            api.supprimerVue(projet.getKey(), false);

            // Message
            updateMessage(base + projet.getNom());
            updateProgress(i, listeVuesExistantes.size());
        }

        // 3. Creation des nouvelles vues

        // Message
        base = "Creation des nouvelles vues :" + NL;
        int i = 0;
        int size = mapApplication.entrySet().size();
        etapePlus();
        updateMessage(base);
        updateProgress(0, size);

        // Parcours de la liste pour créer chaque vue applicative avec ses composants
        for (Map.Entry<String, List<ComposantSonar>> entry : mapApplication.entrySet())
        {
            // Création de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);

            // Message
            String baseVue = base + "traitement : " + vue.getName() + NL;
            updateMessage(baseVue);
            i++;
            updateProgress(i, size);
            for (ComposantSonar composantSonar : entry.getValue())
            {
                updateMessage(baseVue + "Ajout : " + composantSonar.getNom());
                api.ajouterProjet(composantSonar, vue);
            }
        }

        controlRapport.creerFichier();
        return true;
    }

    /**
     * Remonte les composants par application de SonarQube et créée les logs sur les defaults.
     *
     * @return map des composants SonarQube par application
     */
    public Map<String, List<ComposantSonar>> controlerSonarQube()
    {
        // Récupération des composants Sonar
        Map<String, ComposantSonar> mapCompos = recupererComposantsSonar();
        HashMap<String, List<ComposantSonar>> retour = creerMapApplication(mapCompos);

        // On ne crée pas le fichier avec l'option VUE
        if (option != CreerVueParAppsTaskOption.VUE)
            creerFichiersExtraction();

        return retour;
    }

    /**
     * Crée une map de toutes les applications dans Sonar avec pour chacunes la liste des composants liés.
     * Enregistre aussi la liste des composants avec problème sur el code application
     *
     * @param mapCompos
     * @return
     */
    private HashMap<String, List<ComposantSonar>> creerMapApplication(Map<String, ComposantSonar> mapCompos)
    {
        // Initialisation de la map
        HashMap<String, List<ComposantSonar>> retour = new HashMap<>();

        // Message
        String base = "Traitements des composants :" + NL;
        updateMessage(base);
        int i = 0;
        inconnues = 0;

        // Itération sur la liste des projets
        for (ComposantSonar compo : mapCompos.values())
        {
            // Message
            updateMessage(base + compo.getNom());
            i++;
            updateProgress(i, mapCompos.size());

            // Test si le code application est vide, cela veut dire que le projet n'a pas de code application.
            if (!compo.getAppli().isEmpty())
            {
                String application = compo.getAppli().trim().toUpperCase(Locale.FRANCE);

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, compo))
                    continue;

                // Mise à jour de la map de retour avec en clef, le code application et en valeur : la liste des projets liés.
                if (retour.containsKey(application))
                    retour.get(application).add(compo);
                else
                {
                    List<ComposantSonar> liste = new ArrayList<>();
                    liste.add(compo);
                    retour.put(application, liste);
                }
            }
            else
            {
                LOGSANSAPP.warn("Application non renseignée - Composant : " + compo.getNom());
                controlRapport.addInfo(TypeInfo.COMPOSANSAPP, compo.getNom(), null);
                composPbAppli.add(compo);
            }
        }
        
        
        LOGINCONNUE.info("Nombre d'applis inconnues : " + inconnues);

        // Sauvegarde des applications après mise à jour des données
        new ControlXML().saveParam(fichiersXML);
        return retour;
    }

    /**
     * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
     *
     * @param application
     *            Application enregistrée pour le composant dans Sonar.
     * @param composPbAppli
     *            Liste des composants avec un problème sur le nom de l'application.
     * @param nom
     *            Nom du composant Sonar.
     * @param inconnues
     */
    private boolean testAppli(String application, ComposantSonar compo)
    {
        String nom = compo.getNom();

        // Gestion des composants avec application INCONNUE
        if (Statics.INCONNUE.equalsIgnoreCase(application))
        {
            LOGINCONNUE.warn("Application : INCONNUE - Composant : " + nom);
            inconnues++;
            composPbAppli.add(compo);
            return true;
        }

        // Gestion des composants avec application connue
        if (applications.containsKey(application))
        {
            Application app = applications.get(application);
            if (!app.isActif())
            {
                LOGNONLISTEE.warn("Application obsolète : " + application + " - composant : " + nom);
                controlRapport.addInfo(TypeInfo.APPLIOBSOLETE, nom, application);
            }
            app.ajouterldcSonar(compo.getLdc());
            app.majValSecurite(compo.getSecurity());
            app.ajouterVulnerabilites(compo.getVulnerabilites());
            applisOpenSonar.add(app);
            return true;
        }
        
        // Gestion des composants sans application
        LOGNONLISTEE.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
        controlRapport.addInfo(TypeInfo.APPLINONREF, nom, application);
        composPbAppli.add(compo);
        return false;
    }

    /**
     * Permet d'enregistrer la liste des applications dans le fichier excel.
     */
    private void creerFichiersExtraction()
    {
        if (option == CreerVueParAppsTaskOption.VUE)
            throw new TechnicalException("Control.task.CreerVueParAppsTask.creerFichierExtraction - Demande de création d'extraction sans fichier", null);

        // Fichier Sonar
        ControlAppsW controlAppsW = ExcelFactory.getWriter(TypeColAppsW.class, file);
        controlAppsW.creerfeuilleSonar(applisOpenSonar);
        controlAppsW.write();
        
        // Fichier des problèmes des codes apllication       
        List<CompoPbApps> listePbApps = new ArrayList<>();
        
        for (ComposantSonar compo : composPbAppli)
        {
            CompoPbApps pbApps = ModelFactory.getModel(CompoPbApps.class);
            
            // Infos depuis le composant
            pbApps.setCodeComposant(compo.getNom());
            pbApps.setCodeAppli(compo.getAppli());
            if (compo.getLot().isEmpty())
            {
                pbApps.setLotRTC(COMPOSANSLOT);
                listePbApps.add(pbApps);
                continue;
            }
            pbApps.setLotRTC(compo.getLot());
            
            //  CPI Lot depuis la map RTC
            LotSuiviRTC lotSuiviRTC = Statics.fichiersXML.getMapLotsRTC().get(compo.getLot());
            pbApps.setCpiLot(lotSuiviRTC.getCpiProjet());
            
            // Departement, service et chef de service depuis la map Clarity
            new ControlModelInfo().controleClarity(pbApps, lotSuiviRTC.getProjetClarity());         
            
            listePbApps.add(pbApps);
        }
        
        // Ecriture fichier
        ControlPbApps controlPbApps = ExcelFactory.getWriter(TypeColPbApps.class, new File("pbApps1.xlsx"));
        controlPbApps.creerfeuille(listePbApps);       
        controlPbApps.write();  
    }

    /*---------- ACCESSEURS ----------*/
}
