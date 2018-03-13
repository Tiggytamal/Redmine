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
     * Permet de remonter toutes les vues d�j� cr��es
     * 
     * @return
     */
    public List<Vue> getVues()
    {
        final Response response = appelWebserviceGET("api/views/list");

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            logger.info("Liste des Vues retourn�es depuis Sonar");
            return response.readEntity(Retour.class).getListeVues();
        }
        else
        {
            logger.error("Impossible de retourner les vues depuis Sonar = " + "api/views/list");
        }

        return new ArrayList<>();
    }

    /**
     * Permet de v�rifier si l'utilisateur a bien les acc�s � SonarQube
     * 
     * @return true si l'utilisateur a les acc�s.
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
     * Remonte les donn�es m�triques sp�cifiques � un composant
     * 
     * @param composantKey
     *            cl� du composant dans la base SonarQube
     * @param metricKeys
     *            cl� des m�triques d�sir�es (issues, bugs, vulnerabilitie, etc..)
     * @return un objet de type {@link Composant} avec toutes les informations sur celui-ci
     * @throws SecurityException
     * @throws NoSuchFieldException
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
            logger.error("Erreur API : api/measures/component - Composant : " + paramComposant.getValeur());
            return new Composant();
        }
    }

    /**
     * Donne le nombre de probl�med de s�curit� en cours et � prendre en compte d'un composant.
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
        final Response response = appelWebserviceGET("/api/issues/search", paramComposant, paramSeverities, paramSinceLeakPeriod, paramTypes, paramResolved);

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(IssuesSimple.class).getTotal();
        }
        else
        {
            logger.error("Erreur API : api/issues/search - Composant : " + paramComposant.getValeur());
            return 0;
        }
    }

    /**
     * Donne le nombre de probl�med de s�curit� en cours et � prendre en compte d'un composant.
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

        // 2. appel du webservices
        final Response response = appelWebserviceGET("/api/issues/search", paramComposant, paramSeverities, paramSinceLeakPeriod, paramResolved);

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(Issues.class).getListIssues();
        }
        else
        {
            logger.error("Erreur API : api/issues/search - Composant : " + paramComposant.getValeur());
            return new ArrayList<>();
        }
    }

    public String getVersionComposant(String resource)
    {
        // 1. Cr�ation des param�tres de la requ�te
        Parametre paramResource = new Parametre("resource", resource);
        Parametre paramCategorie = new Parametre("categories", "Version");

        // 2. appel du webservices
        final Response response = appelWebserviceGET("/api/events", paramResource, paramCategorie);

        // 3. Test du retour et renvoie de la derni�re version si ok.
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
            logger.error("Erreur API : /api/issues/search - Composant : " + paramResource.getValeur());
        }
        return "";
    }

    /**
     * Retourne tous les composants pr�sents dans SonarQube
     * 
     * @return
     */
    public List<Projet> getComposants()
    {
        Parametre param = new Parametre("search", "composant ");
        final Response response = appelWebserviceGET("api/projects/index", param);

        if (response.getStatus() == Status.OK.getStatusCode())
        {
            logger.info("R�cup�ration de la liste des composants OK");
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
        throw new FunctionalException(Severity.SEVERITY_ERROR, "impossible de trouver la Qualitygate avec le nom donn� : " + nomQG);
    }

    /**
     * Permet de remonter la liste des QualityGate pr�sentes dans SonarQube.
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

    /*---------- METHODES PUBLIQUES POST ----------*/

    /**
     * Cr�e une vue dans SonarQube
     * 
     * @param vue
     * @return
     */
    public void creerVue(Vue vue)
    {
        if (vue == null)
            return;

        Response response = appelWebservicePOST("api/views/create", vue);
        logger.info("Creation vue : " + vue.getKey() + " - nom : " + vue.getName() + ": HTTP " + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Cr�e une vue dans SonarQube asynchrone
     * 
     * @param vue
     * @return
     */
    public Future<Response> creerVueAsync(Vue vue)
    {
        return appelWebserviceAsyncPOST("api/views/create", vue);
    }

    /**
     * Supprime une vue dans SonarQube
     * 
     * @param vue
     * @return
     */
    public void supprimerVue(Vue vue)
    {
        if (vue == null)
            return;

        Response response = appelWebservicePOST("api/views/delete", new Clef(vue.getKey()));
        logger.info("retour supprimer vue " + vue.getKey() + " : " + response.getStatus() + " " + response.getStatusInfo());
        gestionErreur(response);
    }

    /**
     * Ajoute des sous-vue d�j� existantes � une vue donn�e
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
     * Ajoute une sous-vue d�j� existantes � une vue donn�e
     * 
     * @param listeViews
     * @param parent
     * @return
     */
    public void ajouterSousVue(Vue vue, Vue parent)
    {
        AjouterVueLocale localView = new AjouterVueLocale(parent.getKey(), vue.getKey());
        Response response = appelWebservicePOST("api/views/add_local_view", localView);
        logger.info("Vue " + parent.getName() + " ajout sous-vue " + vue.getName() + " : HTTP " + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Ajoute des projets d�j� existants � une vue donn�e
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
     * Ajoute un projet d�j� existant � une vue donn�e
     * 
     * @param parent
     * @param projet
     */
    public void ajouterProjet(Projet projet, Vue parent)
    {
        AjouterProjet addProjet = new AjouterProjet(parent.getKey(), projet.getKey());
        Response response = appelWebservicePOST("api/views/add_project", addProjet);
        logger.info("Vue " + parent.getKey() + " ajout sous-projet " + projet.getNom() + ": HTTP " + response.getStatus());
        gestionErreur(response);
    }

    /**
     * Force la mise � jour de toutes les vues dans SonarQube.
     */
    public void majVues()
    {
        Response response = appelWebservicePOST("api/views/run", null);
        gestionErreur(response);
    }
    
    public void associerQualitygate(Projet projet, QualityGate qg)
    {
        AssocierQG assQG = new AssocierQG(qg.getId(), projet.getId());
        Response response = appelWebservicePOST("api/qualitygates/select", assQG);
        logger.info("projet " + projet.getNom() + " association " + qg.getName() + ": HTTP " + response.getStatus());
        gestionErreur(response);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Appel des webservices en GET
     * 
     * @param url
     *            Url du webservices
     * @param params
     *            Param�tres optionnels de la requ�te
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
     *            Entite envoy�e � la requete en param�tre. Utilise un objet impl�mentant l'interface {@link ModeleSonar}. Le param�tre peut �tre null s'il n'y a
     *            pas beaoin de param�tres.
     * @return
     */
    public Response appelWebservicePOST(final String url, ModeleSonar entite)
    {
        // Cr�ation ed la requ�te
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
     *            Entite envoy�e � la requete en param�tre. Utilise un objet impl�mentant l'interface {@link ModeleSonar}. Le param�tre peut �tre null s'il n'y a
     *            pas beaoin de param�tres.
     * @return
     */
    public Future<Response> appelWebserviceAsyncPOST(final String url, ModeleSonar entite)
    {
        // Cr�ation de la requ�te
        WebTarget requete = webTarget.path(url);
        Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);

        if (entite == null)
        {
            return builder.async().post(Entity.text(""));
        }
        return builder.async().post(Entity.entity(entite, MediaType.APPLICATION_JSON));
    }

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
                    logger.error(message.getMsg());
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
     *            liste d'{@link sonarapi.model.Event} des changements de version
     * @return
     */
    private String controleVersion(List<Event> liste)
    {
        if (liste == null)
            throw new IllegalArgumentException("la liste ne peut pas �tre nulle");

        LocalDateTime date = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);
        String retour = "";
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
}