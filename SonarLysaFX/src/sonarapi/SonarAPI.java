package sonarapi;

import static utilities.Statics.logger;

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

import model.enums.TypeParam;
import sonarapi.model.AjouterProjet;
import sonarapi.model.AjouterVueLocale;
import sonarapi.model.AssocierQG;
import sonarapi.model.Clef;
import sonarapi.model.Composant;
import sonarapi.model.Event;
import sonarapi.model.Issue;
import sonarapi.model.Issues;
import sonarapi.model.IssuesSimple;
import sonarapi.model.Message;
import sonarapi.model.ModeleSonar;
import sonarapi.model.Parametre;
import sonarapi.model.Projet;
import sonarapi.model.QualityGate;
import sonarapi.model.Retour;
import sonarapi.model.Validation;
import sonarapi.model.Vue;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class SonarAPI
{

    /*---------- ATTRIBUTS ----------*/

    private final WebTarget webTarget;
    private final String codeUser;
    private static final String AUTHORIZATION = "Authorization";
    private static final String HTTP = ": HTTP ";

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
    public SonarAPI(final String url, final String user, final String password)
    {
        webTarget = ClientBuilder.newClient().target(url);
        StringBuilder builder = new StringBuilder(user);
        builder.append(":");
        builder.append(password);
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
    }

    /**
     *
     * @param url
     *            Url du serveur SonarQube
     * @param user
     *            id de l'utilisateur
     * @param password
     *            mot de passe de l'utilisateur
     * @return Une instance singleton de l'api
     * @throws IOException
     * @throws SecurityException
     */
    public static SonarAPI getInstanceTest()
    {
        return new SonarAPI(Statics.proprietesXML.getMapParams().get(TypeParam.URLSONARTEST), "admin", "admin");
    }

    /*---------- METHODES PUBLIQUES GET ----------*/

    /**
     * Permet de remonter toutes les vues déjà créées
     * 
     * @return
     */
    public List<Vue> getVues()
    {
        final Response response = appelWebserviceGET("api/views/list");

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            logger.info("Liste des Vues retournées depuis Sonar");
            return response.readEntity(Retour.class).getListeVues();
        }
        else
        {
            logger.error("Impossible de retourner les vues depuis Sonar = " + "api/views/list");
        }

        return new ArrayList<>();
    }

    /**
     * Permet de vérifier si l'utilisateur a bien les accès à SonarQube
     * 
     * @return true si l'utilisateur a les accès.
     */
    public boolean verificationUtilisateur()
    {
        final Response response = appelWebserviceGET("api/authentication/validate");

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            logger.info("Utilisateur OK");
            return response.readEntity(Validation.class).isValid();
        }
        else
        {
            logger.info("Utilisateur KO");
        }

        return false;
    }

    /**
     * Remonte les données métriques spécifiques à un composant
     * 
     * @param composantKey
     *            clé du composant dans la base SonarQube
     * @param metricKeys
     *            clé des métriques désirées (issues, bugs, vulnerabilitie, etc..)
     * @return un objet de type {@link Composant} avec toutes les informations sur celui-ci
     * @throws SecurityException
     * @throws NoSuchFieldException
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
            {
                valeur.append(",");
            }
        }
        paramMetrics.setValeur(valeur.toString());

        // 2. appel du webservices
        final Response response = appelWebserviceGET("api/measures/component", paramComposant, paramMetrics);

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(Retour.class).getComponent();
        }
        else
        {
            logger.error(erreurAPI("measures/component") + paramComposant.getValeur());
            return new Composant();
        }
    }

    /**
     * Donne le nombre de problèmed de sécurité en cours et à prendre en compte d'un composant.
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
        final Response response = appelWebserviceGET("/api/issues/search", paramComposant, paramSeverities, paramSinceLeakPeriod, paramTypes, paramResolved);

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(IssuesSimple.class).getTotal();
        }
        else
        {
            logger.error(erreurAPI("issues/search") + paramComposant.getValeur());
            return 0;
        }
    }

    /**
     * Donne le nombre de problèmed de sécurité en cours et à prendre en compte d'un composant.
     * 
     * @param componentKey
     * @return
     */
    public List<Issue> getIssuesComposant(String componentKey)
    {
        // 1. Création des paramètres de la requête
        Parametre paramComposant = new Parametre("componentKeys", componentKey);
        Parametre paramSeverities = new Parametre("severities", "CRITICAL, BLOCKER");
        Parametre paramSinceLeakPeriod = new Parametre("sinceLeakPeriod", "true");
        Parametre paramResolved = new Parametre("resolved", "false");

        // 2. appel du webservices
        final Response response = appelWebserviceGET("/api/issues/search", paramComposant, paramSeverities, paramSinceLeakPeriod, paramResolved);

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(Issues.class).getListIssues();
        }
        else
        {
            logger.error(erreurAPI("issues/search") + paramComposant.getValeur());
            return new ArrayList<>();
        }
    }

    public String getVersionComposant(String resource)
    {
        // 1. Création des paramètres de la requête
        Parametre paramResource = new Parametre("resource", resource);
        Parametre paramCategorie = new Parametre("categories", "Version");

        // 2. appel du webservices
        final Response response = appelWebserviceGET("/api/events", paramResource, paramCategorie);

        // 3. Test du retour et renvoie de la dernière version si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            List<Event> liste = response.readEntity(new GenericType<List<Event>>() {
            });
            if (liste != null && !liste.isEmpty())
            {
                return controleVersion(liste);
            }
        }
        else
        {
            logger.error(erreurAPI("events") + paramResource.getValeur());
        }
        return "";
    }

    /**
     * Retourne tous les composants présents dans SonarQube
     * 
     * @return
     */
    public List<Projet> getComposants()
    {
        Parametre param = new Parametre("search", "composant ");
        final Response response = appelWebserviceGET("api/projects/index", param);

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            logger.info("Récupération de la liste des composants OK");
            return response.readEntity(new GenericType<List<Projet>>() {
            });
        }
        else
        {
            logger.error("Impossible de remonter tous les composants de Sonar - API : api/projects/index/search=composant ");
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Impossible de remonter tous les composants de Sonar - API : api/projects/index/search=composant ");
        }
    }

    /**
     * Remonte l'id de la QualityGate avec le nom donnée. Lance une erreur fonctionnelle si jamais, i ln'y a pas eu de QualityGate trouvée dans SonarQube.
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
        throw new FunctionalException(Severity.SEVERITY_ERROR, "impossible de trouver la Qualitygate avec le nom donné : " + nomQG);
    }

    /**
     * Permet de remonter la liste des QualityGate présentes dans SonarQube.
     * 
     * @return
     */
    public List<QualityGate> getListQualitygate()
    {
        Response response = appelWebserviceGET("api/qualitygates/list");

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(Retour.class).getQualityGates();
        }
        else
        {
            logger.error("Impossible de remonter les QualityGate de Sonar - API : api/qualitygates/list");
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Impossible de remonter les QualityGate de Sonar - API : api/qualitygates/list");
        }
    }

    /**
     * retourne si une vue existe ou non dans SonarQube. Pour cela, essaie de remonter les informations de la vue.<br>
     * Si HTTP 200 => retourne vrai.<br>
     * Si HTTP 404 => retourne faux <br>
     * Autre => retourne faux avec log erreur
     * 
     * @param vueKey
     * @return
     */
    public boolean testVueExiste(String vueKey)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La méthode sonarapi.SonarAPI.testVueExiste a son argument nul");

        Response response = appelWebserviceGET("api/views/show", new Parametre("key", vueKey));
        if (response.getStatus() == Status.OK.getStatusCode())
            return true;
        if (response.getStatus() == Status.NOT_FOUND.getStatusCode())
            return false;
        logger.error(erreurAPI("views/show") + vueKey);
        return false;
    }

    /*---------- METHODES PUBLIQUES POST ----------*/

    /**
     * Crée une vue dans SonarQube
     * 
     * @param vue
     * @return
     */
    public void creerVue(Vue vue)
    {
        if (vue == null)
            return;

        Response response = appelWebservicePOST("api/views/create", vue);
        logger.info("Creation vue : " + vue.getKey() + " - nom : " + vue.getName() + HTTP + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Crée une vue dans SonarQube asynchrone
     * 
     * @param vue
     * @return
     */
    public Future<Response> creerVueAsync(Vue vue)
    {
        return appelWebserviceAsyncPOST("api/views/create", vue);
    }

    /**
     * Supprime un projet dans SonarQube, avec ou non gestion des erreurs
     * 
     * @param vue
     * @return
     */
    public void supprimerProjet(Vue vue, boolean erreur)
    {
        if (vue == null)
            throw new IllegalArgumentException("La méthode sonarapi.SonarAPI.supprimerVue a son argument nul");

        supprimerProjet(vue.getKey(), erreur);
    }

    /**
     * Supprime un projet dans SonarQube depuis la clef avec ou non gestion des erreur, et vérifie que celle-ci n'existe plus pendant 2s.
     * 
     * @param vueKey
     *            clef de la vue à supprimer
     * @return
     */
    public void supprimerProjet(String vueKey, boolean erreur)
    {
        if (vueKey == null || vueKey.isEmpty())
            throw new IllegalArgumentException("La méthode sonarapi.SonarAPI.supprimerVue a son argument nul");

        Response response = appelWebservicePOST("api/projects/delete", new Clef(vueKey));
        logger.info("retour supprimer projet " + vueKey + " : " + response.getStatus() + " " + response.getStatusInfo());
        if (erreur)
            gestionErreur(response);
    }

    /**
     * Ajoute des sous-vue déjà existantes à une vue donnée
     * 
     * @param listeViews
     * @param parent
     * @return
     */
    public void ajouterSousVues(Collection<Vue> listeViews, Vue parent)
    {
        for (Vue vue : listeViews)
        {
            ajouterSousVue(vue, parent);
        }
    }

    /**
     * Ajoute une sous-vue déjà existantes à une vue donnée
     * 
     * @param listeViews
     * @param parent
     * @return
     */
    public void ajouterSousVue(Vue vue, Vue parent)
    {
        AjouterVueLocale localView = new AjouterVueLocale(parent.getKey(), vue.getKey());
        Response response = appelWebservicePOST("api/views/add_local_view", localView);
        logger.info("Vue " + parent.getName() + " ajout sous-vue " + vue.getName() + HTTP + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Ajoute des projets déjà existants à une vue donnée
     * 
     * @param listeViews
     * @param parent
     * @return
     */
    public void ajouterSousProjets(List<Projet> listeProjets, Vue parent)
    {
        for (Projet projet : listeProjets)
        {
            ajouterProjet(projet, parent);
        }
    }

    /**
     * Ajoute un projet déjà existant à une vue donnée
     * 
     * @param parent
     * @param projet
     */
    public void ajouterProjet(Projet projet, Vue parent)
    {
        AjouterProjet addProjet = new AjouterProjet(parent.getKey(), projet.getKey());
        Response response = appelWebservicePOST("api/views/add_project", addProjet);
        logger.info("Vue " + parent.getKey() + " ajout sous-projet " + projet.getNom() + HTTP + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Force la mise à jour de toutes les vues dans SonarQube.
     */
    public void majVues()
    {
        Response response = appelWebservicePOST("api/views/run", null);
        gestionErreur(response);
    }

    /**
     * Permet d'associer un QualityGate à un composant donné.
     * 
     * @param projet
     * @param qg
     */
    public void associerQualitygate(Projet projet, QualityGate qg)
    {
        AssocierQG assQG = new AssocierQG(qg.getId(), projet.getId());
        Response response = appelWebservicePOST("api/qualitygates/select", assQG);
        logger.info("projet " + projet.getNom() + " association " + qg.getName() + HTTP + response.getStatus());
        gestionErreur(response);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Appel des webservices en GET
     * 
     * @param url
     *            Url du webservices
     * @param params
     *            Paramètres optionnels de la requête
     * @return
     */
    public Response appelWebserviceGET(final String url, Parametre... params)
    {
        WebTarget requete = webTarget.path(url);

        if (params == null)
        {
            return requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).get();
        }

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
     *            Entite envoyée à la requete en paramètre. Utilise un objet implémentant l'interface {@link ModeleSonar}. Le paramètre peut être null s'il n'y a
     *            pas beaoin de paramètres.
     * @return
     */
    public Response appelWebservicePOST(final String url, ModeleSonar entite)
    {
        // Création ed la requête
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
        {
            return builder.post(Entity.text(""));
        }
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
    public Future<Response> appelWebserviceAsyncPOST(final String url, ModeleSonar entite)
    {
        // Création de la requête
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
        {
            return builder.async().post(Entity.text(""));
        }
        return builder.async().post(Entity.entity(entite, MediaType.APPLICATION_JSON));
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
            if (erreurs != null)
            {
                for (Message message : erreurs)
                {
                    logger.error(message.getMsg());
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Itération sur tous les Event retournés par le webService pour être sûr de bien retourner les informations du plus récent.
     * 
     * @param liste
     *            liste d'{@link sonarapi.model.Event} des changements de version
     * @return
     */
    private String controleVersion(List<Event> liste)
    {
        if (liste == null)
            throw new IllegalArgumentException("la liste ne peut pas être nulle");

        LocalDateTime date = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);
        String retour = "";
        // Itération sur la liste pour récupérer la date la plus récente.
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

    private String erreurAPI(String api)
    {
        return "Erreur API : api/" + api + " - Composant : ";
    }
}