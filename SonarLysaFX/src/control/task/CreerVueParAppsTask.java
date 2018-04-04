package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;
import static utilities.Statics.logSansApp;
import static utilities.Statics.loginconnue;
import static utilities.Statics.lognonlistee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.parent.SonarTask;
import junit.control.ControlSonarTest;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.Statics;
import utilities.Utilities;

public class CreerVueParAppsTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String TITRE = "Vues par Application";
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        // Pas de traitement pour cette tâche

    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParApplication();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @SuppressWarnings ("unchecked")
    private boolean creerVueParApplication()
    {
        // 1 .Création de la liste des composants par application
        Map<String, List<Projet>> mapApplication;

        if (ControlSonarTest.deser)
            mapApplication = Utilities.deserialisation("d:\\mapApplis.ser", HashMap.class);
        else
        {
            mapApplication = controlerSonarQube();
            Utilities.serialisation("d:\\mapApplis.ser", mapApplication);
        }

        // 2. Suppression des vues existantes
        
        // Message
        String base = "Suppression des vues existantes :" + NL;
        updateMessage(base);
        
        // Traitement
        List<Projet> listeVuesExistantes = api.getVuesParNom("APPLI MASTER ");
        for (int i = 0; i < listeVuesExistantes.size(); i++)
        {
            Projet projet = listeVuesExistantes.get(i);
            api.supprimerProjet(projet.getKey(), true);
            
            // Message
            updateMessage(base + projet.getNom());
            updateProgress(i, listeVuesExistantes.size());
        }

        // 3. Creation des nouvelles vues
        
        base = "Creation des nouvelles vues :" + NL;
        int i = 0;
        
        // Parcours de la liste pour créer chaque vue applicative avec ses composants
        for (Map.Entry<String, List<Projet>> entry : mapApplication.entrySet())
        {
            // Création de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);
            
            // Message
            updateMessage(base + "traitement : " + vue.getName() + NL);
            updateProgress(++i, mapApplication.entrySet().size());
            api.ajouterSousProjets(entry.getValue(), vue);
        }
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
        HashMap<String, List<Projet>> mapApplications = new HashMap<>();
        
        // Message
        String base = "Traitements des composants :" + NL;
        updateMessage(base);
        int i = 0;
        
        // Itération sur la liste des projets
        for (Projet projet : mapProjets.values())
        {            
            // Récupération du code application
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "application" });
            
            // Message
            updateMessage(base + composant.getNom());
            updateProgress(i++, mapProjets.size());

            // Test si la liste est vide, cela veut dire que le projet n'a pas de code application.
            if (!composant.getMetriques().isEmpty())
            {
                String application = composant.getMetriques().get(0).getValue().trim().toUpperCase();

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, projet.getNom()))
                    continue;

                // Mise à jour de la map de retour avec en clef, le code application et en valeur : la liste des projets
                // liés.
                if (mapApplications.keySet().contains(application))
                    mapApplications.get(application).add(projet);
                else
                {
                    List<Projet> liste = new ArrayList<>();
                    liste.add(projet);
                    mapApplications.put(application, liste);
                }
            }
            else
                logSansApp.warn("Application non renseignée - Composant : " + projet.getNom());
        }
        return mapApplications;
    }
    
    /**
     * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
     *
     * @param application
     *            Application enregistrée pour le composant dans Sonar.
     * @param nom
     *            Nom du composant Sonar.
     */
    private boolean testAppli(String application, String nom)
    {
        if (application.equals(Statics.INCONNUE))
        {
            loginconnue.warn("Application : INCONNUE - Composant : " + nom);
            return false;
        }

        Map<String, Boolean> vraiesApplis = fichiersXML.getMapApplis();

        if (vraiesApplis.keySet().contains(application))
        {
            if (vraiesApplis.get(application))
                return true;

            lognonlistee.warn("Application obsolète : " + application + " - composant : " + nom);
            return false;
        }
        lognonlistee.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
        return false;
    }
}
