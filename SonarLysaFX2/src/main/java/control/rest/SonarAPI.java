package control.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;

import control.task.AbstractTask;
import control.task.maj.ExceptionTraiterCompo;
import model.bdd.ComposantBase;
import model.bdd.IssueBase;
import model.bdd.Utilisateur;
import model.enums.InstanceSonar;
import model.enums.Metrique;
import model.enums.OptionGestionErreur;
import model.enums.OptionGetCompos;
import model.enums.Param;
import model.enums.QG;
import model.enums.Severity;
import model.enums.TypeBranche;
import model.enums.TypeObjetSonar;
import model.parsing.ComposantJSON;
import model.rest.sonarapi.Analyse;
import model.rest.sonarapi.Analyses;
import model.rest.sonarapi.Branche;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Composants;
import model.rest.sonarapi.DetailRegle;
import model.rest.sonarapi.Event;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Issues;
import model.rest.sonarapi.Message;
import model.rest.sonarapi.ObjetSonar;
import model.rest.sonarapi.ParamAPI;
import model.rest.sonarapi.QualityGate;
import model.rest.sonarapi.QualityGates;
import model.rest.sonarapi.QualityProfile;
import model.rest.sonarapi.QualityProfiles;
import model.rest.sonarapi.Regle;
import model.rest.sonarapi.Regles;
import model.rest.sonarapi.Retour;
import model.rest.sonarapi.StatutQGProjet;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe d'appel des webservices SonarQube 6 et 7.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class SonarAPI extends AbstractControlRest
{

    /*---------- ATTRIBUTS ----------*/

    /** key générique pour les appels aux webservices et la création des clefs des vues */
    public static final String KEY = "key";

    /** logger général */
    private static final Logger LOGGER = Utilities.getLogger("complet-log");

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    // Liste des api utilisees
    private static final String APPLICREATE = "api/applications/create";
    private static final String APPLIDELETE = "api/applications/delete";
    private static final String APPLIADD = "api/applications/add_project";
    private static final String AUTHLOGIN = "api/authentication/login";
    private static final String COMPOSEARCH = "api/components/search";
    private static final String COMPOSEARCHALL = "api/components/search_projects";
    private static final String COMPOSHOW = "api/components/show";
    private static final String ISSUESASSIGN = "api/issues/assign";
    private static final String ISSUESSEARCH = "api/issues/search";
    private static final String MEASURESCOMPONENT = "api/measures/component";
    private static final String ANALYSES = "api/project_analyses/search";
    private static final String PROJECTSINDEX = "api/projects/index";
    private static final String PROJECTBRANCHLIST = "api/project_branches/list";
    private static final String PROJECTDELETE = "api/projects/delete";
    private static final String QUALITYGATELIST = "api/qualitygates/list";
    private static final String QUALITYGATESELECT = "api/qualitygates/select";
    private static final String PROJECTQGINFO = "api/qualitygates/project_status";
    private static final String QUALITYGATESHOW = "api/qualitygates/show";
    private static final String QUALITYPROFILE = "api/qualityprofiles/search";
    private static final String RULESSEARCH = "api/rules/search";
    private static final String RULESSHOW = "api/rules/show";
    private static final String PORTFOLIOADDSUBPF = "api/views/add_local_view";
    private static final String PORTFOLIOADD = "api/views/add_project";
    private static final String PORTFOLIOCREATE = "api/views/create";
    private static final String PORTFOLIODELETE = "api/views/delete";
    private static final String PORTFOLIOCALCUL = "api/views/refresh";

    // Constantes statiques
    private static final String PROJECT = "project";
    private static final String HTTP = ": HTTP ";
    private static final String VERSION = "VERSION";
    private static final String APPLI = "application";
    private static final String BRANCH = "branch";
    private static final String ERREURAPI = "Erreur au moment de l'appel de l'API : ";
    private static final Pattern DATE4 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}$");
    private static final Pattern DATE2 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$");

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Création d'une instance de l'API pour accéder à SonarCube.
     *
     * @param urlSonar
     *                 Url du serveur SonarQube.
     */
    private SonarAPI(Param urlSonar)
    {
        super(urlSonar);
    }

    /**
     * Création d'une instance de l'API pour accéder à SonarCube. Par defaut on utlise l'instance LEGACY
     * 
     * @return
     *         L'instance de l'API.
     */
    public static SonarAPI build()
    {
        return new SonarAPI(Param.URLSONAR);
    }

    /**
     * Création d'une instance de l'API pour accéder à SonarQube. Par defaut on utlise l'instance LEGACY
     * 
     * @param instance
     *                 L'instance du serveur Sonar désiré.
     * @return
     *         L'instance de l'API.
     */
    public static SonarAPI build(InstanceSonar instance)
    {
        if (instance == null || instance == InstanceSonar.LEGACY)
            return new SonarAPI(Param.URLSONAR);
        else
            return new SonarAPI(Param.URLSONARMC);
    }

    /*---------- METHODES PUBLIQUES GET ----------*/

    /**
     * Retourne les composants présent dans SonarQube selon l'option choisie:<br>
     * COMPLETE : Tous les composant plus les branches présentes dans SonarQube.<br>
     * MINIMALE : Les composants analysés depuis la dernière mise à jour sans prise en compte des branches.<br>
     * PARTIELLE : Les composants et les branches analysés depuis la dernière mise à jour sans prise en compte des branches.
     * 
     * @param option
     *               Option de traitement.
     * @param task
     *               Tâche parente.
     * @return
     *         La liste des composants présents dans SonarQube.
     */
    public List<ComposantSonar> getComposants(OptionGetCompos option, AbstractTask task)
    {
        List<ComposantSonar> retour = getComposantsSansBranche(task);

        // Traitement composants plantes
        for (Iterator<ComposantSonar> iter = retour.iterator(); iter.hasNext();)
        {
            if (iter.next().getDateAnalyse() == null)
                iter.remove();
        }

        // On ne traite pas les branche dans le cas d'une MAJ minimale.
        if (option == OptionGetCompos.MINIMALE)
            return retour;

        traitementBranches(retour, task);
        return retour;
    }

    /**
     * Retourne tous les composants présents dans SonarQube sans prise en compte des branches.
     * 
     * @param task
     *             Tâche parente.
     * @return
     *         La liste des composants présents dans le serveur Sonar.
     */
    public List<ComposantSonar> getComposantsSansBranche(AbstractTask task)
    {
        // 1. Récupération de tous les composants SonarQube.

        // Variables
        List<ComposantSonar> retour = new ArrayList<>();
        int page = 0;
        Composants composants;
        int nbreCompos = 0;
        int totalCompos = 0;

        // Mise en place des paramètres
        ParamAPI[] params = new ParamAPI[3];
        ParamAPI paramPage = new ParamAPI("p", String.valueOf(page));
        params[0] = paramPage;
        params[1] = new ParamAPI("ps", "500");
        params[2] = new ParamAPI("f", "analysisDate");

        // Boucle pour récupérer toutes les erreurs en paginant la requête
        do
        {
            // MAJ Paramètre de pagination
            page++;
            paramPage.setValeur(String.valueOf(page));

            // 2. appel du webservice
            Response response = appelWebserviceGET(COMPOSEARCHALL, params);

            // 3. Test du retour et renvoie du composant si ok.
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                composants = response.readEntity(Composants.class);
                retour.addAll(composants.getListeComposants());
            }
            else
            {
                LOGGER.error(erreurAPI(COMPOSEARCHALL));
                return retour;
            }

            // 4. affichage de l'avancée du traitement.
            nbreCompos = page * composants.getPaging().getPageSize();
            totalCompos = composants.getPaging().getTotal();
            if (task != null)
                task.updateProgress(nbreCompos, totalCompos);
        }
        while (nbreCompos < totalCompos);

        return retour;
    }

    /**
     * Retourne le nombre de compoasnt SonarQube avec un Quality Gate rouge.
     * 
     * @return
     */
    public int getNbreComposQGKO()
    {
        Response response = appelWebserviceGET(COMPOSEARCHALL, new ParamAPI("filter", Metrique.QG.getValeur() + "=" + QG.ERROR.getValeur()));

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Composants.class).getPaging().getTotal();
        else
        {
            LOGGER.error(erreurAPI(COMPOSEARCHALL));
            return -1;
        }
    }

    /**
     * Remonte les details d'un composant depuis SonarQube.
     * 
     * @param compoKey
     *                     Clef du composant traité.
     * @param compoBranche
     *                     Nom de la branche du composant traité.
     * @return
     *         Le CompoasntSonar contenant les informations détaillées.
     */
    public ComposantSonar getDetailsComposant(String compoKey, String compoBranche)
    {
        // 1. Création des paramètres
        ParamAPI paramComposant = new ParamAPI("component", compoKey);
        ParamAPI paramBranche = new ParamAPI(BRANCH, compoBranche);

        // 2. appel du webservices
        Response response = appelWebserviceGET(COMPOSHOW, new ParamAPI[]
        { paramComposant, paramBranche });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Retour.class).getComposant();
        else
        {
            LOGPLANTAGE.error(erreurAPI(COMPOSHOW) + paramComposant.getValeur());
            return null;
        }
    }

    /**
     * Remonte les données métriques spécifiques à un composant.
     * 
     * @param compoKey
     *                     Clé du composant dans la base SonarQube.
     * @param compoBranche
     *                     Nom de la branche du composant dans la base SonarQube.
     * @param metricKeys
     *                     Clé des metriques desirées (issues, bugs, vulnerabilitie, etc..).
     * @return
     *         Un objet de type {@link ComposantSonar} avec toutes les informations sur celui-ci.
     */
    public ComposantSonar getMesuresComposant(String compoKey, String compoBranche, String[] metricKeys)
    {
        // 1. Création des paramètres
        ParamAPI paramComposant = new ParamAPI("componentKey", compoKey);
        ParamAPI paramBranche = new ParamAPI(BRANCH, compoBranche);

        StringBuilder valeur = new StringBuilder();
        for (int i = 0; i < metricKeys.length; i++)
        {
            valeur.append(metricKeys[i]);
            if (i + 1 < metricKeys.length)
                valeur.append(',');
        }
        ParamAPI paramMetrics = new ParamAPI("metricKeys", valeur.toString());

        // 2. appel du webservices
        Response response = appelWebserviceGET(MEASURESCOMPONENT, new ParamAPI[]
        { paramComposant, paramMetrics, paramBranche });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Retour.class).getComposant();
        else
        {
            LOGPLANTAGE.error(erreurAPI(MEASURESCOMPONENT) + paramComposant.getValeur());
            return null;
        }
    }

    /**
     * Permet de remonter la version courante d'un composant depuis SonarQube.
     * 
     * @param compoBase
     *                  Composant traité.
     * @return
     *         La verions du composant.
     */
    public String getVersionComposant(ComposantBase compoBase)
    {
        ParamAPI paramKey = new ParamAPI(PROJECT, compoBase.getKey());
        ParamAPI paramBranche = new ParamAPI(BRANCH, compoBase.getBranche());
        ParamAPI paramCat = new ParamAPI("category", VERSION);
        Response response = appelWebserviceGET(ANALYSES, new ParamAPI[]
        { paramKey, paramBranche, paramCat });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() != Status.OK.getStatusCode())
        {
            LOGPLANTAGE.error(erreurAPI(ANALYSES));
            return Statics.EMPTY;
        }

        List<Analyse> analyses = response.readEntity(Analyses.class).getListAnalyses();
        for (Analyse analyse : analyses)
        {
            if (format(analyse.getDate()).equals(compoBase.getDerniereAnalyse()))
            {
                for (Event event : analyse.getEvents())
                {
                    if (VERSION.equals(event.getCategorie()))
                        return event.getNom();
                }
            }
        }

        LOGPLANTAGE
                .error("control.rest.SonarAPI.getVersionComposant - Impossible de trouver la version à la date de dernière analyse : " + compoBase.getNom() + " - " + compoBase.getDerniereAnalyse());
        return Statics.EMPTY;
    }

    /**
     * Appel une recherche d'Issues avec les paramètres choisis sans prédéfinition.
     * 
     * @param parametres
     *                   Collection des paramètres pour effectuer la recherche.
     * @return
     *         La liste des Issue corespondants à la recherche.
     */
    public List<Issue> getIssuesGenerique(Collection<ParamAPI> parametres)
    {
        // Variables
        List<Issue> retour = new ArrayList<>();
        int page = 0;
        Issues issues;

        // Mise ne place du paramètre de pagination
        ParamAPI paramPage = new ParamAPI("p", String.valueOf(page));
        parametres.add(paramPage);

        // Création array des paramètres
        ParamAPI[] paramsArray = parametres.toArray(new ParamAPI[0]);

        // Boucle pour récupérer toutes les erreurs en paginant la requete
        do
        {
            page++;
            // MAJ Paramètre de pagination
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
                LOGPLANTAGE.error(erreurAPI(ISSUESSEARCH));
                return retour;
            }
        }
        while (page * issues.getPs() < issues.getTotal());

        return retour;
    }

    /**
     * Assigne une issue de SonarQube depuis la base des anomalies non assignées.
     * 
     * @param issue
     *              Issue à assigner.
     * @return
     *         Vrai si l'Issue a été assignée.
     */
    public boolean assignerIssue(IssueBase issue)
    {
        Utilisateur user = issue.getUtilisateur();

        // Création de la requete
        WebTarget requete = webTarget.path(ISSUESASSIGN).queryParam("issue", issue.getKey()).queryParam("assignee", user.getIdentifiant());

        // Appel du webservice et gestion des erreurs
        Response response = appelWebservicePOST(requete);

        // Contrôle du retour - Si tout est bon, on récupère l'ETP de l'auteur
        if (response.getStatus() == Status.OK.getStatusCode() || response.getStatus() == Status.NO_CONTENT.getStatusCode())
            return true;

        return gestionErreur(response);
    }

    /**
     * Retourne un profile qualite depuis son nom dans SonarQube.
     * 
     * @param qpName
     *               Nom du profilQualité à erchercher.
     * @return
     *         Le profil qualité recherché ou null is rien n'a été trouvé.
     */
    public QualityProfile getQualityProfileByName(String qpName)
    {
        // 1. Création des paramètres de la requete
        ParamAPI paramNom = new ParamAPI("qualityProfile", qpName);
        ParamAPI paramLanguage = new ParamAPI("language", "java");

        // 2. appel du webservice
        Response response = appelWebserviceGET(QUALITYPROFILE, new ParamAPI[]
        { paramNom, paramLanguage });

        // 3. Test du retour et renvoie du QualityGate
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            QualityProfiles qps = response.readEntity(QualityProfiles.class);
            if (!qps.getProfiles().isEmpty())
                return qps.getProfiles().get(0);
        }

        // retour objet null et log si pas de retour du wbeservice
        LOGPLANTAGE.error(erreurAPI(QUALITYPROFILE) + paramNom.getValeur());
        return null;
    }

    /**
     * Retourne toutes les régles liées à un profil qualité.
     * 
     * @param qp
     *           Profil traité.
     * @return
     *         La liste des règle du profil.
     */
    public List<Regle> getRulesByQualityProfile(QualityProfile qp)
    {
        return getRulesByQualityProfile(qp.getKey());
    }

    /**
     * Retourne toutes les régles liees à un projet qualite depuis la clef de celui-ci.
     * 
     * @param qualityProfileKey
     *                          Clé du profil qualité traité.
     * @return
     *         La liste des règles du profil.
     */
    public List<Regle> getRulesByQualityProfile(String qualityProfileKey)
    {
        // 1. Création des paramètres de la requête
        ParamAPI paramPage;
        ParamAPI parmaProfile = new ParamAPI("qprofile", qualityProfileKey);
        ParamAPI paramActivation = new ParamAPI("activation", "true");
        ParamAPI paramPs = new ParamAPI("ps", "500");
        List<Regle> retour = new ArrayList<>();
        int page = 0;
        Regles rules;

        // Boucle pour récupérer toutes les erreurs en paginant la requête
        do
        {
            page++;
            // Paramètre de pagination
            paramPage = new ParamAPI("p", String.valueOf(page));

            // 2. appel du webservice
            Response response = appelWebserviceGET(RULESSEARCH, new ParamAPI[]
            { parmaProfile, paramPage, paramPs, paramActivation });

            // 3. Test du retour et renvoie du composant si ok.
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                rules = response.readEntity(Regles.class);
                retour.addAll(rules.getListRegles());
            }
            else
            {
                LOGPLANTAGE.error(erreurAPI(RULESSEARCH) + parmaProfile.getValeur());
                return retour;
            }
        }
        while (page * rules.getPs() < rules.getTotal());

        return retour;
    }

    /**
     * Retourne une liste de régles selon les paramètres definis
     * 
     * @param parametres
     *                   Paramètres de la recherche.
     * @return
     *         La liste des règles recherchées.
     */
    public List<Regle> getReglesGenerique(Collection<ParamAPI> parametres)
    {
        // Variables
        List<Regle> retour = new ArrayList<>();
        int page = 0;
        Regles issues;

        // Mise ne place du paramètre de pagination
        ParamAPI paramPage = new ParamAPI("p", String.valueOf(page));
        parametres.add(paramPage);

        // Création array des paramètres
        ParamAPI[] paramsArray = parametres.toArray(new ParamAPI[0]);

        // Boucle pour récupérer toutes les erreurs en paginant la requete
        do
        {
            page++;
            // MAJ Paramètre de pagination
            paramPage.setValeur(String.valueOf(page));

            // 2. appel du webservices
            Response response = appelWebserviceGET(RULESSEARCH, paramsArray);

            // 3. Test du retour et renvoie du composant si ok.
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                issues = response.readEntity(Regles.class);
                retour.addAll(issues.getListRegles());
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
     * Récupération de tous les details d'une régle
     * 
     * @param regleKey
     *                 Clef de la règle.
     * @return
     *         Objet contenant tous les détails de la règle.
     */
    public DetailRegle getDetailsRegle(String regleKey)
    {
        ParamAPI paramActive = new ParamAPI("actives", "true");
        ParamAPI paramKey = new ParamAPI("key", regleKey);
        Response response = appelWebserviceGET(RULESSHOW, new ParamAPI[]
        { paramActive, paramKey });

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(DetailRegle.class);
        else
        {
            LOGGER.error(erreurAPI(RULESSHOW));
            return null;
        }
    }

    /**
     * Remonte l'id de la QualityGate avec le nom donné. Lance une erreur fonctionnelle si jamais, il n'y a pas eu de QualityGate trouvé dans SonarQube.
     * 
     * @param nomQG
     *              Nom du QualityGate recherché.
     * @return
     *         Le QualityGate recherché.
     */
    public QualityGate getQualityGate(String nomQG)
    {
        // Récupération de la liste des qualityGates
        List<QualityGate> liste = getListQualitygate();
        for (QualityGate qualityGate : liste)
        {
            // Itération sur la liste pour trouver le QualityGate desire.
            if (qualityGate.getNom().equals(nomQG))
            {
                // Appel du webservice pour obtenir les conditions de contrôle du QualityGate
                Response response = appelWebserviceGET(QUALITYGATESHOW, new ParamAPI("name", qualityGate.getNom()));
                if (response.getStatus() == Status.OK.getStatusCode())
                {
                    QualityGate show = response.readEntity(QualityGate.class);
                    qualityGate.setConditions(show.getConditions());
                }
                else
                {
                    String erreur = ERREURAPI + QUALITYGATESHOW;
                    LOGGER.error(erreur);
                    throw new FunctionalException(Severity.ERROR, erreur);
                }
                return qualityGate;
            }
        }

        throw new FunctionalException(Severity.ERROR, "impossible de trouver la Qualitygate avec le nom donne : " + nomQG);
    }

    /**
     * Permet de remonter la liste des QualityGate présentes dans SonarQube.
     * 
     * @return
     *         La liste des QualityGate.
     */
    public List<QualityGate> getListQualitygate()
    {
        Response response = appelWebserviceGET(QUALITYGATELIST);

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(QualityGates.class).getListeQualityGates();
        else
        {
            String erreur = ERREURAPI + QUALITYGATELIST;
            LOGGER.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }

    /**
     * Récupère le statut d'un projet depuis une analyse donnée
     * 
     * @param analyseUUID
     *                    Numéro de l'analyse dans SonarQube.
     * @return
     *         Objet contenant les information de l'analyse, null si rine n'est trouvé.
     */
    public StatutQGProjet getAnalyseQGInfos(String analyseUUID)
    {
        // 1. appel du webservice
        Response response = appelWebserviceGET(PROJECTQGINFO, new ParamAPI("analysisId", analyseUUID));

        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Retour.class).getStatutProjet();
        else
        {
            LOGPLANTAGE.error("Erreur au moment de retrouver les infos d'un composant depuis l'analyse donnée : " + analyseUUID);
            return null;
        }
    }

    /**
     * Retourne la date de l'analyse qui a générée le fichier JSON donné.
     * 
     * @param compoJSON
     *                  Objet JAVA correspondant au fichier JSON.
     * @return
     *         Une date avec la time-zone.
     * @throws ExceptionTraiterCompo
     *                               Exception spécifique au traitement.
     */
    public ZonedDateTime getDateAnalyse(ComposantJSON compoJSON) throws ExceptionTraiterCompo
    {
        String date = LocalDate.of(2019, 1, 1).toString();
        ParamAPI paramKey = new ParamAPI(PROJECT, compoJSON.getProjet().getKey());
        ParamAPI paramBranche = new ParamAPI(BRANCH, compoJSON.getBranche().getNom());
        ParamAPI pparamDate = new ParamAPI("from", date);
        Response response = appelWebserviceGET(ANALYSES, new ParamAPI[]
        { paramKey, paramBranche, pparamDate });

        // 3. Test du retour et renvoie du composant si ok.
        if (response.getStatus() != Status.OK.getStatusCode())
        {
            LOGPLANTAGE.error(erreurAPI(ANALYSES));
            throw new ExceptionTraiterCompo("Impossible de trouver la date de l'analyse (plantage API) : " + compoJSON.getProjet().getKey());
        }

        List<Analyse> analyses = response.readEntity(Analyses.class).getListAnalyses();
        for (Analyse analyse : analyses)
        {
            if (analyse.getKey().equals(compoJSON.getIdAnalyse()))
                return formatZoned(analyse.getDate());
        }
        LOGPLANTAGE.error("Impossible de trouver la date de l'analyse (Analyse obsolète) : " + compoJSON.getProjet().getKey());
        throw new ExceptionTraiterCompo("Impossible de trouver la date de l'analyse (Analyse obsolète) : " + compoJSON.getProjet().getKey());
    }

    /**
     * Récupère une liste de projet Sonar depuis un nom donne et/ou le type donné.
     * 
     * @param nom
     *             Nom de l'objet Sonar.
     * @param type
     *             Type de l'objet Sonar. Enumération regroupant tous les types possibles. Obligatoire.
     * @return
     *         La liste des objets correspondants à la requête.
     */
    public List<ComposantSonar> getObjetSonarParNomOuType(String nom, TypeObjetSonar type)
    {
        // Contrôle
        if (type == null)
            throw new TechnicalException("control.rest.SonarAPI.getObjetSonarParNomOuType - type null");
        // Variables
        List<ComposantSonar> retour = new ArrayList<>();
        List<ParamAPI> params = new ArrayList<>();
        int page = 0;
        Composants composants;

        // Mise en place des paramètres
        ParamAPI paramPage = new ParamAPI("p", String.valueOf(page));
        params.add(paramPage);

        if (nom != null && !nom.isEmpty())
            params.add(new ParamAPI("q", nom));
        params.add(new ParamAPI("qualifiers", type.getValeur()));
        ParamAPI[] paramsArray = params.toArray(new ParamAPI[0]);

        do
        {
            // MAJ Paramètre de pagination
            page++;
            paramPage.setValeur(String.valueOf(page));

            // Appel webService
            Response response = appelWebserviceGET(COMPOSEARCH, paramsArray);

            // Contrôle de la reponse
            if (response.getStatus() == Status.OK.getStatusCode())
            {
                composants = response.readEntity(Composants.class);
                retour.addAll(composants.getListeComposants());
            }
            else
            {
                LOGGER.error(ERREURAPI + PROJECTSINDEX);
                return retour;
            }

        }
        while (page * composants.getPaging().getPageSize() < composants.getPaging().getTotal());

        return retour;
    }

    /*---------- METHODES PUBLIQUES POST ----------*/

    /**
     * Permet d'associer un QualityGate à un composant donné.
     * 
     * @param compo
     *              Composant à traiter.
     * @param qg
     *              QualityGate à associer.
     * @return
     *         Vrai si l'association a été faite.
     */
    public boolean associerQualitygate(ComposantBase compo, QualityGate qg)
    {
        return associerQualitygate(compo, qg.getId());
    }

    /**
     * Permet d'associer un QualityGate à un composant donne depuis l'id du QualityGate
     * 
     * @param compo
     *              Composant à traiter.
     * @param qgId
     *              Id du QualityGate à associer.
     * @return
     *         Vrai si l'association a été faite.
     */
    public boolean associerQualitygate(ComposantBase compo, String qgId)
    {
        // Création requete.
        WebTarget requete = webTarget.path(QUALITYGATESELECT).queryParam("gateId", qgId).queryParam("projectKey", compo.getKey());

        // Appel webservice et gestion erreur.
        Response response = appelWebservicePOST(requete);
        LOGGER.info("projet " + compo.getNom() + " - Association QualityGate Id " + qgId + HTTP + response.getStatus());
        return gestionErreur(response);
    }

    /**
     * Créer d'un objet dans le serveur Sonar. (PortFolio, Appli, autre types non gérés).
     * 
     * @param objet
     *              L'objet à creer.
     * @return
     *         Le status du traitement.
     */
    public Status creerObjetSonar(ObjetSonar objet)
    {
        if (!ObjetSonar.controle(objet))
            return Status.BAD_REQUEST;

        WebTarget requete;

        if (objet.getType() == TypeObjetSonar.PORTFOLIO)
            requete = webTarget.path(PORTFOLIOCREATE);
        else if (objet.getType() == TypeObjetSonar.APPLI)
            requete = webTarget.path(APPLICREATE);
        else
            throw new TechnicalException("control.rest.SonarAPI.creerObjetSonar - type objet non gere : " + objet.getType());

        requete = requete.queryParam("name", objet.getNom()).queryParam(KEY, objet.getKey()).queryParam("description", objet.getDescription());

        Response response = appelWebservicePOST(requete);
        LOGGER.info("Creation objet : " + objet.getKey() + " - nom : " + objet.getNom() + HTTP + response.getStatus());
        gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /**
     * Calcul d'un portfolio/application dans SonarQube.
     * 
     * @param pf
     *           Objet à calculer.
     * @return
     *         Le status du traitement.
     */
    public Status calculObjetSonar(ObjetSonar pf)
    {
        // Contrôle de l'objet
        if (!ObjetSonar.controle(pf))
            return Status.BAD_REQUEST;

        // Création de la requete
        WebTarget requete = webTarget.path(PORTFOLIOCALCUL).queryParam(KEY, pf.getKey());

        // Appel du webservice et gestion des erreurs
        Response response = appelWebservicePOST(requete);
        LOGGER.info("Calcul objet : " + pf.getKey() + " - nom : " + pf.getNom() + HTTP + response.getStatus());
        gestionErreur(response);
        return response.getStatusInfo().toEnum();
    }

    /**
     * Supression d'un objet Sonar (Portfolio/ Application)
     * 
     * @param objet
     *               Objet à supprimer du serveur.
     * @param option
     *               Option pour savoir si les erreurs sont gérées ou non.
     * @return
     *         Le status du traitement.
     */
    public Status supprimerObjetSonar(ObjetSonar objet, OptionGestionErreur option)
    {
        // Contrôle du pf
        if (!ObjetSonar.controle(objet))
            return Status.BAD_REQUEST;

        // Appel methode suppression
        return supprimerObjetSonar(objet.getKey(), objet.getType(), option);
    }

    /**
     * Supression d'un objet Sonar depuis sa clef et son type.
     * 
     * @param key
     *               Clef de l'objet Sonar à supprimer.
     * @param type
     *               Type de l'objet Sonar à supprimer.
     * @param option
     *               Option pour savoir si les erreurs sont gérées ou non.
     * @return
     *         Le status du traitement.
     */
    public Status supprimerObjetSonar(String key, TypeObjetSonar type, OptionGestionErreur option)
    {
        // Création de la requete
        WebTarget requete;

        if (type == TypeObjetSonar.PORTFOLIO)
            requete = webTarget.path(PORTFOLIODELETE).queryParam(KEY, key);
        else if (type == TypeObjetSonar.APPLI)
            requete = webTarget.path(APPLIDELETE).queryParam(APPLI, key);
        else if (type == TypeObjetSonar.PROJECT)
            requete = webTarget.path(PROJECTDELETE).queryParam(PROJECT, key);
        else
            throw new TechnicalException("control.rest.SonarAPI.supprimerObjetSonar - type objet non gere : " + type);

        // Appel et gestion des erreurs
        Response response = appelWebservicePOST(requete);
        LOGGER.info("Suppression objet : " + key + HTTP + response.getStatus());
        if (option == OptionGestionErreur.OUI)
            gestionErreur(response);

        return response.getStatusInfo().toEnum();
    }

    /**
     * Ajout d'un sous-projet à un protfolio/appli Sonar. Ne marche pas pour ajouter un portfolio/appli.
     * 
     * @param pf
     *                   Portfolio/Appli traitée.
     * @param projectKey
     *                   Projet à ajouter au portfolio/appli
     * @return
     *         Le status du traitement.
     */
    public Status ajouterSousProjet(ObjetSonar pf, String projectKey)
    {
        // Création de la requete
        WebTarget requete;

        if (pf.getType() == TypeObjetSonar.PORTFOLIO)
            requete = webTarget.path(PORTFOLIOADD).queryParam(KEY, pf.getKey());
        else if (pf.getType() == TypeObjetSonar.APPLI)
            requete = webTarget.path(APPLIADD).queryParam(APPLI, pf.getKey());
        else
            throw new TechnicalException("control.rest.SonarAPI.ajouterSousProjet - type objet non gere : " + pf.getType());
        requete = requete.queryParam(PROJECT, projectKey);

        // Appel et gestion des erreurs
        Response response = appelWebservicePOST(requete);
        LOGGER.info("Ajout objet : " + projectKey + HTTP + response.getStatus());
        gestionErreur(response);

        return response.getStatusInfo().toEnum();
    }

    /**
     * Ajout d'un sous-portfolio à un portfolio/appli. Ne marche pas pour ajouter un projet.
     * 
     * @param pf
     *               Portfolio/Appli à traiter.
     * @param sousPf
     *               Portfolio/Appli à ajouter.
     * @return
     *         Le status du traitement.
     */
    public Status ajouterSousPortfolio(ObjetSonar pf, ObjetSonar sousPf)
    {
        // Contrôle du pf
        if (!ObjetSonar.controle(pf) || !ObjetSonar.controle(sousPf))
            return Status.BAD_REQUEST;

        // Cereation de la requete
        WebTarget requete = webTarget.path(PORTFOLIOADDSUBPF).queryParam(KEY, pf.getKey()).queryParam("ref_key", sousPf.getKey());

        // Appel et gestion des erreurs
        Response response = appelWebservicePOST(requete);
        LOGGER.info("ajouterSousPortfolio : " + pf.getKey() + " - sousPF : " + sousPf.getKey() + HTTP + response.getStatus());
        gestionErreur(response);

        return response.getStatusInfo().toEnum();
    }

    /**
     * Permet de vérifier si l'utilisateur a bien les accés à SonarQube
     * 
     * @return true si l'utilisateur a les accés.
     */
    public boolean connexionUtilisateur()
    {
        WebTarget requete = webTarget.path(AUTHLOGIN).queryParam("login", Statics.info.getPseudo()).queryParam("password", Statics.info.getMotDePasse());
        Response response = appelWebservicePOST(requete);

        return response.getStatus() == Status.OK.getStatusCode();
    }

    /**
     * Retourne tous les composants plantés du serveur SonarQube
     * 
     * @param task
     *             Tâche parente.
     * @return
     *         La liste des composants placntés.
     */
    public List<ComposantSonar> getComposPlantes(AbstractTask task)
    {
        // Récupération de tous les master des composants su serveur Sonar
        List<ComposantSonar> retour = getComposantsSansBranche(task);

        // Itération sur la liste pour supprimer tous ceux qui ne sont pas plantes, c'est-à-dire avec une date d'analyse
        for (Iterator<ComposantSonar> iter = retour.iterator(); iter.hasNext();)
        {
            if (iter.next().getDateAnalyse() != null)
                iter.remove();
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Tranforme une chaîne de caractères provenant de SoanrQube en Objet LocalDateTime.
     * 
     * @param date
     *             La date provenant de SonarQube.
     * @return
     *         La LocalDateTime.
     */
    private LocalDateTime format(String date)
    {
        if (DATE4.matcher(date).matches())
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
        else if (DATE2.matcher(date).matches())
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        else
            throw new TechnicalException("control.rest.SOnarAPI.format - format date non conforme : " + date);
    }

    /**
     * Tranforme une chaîne de caractères provenant de SoanrQube en Objet ZonedDateTime.
     * 
     * @param date
     *             La date provenant de SonarQube.
     * @return
     *         La ZonedDateTime.
     */
    private ZonedDateTime formatZoned(String date)
    {
        if (DATE4.matcher(date).matches())
            return ZonedDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
        throw new TechnicalException("control.rest.SOnarAPI.format - format date non conforme : " + date);
    }

    /**
     * Appels SonarQube pour récupérer toutes les branches des composants.
     * 
     * @param retour
     *               La liste des composants initiaux.
     * @param task
     *               La tâche parente.
     */
    private void traitementBranches(List<ComposantSonar> retour, AbstractTask task)
    {
        // Variables
        List<Branche> branches;
        int i = 0;
        int size = retour.size();
        task.setBaseMessage(task.getBaseMessage() + "Récupération des branches des composants...\n");
        long debut = System.currentTimeMillis();

        for (ListIterator<ComposantSonar> iter = retour.listIterator(); iter.hasNext();)
        {
            ComposantSonar compo = iter.next();

            // 1. appel du webservice
            Response response = appelWebserviceGET(PROJECTBRANCHLIST, new ParamAPI(PROJECT, compo.getKey()));
            task.updateMessage(compo.getNom());

            // 2. Test retour du webservice.
            if (response.getStatus() == Status.OK.getStatusCode())
                branches = response.readEntity(Retour.class).getBranches();
            else
            {
                LOGPLANTAGE.error(erreurAPI(PROJECTBRANCHLIST) + compo.getNom());
                continue;
            }

            // 3. Traitement des branches
            for (Branche branche : branches)
            {
                if (branche.getDateAnalyse() != null && !branche.isPrincipale() && branche.getType() == TypeBranche.LONG)
                {
                    ComposantSonar compoBranche = ComposantSonar.from(compo);
                    compoBranche.setBranche(branche.getNom());
                    compoBranche.setDateAnalyse(branche.getDateAnalyse());
                    compoBranche.setQualityGate(branche.getStatut().getQualityGate());
                    iter.add(compoBranche);
                }
            }

            // 4. Mise à jour affichage
            i++;
            task.updateProgress(i, size);
            task.calculTempsRestant(debut, i, size);
        }
    }

    /**
     * Gère les retours d'erreurs des Webservices.
     * 
     * @param response
     *                 Response du WebService provenant de SonarQube.
     * @return
     *         Vrai si le retour est OK.
     */
    private boolean gestionErreur(Response response)
    {
        if (response.getStatus() == Status.OK.getStatusCode() || response.getStatus() == Status.NO_CONTENT.getStatusCode())
            return true;
        Retour retour = response.readEntity(Retour.class);
        if (retour != null)
        {
            for (Message message : retour.getErreurs())
            {
                LOGPLANTAGE.error(message.getMsg());
            }
        }
        return false;
    }

    /**
     * Formatage de l'erreur API.
     * 
     * @param api
     *            Nom de l'API.
     * @return
     *         Retour d'erreur formaté.
     */
    private String erreurAPI(String api)
    {
        if (api != null && api.startsWith("api/"))
            return "Erreur API : " + api + " - Composant : ";
        return "Erreur API : inconnue - Composant : ";
    }
}
