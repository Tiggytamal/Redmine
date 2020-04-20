package control.rtc;

import static utilities.Statics.EMPTY;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.internal.TeamRepository;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.IAuditableHandle;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.IItem;
import com.ibm.team.repository.common.PermissionDeniedException;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.UUID;
import com.ibm.team.repository.common.model.query.BaseContributorQueryModel.ContributorQueryModel;
import com.ibm.team.repository.common.query.IItemQuery;
import com.ibm.team.repository.common.query.IItemQueryPage;
import com.ibm.team.repository.common.query.ast.IPredicate;
import com.ibm.team.repository.common.service.IQueryService;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IDetailedStatus;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.internal.model.query.BaseWorkItemQueryModel.WorkItemQueryModel;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.workflow.IWorkflowAction;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

import control.task.AbstractTask;
import control.task.ChargementRTCTask;
import control.task.LaunchTask;
import dao.Dao;
import dao.DaoComposantBase;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EnumRTC;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.ParamSpec;
import utilities.AbstractToStringImpl;
import utilities.DateHelper;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de contrôle des accés RTC. Singelton.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlRTC extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    /** Type correspondant au espace produit */
    static final String ESPACEPRODUIT = "Process Template Espace Produit";

    /** 8h en milisecondes */
    private static final int MINS8 = 28_800_000;

    /** logger général */
    private static final Logger LOGGER = Utilities.getLogger("complet-log");

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /** Type correspondant au projet RTC */
    private static final String LOTRTC1 = "fr.ca.cat.wi.lotprojet";

    /** Type correspondant au projet RTC */
    private static final String LOTRTC2 = "fr.ca.cat.wi.lotfonctionnement";

    /** Taille de la pagination */
    private static final int PAGESIZE = 512;

    /** Instance du contrôleur */
    private static ControlRTC instance;

    private ITeamRepository repo;
    private IProgressMonitor monitor;
    private Map<String, IProjectArea> pareas;
    private IWorkItemClient workItemClient;
    private IAuditableClient auditableClient;
    private IAuditableCommon auditableCommon;
    private DaoComposantBase daoCompo;
    private Dao<LotRTC, String> daoLotRTC;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant les informations utilisateurs de l'application.
     * 
     */
    private ControlRTC()
    {
        // Contrôle de la configuration
        String adresse = Statics.proprietesXML.getMapParams().get(Param.URLRTC);
        if (adresse == null || adresse.isEmpty())
            throw new TechnicalException("control.ControlRTC - Initialisation : Paramètre Url RTC inconnue!");

        // Controle pour éviter la création par reflexion d'une seconde instance
        if (instance != null)
            throw new AssertionError("control.rtc.ControlRTC - Singleton, instanciation interdite!");

        // Démarrage de la plateforme
        TeamPlatform.startup();

        // Création du repository des projet RTC depuis l'adresse en paramètre
        repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(adresse);

        // Initialisation des clients pour les traitements
        workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
        auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);

        // Daos
        daoCompo = DaoFactory.getMySQLDao(ComposantBase.class);
        daoLotRTC = DaoFactory.getMySQLDao(LotRTC.class);

        monitor = new NullProgressMonitor();
        pareas = new HashMap<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Instanciation singleton
     * 
     * @return
     *         L'instance de la classe
     */
    public static ControlRTC build()
    {
        if (instance == null)
            instance = new ControlRTC();
        return instance;
    }

    /**
     * Connection au repository RTC avec récupération des projets RTC dans un autre Thread, pour éviter à l'application d'être inaccessible trop
     * longtemps.
     * 
     * @return
     *         Vrai si la connexion est effectuée.
     */
    public boolean connexion()
    {
        boolean retour = connexionSimple();

        // Récupération de tous les projets si la liste est vide. Effectuée normalement une seule fois par instance.
        // Lancement d'une tâche pour ne pas bloquer l'application
        if (pareas.isEmpty())
            LaunchTask.startTask(new ChargementRTCTask());

        return retour;
    }

    /**
     * Connexion au serveur RTC sans récupération des projets.
     * 
     * @return
     *         Vrai si la connexion est effectuée.
     */
    public boolean connexionSimple()
    {
        // Test de connexion au serveur RTC
        if (!repo.loggedIn())
        {
            repo.registerLoginHandler((ITeamRepository repository) -> new UsernameAndPasswordLoginInfo(Statics.info.getPseudo(), Statics.info.getMotDePasse()));
            try
            {
                repo.login(monitor);

                // Sauvegarde utilisateur
                Statics.info.setUser(Utilisateur.build(recupContributorDepuisId(Statics.info.getPseudo())));
            }
            catch (TeamRepositoryException e)
            {
                LOGPLANTAGE.error(EMPTY, e);
                return false;
            }
        }
        return true;
    }

    /**
     * Déconnexion à RTC
     */
    public void logout()
    {
        repo.logout();
    }

    /**
     * Fermeture de la plateforme.
     */
    public void shutdown()
    {
        TeamPlatform.shutdown();
    }

    /**
     * Récupère le nom du projet RTC depuis Jazz dont provient le lot en paramètre.
     * 
     * @param lot
     *            Numéro du lot à traiter.
     * @return
     *         Le nom du projet RTC correspondant.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public String recupNomProjetRTCDepuisWiLot(int lot) throws TeamRepositoryException
    {
        IWorkItem workItem = workItemClient.findWorkItemById(lot, IWorkItem.FULL_PROFILE, monitor);
        if (workItem == null)
        {
            LOGGER.warn("Récupération projetRTC - Lot introuvable : " + lot);
            return EMPTY;
        }
        IProjectArea area = (IProjectArea) repo.itemManager().fetchCompleteItem(workItem.getProjectArea(), IItemManager.DEFAULT, monitor);
        return area.getName();
    }

    /**
     * Récupération des informations d'un objet RTC. A utiliser la pluspart du temps sauf pour les IAttribut.
     * En cas de plantage utiliser recupEltDepuisHandle.
     * ex: recupItemDepuisHandle(IWorkItem.class, handle));
     * 
     * @param classRetour
     *                    Classe de l'objet désiré.
     * @param handle
     *                    Objet inital sans toutes les informations (Handle).
     * @param             <R>
     *                    Class de l'objet désiré.
     * @param             <T>
     *                    Classe initiale u conteneur.
     * @return
     *         L'objet RTC complet.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public <R extends T, T extends IAuditableHandle> R recupItemDepuisHandle(Class<R> classRetour, T handle) throws TeamRepositoryException
    {
        return classRetour.cast(repo.itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, monitor));
    }

    /**
     * Récupération des informations d'un objet RTC. A utiliser pour le IAttribut et pour les objets qui ne marche pas avec recupItemDepuisHandle.
     * ex: recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
     * 
     * @param classRetour
     *                    Classe de l'objet désiré.
     * @param handle
     *                    Objet inital sans toutes les informations (Handle).
     * @param profil
     *                    Profil RTC à utiliser pour le traitement.
     * @param             <R>
     *                    Class de l'objet désiré.
     * @param             <T>
     *                    Classe initiale u conteneur.
     * @return
     *         L'objet RTC complet.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public <R extends T, T extends IAuditableHandle> R recupEltDepuisHandle(Class<R> classRetour, T handle, ItemProfile<? extends T> profil) throws TeamRepositoryException
    {
        return classRetour.cast(auditableClient.fetchCurrentAuditable(handle, profil, monitor));
    }

    /**
     * Permet de retourner la valeur {@code Identifier} d'un attribut depuis la valeur {@code String}.
     * 
     * @param name
     *             valeur de l'attribut sous forme d'une chaîne de caractères.
     * @param ia
     *             attribut dont on veut la valeur.
     * @return
     *         L'identifiant compatible avec RTC.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    @SuppressWarnings("unchecked")
    public Identifier<ILiteral> recupLiteralDepuisString(String name, IAttributeHandle ia) throws TeamRepositoryException
    {
        Identifier<ILiteral> literalID = null;
        IEnumeration<? extends ILiteral> enumeration = workItemClient.resolveEnumeration(ia, null);

        List<? extends ILiteral> literals = enumeration.getEnumerationLiterals();
        for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
        {
            ILiteral iLiteral = iterator.next();
            if (iLiteral.getName().equals(name))
            {
                literalID = (Identifier<ILiteral>) iLiteral.getIdentifier2();
                break;
            }
        }
        return literalID;
    }

    /**
     * Calcul de l'etat d'un objet RTC (IWorkItem).
     * 
     * @param item
     *             Objet à traiter.
     * @return
     *         L'état de l'objet en String.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public String recupEtatElement(IWorkItem item) throws TeamRepositoryException
    {
        if (item == null)
            return EMPTY;
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(item, monitor);
        return workflowInfo.getStateName(item.getState2()).trim();
    }

    /**
     * Récupération d'un IWorlItem depuis son identifiant (numéro de lot ou d'naomalie, ...).
     * 
     * @param id
     *           Identifiant de l'objet à récupérer.
     * @return
     *         L'objet recherché ou null.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public IWorkItem recupWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        return workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, monitor);
    }

    /**
     * Suppression d'un IWorkItem en utilisant son identifiant.
     * Fonction bloquée sur le serveur RTC selon les accès de l'utilisateur.
     * 
     * @param id
     *           Identifiant de l'objet à supprimer.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     * @return
     *         Code de retour de la suppression.
     */
    public int supprimerWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        IWorkItem wi = recupWorkItemDepuisId(id);

        // On sort un warning si l'objet est dej inexistant pour éviter les plantages
        if (wi == null)
            return IStatus.WARNING;
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);
        IStatus status = workingCopy.delete(monitor);
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        return status.getCode();
    }

    /**
     * Retourne la valeur d'un attribut d'un IWorkItem sous forme d'une chaîne de caractères.
     * 
     * @param attrb
     *              Atribut à récupérer.
     * @param item
     *              Objet à traiter.
     * @return
     *         Valeur de retour de l'attribut.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public String recupValeurAttribut(IAttribute attrb, IWorkItem item) throws TeamRepositoryException
    {
        Object objet = attrb.getValue(auditableCommon, item, monitor);
        if (objet instanceof Identifier)
        {
            @SuppressWarnings("unchecked")
            Identifier<? extends ILiteral> literalID = (Identifier<? extends ILiteral>) objet;
            List<? extends ILiteral> literals = workItemClient.resolveEnumeration(attrb, monitor).getEnumerationLiterals();

            for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
            {
                ILiteral iLiteral = iterator.next();
                if (iLiteral.getIdentifier2().equals(literalID))
                    return iLiteral.getName().trim();
            }
            return EMPTY;
        }

        if (objet instanceof String)
            return ((String) objet).trim();

        return EMPTY;
    }

    /**
     * Retourne le nom de la personne connectée à RTC.
     * 
     * @return
     *         Le nom de la personne au format RTC (NOM, Prénom)
     */
    public String recupNomContributorConnecte()
    {
        return repo.loggedInContributor().getName().trim();
    }

    /**
     * Retourne un Contributor depuis le nom d'une personne.
     * 
     * @param nom
     *            Nom de la personne dans RTC.
     * @return
     *         L'objet RTC correspondant à cette personne ou null.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public IContributor recupContributorDepuisNom(String nom) throws TeamRepositoryException
    {
        if (nom == null)
            return null;

        // Creation Query depuis ContributorQueryModel
        final IItemQuery query = IItemQuery.FACTORY.newInstance(ContributorQueryModel.ROOT);

        // Predicate avec un paramètre poru chercher depuis le nom avec un paramètre de type String
        final IPredicate predicate = ContributorQueryModel.ROOT.name()._eq(query.newStringArg());

        // Utilisation du Predicate en filtre.
        final IItemQuery filtered = (IItemQuery) query.filter(predicate);

        // Appel Service de requetes depuis TeamRepository et non l'interface.
        final IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la reqete avec le filtre
        final IItemQueryPage page = qs.queryItems(filtered, new Object[]
        { nom }, 1);

        // Retour de l'objet
        final List<?> handles = page.getItemHandles();
        if (!handles.isEmpty())
        {
            return recupItemDepuisHandle(IContributor.class, (IContributorHandle) handles.get(0));
        }
        return null;
    }

    /**
     * Retourne un Contributor depuis l'ETP d'une personne.
     * 
     * @param id
     *           ETP de la personne.
     * @return
     *         L'objet RTC correspondant à cette personne ou null.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public IContributor recupContributorDepuisId(String id) throws TeamRepositoryException
    {
        if (id == null || id.isEmpty())
            return null;

        // Creation Query depuis ContributorQueryModel
        final IItemQuery query = IItemQuery.FACTORY.newInstance(ContributorQueryModel.ROOT);

        // Predicate avec un paramètre poru chercher depuis le nom avec un paramètre de type String
        final IPredicate predicate = ContributorQueryModel.ROOT.userId()._eq(query.newStringArg());

        // Utilisation du Predicate en filtre.
        final IItemQuery filtered = (IItemQuery) query.filter(predicate);

        // Appel Service de requetes depuis TeamRepository et non l'interface.
        final IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la reqete avec le filtre
        final IItemQueryPage page = qs.queryItems(filtered, new Object[]
        { id }, 1);

        // Retour de l'objet
        final List<?> handles = page.getItemHandles();
        if (handles.isEmpty())
            return null;

        return (IContributor) repo.itemManager().fetchCompleteItem((IContributorHandle) handles.get(0), IItemManager.DEFAULT, monitor);
    }

    /**
     * Récupération de tous les lots RTC selon les paramètres fournis : <br>
     * - date Création = Tous les lots doivent avoir une date de création postérieure à celle-ci. <br>
     * Une erreur sera remontée si la date de création est nulle.
     * 
     * @param dateCreation
     *                     Date de création des lots.
     * @param task
     *                     Tâche parente.
     * @return
     *         La liste des lots RTC.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    @SuppressWarnings("unchecked")
    public List<LotRTC> recupLotsRTC(LocalDate dateCreation, AbstractTask task) throws TeamRepositoryException
    {
        // Contrôle que la date de création est renseignee
        if (dateCreation == null)
            throw new TechnicalException("Demande de récupération des lots RTC sans date de création initiale.");

        // Requetage sur RTC pour récupérer tous les Lots

        // Creation Query depuis ContributorQueryModel
        IItemQuery query = IItemQuery.FACTORY.newInstance(WorkItemQueryModel.ROOT);

        // Predicate avec un paramètre poru chercher depuis le nom avec un paramètre de type String
        IPredicate predicatFinal = WorkItemQueryModel.ROOT.workItemType()._eq(LOTRTC1)._or(WorkItemQueryModel.ROOT.workItemType()._eq(LOTRTC2));

        // Prise en compte de la date de création si elle est fournie
        IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(DateHelper.convertToOldDate(dateCreation, ZoneId.of("GMT")));
        predicatFinal = predicatFinal._and(predicatCreation);

        // Gestion des lots des composants. On ne prend que les lots qui ont un composantSonar dans la base.

        // Récupération des numéros de lots depuis la base de données
        List<String> listeLots = daoCompo.recupLotsAvecComposants();

        // Création de la liste de numéros de lots
        Number[] lots = new Number[listeLots.size()];
        int i = 0;
        for (String lot : listeLots)
        {
            lots[i] = Integer.valueOf(lot);
            i++;
        }

        // création du predicat
        IPredicate predicatLots = WorkItemQueryModel.ROOT.id()._in(lots);
        predicatFinal = predicatFinal._and(predicatLots);

        // Utilisation du Predicate en filtre.
        IItemQuery filtered = (IItemQuery) query.filter(predicatFinal);

        // Appel Service de requetes depuis TeamRepository et non l'interface.
        IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la reqete avec le filtre
        int pageSize = PAGESIZE;
        IItemQueryPage page = qs.queryItems(filtered, new Object[] {}, pageSize);

        // Liste de tous les lots trouves. On peut utiliser des IWorkItemHandles directement car la requete ne remonte que des WorkItems
        List<IWorkItemHandle> handles = new ArrayList<>();
        handles.addAll(page.getItemHandles());
        int nextPosition = page.getNextStartPosition();
        UUID token = page.getToken();

        // On itere sur toutes les pages de retour de la requetes pour remplir la liste.
        while (nextPosition != -1)
        {
            IItemQueryPage nextPage = (IItemQueryPage) qs.fetchPage(token, nextPosition, pageSize);
            nextPosition = nextPage.getNextStartPosition();
            handles.addAll(nextPage.getItemHandles());
        }

        task.setBaseMessage("Mise à jour des lots RTC incomplets...\n");
        task.updateMessage("");
        List<LotRTC> retour = miseAJourLotsIncomplets(task);

        // transformation des IWorkItemHandle en LotRTC

        // Variables
        i = 0;
        int size = handles.size();
        task.setBaseMessage("Mise à jour des lots RTC incomplets...OK\nRécupération lots RTC...\n");
        String lot = "Lot ";
        String sur = " sur ";
        long debut = System.currentTimeMillis();

        // valorisation de la liste de retour
        for (IWorkItemHandle handle : handles)
        {
            if (task.isCancelled())
                break;

            // Récupération de l'objet complet depuis l'handle de la requete
            retour.add(creerLotSuiviRTCDepuisItem(recupItemDepuisHandle(IWorkItem.class, handle)));

            // Affichage
            i++;
            task.calculTempsRestant(debut, i, size);
            task.updateProgress(i, size);
            task.updateMessage(new StringBuilder(lot).append(i).append(sur).append(size).toString());
        }

        return retour;
    }

    /**
     * Remonte une liste des tous les états passés par le lot ainsi que les dates de mise à jour.
     * 
     * @param lot
     *            Le lot à traiter.
     * @return
     *         Une map des différents états passés avec leurs dates de mise à jour.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    @SuppressWarnings("unchecked")
    public Map<EtatLot, LocalDate> recupDatesEtatsLot(IItem lot) throws TeamRepositoryException
    {
        // Manager
        IItemManager itemManager = repo.itemManager();

        // Récupération de l'historique des modifications faites sur le lot.
        List<IAuditableHandle> handles = itemManager.fetchAllStateHandles((IAuditableHandle) lot.getItemHandle(), monitor);
        List<IWorkItem> workItems = itemManager.fetchCompleteStates(handles, null);

        // Tri de la liste par dates decroissantes
        workItems.sort((o1, o2) -> o2.modified().compareTo(o1.modified()));

        // Transfert des données dans une map, pour avoir la première date de mise à jour de chaque etat.
        Map<EtatLot, LocalDate> retour = new EnumMap<>(EtatLot.class);
        for (IWorkItem iWorkItem : workItems)
        {
            IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(iWorkItem, monitor);
            String etat = workflowInfo.getStateName(iWorkItem.getState2());
            retour.put(EtatLot.from(etat), DateHelper.localDate(iWorkItem.modified()));
        }

        return retour;
    }

    /**
     * Récupération de tous les projets RTC.
     * 
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public void recupTousLesProjets() throws TeamRepositoryException
    {
        recupTousLesProjets(null);
    }

    /**
     * Récupération de tous les projets RTC avec intégration d'un moniteur.
     * 
     * @param monitor
     *                Moniteur du traitement.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public void recupTousLesProjets(IProgressMonitor monitor) throws TeamRepositoryException
    {
        if (!pareas.isEmpty())
            return;

        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, monitor))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
    }

    /**
     * Listage de tous les type de workItem d'un projet pour le developpement de l'application.<br>
     * Affiche le nom et le type de chaque objet dans les logs.
     * 
     * @param projet
     *               Nom du projet à traiter.
     * @return
     *         La liste de tous les type d'objet.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public List<IWorkItemType> recupListeTypeWorkItem(String projet) throws TeamRepositoryException
    {
        List<IWorkItemType> liste = workItemClient.findWorkItemTypes(pareas.get(projet), monitor);
        for (IWorkItemType type : liste)
        {
            LOGGER.info("nom : " + type.getDisplayName());
            LOGGER.info("identifiant : " + type.getIdentifier() + Statics.NL);
        }
        return liste;
    }

    /**
     * Listage de tous les type de workItem d'un projet pour le developpement de l'application.
     * Affiche le nom et l'identifiant de chaque attribut dans les logs.
     * 
     * @param id
     *           Identifiant du projett à traiter.
     * @return
     *         La liste de tous les attributs.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public List<IAttributeHandle> recupListeCustomAttributes(int id) throws TeamRepositoryException
    {
        List<IAttributeHandle> liste = recupWorkItemDepuisId(id).getCustomAttributes();
        for (IAttributeHandle handle : liste)
        {
            IAttribute attribut = recupItemDepuisHandle(IAttribute.class, handle);

            LOGGER.info("attribut : " + attribut.getDisplayName());
            LOGGER.info("identifiant : " + attribut.getIdentifier() + Statics.NL);
        }
        return liste;
    }

    /**
     * Création d'une anomalie dans RTC depuis le DefautQualite.
     * 
     * @param dq
     *           DefautQualite à traiter pour créer l'anomalie.
     * @return
     *         L'identifiant de l'naomlie créée.
     */
    public int creerAnoRTC(DefautQualite dq)
    {
        IWorkItem workItem = null;

        try
        {
            IProjectArea projet = pareas.get(dq.getLotRTC().getProjetRTC());
            if (projet == null)
                return 0;

            // Type de l'objet
            IWorkItemType itemType = workItemClient.findWorkItemType(projet, "defect", monitor);

            List<ICategory> categories = workItemClient.findCategories(projet, ICategory.FULL_PROFILE, monitor);
            ICategory cat = null;

            // On récupère une categorie "Projet"
            for (ICategory iCategory : categories)
            {
                if ("Projet".equals(iCategory.getName()))
                {
                    cat = iCategory;
                    break;
                }
            }

            // On récupère une categorie "Anomalie" si on a pas trouve Projet
            if (cat == null)
            {
                for (ICategory iCategory : categories)
                {
                    if (iCategory.getName().contains("Anomalie"))
                    {
                        cat = iCategory;
                        break;
                    }
                }
            }

            // Si aucune categorie ne correspond, on prend la première par defaut.
            if (cat == null)
                cat = categories.get(0);

            // Création
            WorkItemInitialization init = new WorkItemInitialization(itemType, cat, projet, dq);
            IWorkItemHandle handle = init.run(itemType, monitor);
            workItem = auditableClient.fetchCurrentAuditable(handle, WorkItem.FULL_PROFILE, monitor);
        }
        catch (TeamRepositoryException e)
        {
            LOGGER.error("Erreur traitement RTC création de Defect. Lot : " + dq.getLotRTC().getNumero());
            LOGPLANTAGE.error(EMPTY, e);
        }

        if (workItem == null)
            return 0;

        LOGGER.info("Creation anomalie RTC numero : " + workItem.getId() + " pour " + dq.getLotRTC().getNumero());
        return workItem.getId();

    }

    /**
     * Contrôle l'etat d'une anoRTC, ainsi que ses dates de mise à jour.
     * Aucune action s'il n'y pas d'anomlie sur le DefautQualite.
     * 
     * @param dq
     *           DefautQualite lié à l'anomalie.
     */
    public void controleAnoRTC(DefautQualite dq)
    {
        if (dq.getNumeroAnoRTC() == 0)
            return;

        IWorkItem anoRTC;
        try
        {
            anoRTC = recupWorkItemDepuisId(dq.getNumeroAnoRTC());
            dq.setEtatAnoRTC(recupEtatElement(anoRTC));

            // Correction si l'on a pas deje la date de création du Defect
            if (dq.getDateCreation() == null)
                dq.setDateCreation(DateHelper.convert(LocalDate.class, anoRTC.getCreationDate()));

            // Mise à jour de la date de resolution
            if (anoRTC.getResolutionDate() != null)
                dq.setDateReso(DateHelper.convert(LocalDate.class, anoRTC.getResolutionDate()));
            else
                dq.setDateReso(null);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error("Erreur récupération information Defect. Lot : " + dq.getLotRTC().getNumero(), e);
        }
    }

    /**
     * Teste si l'anomalie RTC est close.
     * 
     * @param numeroAno
     *                  Numero de l'anomalie à tester.
     * @return
     *         Vrai s'il n'y a pas d'naomlie ou que celle-ci ets close.
     */
    public boolean testSiAnoRTCClose(int numeroAno)
    {
        if (numeroAno == 0)
            return true;

        try
        {
            String etat = recupEtatElement(recupWorkItemDepuisId(numeroAno)).trim();
            return Statics.ANOCLOSE.equals(etat) || "Abandonnée".equals(etat);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            return false;
        }
    }

    /**
     * Ajoute un commentaire de relance sur une anomalie RTC et retourne le propriétaire de celle-ci.
     * 
     * @param id
     *           Identifiant de l'anomlie.
     * @return
     *         L'utilisateur possédant l'anomalie.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public Utilisateur relancerAno(int id) throws TeamRepositoryException
    {
        // Ajout du commentaire de relance
        IWorkItem wi = ajoutercommentaireAno(id, Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANORELANCE));

        // Renvoi du propriétaire de l'anomalie
        return Utilisateur.build(recupItemDepuisHandle(IContributor.class, wi.getOwner()));
    }

    /**
     * Ajoute un commentaire sur une anomalie RTC.
     * 
     * @param id
     *              Identifiant de l'anomalie.
     * @param texte
     *              Texte du commentaire à ajouter.
     * @return
     *         L'anomalie.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public IWorkItem ajoutercommentaireAno(int id, String texte) throws TeamRepositoryException
    {
        // Création workingCopy
        IWorkItem wi = recupWorkItemDepuisId(id);
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // Création du commentaire
        XMLString xmlTexte = XMLString.createFromPlainText(texte);

        // Ajout du commentaire et sauvegarde
        workingCopy.getWorkItem().getComments().append(workingCopy.getWorkItem().getComments().createComment(repo.loggedInContributor(), xmlTexte));
        IStatus statut = workingCopy.save(monitor);
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        if (!statut.isOK())
            throw new TechnicalException("control.rtc.ControlRTC.relancerAno - erreur au moment de la relance de l'anomalie : " + id);
        return wi;
    }

    /**
     * Réouverture d'une anomalie close.
     * 
     * @param dq
     *           DefautQualite lié à l'anomalie.
     * @return
     *         Vrai si l'anomalie a été réouverte.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public boolean rouvrirAnoRTC(DefautQualite dq) throws TeamRepositoryException
    {
        // récupération du workItem puis du workflow depuis le numero de l'anomalie
        IWorkItem wi = recupWorkItemDepuisId(dq.getNumeroAnoRTC());
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(wi, monitor);
        if (workflowInfo == null)
        {
            LOGPLANTAGE.error("control.rtc.ControlRTC.reouvrirAnoRTC - impossible de trouver le workflow de l'anomalie : " + dq.getNumeroAnoRTC());
            return false;
        }

        // Création du copyManager pour pouvoir modifier les données de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // Dans le cas d'un espace produit, on ne peut pas réouvrir l'anomalie, donc création d'une nouvelle
        if (recupItemDepuisHandle(IProjectArea.class, wi.getProjectArea()).getProcessName().contains(ESPACEPRODUIT))
        {
            // On ne crée pas de nouvelle anomalie si celle-ci n'est pas clôturée, abandonnée, ou rejeté
            EtatAnoRTCProduit etatLot = EtatAnoRTCProduit.from(workflowInfo.getStateName(wi.getState2()).trim());
            if (etatLot != EtatAnoRTCProduit.CLOSE && etatLot != EtatAnoRTCProduit.ABANDONNEE && etatLot != EtatAnoRTCProduit.REJETE)
                return false;

            // création de la nouvelle anomalie
            int numeroAno = creerAnoRTC(dq);
            if (numeroAno != 0)
            {
                dq.setNumeroAnoRTC(numeroAno);
                return true;
            }
            return false;
        }
        else
        {
            // Récuperatoin de l'état et réouverture de l'anomalie
            EtatAnoRTC etatLot = EtatAnoRTC.from(workflowInfo.getStateName(wi.getState2()).trim());
            if (etatLot == EtatAnoRTC.CLOSE || etatLot == EtatAnoRTC.ABANDONNEE)
                return changerEtatAno(workingCopy, workflowInfo, wi, "Réouvrir");
            return etatLot != EtatAnoRTC.CLOSE && etatLot != EtatAnoRTC.ABANDONNEE;
        }
    }

    /**
     * Cloture d'une anomalie RTC peu importe l'etat du workflow.
     * 
     * @param id
     *           Identifiant de l'anomalie.
     * @return
     *         Vrai si l'anomalie a été clôturée.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public boolean fermerAnoRTC(int id) throws TeamRepositoryException
    {
        // récupération du workItem puis du workflow depuis le numero de l'anomalie
        IWorkItem wi = recupWorkItemDepuisId(id);
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(wi, monitor);
        if (workflowInfo == null)
        {
            LOGPLANTAGE.error("control.rtc.ControlRTC.fermerAnoRTC - impossible de trouver le workflow de l'anomalie : " + id);
            return false;
        }

        // Création du copyManager pour pouvoir modifier les données de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // Ajout des données necessaires pour cleturer si elles ne sont pas presentes

        // Date de livraison / homolation
        IAttribute date = workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.DATELIVHOMO.getValeur(), monitor);
        if (date != null && wi.getValue(date) == null)
        {
            workingCopy.getWorkItem().setValue(date, new Timestamp(System.currentTimeMillis()));
            workingCopy.save(monitor);
        }

        // Entite responsable correction
        IAttribute resp = workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.ENTITERESPCORRECTION.getValeur(), monitor);
        if (resp != null && wi.getValue(resp).equals(recupLiteralDepuisString("-", resp)))
        {
            workingCopy.getWorkItem().setValue(resp, recupLiteralDepuisString("MOE", resp));
            workingCopy.save(monitor);
        }

        // Nature du probleme
        IAttribute nature = workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.NATURE.getValeur(), monitor);
        if (nature != null && wi.getValue(nature).equals(recupLiteralDepuisString("-", nature)))
        {
            workingCopy.getWorkItem().setValue(nature, recupLiteralDepuisString("Qualite", nature));
            workingCopy.save(monitor);
        }

        // Estimation
        if (wi.getDuration() == -1)
        {
            workingCopy.getWorkItem().setDuration(MINS8);
            workingCopy.save(monitor);
        }

        // Boucle pour passer arriver jusqu'à l'anomalie close.
        // Retourne vrai si l'anomalie a été fermée, ou faux s'il y a eu un plantage aux changements d'états.

        if (recupItemDepuisHandle(IProjectArea.class, wi.getProjectArea()).getProcessName().contains(ESPACEPRODUIT))
            return fermerAnoRTCProduit(workflowInfo, workingCopy, wi);
        else
            return fermerAnoRTCClassique(workflowInfo, workingCopy, wi);
    }

    /**
     * Mise à jour d'un DefautQualite depuis RTC.
     * 
     * @param dq
     *           DefautQualite à traiter.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public void majDQ(DefautQualite dq) throws TeamRepositoryException
    {
        // Mise à jour du lot
        LotRTC lot = creerLotSuiviRTCDepuisItem(recupWorkItemDepuisId(Integer.parseInt(dq.getLotRTC().getNumero())));
        dq.getLotRTC().update(lot);

        // Mise à jour de l'état de l'anomalie si elle existe
        if (dq.getNumeroAnoRTC() != 0)
            dq.setEtatAnoRTC(recupEtatElement(recupWorkItemDepuisId(dq.getNumeroAnoRTC())));
    }

    /**
     * Création d'un LotRTC regroupant les informations depuis RTC. Ne prend en compte que les IWorkItem.
     * 
     * @param wi
     *           Objet RTC à traiter.
     * @return
     *         Un LotRTC avec les informations nécessaires pour les traitements.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    public LotRTC creerLotSuiviRTCDepuisItem(IWorkItem wi) throws TeamRepositoryException
    {
        LotRTC retour = ModelFactory.build(LotRTC.class);
        retour.setProjetRTC(recupItemDepuisHandle(IProjectArea.class, wi.getProjectArea()).getName());
        retour.setNumero(String.valueOf(wi.getId()));
        retour.setLibelle(wi.getHTMLSummary().getPlainText());

        // Gestion cpi
        IContributor owner = recupItemDepuisHandle(IContributor.class, wi.getOwner());
        retour.setCpiProjet(Utilisateur.build(owner));

        // Gestion code projet Clarity
        retour.setProjetClarityString(recupValeurAttribut(workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.CLARITY.getValeur(), null), wi).trim());
        if (retour.getProjetClarityString().isEmpty())
            retour.setProjetClarityString(recupValeurAttribut(workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.CODECLARITY.getValeur(), null), wi).trim());

        // Gestion code edition
        retour.setEditionString(recupValeurAttribut(workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.EDITIONSICIBLE.getValeur(), null), wi).trim());
        if (retour.getEditionString().isEmpty())
            retour.setEditionString(recupValeurAttribut(workItemClient.findAttribute(wi.getProjectArea(), EnumRTC.EDITIONSI.getValeur(), null), wi).trim());
        EtatLot etatLot = EtatLot.from(recupEtatElement(wi));
        retour.setEtatLot(etatLot);
        retour.setDateMajEtat(recupDatesEtatsLot(wi).get(etatLot));
        if (etatLot == EtatLot.TERMINE || etatLot == EtatLot.EDITION)
            retour.setDateMep(recupDatesEtatsLot(wi).get(EtatLot.EDITION));

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Prépare la liste des lots RTC avec ceux qui ont des anomalies de contenu.
     * 
     * @param task
     *             Tâche parente.
     * 
     * @return
     *         La liste eds lots incomplets.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    private List<LotRTC> miseAJourLotsIncomplets(AbstractTask task) throws TeamRepositoryException
    {
        // Récupération des lots avec informations manquantes
        List<LotRTC> retour = daoLotRTC.readAll();
        for (Iterator<LotRTC> iter = retour.iterator(); iter.hasNext();)
        {
            LotRTC lot = iter.next();

            // On enleve tous les lots qui ont une edition, un projet clarity et un libellé non vide, ou qui ne sont pas accéssibles par RTC
            if ((!lot.getLibelle().isEmpty() && lot.getEdition() != null && lot.getProjetClarity() != null) || lot.isRtcHS())
                iter.remove();
        }

        // Mise à jour des lots depuis RTC
        IWorkItem wi = null;
        int i = 0;
        long debut = System.currentTimeMillis();
        for (Iterator<LotRTC> iter = retour.iterator(); iter.hasNext();)
        {
            LotRTC lotRTC = iter.next();
            try
            {
                // Récupération du workitem depuis le numero du lot
                wi = recupWorkItemDepuisId(Integer.parseInt(lotRTC.getNumero()));
            }
            catch (PermissionDeniedException e)
            {
                // Si on a pas l'acces au lot. on met à jour le top HS et la base de données
                iter.remove();
                lotRTC.setRtcHS(true);
                daoLotRTC.persist(lotRTC);
                LOGPLANTAGE.error(EMPTY, e);
            }

            if (wi == null || (!LOTRTC1.equals(wi.getWorkItemType()) && !LOTRTC2.equals(wi.getWorkItemType())) || lotRTC.isRtcHS())
                continue;

            LotRTC update = creerLotSuiviRTCDepuisItem(wi);
            lotRTC.update(update);

            i++;
            task.updateMessage("Lot " + lotRTC.getNumero());
            task.updateProgress(i, retour.size());
            task.calculTempsRestant(debut, i, retour.size());
        }

        return retour;
    }

    /**
     * Récupère l'identifiant d'une action à partir de son nom.
     * 
     * @param wfi
     *                   Workflow RTC.
     * @param wi
     *                   Objet RTC à traiter.
     * @param actionName
     *                   Nom de l'action souhaitée.
     * @return
     *         Identifiant de l'action souhaitée.
     */
    private String getAction(IWorkflowInfo wfi, IWorkItem wi, String actionName)
    {
        Identifier<IWorkflowAction>[] actionIds = wfi.getActionIds(wi.getState2());

        for (Identifier<IWorkflowAction> id : actionIds)
        {
            if (wfi.getActionName(id).equals(actionName))
                return id.getStringIdentifier();
        }
        return Statics.EMPTY;
    }

    /**
     * Modifie l'etat d'une anomalie suivant l'action donnée.
     * 
     * @param wc
     *                   Copie RTC de l'objet à traiter.
     * @param wfi
     *                   Workflow RTC.
     * @param wi
     *                   Objet RTC à traiter.
     * @param actionName
     *                   Nom de l'action souhaitée.
     * @return
     *         Vrai si l'action a été effectuée avec succès.
     */
    private boolean changerEtatAno(WorkItemWorkingCopy wc, IWorkflowInfo wfi, IWorkItem wi, String actionName)
    {
        wc.setWorkflowAction(getAction(wfi, wi, actionName));
        IDetailedStatus status = wc.save(monitor);
        boolean ok = status.isOK();
        if (!ok)
            LOGPLANTAGE.error(Statics.EMPTY, status.getException());
        return ok;
    }

    /**
     * Fermeture d'une anomalie RTC dans un espace de type produit.
     * 
     * @param wfi
     *            Workflow RTC.
     * @param wc
     *            Copie RTC de l'objet à traiter.
     * @param wi
     *            Anomalie RTC à traiter.
     * @return
     *         Vrai si l'anomalie a bine été fermée.
     */
    private boolean fermerAnoRTCProduit(IWorkflowInfo wfi, WorkItemWorkingCopy wc, IWorkItem wi)
    {
        boolean ok = true;
        while (ok)
        {
            EtatAnoRTCProduit etatLot = EtatAnoRTCProduit.from(wfi.getStateName(wi.getState2()).trim());

            // Attention trois cases utilisent le même code.
            switch (etatLot)
            {
                case CLOSE:
                case REJETE:
                case ABANDONNEE:
                    workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
                    return true;

                case NOUVELLE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.NOUVELLE.getAction());
                    break;

                case AFFECTEE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.AFFECTEE.getAction());
                    break;

                case ANALYSE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.ANALYSE.getAction());
                    break;

                case CORRIGEE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.CORRIGEE.getAction());
                    break;

                case VMOE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.VMOE.getAction());
                    break;

                case VERIFIEE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.VERIFIEE.getAction());
                    break;

                case VMOA:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.VMOA.getAction());
                    break;

                case ENATTENTE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.ENATTENTE.getAction());
                    break;

                case CORRECTION:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTCProduit.CORRECTION.getAction());
                    break;
            }
        }

        // Cas s'il y a eu un plantage dans la mise à jour de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        return false;
    }

    /**
     * Fermeture d'une anomalie RTC dans un espace classique non produit.
     * 
     * @param wfi
     *            Workflow RTC.
     * @param wc
     *            Copie RTC de l'objet à traiter.
     * @param wi
     *            Anomalie RTC à traiter.
     * @return
     *         Vrai si l'anomalie a bine été fermée.
     */
    private boolean fermerAnoRTCClassique(IWorkflowInfo wfi, WorkItemWorkingCopy wc, IWorkItem wi)
    {
        boolean ok = true;
        while (ok)
        {
            EtatAnoRTC etatLot = EtatAnoRTC.from(wfi.getStateName(wi.getState2()).trim());

            // Attention trois cases utilisent le même code.
            switch (etatLot)
            {
                case CLOSE:
                case REJETEE:
                case ABANDONNEE:
                    workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
                    return true;

                case ENCOURS:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.ENCOURS.getAction());
                    break;

                case NOUVELLE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.NOUVELLE.getAction());
                    break;

                case OUVERTE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.OUVERTE.getAction());
                    break;

                case REOUVERTE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.REOUVERTE.getAction());
                    break;

                case VMOE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.VMOE.getAction());
                    break;

                case VMOA:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.VMOA.getAction());
                    break;

                case RESOLUE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.RESOLUE.getAction());
                    break;

                case VERIFIEE:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.VERIFIEE.getAction());
                    break;

                case EDITEUR:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.EDITEUR.getAction());
                    break;

                case DENOUEMENT:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.DENOUEMENT.getAction());
                    break;

                case ATTEDITEUR:
                    ok = changerEtatAno(wc, wfi, wi, EtatAnoRTC.ATTEDITEUR.getAction());
                    break;
            }
        }

        // Cas s'il y a eu un plantage dans la mise à jour de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        return false;
    }

    /*---------- ACCESSEURS ----------*/

    ITeamRepository getRepo()
    {
        return repo;
    }

    IWorkItemClient getClient()
    {
        return workItemClient;
    }

    /**
     * Getter de l'instance. On ne verifie pas la nullité pour permettre de fermer l'applicaiton dans le cas d'un mauvais paramétrage.
     * Celle-ci est forcement instanciée au moment des tests de connexion.
     * 
     * @return
     */
    public static ControlRTC getInstance()
    {
        return instance;
    }

    /*---------- CLASSES PRIVEES **********/
}
