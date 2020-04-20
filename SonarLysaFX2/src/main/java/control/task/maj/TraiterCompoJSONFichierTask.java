package control.task.maj;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;

import control.parsing.ControlJSON;
import control.rest.ControlAppCATS;
import control.rest.ControlRepack;
import control.rtc.ControlRTC;
import control.task.AbstractTask;
import control.task.portfolio.CreerPortfolioCompoTU;
import control.word.ControlSimpleFile;
import dao.Dao;
import dao.DaoFactory;
import fxml.Menu;
import javafx.application.Platform;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.IssueBase;
import model.bdd.LotRTC;
import model.bdd.StatistiqueCompo;
import model.bdd.StatistiqueCompoIndex;
import model.bdd.Utilisateur;
import model.enums.EtatCodeAppli;
import model.enums.EtatFichierPic;
import model.enums.Matiere;
import model.enums.Metrique;
import model.enums.Param;
import model.enums.QG;
import model.enums.StatistiqueCompoEnum;
import model.enums.TypeBranche;
import model.enums.TypeDefautQualite;
import model.enums.TypeObjetSonar;
import model.parsing.ComposantJSON;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Mesure;
import model.rest.sonarapi.ObjetSonar;
import model.rest.sonarapi.ParamAPI;
import model.rest.sonarapi.StatutQGCondition;
import model.rest.sonarapi.StatutQGProjet;
import utilities.Statics;
import utilities.Utilities;

