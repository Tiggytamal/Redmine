package control.sonar;

import static utilities.Statics.EMPTY;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.util.AssertException;

import model.bdd.ComposantSonar;
import model.enums.Param;
import model.sonarapi.AjouterProjet;
import model.sonarapi.AjouterVueLocale;
import model.sonarapi.AssocierQG;
import model.sonarapi.Clef;
import model.sonarapi.Composant;
import model.sonarapi.Event;
import model.sonarapi.Issue;
import model.sonarapi.Issues;
import model.sonarapi.IssuesSimple;
import model.sonarapi.ManualMesure;
import model.sonarapi.Message;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import model.sonarapi.QualityGate;
import model.sonarapi.Retour;
import model.sonarapi.Validation;
import model.sonarapi.Vue;
import model.utilities.ModeleSonar;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Classe d'appel des webservices SonarQube 5.7
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class SonarAPI extends AbstractToStringImpl
{

    /*---------- ATTRIBUTS ----------*/
    
    /** logger g�n�ral */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private static final String AUTHORIZATION = "Authorization";
    private static final String HTTP = ": HTTP ";

    // Liste des api utilis�es
    private static final String VIEWSLIST = "api/views/list";
    private static final String PROJECTSINDEX = "api/projects/index";
    private static final String QGLIST = "api/qualitygates/list";
    private static final String ISSUESSEARCH = "api/issues/search";
    private static final String MEASURESCOMPONENT = "api/measures/component";
    private static final String EVENTS = "api/events";
    private static final String VIEWSSHOW = "api/views/show";
    private static final String VIEWSCREATE = "api/views/create";
    private static final String VIEWSMODE = "api/views/mode";    
    private static final String QGSELECT = "api/qualitygates/select";
    private static final String AUTHVALID = "api/authentication/validate";
    
    /** Instance du controleur */
    public static final SonarAPI INSTANCE = new SonarAPI();
    
    private final WebTarget webTarget;
    private final String codeUser;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Cr�ation d'une instance de l'API pour acc�der � SonarCube. La m�thode est priv�e.
     *
     * @param url
     *            Url du serveur SonarQube
     * @param user
     *            id de l'utilisateur
     * @param password
     *            mot de passe de l'utilisateur
     * @throws IOException
     * @throws SecurityException
     */
    private SonarAPI()
    {
        // Protection contre la cr�ation d'une nouvelle instance par r�flexion
        if (INSTANCE != null)
            throw new AssertException();

        webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
        builder.append(":");
        builder.append(Statics.info.getMotDePasse());
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
    }

    /*---------- METHODES PUBLIQUES GET ----------*/

    /**
     * Permet de remonter toutes les vues d�j� cr��es
     * 
     * @return
     */
    public List<Vue> getVues()
    {
        Response response = appelWebserviceGET(VIEWSLIST);

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("Liste des Vues retourn�es depuis Sonar");
            return response.readEntity(Retour.class).getListeVues();
        }
        else
            LOGGER.error("Impossible de retourner les vues depuis Sonar = " + VIEWSLIST);

        return new ArrayList<>();
    }

    /**
     * R�cup�re une liste de projet Sonar depuis un nom donn�.
     * 
     * @param nom
     * @return
     */
    public List<Projet> getVuesParNom(String nom)
    {
        // Param�tres
        Parametre paramSearch = new Parametre("search", nom);
        Parametre paramViews = new Parametre("views", "true");

        // Appel webService
        Response response = appelWebserviceGET(PROJECTSINDEX, new Parametre[] { paramSearch, paramViews });

        // Contr�le de la r�ponse
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("Liste des Vues tri�es retourn�es depuis Sonar");
            return response.readEntity(new GenericType<List<Projet>>() { });
        }
        else
            LOGGER.error("Impossible de retourner les vues depuis Sonar = " + PROJECTSINDEX);

        return new ArrayList<>();
    }

    /**
     * Permet de v�rifier si l'utilisateur a bien les acc�s � SonarQube
     * 
     * @return true si l'utilisateur a les acc�s.
     */
    public boolean verificationUtilisateur()
    {
        Response response = appelWebserviceGET(AUTHVALID);

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("Utilisateur OK");
            return response.readEntity(Validation.class).isValid();
        }
        else
            LOGGER.info("Utilisateur KO");

        return false;
    }

    /**
     * Remonte les donn�es m�triques sp�cifiques � un composant
     * 
     * @param composantKey
     *            cl� du composant dans la base SonarQube
     * @param metricKeys
     *            cl� des m�triques d�sir�es (issues, bugs, vulnerabilitie, etc..)
     * @return un objet de type {@link Composant} avec toutes les informations sur celui-ci
     */
    public Composant getMetriquesComposant(String composantKey, String[] metricKeys)
    {
        // 1. Cr�ation des param�tres
        Parametre paramComposant = new Parametre("componentKey", composantKey);

        Parametre paramMetrics = new Parametre();
        paramMetrics.setClef("metricKeys");
        StringBuilder valeur = new StringBuilder();
        for (int i = 0; i < metricKeys.length; i++)
        {
            valeur.append(metricKeys[i]);
            if (i + 1 < metricKeys.length)
                valeur.append(",");
        }
        paramMetrics.setValeur(valeur.toString());

        // 2. appel du webservices
        Response response = appelWebserviceGET(MEASURESCOMPONENT, new Parametre[] { paramComposant, paramMetrics });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Retour.class).getComponent();
        else
        {
            LOGGER.error(erreurAPI(MEASURESCOMPONENT) + paramComposant.getValeur());
            return new Composant();
        }
    }

    /**
     * Donne le nombre de probl�mes de s�curit� en cours et � prendre en compte d'un composant.
     * 
     * @param componentKey
     * @return
     */
    public int getSecuriteComposant(String componentKey)
    {
        // 1. Cr�ation des param�tres de la requ�te
        Parametre paramComposant = new Parametre("componentKeys", componentKey);
        Parametre paramSeverities = new Parametre("severities", "CRITICAL, BLOCKER");
        Parametre paramSinceLeakPeriod = new Parametre("sinceLeakPeriod", "true");
        Parametre paramTypes = new Parametre("types", "VULNERABILITY");
        Parametre paramResolved = new Parametre("resolved", "false");

        // 2. appel du webservices
        Response response = appelWebserviceGET(ISSUESSEARCH, new Parametre[] { paramComposant, paramSeverities, paramSinceLeakPeriod, paramTypes, paramResolved });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(IssuesSimple.class).getTotal();
        else
        {
            LOGGER.error(erreurAPI(ISSUESSEARCH) + paramComposant.getValeur());
            return 0;
        }
    }

    /**
     * Appel une recherche d'Issues avec les param�tres choisis sans pr�d�finition
     * 
     * @param parametres
     * @return
     */
    public List<Issue> getIssuesGenerique(Collection<Parametre> parametres)
    {
        // Variables
        List<Issue> retour = new ArrayList<>();
        int page = 0;
        Issues issues;

        // Mise ne place du param�tre de pagination
        Parametre paramPage = new Parametre("p", String.valueOf(page));
        parametres.add(paramPage);

        // Cr�ation array des param�tres
        Parametre[] paramsArray = parametres.toArray(new Parametre[0]);

        // Boucle pour r�cup�rer toutes les erreurs en paginant la requ�te
        do
        {
            page++;
            // MAJ Param�tre de pagination
            paramPage.setValeur(String.valueOf(page));

            // 2. appel du webservices
            Response response = appelWebserviceGET(ISSUESSEARCH, paramsArray);

            // 3. Test du retour et renvoie du composant si ok.
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                issues = response.readEntity(Issues.class);
                retour.addAll(issues.getListIssues());
            }
            else
            {
                LOGGER.error(erreurAPI(ISSUESSEARCH));
                return retour;
            }
        }
        while (page * issues.getPs() < issues.getTotal());

        return retour;
    }

    /**
     * Donne la liste des probl�mes en cours et � prendre en compte d'un composant.
     * 
     * @param componentKey
     * @return
     */
    public List<Issue> getIssuesComposant(String componentKey)
    {
        // 1. Cr�ation des param�tres de la requ�te
        Parametre paramComposant = new Parametre("componentKeys", componentKey);
        Parametre paramSeverities = new Parametre("severities", "CRITICAL, BLOCKER");
        Parametre paramSinceLeakPeriod = new Parametre("sinceLeakPeriod", "true");
        Parametre paramResolved = new Parametre("resolved", "false");
        Parametre paramPage;

        // Liste de retour
        List<Issue> retour = new ArrayList<>();
        int page = 0;
        Issues issues;

        // Boucle pour r�cup�rer toutes les erreurs en paginant la requ�te
        do
        {
            page++;
            // Param�tre de pagination
            paramPage = new Parametre("p", String.valueOf(page));

            // 2. appel du webservices
            Response response = appelWebserviceGET(ISSUESSEARCH, new Parametre[] { paramComposant, paramSeverities, paramSinceLeakPeriod, paramResolved, paramPage });

            // 3. Test du retour et renvoie du composant si ok.
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                issues = response.readEntity(Issues.class);
                retour.addAll(issues.getListIssues());
            }
            else
            {
                LOGGER.error(erreurAPI(ISSUESSEARCH) + paramComposant.getValeur());
                return retour;
            }
        }
        while (page * issues.getPs() < issues.getTotal());

        return retour;
    }

    /**
     * 
     * @param resource
     * @return
     */
    public String getVersionComposant(String resource)
    {
        // 1. Cr�ation des param�tres de la requ�te
        Parametre paramResource = new Parametre("resource", resource);
        Parametre paramCategorie = new Parametre("categories", "Version");

        // 2. appel du webservices
        Response response = appelWebserviceGET(EVENTS, new Parametre[] { paramResource, paramCategorie });

        // 3. Test du retour et renvoie de la derni�re version si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            List<Event> liste = response.readEntity(new GenericType<List<Event>>() { });
            if (liste != null && !liste.isEmpty())
                return controleVersion(liste);
        }
        else
            LOGGER.error(erreurAPI(EVENTS) + paramResource.getValeur());

        return EMPTY;
    }

    /**
     * Retourne tous les composants pr�sents dans SonarQube
     * 
     * @return
     */
    public List<Projet> getComposants()
    {
        Parametre param = new Parametre("search", "composant ");
        Response response = appelWebserviceGET(PROJECTSINDEX, new Parametre[] { param });

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("R�cup�ration de la liste des composants OK");
            return response.readEntity(new GenericType<List<Projet>>() { });
        }
        else
        {
            String erreur = "Impossible de remonter tous les composants de Sonar - API : " + PROJECTSINDEX + "/search=composant ";
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }

    /**
     * Remonte l'id de la QualityGate avec le nom donn�e. Lance une erreur fonctionnelle si jamais, i ln'y a pas eu de QualityGate trouv�e dans SonarQube.
     * 
     * @param nomQG
     * @return
     */
    public QualityGate getQualityGate(String nomQG)
    {
        List<QualityGate> liste = getListQualitygate();
        for (QualityGate qualityGate : liste)
        {
            if (qualityGate.getName().equals(nomQG))
                return qualityGate;
        }

        String erreur = "impossible de trouver la Qualitygate avec le nom donn� : " + nomQG;
        LOGGER.error(erreur);
        throw new FunctionalException(Severity.ERROR, erreur);
    }

    /**
     * Permet de remonter la liste des QualityGate pr�sentes dans SonarQube.
     * 
     * @return
     */
    public List<QualityGate> getListQualitygate()
    {
        Response response = appelWebserviceGET(QGLIST);

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Retour.class).getQualityGates();
        else
        {
            String erreur = "Impossible de remonter les QualityGate de Sonar - API : " + QGLIST;
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }
    
    public List<Event> getEventsComposant(String key)
    {
        Parametre param = new Parametre("resource", key);
        Parametre paramCategorie = new Parametre("categories", "Version");
        Response response = appelWebserviceGET(EVENTS, new Parametre[] { param, paramCategorie });

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(new GenericType<List<Event>>() { });
        else
        {
            String erreur = "Impossible de remonter tous les �v�nements de Sonar - API : " + EVENTS;
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }

    /**
     * retourne si une vue existe ou non dans SonarQube. Pour cela, essaie de remonter les informations de la vue. <br/>
     * Si HTTP 200 => retourne vrai. <br/>
     * Si HTTP 404 => retourne faux. <br/>
     * Autre => retourne faux avec log erreur
     * 
     * @param vueKey
     * @return
     */
    public boolean testVueExiste(String vueKey)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.testVueExiste a son argument nul ou vide");

        Response response = appelWebserviceGET(VIEWSSHOW, new Parametre[] { new Parametre("key", vueKey) });
        if (response.getStatus() == Status.OK.getStatusCode())
            return true;
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode())
            return false;
        LOGGER.error(erreurAPI(VIEWSSHOW) + vueKey);
        return false;
    }
    
    /**
     * Remonte toutes les informations d'une vue avec la liste des sous-vues.
     * 
     * @param vueKey
     * @return
     */
    public Vue getInfosEtListeSousVues(String vueKey)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.getListSousVues a son argument nul ou vide");
        
        Vue retour = new Vue(vueKey, "vue non trouv�e");
        
        Response response = appelWebserviceGET(VIEWSSHOW, new Parametre[] { new Parametre("key", vueKey) });
        if (response.getStatus() != Status.OK.getStatusCode())
        {
            LOGGER.error(erreurAPI(VIEWSSHOW) + vueKey);
            return retour;
        }
        
        return response.readEntity(Vue.class);        
    }

    /*---------- METHODES PUBLIQUES POST ----------*/

    /**
     * Cr�e une vue dans SonarQube
     * 
     * @param vue
     * @return
     */
    public Status creerVue(Vue vue)
    {
        if (!Vue.controleVue(vue))
            return Status.BAD_REQUEST;

        Response response = appelWebservicePOST(VIEWSCREATE, vue);
        LOGGER.info("Creation vue : " + vue.getKey() + " - nom : " + vue.getName() + HTTP + response.getStatus());
        gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /**
     * Cr�e une vue dans SonarQube asynchrone
     * 
     * @param vue
     * @return
     */
    public Future<Response> creerVueAsync(Vue vue)
    {
        return appelWebserviceAsyncPOST(VIEWSCREATE, vue);
    }

    /**
     * Supprime un projet dans SonarQube, avec ou non gestion des erreurs
     * 
     * @param vue
     */
    public Status supprimerProjet(Vue vue, boolean erreur)
    {
        if (!Vue.controleVue(vue))
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.supprimerProjet a son argument nul");

        return supprimerProjet(vue.getKey(), erreur);
    }

    /**
     * Supprime un projet dans SonarQube depuis la clef avec ou non gestion des erreur, et v�rifie que celle-ci n'existe plus pendant 2s.
     * 
     * @param vueKey
     *            clef de la vue � supprimer
     * @return
     */
    public Status supprimerProjet(String vueKey, boolean erreur)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.supprimerProjet a un argument nul");

        Response response = appelWebservicePOST("api/projects/delete", new Clef(vueKey));
        LOGGER.info("retour supprimer projet " + vueKey + " : " + HTTP + response.getStatus());
        if (erreur)
            gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /**
     * Supprime une vue dans SonarQube, avec ou non gestion des erreurs
     * 
     * @param vue
     */
    public Status supprimerVue(Vue vue, boolean erreur)
    {
        if (!Vue.controleVue(vue))
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.supprimerVue a son argument nul");

        return supprimerVue(vue.getKey(), erreur);
    }

    /**
     * Supprime une vue dans SonarQube depuis la clef avec ou non gestion des erreur.
     * 
     * @param vueKey
     *            clef de la vue � supprimer
     * @return
     */

    /**
     * Supprime une vue dans SonarQube depuis la clef avec ou non gestion des erreur.
     * 
     * @param vueKey
     *            clef de la vue � supprimer
     * @param erreur
     *            bool�en pour traiter ou non les erreurs
     * @return
     */
    public Status supprimerVue(String vueKey, boolean erreur)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La m�thode sonarapi.SonarAPI.supprimerVue a son argument nul");

        Response response = appelWebservicePOST("api/views/delete", new Clef(vueKey));
        LOGGER.info("retour supprimer vue " + vueKey + " : " + HTTP + response.getStatus());
        if (erreur)
            gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /**
     * Ajoute des sous-vue d�j� existantes � une vue donn�e
     * 
     * @param listeViews
     * @param parent
     */
    public void ajouterSousVues(Iterable<Vue> listeViews, Vue parent)
    {
        for (Vue vue : listeViews)
        {
            if (!Vue.controleVue(vue))
                ajouterSousVue(vue, parent);
        }
    }

    /**
     * Ajoute une sous-vue d�j� existantes � une vue donn�e
     * 
     * @param vue
     * @param parent
     */
    public boolean ajouterSousVue(Vue vue, Vue parent)
    {
        AjouterVueLocale localView = new AjouterVueLocale(parent.getKey(), vue.getKey());
        Response response = appelWebservicePOST("api/views/add_local_view", localView);
        LOGGER.info("Vue " + parent.getName() + " ajout sous-vue " + vue.getName() + HTTP + response.getStatus());
        return gestionErreur(response);
    }

    /**
     * Ajoute des projets d�j� existants � une vue donn�e
     * 
     * @param listeProjets
     * @param parent
     */
    public void ajouterSousProjets(Iterable<Projet> listeProjets, Vue parent)
    {
        for (Projet projet : listeProjets)
        {
            ajouterProjet(projet, parent);
        }
    }

    /**
     * Ajoute un projet d�j� existant � une vue donn�e
     * 
     * @param parent
     * @param projet
     */
    public boolean ajouterProjet(Projet projet, Vue parent)
    {
        AjouterProjet addProjet = new AjouterProjet(parent.getKey(), projet.getKey());
        Response response = appelWebservicePOST("api/views/add_project", addProjet);
        LOGGER.info("Vue " + parent.getKey() + " ajout sous-projet " + projet.getNom() + HTTP + response.getStatus());
        return gestionErreur(response);
    }

    /**
     * Ajoute un projet d�j� existant � une vue donn�e
     * 
     * @param parent
     * @param compo
     */
    public boolean ajouterProjet(ComposantSonar compo, Vue parent)
    {
        AjouterProjet addProjet = new AjouterProjet(parent.getKey(), compo.getKey());
        Response response = appelWebservicePOST("api/views/add_project", addProjet);
        LOGGER.info("Vue " + parent.getKey() + " ajout sous-projet " + compo.getNom() + HTTP + response.getStatus());
        return gestionErreur(response);
    }
    
    public boolean setManualMesureView(String key)
    {
        ManualMesure entite = new ManualMesure(key);
        Response response = appelWebservicePOST(VIEWSMODE, entite);
        return gestionErreur(response);
    }

    /**
     * Force la mise � jour de toutes les vues dans SonarQube.
     */
    public boolean majVues()
    {
        Response response = appelWebservicePOST("api/views/run", null);
        return gestionErreur(response);
    }

    /**
     * Permet d'associer un QualityGate � un composant donn�.
     * 
     * @param compo
     * @param qg
     */
    public boolean associerQualitygate(ComposantSonar compo, QualityGate qg)
    {
        AssocierQG assQG = new AssocierQG(qg.getId(), compo.getId());
        Response response = appelWebservicePOST(QGSELECT, assQG);
        LOGGER.info("projet " + compo.getNom() + " association " + qg.getName() + HTTP + response.getStatus());
        return gestionErreur(response);
    }

    /**
     * Appel des webservices en GET sans param�tres suppl�mentaires
     * 
     * @param url
     *            Url du webservices
     * @return
     */
    public Response appelWebserviceGET(String url)
    {
        WebTarget requete = webTarget.path(url);

        return requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).get();
    }

    /**
     * Appel des webservices en GET
     * 
     * @param url
     *            Url du webservices
     * @param params
     *            Param�tres optionnels de la requ�te
     * @return
     */
    public Response appelWebserviceGET(String url, Parametre[] params)
    {
        if (params == null)
            return appelWebserviceGET(url);
        
        WebTarget requete = webTarget.path(url);

        for (Parametre parametre : params)
        {
            requete = requete.queryParam(parametre.getClef(), parametre.getValeur());
        }

        return requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).get();
    }

    /**
     * Appel des webservices en POST
     * 
     * @param url
     *            Url du webservices
     * @param entite
     *            Entite envoy�e � la requete en param�tre. Utilise un objet impl�mentant l'interface {@link ModeleSonar}. Le param�tre peut �tre null s'il n'y a
     *            pas beaoin de param�tres.
     * @return
     */
    public Response appelWebservicePOST(String url, ModeleSonar entite)
    {
        // Cr�ation ed la requ�te
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
            return builder.post(Entity.text(EMPTY));

        return builder.post(Entity.entity(entite, MediaType.APPLICATION_JSON));
    }

    /**
     * Appel des webservices en POST en asynchrone
     * 
     * @param url
     *            Url du webservices
     * @param entite
     *            Entite envoy�e � la requete en param�tre. Utilise un objet impl�mentant l'interface {@link ModeleSonar}. Le param�tre peut �tre null s'il n'y a
     *            pas beaoin de param�tres.
     * @return
     */
    public Future<Response> appelWebserviceAsyncPOST(String url, ModeleSonar entite)
    {
        // Cr�ation de la requ�te
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
            return builder.async().post(Entity.text(EMPTY));

        return builder.async().post(Entity.entity(entite, MediaType.APPLICATION_JSON));
    }

    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * G�re les retours d'erreurs des Webservices.
     * 
     * @param response
     */
    private boolean gestionErreur(Response response)
    {
        if (response.getStatus() != Status.OK.getStatusCode() && response.getStatus() != Status.NO_CONTENT.getStatusCode())
        {
            List<Message> erreurs = response.readEntity(Retour.class).getErrors();
            if (erreurs != null)
            {
                for (Message message : erreurs)
                {
                    LOGPLANTAGE.error(message.getMsg());
                }
            }
            return false;
        }
        return true;
    }

    /**
     * It�ration sur tous les Event retourn�s par le webService pour �tre s�r de bien retourner les informations du plus r�cent.
     * 
     * @param liste
     *            liste d'{@link model.sonarapi.Event} des changements de version
     * @return
     */
    private String controleVersion(List<Event> liste)
    {
        if (liste == null)
            throw new IllegalArgumentException("la liste ne peut pas �tre nulle");

        LocalDateTime date = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);
        String retour = EMPTY;

        // It�ration sur la liste pour r�cup�rer la date la plus r�cente.
        for (Event event : liste)
        {
            LocalDateTime temp = LocalDateTime.parse(event.getDt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
            if (temp.isAfter(date))
            {
                date = temp;
                retour = event.getN();
            }
        }
        return retour;
    }

    /**
     * 
     * @param api
     * @return
     */
    private String erreurAPI(String api)
    {
        return "Erreur API : " + api + " - Composant : ";
    }
}
