package control.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.word.ControlRapport;
import dao.DaoComposantSonar;
import dao.DaoDefaultAppli;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.DefaultAppli;
import model.bdd.LotRTC;
import model.enums.EtatDefault;
import model.enums.Matiere;
import model.enums.OptionMajCompos;
import model.enums.ParamSpec;
import model.enums.QG;
import model.enums.TypeInfo;
import model.enums.TypeMetrique;
import model.enums.TypeRapport;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;
import model.sonarapi.Projet;
import utilities.Statics;

/**
 * Tâche de création de la liste des composants SonarQube avec purge des composants plantés
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajComposantsSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Logger pour de debug dans la console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** Logger général de l'application */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** Nombre d'étapes du traitement */
    private static final short ETAPES = 2;
    /** Titre de la tâche */
    private static final String TITRE = "Création liste des composants";
    /** Numéro de lot inconnu */
    private static final String LOT0 = "000000";

    /** Liste des clefs des composants plantès dans SonarQube */
    private List<String> keysComposPlantes;
    /** Options de la tâche */
    private OptionMajCompos option;
    /** Rapport de la purge */
    private ControlRapport controlR;

    /*---------- CONSTRUCTEURS ----------*/

    public MajComposantsSonarTask(OptionMajCompos option)
    {
        super(ETAPES, TITRE);
        this.option = option;
        annulable = true;
        keysComposPlantes = new ArrayList<>();
        controlR = new ControlRapport(TypeRapport.PURGESONAR);
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        List<ComposantSonar> listeSonar = creerListeComposants();
        return sauvegarde(listeSonar) >= listeSonar.size();
    }

    @Override
    public void annulerImpl()
    {
        // Pas de traitement à l'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private List<ComposantSonar> creerListeComposants()
    {
        // Affichage
        baseMessage = "Création de la liste des composants :\n";
        updateMessage("Récupération des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // Récupération des données de la base de données et de Sonar
        List<ComposantSonar> retour = new ArrayList<>();
        Map<String, Application> mapAppli = DaoFactory.getDao(Application.class).readAllMap();
        Map<String, LotRTC> mapLotRTC = DaoFactory.getDao(LotRTC.class).readAllMap();
        Map<String, DefaultAppli> mapDefAppli = DaoFactory.getDao(DefaultAppli.class).readAllMap();
        List<Projet> projets = api.getComposants();

        // Réinitialisation des matières des lots pour le cas d'un composant qui serait retiré et qui enléverait un type de matière.
        for (LotRTC lotRTC : mapLotRTC.values())
        {
            lotRTC.getMatieres().clear();
        }

        // Affichage
        updateMessage("Récupération OK.");
        etapePlus();
        int i = 0;
        int size = projets.size();
        long debut = System.currentTimeMillis();

        for (Projet projet : projets)
        {
            // Arrêt de a boucle en cas d'annulation
            if (isCancelled())
                break;

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateMessage(projet.getNom());
            updateProgress(i, size);
            LOGCONSOLE.debug(new StringBuilder("Traitement composants Sonar ").append(projet.getNom()).append(" : ").append(i).append(" - ").append(size).toString());

            // Initialisation composant
            ComposantSonar compo = initCompoDepuisProjet(mapCompos, projet);
            List<String> oldVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSVIEUXCOMPOS).split(";"));

            // On ne recalcule pas les composants avec une ancienne version ou ceux qui n'ont pas été mise à jour depuis la dernière mise à jour,
            // mais seulement dans le cas où l'option de mise à jour partielle est activée.
            if (!compoATester(compo, oldVersion))
                continue;

            // Récupération du numéro de lot et de l'applicaiton de chaque composant.
            Composant composant = api.getMetriquesComposant(projet.getKey(),
                    new String[] { TypeMetrique.LOT.getValeur(), TypeMetrique.APPLI.getValeur(), TypeMetrique.EDITION.getValeur(), TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(),
                            TypeMetrique.VULNERABILITIES.getValeur(), TypeMetrique.QG.getValeur(), TypeMetrique.DUPLICATION.getValeur(), TypeMetrique.BLOQUANT.getValeur(),
                            TypeMetrique.CRITIQUE.getValeur() });

            if (composant == null || !testCompoPlante(composant))
                continue;

            initCompoLotEtMatiere(compo, composant, mapLotRTC);

            // Code application
            String codeAppli = getValueMetrique(composant, TypeMetrique.APPLI, Statics.EMPTY);

            // On récupère le code appli des infos du composant et si on ne trouve pas le code application dans la base de données,
            // on crée une nouvelle en spécifiant qu'elle ne fait pas partie du référentiel. Les composants sans code appli auront une application nulle.
            if (mapAppli.containsKey(codeAppli))
                compo.setAppli(mapAppli.get(codeAppli));
            else
            {
                Application appli = Application.getApplicationInconnue(codeAppli);
                compo.setAppli(appli);
                mapAppli.put(codeAppli, appli);
            }

            gestionDefaultsAppli(compo, mapDefAppli);

            // Securite du composant
            if (api.getSecuriteComposant(projet.getKey()) > 0)
                compo.setSecurite(true);

            // Données restantes
            compo.setQualityGate(getValueMetrique(composant, TypeMetrique.QG, QG.NONE.getValeur()));
            compo.setEdition(getValueMetrique(composant, TypeMetrique.EDITION, null));
            compo.setLdc(getValueMetrique(composant, TypeMetrique.LDC, "0"));
            compo.setSecurityRatingDepuisSonar(getValueMetrique(composant, TypeMetrique.SECURITY, "0"));
            compo.setVulnerabilites(getValueMetrique(composant, TypeMetrique.VULNERABILITIES, "0"));
            compo.setBloquants(recupLeakPeriod(getListPeriode(composant, TypeMetrique.BLOQUANT)));
            compo.setCritiques(recupLeakPeriod(getListPeriode(composant, TypeMetrique.CRITIQUE)));
            compo.setDuplication(recupLeakPeriod(getListPeriode(composant, TypeMetrique.DUPLICATION)));
            retour.add(compo);
        }

        // Ecriture de la liste des composants purgés
        controlR.creerFichier();

        // Purge dans la ltable des composants plantés
        purgeComposPlantes(keysComposPlantes);

        // Mise à jour des défaults pour fermer ceux résolus
        majDefAppli(mapDefAppli);

        return retour;
    }

    /**
     * Contrôle pour voir si l'on doit rechercher des modifications sur le composant dans SonarQube
     * 
     * @param compo
     * @param oldVersion
     * @return
     */
    private boolean compoATester(ComposantSonar compo, List<String> oldVersion)
    {
        if (option == OptionMajCompos.COMPLETE)
        {
            api.initCompoVersionEtDateMaj(compo);
            return true;
        }
        return testVersion(compo, oldVersion) && api.initCompoVersionEtDateMaj(compo);
    }

    /**
     * Test si la version d'un composant est récente ou non. Utilise le paramètre de l'application {@code ParamSpec.VERSIONSVIEUXCOMPOS}.
     * 
     * @param compo
     * @param oldVersions
     * @return vrai si la version est récente faux si c'est uen ancienne version
     */
    private boolean testVersion(ComposantSonar compo, List<String> oldVersions)
    {
        for (String version : oldVersions)
        {
            String nom = compo.getNom();
            if (nom.substring(nom.length() - 2, nom.length()).equals(version))
                return false;
        }
        return true;
    }

    private void initCompoLotEtMatiere(ComposantSonar compo, Composant composant, Map<String, LotRTC> mapLotRTC)
    {
        // Lot RTC
        String numeroLot = getValueMetrique(composant, TypeMetrique.LOT, LOT0);

        // Gestion de la matière
        Matiere matiere = testMatiereCompo(compo.getNom());
        compo.setMatiere(matiere);

        // On récupère le numéro de lot des infos du composant et si on ne trouve pas la valeur dans la base de données,
        // on crée un nouveau lot en spécifiant qu'il ne fait pas parti du référentiel. Les composants sans numéro de lot auront un lotRTC inconnu.
        if (mapLotRTC.containsKey(numeroLot))
        {
            compo.setLotRTC(mapLotRTC.get(numeroLot));
            compo.getLotRTC().addMatiere(matiere);
        }
        else
        {
            LotRTC lotRTC = LotRTC.getLotRTCInconnu(numeroLot);
            lotRTC.addMatiere(matiere);
            compo.setLotRTC(lotRTC);
            mapLotRTC.put(numeroLot, lotRTC);
        }
    }

    /**
     * Mise à jour de la table des défaults des applications
     * 
     * @param mapDefAppli
     */
    private void majDefAppli(Map<String, DefaultAppli> mapDefAppli)
    {
        DaoDefaultAppli daoDefAppli = DaoFactory.getDao(DefaultAppli.class);
        daoDefAppli.persist(mapDefAppli.values());
        daoDefAppli.majDateDonnee();
    }

    /**
     * Contrôle des composants et création d'un défault dans le cas d'un code applicatif érroné.
     * 
     * @param compo
     * @param mapDefAppli
     */
    private void gestionDefaultsAppli(ComposantSonar compo, Map<String, DefaultAppli> mapDefAppli)
    {
        if (!compo.getAppli().isReferentiel() && !mapDefAppli.containsKey(compo.getNom()))
        {
            DefaultAppli defAppli = ModelFactory.getModel(DefaultAppli.class);
            defAppli.setCompo(compo);
            defAppli.setDateDetection(LocalDate.now());
            mapDefAppli.put(compo.getNom(), defAppli);
        }
        else if (compo.getAppli().isReferentiel() && mapDefAppli.containsKey(compo.getNom()))
        {
            DefaultAppli defAppli = mapDefAppli.get(compo.getNom());
            defAppli.setEtatDefault(EtatDefault.CLOSE);
            defAppli.setAppliCorrigee(compo.getAppli().getCode());
        }
    }

    /**
     * Test pour trouver les composants planté dans SonarQube
     * 
     * @param composant
     * @return
     */
    private boolean testCompoPlante(Composant composant)
    {
        boolean retour = true;
        if (LOT0.equals(getValueMetrique(composant, TypeMetrique.LOT, LOT0)))
        {
            keysComposPlantes.add(composant.getKey());
            controlR.addInfo(TypeInfo.COMPOPURGE, composant.getKey(), "");
            retour = false;
        }
        return retour;
    }

    /**
     * Initialisation d'un ComposantSonar depuis les information d'un obejt Projet
     * 
     * @param mapCompos
     * @param projet
     * @return
     */
    private ComposantSonar initCompoDepuisProjet(Map<String, ComposantSonar> mapCompos, Projet projet)
    {
        ComposantSonar retour;
        if (mapCompos.containsKey(projet.getKey()))
            retour = mapCompos.get(projet.getKey());
        else
            retour = ModelFactory.getModel(ComposantSonar.class);

        retour.setKey(projet.getKey());
        retour.setNom(projet.getNom());
        retour.setId(projet.getId());
        return retour;
    }

    /**
     * Remonte une liste de période en protégeant des nullPointer
     * 
     * @param metriques
     * @param type
     * @return
     */
    private List<Periode> getListPeriode(Composant compo, TypeMetrique type)
    {
        return compo.getMapMetriques().computeIfAbsent(type, t -> new Metrique(type, null)).getListePeriodes();
    }

    /**
     * Remonte la valeur du premier index de la période qui correspond à la leakPeriod
     * 
     * @param periodes
     * @return
     */
    private float recupLeakPeriod(List<Periode> periodes)
    {
        if (periodes == null)
            return 0F;

        for (Periode periode : periodes)
        {
            if (periode.getIndex() == 1)
                return Float.valueOf(periode.getValeur());
        }
        return 0F;
    }

    /**
     * Sauvegarde les donnèes en base avec retour du nombre de lignes ajoutèes
     * 
     * @param listeSonar
     */
    private int sauvegarde(List<ComposantSonar> listeSonar)
    {
        // Ajout des donnèes et mise à jour de la date de modification de la table
        DaoComposantSonar dao = DaoFactory.getDao(ComposantSonar.class);
        int retour = dao.persist(listeSonar);
        dao.majDateDonnee();
        return retour;
    }

    /**
     * Purge des composantsSoanr plantés dans SonarQube et dans la base de données
     * 
     * @param keysComposPlantes
     */
    private void purgeComposPlantes(List<String> keysComposPlantes)
    {
        DaoComposantSonar dao = DaoFactory.getDao(ComposantSonar.class);

        for (String key : keysComposPlantes)
        {
            // Suppression dans SonarQube
            api.supprimerProjet(key, false);
            api.supprimerVue(key, false);

            LOGGER.debug("suppression composant : {0}", key);

            // Suppression dans la base

            dao.delete(dao.recupEltParIndex(key));
        }

    }

    /*---------- ACCESSEURS ----------*/
}
