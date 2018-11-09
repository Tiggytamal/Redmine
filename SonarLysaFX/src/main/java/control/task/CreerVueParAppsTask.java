package control.task;

import static utilities.Statics.NL;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import control.excel.ControlAppsW;
import control.excel.ControlPbApps;
import control.excel.ControlUA;
import control.excel.ExcelFactory;
import dao.DaoApplication;
import dao.DaoFactory;
import model.CompoPbApps;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.LotRTC;
import model.enums.EtatAppli;
import model.enums.OptionCreerVueParAppsTask;
import model.enums.OptionRecupCompo;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeColAppsW;
import model.enums.TypeColPbApps;
import model.enums.TypeColUA;
import model.rest.sonarapi.Projet;
import model.rest.sonarapi.Vue;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Tâche de création des vues par application. Permet aussi de créer le fichier d'extraction des composant traités dans Sonar ainsi que le fichier des problèmes
 * des codes applicatifs.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerVueParAppsTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Création Vues par Application";

    private static final short ETAPES = 5;
    /** Compo sans lot */
    private static final String COMPOSANSLOT = "Composant sans numéro de lot";
    /** Lot de platefore de fab. pour TomCat */
    private static final String LOT315765 = "315765";

    /** Liste des apllications Dans SonarQube */
    private Set<Application> applisOpenSonar;
    /** Map de toutes les apllications */
    private Map<String, Application> applications;
    /** Liste des composants avec un problème au niveau du cide application */
    private List<ComposantSonar> composPbAppli;
    /** 2numération des options de la tâches */
    private OptionCreerVueParAppsTask option;
    /** Nom du fichier de suavegarde de l'extraction */
    private File file;
    
    private Application appliInconnue;
    private DaoApplication daoApplication;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueParAppsTask(OptionCreerVueParAppsTask option, File file)
    {
        super(ETAPES, TITRE);
        annulable = false;
        daoApplication = DaoFactory.getDao(Application.class);
        applications = daoApplication.readAllMap();
        initAppliInconnue();
        applisOpenSonar = new HashSet<>();
        composPbAppli = new ArrayList<>();
        this.option = option;
        this.file = file;
        startTimers();

        if (option != OptionCreerVueParAppsTask.VUE && file == null)
            throw new TechnicalException("Control.task.CreerVueParAppsTask - Demande de création d'extraction sans fichier", null);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueParApplication();
    }

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueParApplication()
    {
        boolean fichiersOK = true;

        /* ----- 1 .Création de la liste des composants par application ----- */

        Map<String, List<ComposantSonar>> mapApplication = analyseCompoSonar(OptionRecupCompo.DERNIERE);

        /* ----- 2. Création des fichiers d'extraction ----- */

        // On ne crée pas les fichiers avec l'option VUE
        if (option != OptionCreerVueParAppsTask.VUE)
        {
            // Création des fichiers avec mise à jour du booléen.
            if (!creerFichierExtractionAppli())
                fichiersOK = false;

            // Récupération des donnèes sur la dernière version des composants et instanciation de la map des application
            if (!creerFichierProblemesAppli())
                fichiersOK = false;
        }

        // On ne crée pas les vues avec l'option fichier
        if (option == OptionCreerVueParAppsTask.FICHIERS)
            return false;

        /* ----- 3. Suppression des vues existantes ----- */

        // Message
        baseMessage = "Suppression des vues existantes :" + NL;
        etapePlus();
        updateMessage("");
        updateProgress(0, 1);

        // Suppression anciennes vues
        List<Projet> listeVuesExistantes = api.getVuesParNom("APPLI MASTER ");
        int size = listeVuesExistantes.size();
        long debut = System.currentTimeMillis();

        for (int i = 0; i < size; i++)
        {
            Projet projet = listeVuesExistantes.get(i);
            api.supprimerProjet(projet.getKey(), false);
            api.supprimerVue(projet.getKey(), false);

            // Affichage
            calculTempsRestant(debut, i+1, size);
            updateMessage(projet.getNom());
            updateProgress(i, size);
        }

        /* ----- 4. Creation des nouvelles vues ----- */

        // Affichage et variables
        baseMessage = "Creation des nouvelles vues :\n";
        int i = 0;
        size = mapApplication.entrySet().size();
        debut = System.currentTimeMillis();
        etapePlus();
        updateMessage("");
        updateProgress(0, size);

        // Parcours de la liste pour créer chaque vue applicative avec ses composants
        for (Map.Entry<String, List<ComposantSonar>> entry : mapApplication.entrySet())
        {
            // Création de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);

            // Affichage
            i++;
            baseMessage = new StringBuilder("traitement : ").append(vue.getName()).append(NL).toString();
            updateProgress(i, size);
            calculTempsRestant(debut, i, size);
            for (ComposantSonar composantSonar : entry.getValue())
            {
                updateMessage("Ajout : " + composantSonar.getNom());
                api.ajouterProjet(composantSonar, vue);
            }
        }

        return fichiersOK;
    }

    /**
     * Remonte les composants par application de SonarQube et créée les logs sur les defaults.
     *
     * @return map des composants SonarQube par application
     */
    public Map<String, List<ComposantSonar>> analyseCompoSonar(OptionRecupCompo option)
    {
        // Récupération des composants Sonar
        Map<String, ComposantSonar> mapCompos = recupererComposantsSonar(option);
        return creerMapApplication(mapCompos.values());
    }

    /**
     * Crée une map de toutes les applications dans Sonar avec pour chacunes la liste des composants liés. Enregistre aussi la liste des composants avec problème
     * sur el code application
     *
     * @param compos
     * @return
     */
    private HashMap<String, List<ComposantSonar>> creerMapApplication(Collection<ComposantSonar> compos)
    {
        // Initialisation de la map
        HashMap<String, List<ComposantSonar>> retour = new HashMap<>();
        
        Map<String, DefautAppli> mapDefaultAppli = DaoFactory.getDao(DefautAppli.class).readAllMap();

        // Message
        baseMessage = "Traitements des composants :" + NL;
        updateMessage("");
        int i = 0;

        // Itération sur la liste des projets
        for (ComposantSonar compo : compos)
        {
            // Message
            updateMessage(compo.getNom());
            i++;
            updateProgress(i, compos.size());

            // Si l'application n'est pas dans la PIC, on continue au projet suivant.
            testAppli(compo, mapDefaultAppli);

            retour.computeIfAbsent(compo.getAppli().getCode(), k -> new ArrayList<>()).add(compo);
        }

        return retour;
    }

    /**
     * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
     * @param mapDefaultAppli 
     *
     * @param application
     *            Application enregistrée pour le composant dans Sonar.
     * @param composPbAppli
     *            Liste des composants avec un problème sur le nom de l'application.
     * @param nom
     *            Nom du composant Sonar.
     * @param inconnues
     */
    private void testAppli(ComposantSonar compo, Map<String, DefautAppli> mapDefaultAppli)
    {
        Application application = compo.getAppli();
        String codeAppli = application.getCode();

        // Gestion des composants avec application non référencée
        if (!application.isReferentiel())
        {
            if (Statics.EMPTY.equals(codeAppli) || Statics.INCONNUE.equalsIgnoreCase(codeAppli))
                compo.setEtatAppli(EtatAppli.KO);
            else
                compo.setEtatAppli(EtatAppli.FALSE);

            // Ajout à la liste des composants à problème avec test sur les version précedente de celui-ci
            composPbAppli.add(testVersionPrec(compo));
            
            DefautAppli da = mapDefaultAppli.get(compo.getNom());
            if( da == null)
                compo.setAppli(appliInconnue);
            
            else if( da.getAppliCorrigee().isEmpty())
            {
                if (da.getAction() != TypeAction.VERIFIER)
                    compo.setAppli(appliInconnue);
                else
                    compo.setAppli(daoApplication.recupEltParIndex(da.getAppliCorrigee()));
            }
            else
                compo.setAppli(daoApplication.recupEltParIndex(da.getAppliCorrigee()));
        }

        // Gestion des applications obsolètes
        if (!application.isActif())
        {
            composPbAppli.add(compo);
            compo.setEtatAppli(EtatAppli.OBS);
        }

        // Maj données de l'application
        application.ajouterldcSonar(compo.getLdc());
        application.majValSecurite(compo.getSecurityRating());
        application.ajouterVulnerabilites(compo.getVulnerabilites());
        applisOpenSonar.add(application);
    }

    /**
     * Test si l'on trouve un bon code application sur une version précedente du composant. Prends en compte le lot 315765 pour avoir le nom du cpi de la version
     * precédente. Ce lot a été créé par la plateforme de fab. pour mettre à jour l'IHM SOA pour Tomcat.
     * 
     * @param compo
     */
    private ComposantSonar testVersionPrec(ComposantSonar compo)
    {
        int i = 0;
        ComposantSonar retour = compo;
        
        // Récupération du dernier chiffre de la verion du composant. On retourne faux si on a pas un chiffre.
        // Soustraction de 1 pour obtenier la version précedente du composant.
        try
        {
            i = Integer.parseInt(compo.getKey().substring(compo.getKey().length() - 1)) - 1;
        }
        catch (NumberFormatException e)
        {
            return retour;
        }

        // On remplace la version du composant par celle inférieure en démarant à 3.
        for (; i > 0; i--)
        {
            String newKey = compo.getKey().replace(compo.getKey().charAt(compo.getKey().length() - 1) + "", String.valueOf(i));
            ComposantSonar compo2 = mapCompos.get(newKey);

            // Si on trouve un composant, on va tester l'application
            if (compo2 != null)
            {
                // Si on a un composant du lot 315765, on prend le nouveau composant
                if (LOT315765.equals(compo.getLotRTC().getLot()))
                    retour = compo2;

                // On tag le code appli si la nouvelle application est bien référencée.
                if (applications.containsKey(compo2.getAppli().getCode()))
                {
                    retour.setAppli(compo2.getAppli());
                    retour.setEtatAppli(EtatAppli.PREC);
                }

                return retour;
            }
        }
        return retour;
    }

    /**
     * Permet d'enregistrer la liste des applications dans le fichier excel.
     */
    private boolean creerFichierExtractionAppli()
    {
        if (option == OptionCreerVueParAppsTask.VUE)
            throw new TechnicalException("Control.task.CreerVueParAppsTask.creerFichierExtraction - Demande de création d'extraction sans fichier", null);

        etapePlus();
        updateProgress(0, 1);
        updateMessage("Création du fichier de contrôle des applications gérées dans Sonar.");

        // Fichier Sonar
        ControlAppsW controlAppsW = ExcelFactory.getWriter(TypeColAppsW.class, file);
        controlAppsW.creerfeuilleSonar(applisOpenSonar, this);
        return controlAppsW.write();
    }

    /**
     * Crée le fichier des composants avec des problèmes sur les codes application
     */
    private boolean creerFichierProblemesAppli()
    {
        // Affichage
        etapePlus();
        int size = composPbAppli.size();
        int i = 0;
        updateProgress(0, size);
        baseMessage = "création du fichier des composants sans code appli\n";
        updateMessage("");

        // Fichier des problèmes des codes apllication
        List<CompoPbApps> listePbApps = new ArrayList<>();

        // Récupération données catalogue UA
        updateMessage("Récupération des données du fichier catalogue UA");
        ControlUA controlUA = ExcelFactory.getReader(TypeColUA.class, new File(Statics.RESSTEST + "CatalogueUA.xlsx"));
        Map<String, String> mapUA = controlUA.recupDonneesDepuisExcel();

        for (ComposantSonar compo : composPbAppli)
        {
            i++;
            updateProgress(i, size);
            CompoPbApps pbApps = ModelFactory.getModel(CompoPbApps.class);
            LotRTC lotSuiviRTC = compo.getLotRTC();

            // Code composant
            pbApps.setCodeComposant(compo.getNom());
            
            // Traitement des composants plantés dans SonarQube sans lot 
            if (lotSuiviRTC.getLot().isEmpty())
            {
                pbApps.setLotRTC(COMPOSANSLOT);
                listePbApps.add(pbApps);
                continue;
            }

            pbApps.setLotRTC(lotSuiviRTC.getLot());
            pbApps.setCodeAppli(compo.getAppli().getCode());


            if (lotSuiviRTC.getCpiProjet().isEmpty())
                pbApps.setCpiLot("Lot inaccessible depuis RTC");
            else
                pbApps.setCpiLot(lotSuiviRTC.getCpiProjet());

            // Mise à jour depuis Clarity
            pbApps.majDepuisClarity(lotSuiviRTC.getProjetClarity());

            listePbApps.add(pbApps);

            if (pbApps.getCodeComposant().startsWith("Composant ua_"))
                controleUADepuisExcel(pbApps, mapUA);
        }

        // Ecriture fichier
        ControlPbApps controlPbApps = ExcelFactory.getWriter(TypeColPbApps.class, new File(Statics.proprietesXML.getMapParams().get(Param.NOMFICHIERPBAPPLI)));
        controlPbApps.creerfeuille(listePbApps, this);
        return controlPbApps.write();
    }

    /**
     * Contrôle si un composant d'ua est présent dans le catalogue des uas pour récupérer le nom de l'application liées.
     * 
     * @param pbApps
     *            Composant sans appli
     * @param mapUA
     *            map extraite du fichier catalogue
     */
    private void controleUADepuisExcel(CompoPbApps pbApps, Map<String, String> mapUA)
    {
        for (Map.Entry<String, String> entry : mapUA.entrySet())
        {
            // En enlève les _ et les majuscules pour comparer les données
            if (pbApps.getCodeComposant().toLowerCase(Locale.FRANCE).replace("_", "").contains(entry.getKey().toLowerCase(Locale.FRANCE).replace("_", "")))
            {
                pbApps.setCodeAppli(entry.getValue());
                pbApps.setEtatAppli(EtatAppli.CAT);
                break;
            }
        }
    }
    
    /**
     * Initialisation de l'applicaiton inconnue
     */
    private void initAppliInconnue()
    {
        appliInconnue = daoApplication.recupEltParIndex("inconnue");
        if (appliInconnue == null)
        {
            appliInconnue = Application.getApplicationInconnue("inconnue");
            applications.put(appliInconnue.getMapIndex(), appliInconnue);
            daoApplication.persist(appliInconnue);
        }        
    }

    /*---------- ACCESSEURS ----------*/
}
