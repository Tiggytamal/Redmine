package control.rtc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.IAuditableHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.IWorkItemWorkingCopyManager;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import model.enums.TypeEnumRTC;
import model.enums.TypeParam;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de controle des accès RTC sous form dénumération pour forcer le singleton
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlRTC
{
    /*---------- ATTRIBUTS ----------*/

    public static final ControlRTC INSTANCE = new ControlRTC();

    private ITeamRepository repo;
    private IProgressMonitor progressMonitor;
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
        // Controle pour éviter la création par reflexion d'une seconde instance
        if (INSTANCE != null)
            throw new AssertException();

        // Récupération du référentiel de données depuis l'url
        TeamPlatform.startup();
        repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(Statics.proprietesXML.getMapParams().get(TypeParam.URLRTC));
        workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
        auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
        progressMonitor = new NullProgressMonitor();
        pareas = new HashMap<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Connection au repository RTC
     * 
     * @param user
     * @param password
     * @param repoUrl
     * @return
     * @throws TeamRepositoryException
     */
    public boolean connexion()
    {
        repo.registerLoginHandler((ITeamRepository repository) -> new UsernameAndPasswordLoginInfo(Statics.info.getPseudo(), Statics.info.getMotDePasse()));
        try
        {
            // Un erreur de login renvoie une erreur fonctionnelle
            repo.login(progressMonitor);
            if (pareas.isEmpty())
                recupererTousLesProjets();
        } catch (TeamRepositoryException e)
        {
            return false;
        }
        return true;
    }

    public void shutdown()
    {
        TeamPlatform.shutdown();
    }

    /**
     * Récupère le nom du projet RTC depuis Jazz dont provient le lot en paramètre
     * 
     * @param lot
     * @return
     * @throws TeamRepositoryException
     */
    public String recupProjetRTCFromLot(int lot) throws TeamRepositoryException
    {
        IWorkItem workItem = workItemClient.findWorkItemById(lot, IWorkItem.FULL_PROFILE, progressMonitor);
        IProjectArea area = (IProjectArea) repo.itemManager().fetchCompleteItem(workItem.getProjectArea(), IItemManager.DEFAULT, progressMonitor);
        return area.getName();
    }
    
    public <R extends T, T extends IAuditableHandle> R recupererItemDepuisHandle(Class<R> classRetour, T handle) throws TeamRepositoryException
    {
        return classRetour.cast(repo.itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, progressMonitor));
    }
    
    public <R extends T, T extends IAuditableHandle> R recupererEltDepuisHandle(Class<R> classRetour, T handle, ItemProfile<? extends T> profil) throws TeamRepositoryException
    {
        return classRetour.cast(auditableClient.fetchCurrentAuditable(handle, profil, progressMonitor));
    }
    
    /**
     * Calcul de l'état d'un objet RTC
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
        IWorkItem item = workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, progressMonitor);
        if (item == null)
            System.out.println(id);
        return workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, progressMonitor);
    }

    /**
     * Récupération de tous les projets RTC
     * 
     * @throws TeamRepositoryException
     */
    private void recupererTousLesProjets() throws TeamRepositoryException
    {
        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
    }

    /**
     * @param thisItem
     * @param workItemType
     * @param someAttribute
     * @param someLiteral
     * @param parsedConfig
     * @param monitor
     * @return
     * @throws TeamRepositoryException
     */
    public int creerDefect(String projetString)
    {
        IWorkItem workItem = null;
        try
        {
            IProjectArea projet = pareas.get(projetString);

            // Type de l'objet
            IWorkItemType itempType = workItemClient.findWorkItemType(projet, "Defect", progressMonitor);

            // Manager de copie
            IWorkItemWorkingCopyManager manager = workItemClient.getWorkItemWorkingCopyManager();

            // Connexion au manager et récupération du WorkItem
            IWorkItemHandle itemhandle = manager.connectNew(itempType, progressMonitor);
            workItem = auditableClient.fetchCurrentAuditable(itemhandle, IWorkItem.FULL_PROFILE, progressMonitor);

            // Ajout des attributs
            
            IAttribute someAttribute = workItemClient.findAttribute(projet, TypeEnumRTC.ENVIRONNEMENT.toString(), progressMonitor);
            workItem.setValue(someAttribute, "MOE");
            

            // Sauvegarde Workitem
            manager.save(new WorkItemWorkingCopy[] { manager.getWorkingCopy(itemhandle) }, progressMonitor);
        } catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur traitement RTC création de Defect", e);
        }
        return workItem.getId();
    }

    public Object recupvalueAttribut(IAttribute attrb, IWorkItem item) throws TeamRepositoryException
    {
        Object objet = attrb.getValue(auditableCommon, item, progressMonitor);
        if (objet != null && objet instanceof Identifier)
        {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            Identifier<? extends ILiteral> literalID = (Identifier) objet;
            List<? extends ILiteral> literals = workItemClient.resolveEnumeration(attrb, progressMonitor).getEnumerationLiterals();

            for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
            {
                ILiteral iLiteral = iterator.next();

                if (iLiteral.getIdentifier2().equals(literalID))
                    System.out.println(attrb.getDisplayName() + " - valeur = " + iLiteral.getName() + " - identifiant : " + literalID);
            }
        }
        return null;
    }
    
    public IAttribute findAttribute(String projet, String identifier) throws TeamRepositoryException
    {
        return workItemClient.findAttribute(pareas.get(projet), identifier, progressMonitor);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}