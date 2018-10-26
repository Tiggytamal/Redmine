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

import dao.DaoFactory;
import model.bdd.ComposantSonar;
import model.bdd.DateMaj;
import model.enums.Param;
import model.enums.TypeDonnee;
import model.interfaces.ModeleSonar;
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
import model.sonarapi67.Connexion;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Classe d'appel des webservices SonarQube 6 et 7
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class SonarAPI67 extends AbstractToStringImpl
{

    /*---------- ATTRIBUTS ----------*/

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private static final String AUTHORIZATION = "Authorization";
    private static final String HTTP = ": HTTP ";
    private static final String RESOURCE = "resource";
    private static final String CATEGORIES = "categories";
    private static final String VERSION = "Version";

    // Liste des api utilisées
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
    private static final String AUTHLOGIN = "api/authentication/login";

    /** Instance du controleur */
    public static final SonarAPI67 INSTANCE = new SonarAPI67();

    private final WebTarget webTarget;
    private final String codeUser;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Création d'une instance de l'API pour accéder à SonarCube. La méthode est privée.
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
    private SonarAPI67()
    {
        // Protection contre la création d'une nouvelle instance par réflexion
        if (INSTANCE != null)
            throw new AssertException();

        webTarget = ClientBuilder.newClient().target("https://snar-mob.collab.ca-technologies.credit-agricole.fr");
        StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
        builder.append(":");
        builder.append(Statics.info.getMotDePasse());
        codeUser = "Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes());
    }

    /*---------- METHODES PUBLIQUES GET ----------*/
    
    /**
     * Retourne tous les composants présents dans SonarQube
     * 
     * @return
     */
    public List<Projet> getComposants()
    {
        Parametre param = new Parametre("search", "composant ");
        Response response = appelWebserviceGET(PROJECTSINDEX, new Parametre[] { param });

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("Récupération de la liste des composants OK");
            return response.readEntity(new GenericType<List<Projet>>() {
            });
        }
        else
        {
            String erreur = "Impossible de remonter tous les composants de Sonar - API : " + PROJECTSINDEX + "/search=composant ";
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }
    
    /*---------- METHODES PUBLIQUES POST ----------*/


    /**
     * Permet de vérifier si l'utilisateur a bien les accès à SonarQube
     * 
     * @return true si l'utilisateur a les accès.
     */
    public boolean connexionUtilisateur()
    {
        Connexion connexion = new Connexion(Statics.info.getPseudo(), Statics.info.getMotDePasse());
        Response response = appelWebservicePOST(AUTHLOGIN, connexion);

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
     * Supprime un projet dans SonarQube depuis la clef avec ou non gestion des erreur, et vérifie que celle-ci n'existe plus pendant 2s.
     * 
     * @param vueKey
     *            clef de la vue à supprimer
     * @return
     */
    public Status supprimerProjet(String vueKey, boolean erreur)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La méthode sonarapi.SonarAPI.supprimerProjet a un argument nul");

        Response response = appelWebservicePOST("api/projects/delete", new Clef(vueKey));
        LOGGER.info("retour supprimer projet " + vueKey + " : " + HTTP + response.getStatus());
        if (erreur)
            gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /*---------- APPELS GENERIQUES ----------*/

    /**
     * Appel des webservices en GET sans paramètres supplémentaires
     * 
     * @param url
     *            Url du webservices
     * @return
     */
    public Response appelWebserviceGET(String url)
    {
        WebTarget requete = webTarget.path(url);

        return requete.request().header(AUTHORIZATION, codeUser).get();
    }

    /**
     * Appel des webservices en GET
     * 
     * @param url
     *            Url du webservices
     * @param params
     *            Paramètres optionnels de la requête
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

        return requete.request().header(AUTHORIZATION, codeUser).get();
    }

    /**
     * Appel des webservices en POST
     * 
     * @param url
     *            Url du webservices
     * @param entite
     *            Entite envoyée à la requete en paramètre. Utilise un objet implémentant l'interface {@link ModeleSonar}. Le paramètre peut être null s'il n'y a
     *            pas beaoin de paramètres.
     * @return
     */
    public Response appelWebservicePOST(String url, ModeleSonar entite)
    {
        // Création ed la requête
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request();

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
     *            Entite envoyée à la requete en paramètre. Utilise un objet implémentant l'interface {@link ModeleSonar}. Le paramètre peut être null s'il n'y a
     *            pas beaoin de paramètres.
     * @return
     */
    public Future<Response> appelWebserviceAsyncPOST(String url, ModeleSonar entite)
    {
        // Création de la requête
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
            return builder.async().post(Entity.text(EMPTY));

        return builder.async().post(Entity.entity(entite, MediaType.APPLICATION_JSON));
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Gère les retours d'erreurs des Webservices.
     * 
     * @param response
     */
    private boolean gestionErreur(Response response)
    {
        if (response.getStatus() != Status.OK.getStatusCode() && response.getStatus() != Status.NO_CONTENT.getStatusCode())
        {
            List<Message> erreurs = response.readEntity(Retour.class).getErrors();

            for (Message message : erreurs)
            {
                LOGPLANTAGE.error(message.getMsg());
            }

            return false;
        }
        return true;
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
