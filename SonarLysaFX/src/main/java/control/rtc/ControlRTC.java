package control.rtc;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

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
import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.UUID;
import com.ibm.team.repository.common.model.query.BaseContributorQueryModel.ContributorQueryModel;
import com.ibm.team.repository.common.query.IItemQuery;
import com.ibm.team.repository.common.query.IItemQueryPage;
import com.ibm.team.repository.common.query.ast.IPredicate;
import com.ibm.team.repository.common.service.IQueryService;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.internal.model.query.BaseWorkItemQueryModel.WorkItemQueryModel;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import model.Anomalie;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.TypeEnumRTC;
import model.enums.TypeFichier;
import utilities.DateConvert;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de controle des acc�s RTC sous form d�num�ration pour forcer le singleton
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class ControlRTC
{
    /*---------- ATTRIBUTS ----------*/

    private final LocalDate today = LocalDate.now();

    /** logger g�n�ral */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    /** Taille de la pagination */
    private static final int PAGESIZE = 512;

    private ITeamRepository repo;
    private IProgressMonitor progressMonitor;
    private Map<String, IProjectArea> pareas;
    private IWorkItemClient workItemClient;
    private IAuditableClient auditableClient;
    private IAuditableCommon auditableCommon;

    /** Instance du controleur */
    public static final ControlRTC INSTANCE = new ControlRTC();

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de base utilisant les informations utilisateurs de l'application
     * 
     * @throws TeamRepositoryException
     */
    private ControlRTC()
    {
        // Controle pour �viter la cr�ation par reflexion d'une seconde instance
        if (INSTANCE != null)
            throw new AssertException();

        // R�cup�ration du r�f�rentiel de donn�es depuis l'url
        TeamPlatform.startup();
        repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(Statics.proprietesXML.getMapParams().get(Param.URLRTC));
        workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
        auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
        progressMonitor = new NullProgressMonitor();
        pareas = new HashMap<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Connection au repository RTC
     */
    public boolean connexion()
    {
        try
        {
            // Un erreur de login est envoy�e dans les logs de plantage
            if (!repo.loggedIn())
            {
                repo.registerLoginHandler((ITeamRepository repository) -> new UsernameAndPasswordLoginInfo(Statics.info.getPseudo(), Statics.info.getMotDePasse()));
                repo.login(progressMonitor);
            }

            // R�cup�rationd e tous les projets si la iste est vide. Effectu�e normalemetn une seule fois par instance.
            if (pareas.isEmpty())
                recupererTousLesProjets();
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
            return false;
        }
        return true;
    }

    public void shutdown()
    {
        TeamPlatform.shutdown();
    }

    /**
     * R�cup�re le nom du projet RTC depuis Jazz dont provient le lot en param�tre
     * 
     * @param lot
     * @return
     * @throws TeamRepositoryException
     */
    public String recupProjetRTCDepuisWiLot(int lot) throws TeamRepositoryException
    {
        IWorkItem workItem = workItemClient.findWorkItemById(lot, IWorkItem.FULL_PROFILE, progressMonitor);
        if (workItem == null)
        {
            LOGGER.warn("R�cup�ration projetRTC - Lot introuvable : " + lot);
            return "";
        }
        IProjectArea area = (IProjectArea) repo.itemManager().fetchCompleteItem(workItem.getProjectArea(), IItemManager.DEFAULT, progressMonitor);
        return area.getName();
    }

    /**
     * 
     * @param classRetour
     * @param handle
     * @return
     * @throws TeamRepositoryException
     */
    public <R extends T, T extends IAuditableHandle> R recupererItemDepuisHandle(Class<R> classRetour, T handle) throws TeamRepositoryException
    {
        return classRetour.cast(repo.itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, progressMonitor));
    }

    /**
     * 
     * @param classRetour
     * @param handle
     * @param profil
     * @return
     * @throws TeamRepositoryException
     */
    public <R extends T, T extends IAuditableHandle> R recupererEltDepuisHandle(Class<R> classRetour, T handle, ItemProfile<? extends T> profil) throws TeamRepositoryException
    {
        return classRetour.cast(auditableClient.fetchCurrentAuditable(handle, profil, progressMonitor));
    }

    /**
     * Calcul de l'�tat d'un objet RTC
     * 
     * @param item
     * @return
     * @throws TeamRepositoryException
     */
    public String recupEtatElement(IWorkItem item) throws TeamRepositoryException
    {
        if (item == null)
            return "";
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(item, progressMonitor);
        return workflowInfo.getStateName(item.getState2());
    }

    /**
     * 
     * @param id
     * @return
     * @throws TeamRepositoryException
     */
    public IWorkItem recupWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        return workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, progressMonitor);
    }

    /**
     * Cr�ation du Defect dans RTC
     * 
     * @param ano
     *            anomalie servant d'origine au Defect
     * @return
     */
    public int creerDefect(Anomalie ano)
    {
        IWorkItem workItem = null;

        try
        {
            IProjectArea projet = pareas.get(ano.getProjetRTC());

            // Type de l'objet
            IWorkItemType itemType = workItemClient.findWorkItemType(projet, "defect", progressMonitor);

            List<ICategory> categories = workItemClient.findCategories(projet, ICategory.FULL_PROFILE, progressMonitor);
            ICategory cat = null;
            for (ICategory iCategory : categories)
            {
                if ("Projet".equals(iCategory.getName()) || iCategory.getName().contains("Anomalie"))
                    cat = iCategory;
            }

            // Cr�ation
            WorkItemInitialization init = new WorkItemInitialization(itemType, cat, projet, ano);
            IWorkItemHandle handle = init.run(itemType, progressMonitor);
            workItem = auditableClient.fetchCurrentAuditable(handle, WorkItem.FULL_PROFILE, progressMonitor);
        }
        catch (TeamRepositoryException e)
        {
            LOGGER.error("Erreur traitement RTC cr�ation de Defect. Lot : " + ano.getLot());
            LOGPLANTAGE.error(e);
        }

        if (workItem != null)
        {
            LOGGER.info("Creation anomalie RTC num�ro : " + workItem.getId() + " pour " + ano.getLot());
            return workItem.getId();
        }
        return 0;
    }

    /**
     * Retourne la valeur d'un attribut d'un WorkItem RTC sous forme d'une chaine de caract�res.
     * 
     * @param attrb
     * @param item
     * @return
     * @throws TeamRepositoryException
     */
    public String recupererValeurAttribut(IAttribute attrb, IWorkItem item) throws TeamRepositoryException
    {
        Object objet = attrb.getValue(auditableCommon, item, progressMonitor);
        if (objet instanceof Identifier)
        {
            @SuppressWarnings("unchecked")
            Identifier<? extends ILiteral> literalID = (Identifier<? extends ILiteral>) objet;
            List<? extends ILiteral> literals = workItemClient.resolveEnumeration(attrb, progressMonitor).getEnumerationLiterals();

            for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
            {
                ILiteral iLiteral = iterator.next();
                if (iLiteral.getIdentifier2().equals(literalID))
                    return iLiteral.getName();
            }
        }
        else if (objet instanceof String)
            return (String) objet;

        return null;
    }

    /**
     * Retourne le nom de la personne connect� � RTC.
     * 
     * @return
     */
    public String recupNomContributorConnecte()
    {
        return repo.loggedInContributor().getName();
    }

    /**
     * Retourne un Contributor depuis le nom d'une personne
     * 
     * @param nom
     * @return
     * @throws TeamRepositoryException
     */
    public IContributor recupContributorDepuisNom(String nom) throws TeamRepositoryException
    {
        if (nom == null)
            return null;

        // Creation Query depuis ContributorQueryModel
        final IItemQuery query = IItemQuery.FACTORY.newInstance(ContributorQueryModel.ROOT);

        // Predicate avec un param�tre poru chercher depuis le nom avec un param�tre de type String
        final IPredicate predicate = ContributorQueryModel.ROOT.name()._eq(query.newStringArg());

        // Utilisation du Predicate en filtre.
        final IItemQuery filtered = (IItemQuery) query.filter(predicate);

        // Appel Service de requ�tes depuis TeamRepository et non l'interface.
        final IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la req�te avec le filtre
        final IItemQueryPage page = qs.queryItems(filtered, new Object[] { nom }, 1);

        // Retour de l'objet
        final List<?> handles = page.getItemHandles();
        if (!handles.isEmpty())
        {
            return recupererItemDepuisHandle(IContributor.class, (IContributorHandle) handles.get(0));
        }

        return null;
    }

    /**
     * Retourne un Contributor depuis le nom d'une personne
     * 
     * @param id
     * @return
     * @throws TeamRepositoryException
     */
    public IContributor recupContributorDepuisId(String id) throws TeamRepositoryException
    {
        if (id == null)
            return null;

        // Creation Query depuis ContributorQueryModel
        final IItemQuery query = IItemQuery.FACTORY.newInstance(ContributorQueryModel.ROOT);

        // Predicate avec un param�tre poru chercher depuis le nom avec un param�tre de type String
        final IPredicate predicate = ContributorQueryModel.ROOT.userId()._eq(query.newStringArg());

        // Utilisation du Predicate en filtre.
        final IItemQuery filtered = (IItemQuery) query.filter(predicate);

        // Appel Service de requ�tes depuis TeamRepository et non l'interface.
        final IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la req�te avec le filtre
        final IItemQueryPage page = qs.queryItems(filtered, new Object[] { id }, 1);

        // Retour de l'objet
        final List<?> handles = page.getItemHandles();
        if (!handles.isEmpty())
        {
            return (IContributor) repo.itemManager().fetchCompleteItem((IContributorHandle) handles.get(0), IItemManager.DEFAULT, progressMonitor);
        }

        return null;
    }

    /**
     * R�cup�ration de tous les lots RTC selon les param�tres fournis : <br>
     * - remiseAZero = indique si l'on prend tous les loes depuis la date de cr�ation fournie ou seulement depuis la derni�re mise � jour <br>
     * - date Cr�ation = date de la derni�re mise �jour du fichier <br>
     * Si la date de derni�re mise �jour n'est pas connue, une demande de remise � z�ero sera renvoy�e.<br>
     * Une erreur sera remont�e pour toute remise � z�ro sans date limite de cr�ation
     * 
     * @param remiseAZero
     * @param dateCreation
     * @return
     * @throws TeamRepositoryException
     */
    @SuppressWarnings("unchecked")
    public List<IItemHandle> recupLotsRTC(boolean remiseAZero, LocalDate dateCreation) throws TeamRepositoryException
    {
        // 1. Contr�le

        // On retourne une erreur si l'on souhaite une remise � z�ro mais que la date de cr�ation n'est pas renseign�e
        if (remiseAZero && dateCreation == null)
            throw new TechnicalException("m�thode control.rts.ControlRTC.recupLotsRTC : date Creation non renseign�e lors d'une remise � z�ro.", null);

        // 2. Requetage sur RTC pour r�cup�rer tous les Lots

        // Creation Query depuis ContributorQueryModel
        IItemQuery query = IItemQuery.FACTORY.newInstance(WorkItemQueryModel.ROOT);

        // Predicate avec un param�tre poru chercher depuis le nom avec un param�tre de type String
        IPredicate predicatFinal = WorkItemQueryModel.ROOT.workItemType()._eq("fr.ca.cat.wi.lotprojet")._or(WorkItemQueryModel.ROOT.workItemType()._eq("fr.ca.cat.wi.lotfonctionnement"));

        // Prise en compte de la date de cr�ation si elle est fournie
        if (dateCreation != null)
        {
            IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(DateConvert.convertToOldDate(dateCreation));
            predicatFinal = predicatFinal._and(predicatCreation);
        }

        // Dans le cas o� l'on ne fait pas une remise � z�ro du fichier, on ne prend que les lots qui ont �t� modifi�s ou cr��es depuis la derni�re mise � jour.
        if (!remiseAZero)
        {
            String dateMajFichierRTC = Statics.fichiersXML.getDateMaj().get(TypeFichier.LOTSRTC);
            if (dateMajFichierRTC != null && !dateMajFichierRTC.isEmpty())
            {
                // Date de la derni�re mise � jour
                LocalDate lastUpdate = DateConvert.FORMATTER.parse(dateMajFichierRTC, LocalDate::from);

                // Periode entre la derni�re mise � jour et aujourd'hui
                Period periode = Period.between(lastUpdate, today);
                Date datePredicat = DateConvert.convertToOldDate(today.minusDays(periode.getDays()));

                // Pr�dicat des lots qui ont �t� modifi�s depuis la derni�re mise � jour
                IPredicate dateModification = WorkItemQueryModel.ROOT.modified()._gt(datePredicat);

                // Pr�dicat des lots qui ont �t� cr��s depuis la derni�re mise � jour
                IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(datePredicat);

                // Ajout du contr�le Or et modification du pr�dicat final
                IPredicate predicatOu = dateModification._or(predicatCreation);
                predicatFinal = predicatFinal._and(predicatOu);
            }
        }

        // Utilisation du Predicate en filtre.
        IItemQuery filtered = (IItemQuery) query.filter(predicatFinal);

        // Appel Service de requ�tes depuis TeamRepository et non l'interface.
        IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la req�te avec le filtre
        int pageSize = PAGESIZE;
        IItemQueryPage page = qs.queryItems(filtered, new Object[] {}, pageSize);

        // Liste de tous les lots trouv�s.
        List<IItemHandle> retour = new ArrayList<>();
        retour.addAll(page.getItemHandles());
        int nextPosition = page.getNextStartPosition();
        UUID token = page.getToken();

        // On it�re sur toutes les pages de retour de la requ�tes pour remplir la liste.
        while (nextPosition != -1)
        {
            IItemQueryPage nextPage = (IItemQueryPage) qs.fetchPage(token, nextPosition, pageSize);
            nextPosition = nextPage.getNextStartPosition();
            retour.addAll(nextPage.getItemHandles());
        }

        return retour;
    }

    /**
     * Cr�ation d'un LotSuiviRTC regroupant les informations depuis RTC. Ne prend en compte que les IWorkItemHandle
     * 
     * @param item
     *            IItemHandle provenant de RTC.
     * @return {@link model.LotSuiviRTC}
     * @throws TeamRepositoryException
     */
    public LotSuiviRTC creerLotSuiviRTCDepuisHandle(IItemHandle item) throws TeamRepositoryException
    {
        if (!(item instanceof IWorkItemHandle))
            return null;

        IWorkItem workItem = recupererItemDepuisHandle(IWorkItem.class, (IWorkItemHandle) item);
        LotSuiviRTC retour = ModelFactory.getModel(LotSuiviRTC.class);
        retour.setLot(String.valueOf(workItem.getId()));
        retour.setLibelle(workItem.getHTMLSummary().getPlainText());
        retour.setCpiProjet(recupererItemDepuisHandle(IContributor.class, workItem.getOwner()).getName());
        retour.setProjetClarity(recupererValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.CLARITY.getValeur(), null), workItem));
        retour.setEdition(recupererValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.EDITIONSICIBLE.getValeur(), null), workItem));
        retour.setEtatLot(EtatLot.from(recupEtatElement(workItem)));
        retour.setProjetRTC(recupererItemDepuisHandle(IProjectArea.class, workItem.getProjectArea()).getName());
        return retour;
    }

    /**
     * Remonte une liste des tous les �tats pass�s par le lot ainsi que les dates de mise � jour.
     * 
     * @param lot
     * @throws TeamRepositoryException
     */
    @SuppressWarnings("unchecked")
    public Map<EtatLot, LocalDate> recupDatesEtatsLot(IItem lot) throws TeamRepositoryException
    {
        // Manager
        IItemManager itemManager = repo.itemManager();

        // R�cup�ration de l'historique des modifications faites sur le lot.
        List<IAuditableHandle> handles = itemManager.fetchAllStateHandles((IAuditableHandle) lot.getItemHandle(), progressMonitor);
        List<IWorkItem> workItems = itemManager.fetchCompleteStates(handles, null);

        // Tri de la liste par dates d�croissantes
        workItems.sort((o1, o2) -> o2.modified().compareTo(o1.modified()));

        // Transfert des donn�es dans une map, pour avoir la premi�re date de mise � jour de chaque �tat.
        Map<EtatLot, LocalDate> retour = new EnumMap<>(EtatLot.class);
        for (IWorkItem iWorkItem : workItems)
        {
            IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(iWorkItem, progressMonitor);
            String etat = workflowInfo.getStateName(iWorkItem.getState2());
            retour.put(EtatLot.from(etat), DateConvert.localDate(iWorkItem.modified()));
        }

        return retour;
    }

    /**
     * Teste si l'anomalie RTC est close.
     * 
     * @param numeroAno
     *            Num�ro de l'anomalie � tester
     * @return
     */
    public boolean testSiAnomalieClose(int numeroAno)
    {
        if (numeroAno == 0)
            return true;

        try
        {
            String etat = recupEtatElement(recupWorkItemDepuisId(numeroAno)).trim();
            return "Close".equals(etat) || "Abandonn�e".equals(etat);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
            return false;
        }
    }

    /**
     * R�cup�ration de tous les projets RTC
     * 
     * @throws TeamRepositoryException
     */
    public void recupererTousLesProjets() throws TeamRepositoryException
    {
        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    ITeamRepository getRepo()
    {
        return repo;
    }

    IWorkItemClient getClient()
    {
        return workItemClient;
    }
}
