package control.sonar;

import static utilities.Statics.EMPTY;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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
import model.enums.TypeDonnee;
import model.interfaces.ModeleSonar;
import model.sonarapi.Clef;
import model.sonarapi.Composant;
import model.sonarapi.IssuesSimple;
import model.sonarapi.Message;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import model.sonarapi.Retour;
import model.sonarapi.Validation;
import model.sonarapi67.Analyse;
import model.sonarapi67.Analyses;
import model.sonarapi67.Connexion;
import model.sonarapi67.Event;
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
    private static final String VERSION = "VERSION";

    // Liste des api utilisées
    private static final String VIEWSLIST = "api/views/list";
    private static final String PROJECTSINDEX = "api/projects/index";
    private static final String QGLIST = "api/qualitygates/list";
    private static final String ISSUESSEARCH = "api/issues/search";
    private static final String MEASURESCOMPONENT = "api/measures/component";
    private static final String ANALYSES = "api/project_analyses/search";
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
        Response response = appelWebserviceGET(PROJECTSINDEX);

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            LOGGER.info("Récupération de la liste des composants OK");
            return response.readEntity(new GenericType<List<Projet>>() { });
        }
        else
        {
            String erreur = "Impossible de remonter tous les composants de Sonar - API : " + PROJECTSINDEX;
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }

    /**
     * Remonte les données métriques spécifiques à un composant
     * 
     * @param composantKey
     *            clé du composant dans la base SonarQube
     * @param metricKeys
     *            clé des métriques désirées (issues, bugs, vulnerabilitie, etc..)
     * @return un objet de type {@link Composant} avec toutes les informations sur celui-ci
     */
    public Composant getMetriquesComposant(String composantKey, String[] metricKeys)
    {
        // 1. Création des paramètres
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
     * Donne le nombre de problèmes de sécurité en cours et à prendre en compte d'un composant.
     * 
     * @param componentKey
     * @return
     */
    public int getSecuriteComposant(String componentKey)
    {
        // 1. Création des paramètres de la requête
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
     * Vérifie si la date de mise à jour du composant est antérieure à la dernière date de mise à jour de la base des composants. En plus met à la jour la version
     * du composant ERLEASE ou SNAPCHOT.
     * 
     * @param compo
     * @return
     */
    public boolean initCompoVersionEtDateMaj(ComposantSonar compo)
    {
        // 1. Création des paramètres de la requête
        Parametre paramResource = new Parametre("project", compo.getKey());
        Parametre paramCategorie = new Parametre("category", VERSION);

        // 2. appel du webservices
        Response response = appelWebserviceGET(ANALYSES, new Parametre[] { paramResource, paramCategorie });

        // 3. Test du retour et renvoie de la dernière version si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            Analyses analyses = response.readEntity(Analyses.class);
            if (analyses != null)
                return controleVersionEtDateMaj(analyses, compo);
        }
        else
            LOGGER.error(erreurAPI(ANALYSES) + paramResource.getValeur());

        return false;
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
     * Itération sur tous les Event retournés par le webService pour être sûr de bien retourner les informations du plus récent.
     * 
     * @param analyses
     *            liste d'{@link model.sonarapi.Event} des changements de version
     * @return
     */
    private boolean controleVersionEtDateMaj(Analyses analyses, ComposantSonar compo)
    {
        if (analyses == null)
            throw new IllegalArgumentException("la liste ne peut pas être nulle");

        LocalDateTime date = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);
        String version = EMPTY;

        // Itération sur la liste pour récupérer la date la plus récente.
        for (Analyse analyse : analyses.getListAnalyses())
        {
            LocalDateTime temp = LocalDateTime.parse(analyse.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
            if (temp.isAfter(date))
            {
                date = temp;
                for (Event event : analyse.getEvent())
                {
                    if (VERSION.equals(event.getCategory()))
                        version = event.getName();
                }
            }
        }
        compo.setVersionRelease(!version.contains("SNAPSHOT"));
        return date.isAfter(DaoFactory.getDao(DateMaj.class).recupEltParIndex(TypeDonnee.COMPOSANT.toString()).getTimeStamp());
    }

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
