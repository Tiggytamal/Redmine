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
     * Constructeur utilisant les donn�es de l'utilisateur
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
     * Utilis�e pour permettre le retour arri�re si possible du traitement
     */
    public abstract void annuler();
    
    /**
     * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar
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
        return retour;
    }
    
    /**
     * Permet de r�cup�rer les composants de Sonar tri�s par version avec s�pration des composants datastage
     *
     * @return
     */
    protected Map<String, List<Projet>> recupererComposantsSonarVersion(Boolean datastage)
    {
        // R�cup�ration des versions en param�tre
        String[] versions = proprietesXML.getMapParams().get(TypeParam.VERSIONS).split("-");

        // Appel du webservice pour remonter tous les composants
        List<Projet> projets = api.getComposants();

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
                    // utilisant le filtre en param�tre. Si le Boolean est nul, on
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
        {
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
        }

        // Cr�ation de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valoris�e
        if (description != null)
        {
            vue.setDescription(description);
        }

        // Suppresison de la vue pr�cedente
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