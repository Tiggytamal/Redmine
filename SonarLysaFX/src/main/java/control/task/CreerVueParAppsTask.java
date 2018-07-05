package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.mail.ControlMail;
import model.enums.TypeInfoMail;
import model.enums.TypeMail;
import model.sonarapi.Composant;
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
        controlMail = new ControlMail();
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParApplication();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    

    private boolean creerVueParApplication()
    {
        // 1 .Création de la liste des composants par application
        @SuppressWarnings ("unchecked")
        Map<String, List<Projet>> mapApplication = Utilities.recuperation(Main.DESER, Map.class, "mapApplis.ser", this::controlerSonarQube);

        // 2. Suppression des vues existantes
        
        // Message        
        String base = "Suppression des vues existantes :" + NL;
        etapePlus();
        updateMessage(base);
        updateProgress(0, 1);
        
        // Traitement
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
        for (Map.Entry<String, List<Projet>> entry : mapApplication.entrySet())
        {            
            // Création de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);
            
            // Message
            String baseVue = base + "traitement : " + vue.getName() + NL;
            updateMessage(baseVue);
            updateProgress(++i, size);
            for (Projet projet : entry.getValue())
            {
                updateMessage(baseVue + "Ajout : " + projet.getNom());
                api.ajouterProjet(projet, vue);
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
    private Map<String, List<Projet>> controlerSonarQube()
    {
        // Récupération des composants Sonar
        Map<String, Projet> mapProjets = recupererComposantsSonar();
        return creerMapApplication(mapProjets);
    }
    
    /**
     * Crée une map de toutes les apllications dans Sonar avec pour chacunes la liste des composants liés.
     *
     * @param mapProjets
     * @return
     */
    private HashMap<String, List<Projet>> creerMapApplication(Map<String, Projet> mapProjets)
    {
        // Initialisation de la map
        HashMap<String, List<Projet>> retour  = new HashMap<>();
        
        // Message
        String base = "Traitements des composants :" + NL;
        updateMessage(base);
        int i = 0;
        inconnues = 0;
        
        // Itération sur la liste des projets
        for (Projet projet : mapProjets.values())
        {            
            // Message
            updateMessage(base + projet.getNom());
            updateProgress(++i, mapProjets.size());
            
            // Récupération du code application
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "application" });
            
            // Test si la liste est vide, cela veut dire que le projet n'a pas de code application.
            if (!composant.getMetriques().isEmpty())
            {
                String application = composant.getMetriques().get(0).getValue().trim().toUpperCase();

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, projet.getNom()))
                    continue;

                // Mise à jour de la map de retour avec en clef, le code application et en valeur : la liste des projets
                // liés.
                if (retour.keySet().contains(application))
                    retour.get(application).add(projet);
                else
                {
                    List<Projet> liste = new ArrayList<>();
                    liste.add(projet);
                    retour.put(application, liste);
                }
            }
            else
            {
                LOGSANSAPP.warn("Application non renseignée - Composant : " + projet.getNom());
                controlMail.addInfo(TypeInfoMail.COMPOSANSAPP, projet.getNom(), null);
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

        Map<String, Boolean> vraiesApplis = fichiersXML.getMapApplis();

        if (vraiesApplis.keySet().contains(application))
        {
            if (vraiesApplis.get(application))
                return true;

            LOGNONLISTEE.warn("Application obsolète : " + application + " - composant : " + nom);
            controlMail.addInfo(TypeInfoMail.APPLIOBSOLETE, nom, application);
            return false;
        }
        LOGNONLISTEE.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
        controlMail.addInfo(TypeInfoMail.APPLINONREF, nom, application);
        return false;
    }
}