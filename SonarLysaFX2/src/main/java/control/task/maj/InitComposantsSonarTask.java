package control.task.maj;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import control.rest.ControlAppCATS;
import control.rest.ControlRepack;
import control.task.AbstractTask;
import control.task.portfolio.CreerPortfolioCompoTU;
import dao.Dao;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.bdd.StatistiqueCompo;
import model.bdd.StatistiqueCompoIndex;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.Metrique;
import model.enums.OptionGetCompos;
import model.enums.OptionTypeLong;
import model.enums.StatistiqueCompoEnum;
import model.enums.TypeObjetSonar;
import model.rest.repack.RepackREST;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Mesure;
import model.rest.sonarapi.ObjetSonar;
import utilities.DateHelper;
import utilities.Statics;

/**
 * Tâche de création de la liste complète des composants SonarQube depuis le serveur pour mettre à jour la table entiérement.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class InitComposantsSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Nombre d'etapes du traitement */
    private static final short ETAPES = 2;
    /** Titre de la tâche */
    private static final String TITRE = "Création liste des composants";

    // Regex
    private static final Pattern PATTERNDATE = Pattern.compile("^\\d{13}$");

    // Contrôleurs
    private ControlRepack cr;
    private ControlAppCATS controlAppCATS;
    private Dao<StatistiqueCompo, StatistiqueCompoIndex> daoStat;

    /*---------- CONSTRUCTEURS ----------*/

    public InitComposantsSonarTask()
    {
        super(ETAPES, TITRE);
        init();
    }

    public InitComposantsSonarTask(AbstractTask tacheParente)
    {
        super(ETAPES, TITRE, tacheParente);
        init();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement à l'annulation
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        List<ComposantBase> liste = traiterListeComposants();
        return sauvegarde(daoCompo, liste) >= liste.size();        
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation générqieu de la tâche.
     */
    private void init()
    {
        daoStat = DaoFactory.getMySQLDao(StatistiqueCompo.class);
        controlAppCATS = ControlAppCATS.INSTANCE;
        cr = new ControlRepack();
        annulable = true;
        startTimers();
    }

    /**
     * Création de la liste des composants depuis le serveur SonarQube.
     * 
     * @return
     *         La liste complète des composants SonarQube.
     */
    private List<ComposantBase> traiterListeComposants()
    {
        // Affichage
        baseMessage = "Création de la liste des composants :\n";
        updateMessage("Récupération des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // Récupération des données de la base de données et de Sonar
        List<ComposantBase> retour = new ArrayList<>();
        Map<String, Application> mapAppli = DaoFactory.getMySQLDao(Application.class).readAllMap();
        List<ComposantSonar> composSonar = api.getComposants(OptionGetCompos.MINIMALE, this);

        // Affichage
        baseMessage = baseMessage.replace("...\n", "...OK\n") + "Mise à jour des données des composants...\n";
        updateMessage(Statics.EMPTY);
        etapePlus();
        int i = 0;
        int size = composSonar.size();
        long debut = System.currentTimeMillis();

        for (ComposantSonar compoSonar : composSonar)
        {
            // Arret de la boucle en cas d'annulation
            if (isCancelled())
                break;

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateMessage(compoSonar.getNom());
            updateProgress(i, size);

            // Initialisation composant
            ComposantBase compoBase = initCompoDepuisSonar(getMapCompo(), compoSonar);

            // Récupération des informations de chaque composant depuis SonarQube.
            ComposantSonar compoMetriques = api.getMesuresComposant(compoSonar.getKey(), compoSonar.getBranche(), new String[]
            { Metrique.APPLI.getValeur(), Metrique.LDC.getValeur(), Metrique.SECURITY.getValeur(), Metrique.QG.getValeur(), Metrique.BLOQUANT.getValeur(), Metrique.CRITIQUE.getValeur(),
                    Metrique.NEWLDCNOCOVER.getValeur(), Metrique.NEWLDCTOCOVER.getValeur(), Metrique.NEWPCTCOVERLDC.getValeur(), Metrique.PCTCOVERLDC.getValeur() });
            ComposantSonar compoShow = api.getDetailsComposant(compoSonar.getKey(), compoSonar.getBranche());

            // On saute les composants plantés et sans numéro de lot.
            if (compoMetriques == null || compoShow == null)
                continue;

            // Repack
            initDateRepack(compoBase);

            initCodeAppli(compoBase, compoMetriques, mapAppli);

            // Gestion de la propreté (c'est-à-dire ni bloquants ni critiques)
            if (Statics.ZERO.equals(compoMetriques.getValueMetrique(Metrique.BLOQUANT, Statics.ZERO)) && Statics.ZERO.equals(compoMetriques.getValueMetrique(Metrique.CRITIQUE, Statics.ZERO)))
                compoBase.setPropre(true);

            // Données restantes
            compoBase.setDerniereAnalyse(compoShow.getDateAnalyse());
            compoBase.setVersion(compoShow.getVersion());
            compoBase.setSecurityRatingDepuisSonar(compoMetriques.getValueMetrique(Metrique.SECURITY, "0"));
            compoBase.setQualityGate(compoMetriques.getValueMetrique(Metrique.QG, "NONE"));
            compoBase.setMatiere(Matiere.testMatiereCompoMC(compoBase.getNom()));

            initLdc(compoBase, compoMetriques);

            retour.add(compoBase);
        }

        return retour;
    }

    /**
     * Initialisation d'un Composant depuis les informations de SonarQube.
     * 
     * @param mapComposBase
     *                      Map des composants en base de données.
     * @param compoSonar
     *                      Informatiosn provenant de WebSerivec SonarQube.
     * @return
     *         Composant qui sera sauvegarder en base de données.
     */
    private ComposantBase initCompoDepuisSonar(Map<String, ComposantBase> mapComposBase, ComposantSonar compoSonar)
    {
        ComposantBase retour;
        if (mapComposBase.containsKey(compoSonar.getKey() + compoSonar.getBranche()))
            retour = mapComposBase.get(compoSonar.getKey() + compoSonar.getBranche());
        else
            retour = ModelFactory.build(ComposantBase.class);

        retour.setKey(compoSonar.getKey());
        retour.setNom(compoSonar.getNom());
        retour.setBranche(compoSonar.getBranche());
        retour.setInstance(InstanceSonar.LEGACY);
        retour.setDerniereAnalyse(compoSonar.getDateAnalyse());
        retour.setQualityGate(compoSonar.getQualityGate());
        return retour;
    }

    /**
     * Calcul des valeurs des lignes de codes à couvrir et non couvertes pour le composant.
     * Ne retiens pas les caleurs si celles-ci sont à zéro.
     * 
     * @param compoBase
     *                       Composant à sauvegarder en base.
     * @param compoMetriques
     *                       Données provenant de SonarQube.
     * @throws IOException
     */
    private void initLdc(ComposantBase compoBase, ComposantSonar compoMetriques)
    {
        compoBase.setLdc(compoMetriques.getValueMetrique(Metrique.LDC, "0"));

        // Récupération du nombre de nouvelles lignes à couvrir et non couvertes
        // La valeur depuis SonarQube peut-être vide, on protège donc en renvoyant 0
        String valeurToCover = initValeur(compoMetriques, Metrique.NEWLDCTOCOVER);

        String valeurNoCover = initValeur(compoMetriques, Metrique.NEWLDCNOCOVER);

        // On ne sauvegarde rien si les valeurs sont à 0.
        if ("0".equals(valeurToCover) && "0".equals(valeurNoCover))
            return;

        // Sauvegarde des valeurs en base de données et mise à jour du portfolio de couverture
        sauveMetrique(valeurToCover, StatistiqueCompoEnum.NEWLDCTOCOVER, compoBase);
        sauveMetrique(valeurNoCover, StatistiqueCompoEnum.NEWLDCNOCOVER, compoBase);
        api.ajouterSousProjet(new ObjetSonar(CreerPortfolioCompoTU.KEY, CreerPortfolioCompoTU.NOM, Statics.EMPTY, TypeObjetSonar.PORTFOLIO), compoBase.getKey());
    }

    private String initValeur(ComposantSonar compoMetriques, Metrique metrique)
    {
        Mesure mesure = compoMetriques.getMapMetriques().get(metrique);
        if (mesure == null || mesure.getListePeriodes().isEmpty())
            return "0";
        else
            return mesure.getListePeriodes().get(0).getValeur();
    }

    private void sauveMetrique(String valeur, StatistiqueCompoEnum type, ComposantBase compoBase)
    {
        StatistiqueCompoIndex index = new StatistiqueCompoIndex(Statics.TODAY, compoBase, type);
        StatistiqueCompo stat = daoStat.recupEltParIndex(index);

        if (stat == null)
            sauvegarde(daoStat, StatistiqueCompo.build(index, Integer.parseInt(valeur)));
        else
            sauvegarde(daoStat, stat.update(Integer.parseInt(valeur)));
    }

    /**
     * Initilisation de la date de repack du composant ainsi que de la mise à jour de celle pour son lot.
     * 
     * @param compo
     *              Composant à traiter
     */
    private void initDateRepack(ComposantBase compo)
    {
        // Récupération de tous les repacks du composant
        List<RepackREST> repacks = cr.getRepacksComposant(compo);

        // Recuperatoin de la date de chaque repack pour garder la plus recente de toute
        LocalDate date = Statics.DATEINCONNUE;
        for (RepackREST repackREST : repacks)
        {
            if ("BOREAL_Packaging".equals(repackREST.getNomGc()))
                continue;

            LocalDate temp = calculDateRepack(repackREST.getDateCreation());
            if (temp.isAfter(date))
                date = temp;
        }

        if (date != Statics.DATEINCONNUE)
            compo.setDateRepack(date);

        // Calcul de la date de repack du lot en prenant la date la plus récente des repacks des composants
        LotRTC lot = compo.getLotRTC();
        LocalDate repackCompo = compo.getDateRepack();

        if (lot != null && repackCompo != null && Statics.DATEINCO2099.equals(lot.getEdition().getDateMEP()) && (lot.getDateRepack() == null || repackCompo.isAfter(lot.getDateRepack())))
            lot.setDateRepack(repackCompo);
    }

    /**
     * Calcul de la date de Repack depuis les informations du webService de la PIC.
     * 
     * @param dateCreation
     *                     Date provenant de la PIC sous la forme d'une chaîne de caractères.
     * @return
     *         LocalDate calculée.
     */
    private LocalDate calculDateRepack(String dateCreation)
    {
        if (dateCreation.isEmpty() || !PATTERNDATE.matcher(dateCreation).matches())
            return Statics.DATEINCONNUE;
        return DateHelper.convertLong(LocalDate.class, Long.valueOf(dateCreation), OptionTypeLong.MILLISECOND);
    }

    /**
     * Initialisation du code application depuis les informations de SonarQube.
     * 
     * @param compoBase
     *                       Composant à traiter.
     * @param compoMetriques
     *                       Informations provenant de SonarQube.
     * @param mapAppli
     *                       Map des applications en base de données.
     */
    private void initCodeAppli(ComposantBase compoBase, ComposantSonar compoMetriques, Map<String, Application> mapAppli)
    {
        // Code application
        String codeAppli = compoMetriques.getValueMetrique(Metrique.APPLI, Statics.EMPTY).toUpperCase(Locale.FRANCE);

        boolean appliExiste = controlAppCATS.testApplicationExiste(codeAppli);

        // On récupère le code appli des infos du composant et si on ne trouve pas le code application dans la base de données,
        // on crée une nouvelle en specifiant qu'elle ne fait pas partie du référentiel. Les composants sans code appli auront une application nulle.
        Application appli = mapAppli.get(codeAppli);
        if (appli != null)
        {
            appli.setReferentiel(appliExiste);
            compoBase.setAppli(appli);
        }
        else
        {
            appli = Application.getApplication(codeAppli, appliExiste);
            compoBase.setAppli(appli);
            mapAppli.put(codeAppli, appli);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
