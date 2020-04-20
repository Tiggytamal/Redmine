package control.rtc;

import static utilities.Statics.EMPTY;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.ibm.team.workitem.common.model.IComment;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.workflow.IWorkflowAction;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import control.task.AbstractTask;
import dao.DaoComposantSonar;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DateMaj;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.EtatAnoRTC;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeCom;
import model.enums.TypeDonnee;
import model.enums.TypeEnumRTC;
import utilities.AbstractToStringImpl;
import utilities.DateConvert;
import utilities.Statics;

/**
 * Classe de controle des acc�s RTC sous forme se singleton
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class ControlRTC extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    /** logger g�n�ral */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    /** Taille de la pagination */
    private static final int PAGESIZE = 512;

    /** Instance du controleur */
    public static final ControlRTC INSTANCE = new ControlRTC();

    private ITeamRepository repo;
    private IProgressMonitor monitor;
    private Map<String, IProjectArea> pareas;
    private IWorkItemClient workItemClient;
    private IAuditableClient auditableClient;
    private IAuditableCommon auditableCommon;

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
        monitor = new NullProgressMonitor();
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
                repo.login(monitor);
            }

            // R�cup�rationd e tous les projets si la iste est vide. Effectu�e normalemetn une seule fois par instance.
            if (pareas.isEmpty())
                recupTousLesProjets();
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
        IWorkItem workItem = workItemClient.findWorkItemById(lot, IWorkItem.FULL_PROFILE, monitor);
        if (workItem == null)
        {
            LOGGER.warn("R�cup�ration projetRTC - Lot introuvable : " + lot);
            return EMPTY;
        }
        IProjectArea area = (IProjectArea) repo.itemManager().fetchCompleteItem(workItem.getProjectArea(), IItemManager.DEFAULT, monitor);
        return area.getName();
    }

    /**
     * 
     * @param classRetour
     * @param handle
     * @return
     * @throws TeamRepositoryException
     */
    public <R extends T, T extends IAuditableHandle> R recupItemDepuisHandle(Class<R> classRetour, T handle) throws TeamRepositoryException
    {
        return classRetour.cast(repo.itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, monitor));
    }

    /**
     * 
     * @param classRetour
     * @param handle
     * @param profil
     * @return
     * @throws TeamRepositoryException
     */
    public <R extends T, T extends IAuditableHandle> R recupEltDepuisHandle(Class<R> classRetour, T handle, ItemProfile<? extends T> profil) throws TeamRepositoryException
    {
        return classRetour.cast(auditableClient.fetchCurrentAuditable(handle, profil, monitor));
    }

    /**
     * Permet de retourner la valeur {@code Identifier} d'un attribut depuis la valeur {@code String}
     * 
     * @param name
     *            valeur de l'attribut sous firme d'une cha�ne de caract�re
     * @param ia
     *            attribut dont on veut la valeur
     * @return
     * @throws TeamRepositoryException
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
     * Calcul de l'�tat d'un objet RTC
     * 
     * @param item
     * @return
     * @throws TeamRepositoryException
     */
    public String recupEtatElement(IWorkItem item) throws TeamRepositoryException
    {
        if (item == null)
            return EMPTY;
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(item, monitor);
        return workflowInfo.getStateName(item.getState2()).trim();
    }

    /**
     * 
     * @param id
     * @return
     * @throws TeamRepositoryException
     */
    public IWorkItem recupWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        return workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, monitor);
    }

    /**
     * 
     * @param id
     * @throws TeamRepositoryException
     */
    public IStatus supprimerWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        IWorkItem workItem = recupWorkItemDepuisId(id);
        workItemClient.getWorkItemWorkingCopyManager().connect(workItem, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(workItem);
        IStatus status = workingCopy.delete(monitor);
        workItemClient.getWorkItemWorkingCopyManager().disconnect(workItem);
        return status;
    }

    /**
     * Retourne la valeur d'un attribut d'un WorkItem RTC sous forme d'une chaine de caract�res.
     * 
     * @param attrb
     * @param item
     * @return
     * @throws TeamRepositoryException
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
        }
        else if (objet instanceof String)
            return ((String) objet).trim();

        return null;
    }

    /**
     * Retourne le nom de la personne connect� � RTC.
     * 
     * @return
     */
    public String recupNomContributorConnecte()
    {
        return repo.loggedInContributor().getName().trim();
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
            return recupItemDepuisHandle(IContributor.class, (IContributorHandle) handles.get(0));
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
        if (handles.isEmpty())
            return null;

        return (IContributor) repo.itemManager().fetchCompleteItem((IContributorHandle) handles.get(0), IItemManager.DEFAULT, monitor);
    }

    /**
     * R�cup�ration de tous les lots RTC selon les param�tres fournis : <br>
     * - remiseAZero = indique si l'on prend tous les les depuis la date de cr�ation fournie ou seulement depuis la derni�re mise � jour <br>
     * - date Cr�ation = date de la derni�re mise �jour du fichier <br>
     * Si la date de derni�re mise � jour n'est pas connue, une demande de remise � z�ro sera renvoy�e.<br>
     * Une erreur sera remont�e pour toute remise � z�ro sans date limite de cr�ation
     * 
     * @param remiseAZero
     * @param dateCreation
     * @return
     * @throws TeamRepositoryException
     */
    @SuppressWarnings("unchecked")
    public List<LotRTC> recupLotsRTC(LocalDate dateCreation, AbstractTask task) throws TeamRepositoryException
    {
        // Requetage sur RTC pour r�cup�rer tous les Lots

        // Creation Query depuis ContributorQueryModel
        IItemQuery query = IItemQuery.FACTORY.newInstance(WorkItemQueryModel.ROOT);

        // Predicate avec un param�tre poru chercher depuis le nom avec un param�tre de type String
        IPredicate predicatFinal = WorkItemQueryModel.ROOT.workItemType()._eq("fr.ca.cat.wi.lotprojet")._or(WorkItemQueryModel.ROOT.workItemType()._eq("fr.ca.cat.wi.lotfonctionnement"));

        // Prise en compte de la date de cr�ation si elle est fournie
        if (dateCreation != null)
        {
            IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(DateConvert.convertToOldDate(dateCreation, ZoneId.of("GMT")));
            predicatFinal = predicatFinal._and(predicatCreation);
        }

        // Sinon, on ne prend que les lots qui ont �t� modifi�es ou cr��es depuis la derni�re mise � jour.
        else
        {
            // R�cup�ration de la date de mise � jour depuis la base de donn�es
            LocalDateTime lastUpdate = DaoFactory.getDao(DateMaj.class).recupEltParIndex(TypeDonnee.LOTSRTC.toString()).getTimeStamp();
            if (lastUpdate != null)
            {
                // Derni�re date de mise � jour. On utilise la zone GMT car en interne RTC prend les dates dans ce format.
                Date datePredicat = DateConvert.convertToOldDate(lastUpdate, ZoneId.of("GMT"));

                // Pr�dicat des lots qui ont �t� modifi�s depuis la derni�re mise � jour
                IPredicate dateModification = WorkItemQueryModel.ROOT.modified()._gt(datePredicat);

                // Pr�dicat des lots qui ont �t� cr��s depuis la derni�re mise � jour
                IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(datePredicat);

                // Ajout du contr�le Or et modification du pr�dicat final
                IPredicate predicatOu = dateModification._or(predicatCreation);
                predicatFinal = predicatFinal._and(predicatOu);
            }
        }

        // Gestion des lots des composants. On ne prend que les lots qui ont un composantSonar dans la base.

        // R�cup�ration des num�ros de lots depuis la base de donn�es
        DaoComposantSonar dao = DaoFactory.getDao(ComposantSonar.class);
        List<String> listeLots = dao.recupLotsAvecComposants();

        // Cr�ation de la liste de num�ros de lots
        Number[] lots = new Number[listeLots.size()];
        int i = 0;
        for (String lot : listeLots)
        {
            lots[i] = Integer.valueOf(lot);
            i++;
        }

        // cr�ation du pr�dicat
        IPredicate predicatLots = WorkItemQueryModel.ROOT.id()._in(lots);
        predicatFinal = predicatFinal._and(predicatLots);

        // Utilisation du Predicate en filtre.
        IItemQuery filtered = (IItemQuery) query.filter(predicatFinal);

        // Appel Service de requ�tes depuis TeamRepository et non l'interface.
        IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la req�te avec le filtre
        int pageSize = PAGESIZE;
        IItemQueryPage page = qs.queryItems(filtered, new Object[] {}, pageSize);

        // Liste de tous les lots trouv�s. ON peut utiliser des IWorkItemHandles directement car la requ�te ne remonte que des WorkItems
        List<IWorkItemHandle> handles = new ArrayList<>();
        handles.addAll(page.getItemHandles());
        int nextPosition = page.getNextStartPosition();
        UUID token = page.getToken();

        // On it�re sur toutes les pages de retour de la requ�tes pour remplir la liste.
        while (nextPosition != -1)
        {
            IItemQueryPage nextPage = (IItemQueryPage) qs.fetchPage(token, nextPosition, pageSize);
            nextPosition = nextPage.getNextStartPosition();
            handles.addAll(nextPage.getItemHandles());
        }

        task.setBaseMessage("Mise � jour des lots RTC incomplets...");
        task.updateMessage("");
        List<LotRTC> retour = miseAJourLotincomplets();

        // transformation des IWorkItemHandle en LotRTC

        // Variables
        i = 0;
        int size = handles.size();
        task.setBaseMessage("R�cup�ration lots RTC...");
        String lot = "Lot ";
        String sur = " sur ";
        long debut = System.currentTimeMillis();

        // valorisation de la liste de retour
        for (IWorkItemHandle handle : handles)
        {
            if (task.isCancelled())
                break;

            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
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
     * Pr�pare la liste des lots RTC avec ceux qui ont des anomalies de contenu.
     * 
     * @return
     * @throws TeamRepositoryException
     */
    private List<LotRTC> miseAJourLotincomplets() throws TeamRepositoryException
    {
        // R�cup�ration des lots avec informations manquantes
        List<LotRTC> retour = DaoFactory.getDao(LotRTC.class).readAll();
        for (Iterator<LotRTC> iter = retour.iterator(); iter.hasNext();)
        {
            LotRTC lot = iter.next();

            // On enl�ve tous les lots qui ont une �dition, un projet clarity et un libell� non vide, ou qui ne sont pas accessibles par RTC
            if ((!lot.getLibelle().isEmpty() && lot.getEdition() != null && lot.getProjetClarity() != null) || lot.isRtcHS())
                iter.remove();
        }

        // Mise � jour des lots depuis RTC
        IWorkItem wi;
        for (Iterator<LotRTC> iter = retour.iterator(); iter.hasNext();)
        {
            LotRTC lotRTC = iter.next();
            try
            {
                // R�cup�ration du workitem depuis le num�ro du lot
                wi = ControlRTC.INSTANCE.recupWorkItemDepuisId(Integer.parseInt(lotRTC.getLot()));
            }
            catch (PermissionDeniedException e)
            {
                // Si on a pas l'acc�s au lot. on met� jour le top HS et la base de donn�es
                iter.remove();
                lotRTC.setRtcHS(true);
                DaoFactory.getDao(LotRTC.class).persist(lotRTC);
                continue;
            }

            // Mise � jour du lot avec les nouvelles informations
            LotRTC update = ControlRTC.INSTANCE.creerLotSuiviRTCDepuisItem(wi);
            lotRTC.update(update);
        }

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
        List<IAuditableHandle> handles = itemManager.fetchAllStateHandles((IAuditableHandle) lot.getItemHandle(), monitor);
        List<IWorkItem> workItems = itemManager.fetchCompleteStates(handles, null);

        // Tri de la liste par dates d�croissantes
        workItems.sort((o1, o2) -> o2.modified().compareTo(o1.modified()));

        // Transfert des donn�es dans une map, pour avoir la premi�re date de mise � jour de chaque �tat.
        Map<EtatLot, LocalDate> retour = new EnumMap<>(EtatLot.class);
        for (IWorkItem iWorkItem : workItems)
        {
            IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(iWorkItem, monitor);
            String etat = workflowInfo.getStateName(iWorkItem.getState2());
            retour.put(EtatLot.from(etat), DateConvert.localDate(iWorkItem.modified()));
        }

        return retour;
    }

    /**
     * Remonte une liste des tous les �tats pass�s par le lot ainsi que les dates de mise � jour.
     * 
     * @param lot
     * @throws TeamRepositoryException
     */
    @SuppressWarnings("unchecked")
    public Map<EtatAnoRTC, LocalDate> recupDatesEtatsAnoRTC(IItem lot) throws TeamRepositoryException
    {
        // Manager
        IItemManager itemManager = repo.itemManager();

        // R�cup�ration de l'historique des modifications faites sur le lot.
        List<IAuditableHandle> handles = itemManager.fetchAllStateHandles((IAuditableHandle) lot.getItemHandle(), monitor);
        List<IWorkItem> workItems = itemManager.fetchCompleteStates(handles, null);

        // Tri de la liste par dates d�croissantes
        workItems.sort((o1, o2) -> o2.modified().compareTo(o1.modified()));

        // Transfert des donn�es dans une map, pour avoir la premi�re date de mise � jour de chaque �tat.
        Map<EtatAnoRTC, LocalDate> retour = new EnumMap<>(EtatAnoRTC.class);
        for (IWorkItem iWorkItem : workItems)
        {
            IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(iWorkItem, monitor);
            String etat = workflowInfo.getStateName(iWorkItem.getState2()).trim();
            retour.put(EtatAnoRTC.from(etat), DateConvert.localDate(iWorkItem.modified()));
        }

        return retour;
    }

    /**
     * R�cup�ration de tous les projets RTC
     * 
     * @throws TeamRepositoryException
     */
    public void recupTousLesProjets() throws TeamRepositoryException
    {
        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
    }

    /**
     * Listage de tous les type de workItem d'un projet.
     * 
     * @return
     */
    public List<IWorkItemType> recupListeTypeWorkItem(String projet) throws TeamRepositoryException
    {
        List<IWorkItemType> liste = workItemClient.findWorkItemTypes(pareas.get(projet), monitor);
        for (IWorkItemType type : liste)
        {
            LOGGER.info("nom : " + type.getDisplayName());
            LOGGER.info("identifiant : " + type.getIdentifier() + Statics.NL);
        }
        return workItemClient.findWorkItemTypes(pareas.get(projet), monitor);
    }

    /**
     * Listage de tous les type de workItem d'un projet.
     * 
     * @return
     * @throws TeamRepositoryException
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
     * Cr�ation d'une anomalie dans RTC
     * 
     * @param dq
     *            anomalie servant d'origine au Defect
     * @param typeDefaut
     * @return
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
            for (ICategory iCategory : categories)
            {
                if ("Projet".equals(iCategory.getName()))
                {
                    cat = iCategory;
                    break;
                }
            }

            // S'il n'y a pas de cat�gorie trouv�e, on cherche une cat�gorie avec Anomalie ou "Non affect�"
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
            
            // Si c'est toujours null, on prend la premi�re cat�gorie trouv�e.
            if (cat == null)
               cat = categories.get(0);

            // Cr�ation
            WorkItemInitialization init = new WorkItemInitialization(itemType, cat, projet, dq);
            IWorkItemHandle handle = init.run(itemType, monitor);
            workItem = auditableClient.fetchCurrentAuditable(handle, WorkItem.FULL_PROFILE, monitor);
        }
        catch (TeamRepositoryException e)
        {
            LOGGER.error("Erreur traitement RTC cr�ation de Defect. Lot : " + dq.getLotRTC());
            LOGPLANTAGE.error(e);
        }

        if (workItem == null)
            return 0;

        LOGGER.info("Creation anomalie RTC num�ro : " + workItem.getId() + " pour " + dq.getLotRTC().getLot());
        return workItem.getId();

    }

    /**
     * Contr�le l'�tat d'une anoRTC.
     * 
     * @param dq
     */
    public void controleAnoRTC(DefautQualite dq)
    {
        if (dq.getNumeroAnoRTC() == 0)
            return;

        IWorkItem anoRTC;
        try
        {
            anoRTC = recupWorkItemDepuisId(dq.getNumeroAnoRTC());
            dq.setEtatRTC(recupEtatElement(anoRTC));

            // Correction si l'on a pas d�j� la date de cr�ation du Defect
            if (dq.getDateCreation() == null)
                dq.setDateCreation(DateConvert.convert(LocalDate.class, anoRTC.getCreationDate()));

            // Mise � jour de la date de r�solution
            if (anoRTC.getResolutionDate() != null)
                dq.setDateReso(DateConvert.convert(LocalDate.class, anoRTC.getResolutionDate()));
            else
                dq.setDateReso(null);
        }
        catch (TeamRepositoryException e)
        {
            LOGGER.error("Erreur r�cup�ration information Defect. Lot : " + dq.getLotRTC());
            LOGPLANTAGE.error(e);
        }
    }

    /**
     * Ajoute un commentaire � une anoRTC s'il y a un d�fault appli.
     * 
     * @param da
     */
    public boolean ajoutCommAppliAnoRTC(DefautAppli da, int numeroAno)
    {
        try
        {
            String texte = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEAPPLI).replace("xxxxx", da.getCompo().getNom().replace("Composant ", Statics.EMPTY));
            if (!da.getAppliCorrigee().isEmpty())
                texte += Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTENEWAPPLI).replace("-code-", da.getAppliCorrigee());
            ajoutCommentaireAnoRTC(numeroAno, texte, TypeCom.APPLICATION);
            return true;
        }
        catch (TeamRepositoryException e)
        {
            LOGGER.error("control.rtc.controlRTC.ajoutAppliAnoRTC - Erreur modification Defect. Lot : " + da.getCompo().getLotRTC().getLot());
            LOGPLANTAGE.error(e);
            return false;
        }
    }

    /**
     * Teste si l'anomalie RTC est close.
     * 
     * @param numeroAno
     *            Num�ro de l'anomalie � tester
     * @return
     */
    public boolean testSiAnoRTCClose(int numeroAno)
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
     * Ajoute un commentaire de relance sur une anomalie RTC
     * 
     * @param id
     * @throws TeamRepositoryException
     */
    public void relancerAno(int id) throws TeamRepositoryException
    {
        ajoutCommentaireAnoRTC(id, Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTERELANCE), TypeCom.RELANCE);
    }

    public boolean reouvrirAnoRTC(int id) throws TeamRepositoryException
    {
        // r�cup�ration du workItem puis du workflow depuis le num�ro de l'anomalie
        IWorkItem wi = recupWorkItemDepuisId(id);
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(wi, monitor);
        if (workflowInfo == null)
        {
            LOGPLANTAGE.error("control.rtc.ControlRTC.reouvrirAnoRTC - impossible de trouver le workflow de l'anomalie : " + id);
            return false;
        }

        // Cr�ation du copyManager pour pouvoir modifier les donn�es de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // R�cup�ratoinde l'�tat et r�ouverture de l'anomalie
        EtatAnoRTC etatLot = EtatAnoRTC.from(workflowInfo.getStateName(wi.getState2()).trim());
        if (etatLot == EtatAnoRTC.CLOSE || etatLot == EtatAnoRTC.ABANDONNEE)
            return changerEtatAno(workingCopy, workflowInfo, wi, "R�ouvrir");
        return etatLot != EtatAnoRTC.CLOSE && etatLot != EtatAnoRTC.ABANDONNEE;
    }

    /**
     * Cloture d'une anomalie RTC peut importe l'�tat du workflow
     * 
     * @param id
     * @return
     * @throws TeamRepositoryException
     */
    public boolean fermerAnoRTC(int id) throws TeamRepositoryException
    {
        // r�cup�ration du workItem puis du workflow depuis le num�ro de l'anomalie
        IWorkItem wi = recupWorkItemDepuisId(id);
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(wi, monitor);
        if (workflowInfo == null)
        {
            LOGPLANTAGE.error("control.rtc.ControlRTC.fermerAnoRTC - impossible de trouver le workflow de l'anomalie : " + id);
            return false;
        }

        // Cr�ation du copyManager pour pouvoir modifier les donn�es de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // Ajout des donn�es necessaires pour cl�turer si elles ne sont pas pr�sentes

        // Date de livraison / homolation
        IAttribute date = workItemClient.findAttribute(wi.getProjectArea(), TypeEnumRTC.DATELIVHOMO.getValeur(), monitor);
        if (wi.getValue(date) == null)
        {
            workingCopy.getWorkItem().setValue(date, new Timestamp(System.currentTimeMillis()));
            workingCopy.save(monitor);
        }

        // Entit� responsable correction
        IAttribute resp = workItemClient.findAttribute(wi.getProjectArea(), TypeEnumRTC.ENTITERESPCORRECTION.getValeur(), monitor);
        if (wi.getValue(resp).equals(recupLiteralDepuisString("-", resp)))
        {
            workingCopy.getWorkItem().setValue(resp, recupLiteralDepuisString("MOE", resp));
            workingCopy.save(monitor);
        }
        
        //Nature du probl�me
        IAttribute nature = workItemClient.findAttribute(wi.getProjectArea(), TypeEnumRTC.NATURE.getValeur(), monitor);
        if (wi.getValue(nature).equals(recupLiteralDepuisString("-", nature)))
        {
            workingCopy.getWorkItem().setValue(nature, recupLiteralDepuisString("Qualit�", nature));
            workingCopy.save(monitor);
        }

        // Estimation
        if (wi.getDuration() == -1)
        {
            workingCopy.getWorkItem().setDuration(28800000);
            workingCopy.save(monitor);
        }

        // Boucle pour passer arriver jusqu'� l'anomalie close.
        // Retourne vrai si l'anomalie a �t� ferm�e ou faux s'il y a eu un plantage aux changements d'�tats.
        boolean ok = true;
        while (ok)
        {
            EtatAnoRTC etatLot = EtatAnoRTC.from(workflowInfo.getStateName(wi.getState2()).trim());

            // Attention trois cases utilisent le m�me code.
            switch (etatLot)
            {
                case CLOSE:
                case REJETEE:
                case ABANDONNEE:
                    workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
                    return true;

                case ENCOURS:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.ENCOURS.getAction());
                    break;

                case NOUVELLE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.NOUVELLE.getAction());
                    break;

                case OUVERTE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.OUVERTE.getAction());
                    break;

                case REOUVERTE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.REOUVERTE.getAction());
                    break;

                case VMOE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.VMOE.getAction());
                    break;

                case VMOA:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.VMOA.getAction());
                    break;

                case RESOLUE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.RESOLUE.getAction());
                    break;

                case VERIFIEE:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.VERIFIEE.getAction());
                    break;

                case EDITEUR:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.EDITEUR.getAction());
                    break;

                case DENOUEMENT:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.DENOUEMENT.getAction());
                    break;

                case ATTEDITEUR:
                    ok = changerEtatAno(workingCopy, workflowInfo, wi, EtatAnoRTC.ATTEDITEUR.getAction());
                    break;
            }
        }

        // Cas s'il y a eu un plantage dans la mise � jour de l'anomalie
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        return false;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * * Cr�ation d'un LotSuiviRTC regroupant les informations depuis RTC. Ne prend en compte que les IWorkItem
     * 
     * @param workItem
     * @return
     * @throws TeamRepositoryException
     */
    public LotRTC creerLotSuiviRTCDepuisItem(IWorkItem workItem) throws TeamRepositoryException
    {
        LotRTC retour = ModelFactory.build(LotRTC.class);
        retour.setLot(String.valueOf(workItem.getId()));
        retour.setLibelle(workItem.getHTMLSummary().getPlainText());
        retour.setCpiProjet(recupItemDepuisHandle(IContributor.class, workItem.getOwner()).getName());
        retour.setProjetClarityString(recupValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.CLARITY.getValeur(), null), workItem).trim());
        if (retour.getProjetClarityString().isEmpty())
            retour.setProjetClarityString(recupValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.CODECLARITY.getValeur(), null), workItem).trim());
        retour.setEditionString(recupValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.EDITIONSICIBLE.getValeur(), null), workItem).trim());
        if (retour.getEditionString().isEmpty())
            retour.setEditionString(recupValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.EDITIONSI.getValeur(), null), workItem).trim());
        EtatLot etatLot = EtatLot.from(recupEtatElement(workItem));
        retour.setEtatLot(etatLot);
        retour.setProjetRTC(recupItemDepuisHandle(IProjectArea.class, workItem.getProjectArea()).getName());
        retour.setDateMajEtat(recupDatesEtatsLot(workItem).get(etatLot));
        return retour;
    }

    /**
     * R�cup�re l'identifiant d'une action � partir de son nom
     * 
     * @param workflowInfo
     * @param workItem
     * @param actionName
     * @return
     */
    private String getAction(IWorkflowInfo workflowInfo, IWorkItem workItem, String actionName)
    {
        Identifier<IWorkflowAction>[] actionIds = workflowInfo.getActionIds(workItem.getState2());

        for (Identifier<IWorkflowAction> id : actionIds)
        {
            if (workflowInfo.getActionName(id).equals(actionName))
                return id.getStringIdentifier();
        }
        return "";
    }

    /**
     * Modifie l'�tat d'une anomalie suivant l'action donn�e
     * 
     * @param workflowInfo
     * @param workItem
     * @param actionName
     * @throws TeamRepositoryException
     */
    private boolean changerEtatAno(WorkItemWorkingCopy workingCopy, IWorkflowInfo workflowInfo, IWorkItem workItem, String actionName)
    {
        workingCopy.setWorkflowAction(getAction(workflowInfo, workItem, actionName));
        IDetailedStatus status = workingCopy.save(monitor);
        boolean ok = status.isOK();
        if (!ok)
            LOGPLANTAGE.error(status.getException());
        return ok;
    }

    /**
     * Permet d'ajouter un commentaire � une workItem RTC
     * 
     * @param id
     *            id du WorkItem
     * @param texteCommentaire
     *            texte du commentaire
     * @param type
     *            Type du commenaire
     * @return vrai si un commentaire � �t� ajout�, sinon faux.
     * @throws TeamRepositoryException
     */
    private boolean ajoutCommentaireAnoRTC(int id, String texteCommentaire, TypeCom type) throws TeamRepositoryException
    {
        // Cr�ation workingCopy
        IWorkItem wi = recupWorkItemDepuisId(id);
        workItemClient.getWorkItemWorkingCopyManager().connect(wi, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = workItemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wi);

        // R�cup�ration de la liste des commentaires, et contr�le pour ne pas ajouter deux fois le m�me commentaire de d�faut de code application
        IComment[] commentaires = workingCopy.getWorkItem().getComments().getContents();
        XMLString xmlTexte = XMLString.createFromPlainText(texteCommentaire);
        for (IComment comm : commentaires)
        {
            if (type == TypeCom.APPLICATION && comm.getHTMLContent().equals(xmlTexte))
                return false;
        }

        // Ajout du commentaire, et sauvegarde
        workingCopy.getWorkItem().getComments().append(workingCopy.getWorkItem().getComments().createComment(repo.loggedInContributor(), xmlTexte));
        workingCopy.save(monitor);
        workItemClient.getWorkItemWorkingCopyManager().disconnect(wi);
        return true;
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

    /*---------- CLASSES PRIVEES **********/
}
