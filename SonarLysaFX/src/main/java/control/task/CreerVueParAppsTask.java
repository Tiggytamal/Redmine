package control.task;

import static utilities.Statics.NL;
import static utilities.Statics.fichiersXML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.excel.ControlAppsW;
import control.excel.ControlPbApps;
import control.excel.ControlUA;
import control.excel.ExcelFactory;
import control.word.ControlRapport;
import control.xml.ControlXML;
import model.Application;
import model.CompoPbApps;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatAppli;
import model.enums.OptionCreerVueParAppsTask;
import model.enums.OptionRecupCompo;
import model.enums.Param;
import model.enums.TypeColAppsW;
import model.enums.TypeColPbApps;
import model.enums.TypeColUA;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import model.sonarapi.Projet;
import model.sonarapi.Vue;
import model.utilities.ControlModelInfo;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

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

    /** logger composants sans applications */
    private static final Logger LOGSANSAPP = LogManager.getLogger("sansapp-log");
    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    /** logger applications non listée dans le référentiel */
    private static final Logger LOGNONLISTEE = LogManager.getLogger("nonlistee-log");

    private static final short ETAPES = 5;
    /** compo sans lot */
    private static final String COMPOSANSLOT = "Composant sans numéro de lot";

    private static final String LOT315765 = "315765";

    /** Nombre de composants avec application inconnues */
    private int inconnues;
    /** Controleur des rapports */
    private ControlRapport controlRapport;
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

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueParAppsTask(OptionCreerVueParAppsTask option, File file)
    {
        super(ETAPES, TITRE);
        annulable = false;
        inconnues = 0;
        applications = fichiersXML.getMapApplis();
        controlRapport = new ControlRapport(TypeRapport.VUEAPPS);
        applisOpenSonar = new HashSet<>();
        composPbAppli = new ArrayList<>();
        this.option = option;
        this.file = file;

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
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerVueParApplication()
    {
        boolean fichiersOK = true;
        
        /* -----  1 .Création de la liste des composants par application ----- */
        
        @SuppressWarnings("unchecked")
        Map<String, List<ComposantSonar>> mapApplication = Utilities.recuperation(Main.DESER, Map.class, "mapApplis.ser", this::controlerSonarQube);
        
        /* ----- 2. Création des fichiers d'extraction ----- */
        
        // On ne crée pas les fichiers avec l'option VUE
        if (option != OptionCreerVueParAppsTask.VUE)
        {
            // Création des fichiers avec mise à jour du booléen.
            if (!creerFichierExtractionAppli())
                fichiersOK = false;
            if (!creerFichierProblemesAppli())
                fichiersOK = false;            
        }

        // Création du fichier de rapport word
        controlRapport.creerFichier();
        
        // On ne crée pas les vues avec l'option fichier
        if (option == OptionCreerVueParAppsTask.FICHIERS)
            return false;

        /* ----- 3. Suppression des vues existantes ----- */

        // Message
        String base = "Suppression des vues existantes :" + NL;
        etapePlus();
        updateMessage(base);
        updateProgress(0, 1);

        // Suppression anciennes vues
        List<Projet> listeVuesExistantes = api.getVuesParNom("APPLI MASTER ");
        for (int i = 0; i < listeVuesExistantes.size(); i++)
        {
            Projet projet = listeVuesExistantes.get(i);
            api.supprimerProjet(projet.getKey(), false);
            api.supprimerVue(projet.getKey(), false);

            // Message
            updateMessage(base + projet.getNom());
            updateProgress(i, listeVuesExistantes.size());
        }

        /* ----- 4. Creation des nouvelles vues ----- */

        // Message
        base = "Creation des nouvelles vues :" + NL;
        int i = 0;
        int size = mapApplication.entrySet().size();
        etapePlus();
        updateMessage(base);
        updateProgress(0, size);

        // Parcours de la liste pour créer chaque vue applicative avec ses composants
        for (Map.Entry<String, List<ComposantSonar>> entry : mapApplication.entrySet())
        {
            // Création de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);

            // Message
            String baseVue = base + "traitement : " + vue.getName() + NL;
            updateMessage(baseVue);
            i++;
            updateProgress(i, size);
            for (ComposantSonar composantSonar : entry.getValue())
            {
                updateMessage(baseVue + "Ajout : " + composantSonar.getNom());
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
    public Map<String, List<ComposantSonar>> controlerSonarQube()
    {
        // Récupération des composants Sonar
        Map<String, ComposantSonar> mapCompos = recupererComposantsSonar(OptionRecupCompo.PATRIMOINE);
        return creerMapApplication(mapCompos);
    }

    /**
     * Crée une map de toutes les applications dans Sonar avec pour chacunes la liste des composants liés. Enregistre aussi la liste des composants avec problème
     * sur el code application
     *
     * @param mapCompos
     * @return
     */
    private HashMap<String, List<ComposantSonar>> creerMapApplication(Map<String, ComposantSonar> mapCompos)
    {
        // Initialisation de la map
        HashMap<String, List<ComposantSonar>> retour = new HashMap<>();

        // Message
        String base = "Traitements des composants :" + NL;
        updateMessage(base);
        int i = 0;
        inconnues = 0;

        // Itération sur la liste des projets
        for (ComposantSonar baseCompo : mapCompos.values())
        {
            ComposantSonar compo = ModelFactory.getModelWithParams(ComposantSonar.class, baseCompo);

            // Message
            updateMessage(base + compo.getNom());
            i++;
            updateProgress(i, mapCompos.size());

            // Test si le code application est vide, cela veut dire que le projet n'a pas de code application.
            if (!compo.getAppli().isEmpty())
            {
                String application = compo.getAppli().trim().toUpperCase(Locale.FRANCE);

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, compo))
                    continue;

                // Mise à jour de la map de retour avec en clef, le code application et en valeur : la liste des projets liés.
                if (retour.containsKey(application))
                    retour.get(application).add(compo);
                else
                {
                    List<ComposantSonar> liste = new ArrayList<>();
                    liste.add(compo);
                    retour.put(application, liste);
                }
            }
            else
            {
                LOGSANSAPP.warn("Application non renseignée - Composant : " + compo.getNom());
                controlRapport.addInfo(TypeInfo.COMPOSANSAPP, compo.getNom(), null);
                compo.setEtatAppli(EtatAppli.KO);
                composPbAppli.add(compo);
            }
        }

        LOGINCONNUE.info("Nombre d'applis inconnues : " + inconnues);

        // Sauvegarde des applications après mise à jour des données
        new ControlXML().saveParam(fichiersXML);
        return retour;
    }

    /**
     * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
     *
     * @param application
     *            Application enregistrée pour le composant dans Sonar.
     * @param composPbAppli
     *            Liste des composants avec un problème sur le nom de l'application.
     * @param nom
     *            Nom du composant Sonar.
     * @param inconnues
     */
    private boolean testAppli(String application, ComposantSonar compo)
    {
        String nom = compo.getNom();

        // Gestion des composants avec application INCONNUE
        if (Statics.INCONNUE.equalsIgnoreCase(application))
        {
            LOGINCONNUE.warn("Application : INCONNUE - Composant : " + nom);
            compo.setEtatAppli(EtatAppli.KO);
            inconnues++;

            ;

            composPbAppli.add(testVersionPrec(compo));
            return true;
        }

        // Gestion des composants avec application connue
        if (applications.containsKey(application))
        {
            Application app = applications.get(application);

            // Maj rapport et logs pour les applications obsolètes
            if (!app.isActif())
            {
                LOGNONLISTEE.warn("Application obsolète : " + application + " - composant : " + nom);
                compo.setEtatAppli(EtatAppli.OBS);
                controlRapport.addInfo(TypeInfo.APPLIOBSOLETE, nom, application);
                composPbAppli.add(compo);
                return false;
            }

            // Maj données de l'application
            app.ajouterldcSonar(compo.getLdc());
            app.majValSecurite(compo.getSecurityRating());
            app.ajouterVulnerabilites(compo.getVulnerabilites());
            applisOpenSonar.add(app);
            return true;
        }

        // Gestion des composants sans application
        LOGNONLISTEE.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
        controlRapport.addInfo(TypeInfo.APPLINONREF, nom, application);
        compo.setEtatAppli(EtatAppli.FALSE);

        testVersionPrec(compo);

        composPbAppli.add(compo);
        return false;
    }

    /**
     * Test si l'on trouve un bon code application sur une version précedente du composant Prends en compte le lot 315765 pour avoir le nom du cpi de la version
     * precédente. Ce lot a été créé par la plateforme defab pour mettre à jour l'IHM SOA pour Tomcat.
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
            ComposantSonar compo2 = fichiersXML.getMapComposSonar().get(newKey);

            // Si on trouve un composant, on va tester l'application
            if (compo2 != null)
            {
                // Si on a un composant du lot 315765, on prend le nouveau composant
                if (compo.getLot().equals(LOT315765))
                {
                    retour = compo2;
                    retour.setEtatAppli(EtatAppli.KO);
                }

                // On tag le code appli si la nouvelle application est bien référencée.
                if (applications.containsKey(compo2.getAppli()))
                {
                    retour.setEtatAppli(EtatAppli.PREC);
                    retour.setAppli(compo2.getAppli());
                    controlRapport.addInfo(TypeInfo.APPLICOMPOPRECOK, compo.getNom(), compo2.getAppli());
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
        updateProgress(-1, 0);
        updateMessage("Création du fichier de contrôle des applications gérées dans Sonar.");

        // Fichier Sonar
        ControlAppsW controlAppsW = ExcelFactory.getWriter(TypeColAppsW.class, file);
        controlAppsW.creerfeuilleSonar(applisOpenSonar);
        return controlAppsW.write();
    }

    /**
     * Crée le fichier des composants avec des problèmes sur les codes application
     */
    private boolean creerFichierProblemesAppli()
    {
        etapePlus();
        int size = composPbAppli.size();
        int i = 0;
        updateProgress(0, size);
        updateMessage("création du fichier des composants sans code appli.");

        // Fichier des problèmes des codes apllication
        List<CompoPbApps> listePbApps = new ArrayList<>();

        // Récupération
        ControlUA controlUA = ExcelFactory.getReader(TypeColUA.class, new File(Statics.RESSTEST + "CatalogueUA.xlsx"));
        Map<String, String> mapUA = controlUA.recupDonneesDepuisExcel();

        for (ComposantSonar compo : composPbAppli)
        {
            i++;
            updateProgress(i, size);
            CompoPbApps pbApps = ModelFactory.getModel(CompoPbApps.class);

            // Infos depuis le composant
            pbApps.setCodeComposant(compo.getNom());
            if (compo.getLot().isEmpty())
            {
                pbApps.setLotRTC(COMPOSANSLOT);
                listePbApps.add(pbApps);
                continue;
            }
            pbApps.setLotRTC(compo.getLot());
            pbApps.setEtatAppli(compo.getEtatAppli());
            pbApps.setCodeAppli(compo.getAppli());

            // CPI Lot depuis la map RTC
            LotSuiviRTC lotSuiviRTC = fichiersXML.getMapLotsRTC().get(compo.getLot());
            if (lotSuiviRTC == null)
                pbApps.setCpiLot("Lot inaccessible depuis RTC");
            else
            {
                pbApps.setCpiLot(lotSuiviRTC.getCpiProjet());

                // Departement, service et chef de service depuis la map Clarity
                new ControlModelInfo().controleClarity(pbApps, lotSuiviRTC.getProjetClarity());
            }

            listePbApps.add(pbApps);

            if (pbApps.getCodeComposant().startsWith("Composant ua_"))
                controleUADepuisExcel(pbApps, mapUA);

        }

        // Ecriture fichier
        ControlPbApps controlPbApps = ExcelFactory.getWriter(TypeColPbApps.class, new File(Statics.proprietesXML.getMapParams().get(Param.NOMFICHIERPBAPPLI)));
        controlPbApps.creerfeuille(listePbApps);
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
                controlRapport.addInfo(TypeInfo.COMPOUAEXCEL, pbApps.getCodeComposant(), entry.getValue());
                break;
            }
        }
    }
    
    /*---------- ACCESSEURS ----------*/
}
