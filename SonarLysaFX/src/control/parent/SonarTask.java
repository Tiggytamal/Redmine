package control.parent;

import static utilities.Statics.info;
import static utilities.Statics.proprietesXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.concurrent.Task;
import junit.control.ControlSonarTest;
import model.enums.TypeParam;
import sonarapi.SonarAPI;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.FunctionalException;
import utilities.Utilities;
import utilities.enums.Severity;

public abstract class SonarTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/

    protected SonarAPI api;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    /**
     * Constructeur utilisant les données de l'utilisateur
     */
    protected SonarTask()
    {
        if (!info.controle())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), info.getPseudo(), info.getMotDePasse());
    }
    
    /**
     * Constructeur acceptant des valeurs externes pour le pseudo et le mot de passe
     * 
     * @param pseudo
     * @param mdp
     */
    protected SonarTask(String pseudo, String mdp)
    {
        if (pseudo == null || pseudo.isEmpty() || mdp == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "pseudo et/ou mdp vides");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), pseudo, mdp);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Utilisée pour permettre le retour arrière si possible du traitement
     */
    public abstract void annuler();
    
    /**
     * Permet de récupérer la dernière version de chaque composants créés dans Sonar
     *
     * @return
     */
    @SuppressWarnings ("unchecked")
    protected Map<String, Projet> recupererComposantsSonar()
    {
        // Appel du webservice pour remonter tous les composants
        List<Projet> projets;

        if (ControlSonarTest.deser)
        {
            projets = Utilities.deserialisation("d:\\composants.ser", List.class);
        }
        else
        {
            projets = api.getComposants();
            Utilities.serialisation("d:\\composants.ser", projets);
        }

        // Triage ascendant de la liste par nom de projet
        projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D*");

        // Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caractères créées par la regex comme clef dans la map.
        // Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de
        // version obsolète.
        Map<String, Projet> retour = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getNom());
            if (matcher.find())
            {
                retour.put(matcher.group(0), projet);
            }
        }
        return retour;
    }
    
    /**
     * Permet de récupérer les composants de Sonar triés par version avec sépration des composants datastage
     *
     * @return
     */
    protected Map<String, List<Projet>> recupererComposantsSonarVersion(Boolean datastage)
    {
        // Récupération des versions en paramètre
        String[] versions = proprietesXML.getMapParams().get(TypeParam.VERSIONS).split("-");

        // Appel du webservice pour remonter tous les composants
        List<Projet> projets = api.getComposants();

        // Création de la map de retour en utilisant les versions données
        Map<String, List<Projet>> retour = new HashMap<>();

        for (String version : versions)
        {
            retour.put(version, new ArrayList<>());
        }

        // Itération sur les projets pour remplir la liste de retour
        for (Projet projet : projets)
        {
            for (String version : versions)
            {
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans
                // version E32
                if (projet.getNom().endsWith(Utilities.transcoEdition(version)))
                {
                    // Selon que l'on regarde les composants datastage ou non, on remplie la liste en conséquence en
                    // utilisant le filtre en paramètre. Si le Boolean est nul, on
                    // prend tous les composants
                    String filtre = proprietesXML.getMapParams().get(TypeParam.FILTREDATASTAGE);
                    if (datastage == null || datastage && projet.getNom().startsWith(filtre) || !datastage && !projet.getNom().startsWith(filtre))
                        retour.get(version).add(projet);
                }
            }
        }
        return retour;
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
    protected Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contrôle
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
        }

        // Création de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valorisée
        if (description != null)
        {
            vue.setDescription(description);
        }

        // Suppresison de la vue précedente
        if (suppression)
        {
            api.supprimerProjet(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }
   
    /*---------- ACCESSEURS ----------*/  
}