/**
 * Classe de traitement des tâches de traitement des Composants JSON provenant des fichiers d'assemblage de la PIC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class TraiterCompoJSONFichierTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    // Regex
    private static final Pattern PATTERNSPLIT = Pattern.compile(" ");

    // Autres constantes
    private static final boolean ASSIGNATIONOK = false;
    private static final int MINAUTEUR = 2;

    /** Nombre d'etapes du traitement */
    private static final int ETAPES = 1;

    /** Titre de la tâche */
    private static final String TITRE = "Traitement fichier SonarQube";

    private File file;
    private ControlRTC controlRTC;
    private Dao<IssueBase, String> daoIssueBase;
    private Dao<DefautQualite, String> daoDQ;
    private Dao<Application, String> daoAppli;
    private ControlAppCATS controlAppCATS;
    private ControlRepack cr;
    private Dao<StatistiqueCompo, StatistiqueCompoIndex> daoStat;

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterCompoJSONFichierTask(File file)
    {
        super(ETAPES, TITRE);
        this.file = file;
        controlRTC = ControlRTC.build();
        controlRTC.connexion();
        daoStat = DaoFactory.getMySQLDao(StatistiqueCompo.class);
        daoIssueBase = DaoFactory.getMySQLDao(IssueBase.class);
        daoDQ = DaoFactory.getMySQLDao(DefautQualite.class);
        daoAppli = DaoFactory.getMySQLDao(Application.class);
        controlAppCATS = ControlAppCATS.INSTANCE;
        cr = new ControlRepack();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'annulation possible
    }

    @Override
    public Boolean call() throws IOException
    {

        ComposantJSON compoJSON = new ControlJSON().parsingCompoJSON(file);
        if (compoJSON.getBranche().getType() != TypeBranche.LONG)
        {
            suppressionFichier(true);
            return Boolean.FALSE;
        }

        try
        {
            // mise à jour du composant
            ComposantBase compoBase = traiterComposantDepuisFichier(compoJSON);

            // Assignation des anomalies au créateur de celles-ci
            List<IssueBase> issuesBase = new ArrayList<>();

            if (ASSIGNATIONOK)
                issuesBase = assignerAnoSonar(compoJSON);

            // Mise à jour du defaut
            boolean aDefauts = traiterDefauts(compoBase, compoJSON, issuesBase);

            // Persistance des issues qui n'ont pas pu être assignées
            if (aDefauts)
                sauvegarde(daoIssueBase, issuesBase);
        }
        catch (ExceptionTraiterCompo | TeamRepositoryException e)
        {
            // Les exceptions de traitement contrôlées et RTC sont attrapées, on log l'erreur mais on n'arrête pas le traitement.
            LOGPLANTAGE.error(Statics.EMPTY, e);
            return Boolean.FALSE;
        }
        catch (Exception e)
        {
            // Pour tout autre plantage, on log l'erreur, et on relance l'exception, avec arrêt du traitement et changement de l'affichage.
            LOGPLANTAGE.error(Statics.EMPTY, e);
            Menu.updateFichiersActif(EtatFichierPic.INACTIF);
            throw e;
        }

        // Mise à jour de l'affichage du nombre de lignes de code
        Platform.runLater(Menu::updateNbreLignes);

        // Suppression du fichier
        suppressionFichier(false);

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création du composant à enregistrer en base depuis le fichier JSON.
     * 
     * @param compoJSON
     *                  Objet JAVA provenant du traitement du fichier JSON.
     * @return
     *         Le composant à enregistrer en base.
     * @throws TeamRepositoryException
     *                                 Exception provenant de RTC.
     * @throws ExceptionTraiterCompo
     *                                 Exceptio spécifique au traitement.
     */
    private ComposantBase traiterComposantDepuisFichier(ComposantJSON compoJSON) throws TeamRepositoryException, ExceptionTraiterCompo
    {
        // Récupération des informations de chaque composant depuis SonarQube.
        ComposantSonar compoMetriques = api.getMesuresComposant(compoJSON.getProjet().getKey(), compoJSON.getBranche().getNom(), new String[]
        { Metrique.APPLI.getValeur(), Metrique.LDC.getValeur(), Metrique.SECURITY.getValeur(), Metrique.BLOQUANT.getValeur(), Metrique.CRITIQUE.getValeur(), Metrique.NEWLDCNOCOVER.getValeur(),
                Metrique.NEWLDCTOCOVER.getValeur(), Metrique.NEWPCTCOVERLDC.getValeur(), Metrique.PCTCOVERLDC.getValeur() });
        ComposantSonar compoShow = api.getDetailsComposant(compoJSON.getProjet().getKey(), compoJSON.getBranche().getNom());

        if (compoShow == null || compoMetriques == null || compoJSON.getNumeroLot() == 0)
            throw new ExceptionTraiterCompo("Plantage récupération composant depuis SonarQube : " + compoJSON.getProjet().getNom());

        // Initialisation composant
        ComposantBase compoBase = initDepuisJSON(compoJSON);

        // Mise à jour des données
        compoBase.setDateLeakPeriod(compoShow.getDateLeakPeriod());
        compoBase.setDerniereAnalyse(compoShow.getDateAnalyse());
        compoBase.setVersion(compoShow.getVersion());

        // Gestion de la version
        // Mise ejour de la version max si on progresse de version
        if (compoBase.getVersionMax().isEmpty() || compoBase.isVersionOK())
            compoBase.setVersionMax(compoShow.getVersion());
        else
        {
            // Création d'une ligne dans le fichier d'erreur s'il y a une regression de version
            ControlSimpleFile control = ControlSimpleFile.majFichierVersion();
            control.ajouterErreurCompo(compoBase);
            control.fermeture();
        }

        // Gestion de la propreté (c'est-à-dire ni bloquants ni critiques)
        if (Statics.ZERO.equals(compoMetriques.getValueMetrique(Metrique.BLOQUANT, "0")) && Statics.ZERO.equals(compoMetriques.getValueMetrique(Metrique.CRITIQUE, "0")))
            compoBase.setPropre(true);

        compoBase.setSecurityRatingDepuisSonar(compoMetriques.getValueMetrique(Metrique.SECURITY, "0"));
        compoBase.setMatiere(Matiere.testMatiereCompoMC(compoBase.getNom()));

        // Mise à jour du Lot
        initLot(compoBase, compoJSON);

        // Mise à jour de la dateRepack
        initDateRepack(compoBase);

        // Mise à jour du code application
        initCodeAppli(compoBase, compoMetriques);

        // Statistiques des lignes de code
        initLdc(compoBase, compoMetriques);

        // Persistance des modifications
        sauvegarde(daoCompo, compoBase);

        return compoBase;
    }

    /**
     * Initialisation du composant à enregistrer en base depuis l'objet provenant du fichier JSON.
     * 
     * @param compoJSON
     *                  Objet JAVA provenant du fichier JSON.
     * @return
     *         L'objet à enregistrer en base en données.
     */
    private ComposantBase initDepuisJSON(ComposantJSON compoJSON)
    {
        ComposantBase retour = getMapCompo().get(compoJSON.getProjet().getKey() + compoJSON.getBranche().getNom());

        if (retour == null)
        {
            retour = ModelFactory.build(ComposantBase.class);
            retour.setKey(compoJSON.getProjet().getKey());
            retour.setNom(compoJSON.getProjet().getNom());
            retour.setBranche(compoJSON.getBranche().getNom());
            getMapCompo().put(compoJSON.getProjet().getKey() + compoJSON.getBranche().getNom(), retour);
        }

        retour.setNom(compoJSON.getProjet().getNom());
        retour.setInstance(compoJSON.getInstance());
        retour.setQualityGate(compoJSON.getQualityGate());
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
     * Initialisation du lot RTC rattaché au composant.
     * 
     * @param compoBase
     *                  Composant à sauvegarder en base.
     * @param compoJSON
     *                  Objet JAVA provenant du fichier JSON.
     * @throws TeamRepositoryException
     *                                 Exception provenant de RTC.
     */
    private void initLot(ComposantBase compoBase, ComposantJSON compoJSON) throws TeamRepositoryException
    {
        // Lot RTC
        String numeroLot = String.valueOf(compoJSON.getNumeroLot());

        // Récupération du lot depuis la base de donnée
        LotRTC lotRTCBase = DaoFactory.getMySQLDao(LotRTC.class).recupEltParIndex(numeroLot);

        // Récupération des informations du lot depuis RTC
        LotRTC lotRTC = controlRTC.creerLotSuiviRTCDepuisItem(controlRTC.recupWorkItemDepuisId(Integer.parseInt(numeroLot)));
        new MajLotsRTCTask(null, this).majLotRTC(lotRTC);

        if (lotRTCBase == null)
            compoBase.setLotRTC(lotRTC);
        else
            compoBase.setLotRTC(lotRTCBase.update(lotRTC));
    }

    /**
     * Initialisation de la date de mise en production du repack contenant le composant.
     * 
     * @param compoBase
     *                  Composant traité.
     */
    private void initDateRepack(ComposantBase compoBase)
    {
        // Mise à jour de la date de rapck du composant.
        LocalDate repackCompo = compoBase.majDateRepack(cr.getRepacksComposant(compoBase));

        // Mise à jour de la date de repack du lot lié au composant.
        if (repackCompo != null)
            compoBase.getLotRTC().majDateRepack(repackCompo);
    }

    /**
     * Initialisation du code application du composant depuis les données venues de SonarQube.
     * 
     * @param compoBase
     *                       Composant traité.
     * @param compoMetriques
     *                       Données provenant de SonarQube.
     */
    private void initCodeAppli(ComposantBase compoBase, ComposantSonar compoMetriques)
    {
        // Code application
        String codeAppli = compoMetriques.getValueMetrique(Metrique.APPLI, Statics.EMPTY).toUpperCase(Locale.FRENCH);

        Application appli = daoAppli.recupEltParIndex(codeAppli);

        boolean appliExiste = controlAppCATS.testApplicationExiste(codeAppli);

        // On récupère le code appli des infos du composant et si on ne trouve pas le code application dans la base de données, on en crée une nouvelle.
        // Le composant sera persisté à la fin du traitement, pas besoin d'enregistrer l'application immédiatement
        if (appli != null)
            appli.setReferentiel(appliExiste);
        else
            appli = Application.getApplication(codeAppli, appliExiste);

        compoBase.setAppli(appli);
    }

    /**
     * Traitements des défauts - création et mise à jour.
     * 
     * @param compoBase
     *                   Composant traité.
     * @param compoJSON
     *                   Objet provenant du traitement du fichier JSON.
     * @param issuesBase
     *                   Liste des Issues provenant de SonarQube.
     * @return
     *         Vrai si le traitement n'a pas eu de plantages.
     * @throws ExceptionTraiterCompo
     *                               Exception spécifiques au traitement.
     */
    private boolean traiterDefauts(ComposantBase compoBase, ComposantJSON compoJSON, List<IssueBase> issuesBase) throws ExceptionTraiterCompo
    {
        // Récupération du defaut depuis la base de donnée, s'il existe deje, sinon création d'un nouveau defaut si le composant est en erreur.
        DefautQualite dq = daoDQ.recupEltParIndex(compoBase.getMapIndex() + compoBase.getLotRTC().getNumero());
        EtatCodeAppli etatCodeAppli = EtatCodeAppli.calculCodeAppli(compoBase);
        if (dq == null && (compoBase.getQualityGate() == QG.ERROR || etatCodeAppli == EtatCodeAppli.ERREUR))
            dq = creerDefaut(compoBase, issuesBase);

        if (dq == null)
            return false;

        // Mise à jour des défauts existants et persistances des modifitcaions en base de données
        miseAJourDefaut(dq, compoJSON, etatCodeAppli);

        // Ajout des assignés qui ne sont pas déjà repertoriés dans le défaut
        for (IssueBase issueBase : issuesBase)
        {
            if (!issueBase.getUtilisateur().equals(Statics.USERINCONNU))
                dq.ajouterCreateurIssue(issueBase.getUtilisateur());
            issueBase.setDefautQualite(dq);
        }

        sauvegarde(daoDQ, dq);
        return true;
    }

    /**
     * Création d'un nouveau defaut avec les informations essentielles
     * 
     * @param compoBase
     *                   Composant à traiter
     * @param issuesBase
     *                   Liste des anomalies sur le composant
     * @return
     *         Le DefautQualite
     */
    private DefautQualite creerDefaut(ComposantBase compoBase, List<IssueBase> issuesBase)
    {
        // Initialisation avec le composant et le lot liés
        DefautQualite retour = ModelFactory.build(DefautQualite.class);
        retour.setCompo(compoBase);
        retour.setLotRTC(compoBase.getLotRTC());
        retour.initMapIndex();

        // Ajout de chaque ETP des créateurs de défauts
        for (IssueBase assigne : issuesBase)
        {
            if (!assigne.getUtilisateur().equals(Statics.USERINCONNU))
                retour.ajouterCreateurIssue(assigne.getUtilisateur());
        }

        // Mise à jour de la date de repack
        if (retour.getLotRTC().getDateRepack() != null)
            retour.setDateMepPrev(retour.getLotRTC().getDateRepack());
        else
            retour.setDateMepPrev(retour.getLotRTC().getEdition().getDateMEP());

        return retour;
    }

    /**
     * Mise à jour d'un défaut avec les informations de Jazz-RTC.
     * 
     * @param dq
     *                      DéfautQualite traité.
     * @param compoJSON
     *                      Objet JAVA provenant du fichier JSON initial
     * @param etatCodeAppli
     *                      Etat du code application
     * @throws ExceptionTraiterCompo
     *                               Exception spécifique au traitement
     */
    private void miseAJourDefaut(DefautQualite dq, ComposantJSON compoJSON, EtatCodeAppli etatCodeAppli) throws ExceptionTraiterCompo
    {
        // Analyse pour vérifier le type de defaut
        StatutQGProjet sp = api.getAnalyseQGInfos(compoJSON.getIdAnalyse());

        if (sp == null)
            throw new ExceptionTraiterCompo("Impossible de retrouver l'analyse du composant donne : " + compoJSON.getProjet().getNom() + " - analyse : " + compoJSON.getIdAnalyse());

        boolean code = false;

        // Itération sur les conditions
        for (StatutQGCondition cond : sp.getConditions())
        {
            if ("ERROR".equals(cond.getStatus()) && !"notecodeappli".equals(cond.getMetricKey()))
                code = true;
        }

        // Test état code appli
        if (etatCodeAppli == EtatCodeAppli.ERREUR && code)
            dq.setTypeDefaut(TypeDefautQualite.MIXTE);
        else if (etatCodeAppli == EtatCodeAppli.ERREUR)
            dq.setTypeDefaut(TypeDefautQualite.APPLI);
        else if (code)
            dq.setTypeDefaut(TypeDefautQualite.SONAR);
        else
        {
            // Pas de traitement dans les autres cas.
        }

        dq.setEtatCodeAppli(etatCodeAppli);

        // Enregistrement de l'id de la dernière analyse
        dq.setAnalyseId(compoJSON.getIdAnalyse());

        // Mise à jour des informations de l'anomalie
        controlRTC.controleAnoRTC(dq);
    }

    /**
     * Assignation des anomalies SonarQube existantes par encore assignées.
     * 
     * @param compoJSON
     *                  Objet JAVA provenant du fichier initial JSON.
     * @return
     *         Liste des anomalies qui n'ont pas pu être assignées.
     * @throws TeamRepositoryException
     *                                 Exception RTC.
     * @throws ExceptionTraiterCompo
     *                                 Exception spécifique au traitement.
     */
    private List<IssueBase> assignerAnoSonar(ComposantJSON compoJSON) throws TeamRepositoryException, ExceptionTraiterCompo
    {
        // Liste de retour des issues non assignées
        List<IssueBase> retour = new ArrayList<>();

        // Date de l'analyse
        ZonedDateTime dateAnalyse = api.getDateAnalyse(compoJSON);

        // Paramètre d'appel pour récupérer les défauts sonar
        List<ParamAPI> params = new ArrayList<>();
        params.add(new ParamAPI("severities", "CRITICAL, BLOCKER"));
        params.add(new ParamAPI("resolved", "false"));
        params.add(new ParamAPI("types", "VULNERABILITY, CODE_SMELL, BUG"));
        params.add(new ParamAPI("createdAt", dateAnalyse.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXX"))));
        params.add(new ParamAPI("onComponentOnly", "false"));

        List<Issue> issues = api.getIssuesGenerique(params);

        for (Issue issue : issues)
        {
            // CReation avec id issue
            IssueBase issueBase = IssueBase.build(issue);

            // Traitement auteur de l'anomalie
            String prepare = preparationNomAuteur(issue.getAuteur());

            // Récupération de l'identifiant de l'auteur.
            IContributor contrib = controlRTC.recupContributorDepuisNom(prepare);
            if (contrib == null)
            {
                LOGPLANTAGE.error("Utilisateur non renseigne ou inconnu dans RTC : " + prepare);
                issueBase.setUtilisateur(Statics.USERINCONNU);
                issueBase.setAnalyseId(compoJSON.getIdAnalyse());
                retour.add(issueBase);
                continue;
            }

            // Ajout utilisateur au defaut
            issueBase.setUtilisateur(Utilisateur.build(contrib));

            // Assignation defaut
            if (!api.assignerIssue(issueBase))
            {
                issueBase.setAnalyseId(compoJSON.getIdAnalyse());
                retour.add(issueBase);
            }
        }

        return retour;
    }

    /**
     * Suppression du fichier JSON aprés traitement et sauvegarde de celui-ci au besoin.
     * 
     * @param sauvegardeFichier
     *                          Si vrai, sauvegarde le fichier dans le repertoire paramétré.
     * @throws IOException
     *                     Exception si erreur au moment de suavegarder le fichier.
     */
    private void suppressionFichier(boolean sauvegardeFichier) throws IOException
    {
        if (sauvegardeFichier)
            Files.copy(file.toPath(), Paths.get(Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATHRAPPORT) + file.getName()), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(file.toPath());
    }

    /**
     * Transforme le nom d'un auteur de SonarQube pour pouvoir trouver son ETP dans RTC.
     * 
     * @param auteur
     *               Nom à traiter.
     * @return
     *         Nom modifié pour correspondre au standard de RTC.
     */
    private String preparationNomAuteur(String auteur)
    {
        if (auteur.length() < MINAUTEUR)
            return Statics.EMPTY;

        String[] split = PATTERNSPLIT.split(auteur);
        StringBuilder builder = new StringBuilder();

        // Nom en majuscule
        for (int i = 0; i < split.length - 1; i++)
        {
            builder.append(split[i].toUpperCase(Locale.FRANCE)).append(Statics.SPACE);
        }

        // Prenom avec la première lettre en majuscule
        String prenom = split[split.length - 1].toLowerCase(Locale.FRANCE);
        prenom = prenom.substring(0, 1).toUpperCase(Locale.FRANCE) + prenom.substring(1);

        // Renvoie avec ajout de l'espace entre deux.
        return builder.append(prenom).toString();
    }

    /*---------- ACCESSEURS ----------*/
}
