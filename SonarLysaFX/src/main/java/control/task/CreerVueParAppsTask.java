package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.excel.ControlAppsW;
import control.excel.ExcelFactory;
import control.mail.ControlMail;
import model.Application;
import model.ComposantSonar;
import model.enums.TypeColApps;
import model.enums.TypeInfoMail;
import model.enums.TypeMail;
import model.sonarapi.Projet;
import model.sonarapi.Vue;
import utilities.Statics;
import utilities.Utilities;

public class CreerVueParAppsTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String TITRE = "Vues par Application";
    private int inconnues;
    private ControlMail controlMail;
    private Set<Application> applisOpenSonar;
    private List<Application> applisOpenNonSonar;
    private Map<String, Application> applications;
    
    /** logger composants sans applications */
    private static final Logger LOGSANSAPP = LogManager.getLogger("sansapp-log");
    /** logger composants avec application INCONNUE*/
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    /** logger applications non listée dans le référentiel */
    private static final Logger LOGNONLISTEE = LogManager.getLogger("nonlistee-log");
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public CreerVueParAppsTask()
    {
        super(3);
        annulable = false;
        inconnues = 0;
        applications = fichiersXML.getMapApplis();
        controlMail = new ControlMail();
        applisOpenNonSonar = initApplisOpen();
        applisOpenSonar = new HashSet<>();

        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParApplication();
    }

    /*---------- METHODES PRIVEES ----------*/    

    private boolean creerVueParApplication()
    {
        // 1 .Création de la liste des composants par application
        @SuppressWarnings ("unchecked")
        Map<String, List<ComposantSonar>> mapApplication = Utilities.recuperation(Main.DESER, Map.class, "mapApplis.ser", this::controlerSonarQube);

        // 2. Suppression des vues existantes
        
        // Message        
        String base = "Suppression des vues existantes :" + NL;
        etapePlus();
        updateMessage(base);
        updateProgress(0, 1);
        
        // SUpression anciennes vues
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
            updateProgress(++i, size);
            for (ComposantSonar composantSonar : entry.getValue())
            {
                updateMessage(baseVue + "Ajout : " + composantSonar.getNom());
                api.ajouterProjet(composantSonar, vue);
            }
        }

        controlMail.envoyerMail(TypeMail.VUEAPPS);
        return true;
    }
    
    /**
     * Remonte les composants par application de SonarQube et créée les logs sur les defaults.
     *
     * @return map des composants SonarQube par application
     */
    private Map<String, List<ComposantSonar>> controlerSonarQube()
    {
        // Récupération des composants Sonar
        Map<String, ComposantSonar> mapCompos = recupererComposantsSonar();
        HashMap<String, List<ComposantSonar>> retour = creerMapApplication(mapCompos);
        creerFichierExtraction();
        
        return retour;
    }

    /**
     * Crée une map de toutes les apllications dans Sonar avec pour chacunes la liste des composants liés.
     *
     * @param mapCompos
     * @return
     */
    private HashMap<String, List<ComposantSonar>> creerMapApplication(Map<String, ComposantSonar> mapCompos)
    {
        // Initialisation de la map
        HashMap<String, List<ComposantSonar>> retour  = new HashMap<>();
        
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
            updateProgress(++i, mapCompos.size());
            
            // Test si le code application est vide, cela veut dire que le projet n'a pas de code application.
            if (!compo.getAppli().isEmpty())
            {
                String application = compo.getAppli().trim().toUpperCase();

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, compo.getNom()))
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
                controlMail.addInfo(TypeInfoMail.COMPOSANSAPP, compo.getNom(), null);
            }
        }
        LOGINCONNUE.info("Nombre d'applis inconnues : " + inconnues);
        return retour;
    }
    
    /**
     * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
     *
     * @param application
     *            Application enregistrée pour le composant dans Sonar.
     * @param nom
     *            Nom du composant Sonar.
     * @param inconnues 
     */
    private boolean testAppli(String application, String nom)
    {
        if (application.equalsIgnoreCase(Statics.INCONNUE))
        {
            LOGINCONNUE.warn("Application : INCONNUE - Composant : " + nom);
            inconnues++;
            return true;
        }

        if (applications.containsKey(application))
        {
            Application app = applications.get(application);
            if (app.isActif())
            {
                applisOpenNonSonar.remove(app);
                applisOpenSonar.add(app);
                return true;
            }

            LOGNONLISTEE.warn("Application obsolète : " + application + " - composant : " + nom);
            controlMail.addInfo(TypeInfoMail.APPLIOBSOLETE, nom, application);
            return false;
        }
        LOGNONLISTEE.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
        controlMail.addInfo(TypeInfoMail.APPLINONREF, nom, application);
        return false;
    }
    
    /**
     * 
     * @return
     */
    private List<Application> initApplisOpen()
    {
        List<Application> retour = new ArrayList<>();
        
        for (Application app : applications.values())
        {
            if (app.isActif() && app.isOpen())
                retour.add(app);
        }
        
        return retour;
    }
    
    /**
     * Permet d'enregistrer la liste des applications dans le fichier excel.
     */
    private void creerFichierExtraction()
    {
        ControlAppsW control = ExcelFactory.getWriter(TypeColApps.class, new File("d:\\testExtractApps.xlsx"));
        
        control.creerfeuilleSonar(applisOpenSonar);
        control.creerfeuilleNonSonar(applisOpenNonSonar);       
        control.write();
    }
    
    /*---------- ACCESSEURS ----------*/
}