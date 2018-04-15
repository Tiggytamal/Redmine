package control.rtc;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.ibm.team.repository.common.IItem;
import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.IWorkItemWorkingCopyManager;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import model.Anomalie;
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
        }
        catch (TeamRepositoryException e)
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

    /**
     * Calcul de l'état d'un objet RTC
     * 
     * @param item
     * @return
     * @throws TeamRepositoryException
     */
    public String recupEtatElement(IWorkItem item) throws TeamRepositoryException
    {
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(item, progressMonitor);
        return workflowInfo.getStateName(item.getState2());
    }

    public IWorkItem recupWorkItemDepuisId(int id) throws TeamRepositoryException
    {
        return workItemClient.findWorkItemById(id, IWorkItem.FULL_PROFILE, progressMonitor);
    }

    /**
     * Récupération ed tous les projets RTC
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

    // /**
    // * Récupération d'un WorkItem depuis son identifiant et le projet RTC dont il est issu.
    // *
    // * @param projets
    // * @param elementId
    // * @return
    // * @throws TeamRepositoryException
    // */
    // public IWorkItem fetchElementParId(String projet, String elementId) throws TeamRepositoryException
    // {
    // List<String> liste = new ArrayList<>();
    // liste.add(projet);
    // return fetchElementParId(liste, elementId);
    // }
    //
    // /**
    // * Récupération d'un WorkItem depuis son identifiant et une liste de projets RTC dont il peut être issu.
    // *
    // * @param projets
    // * @param elementId
    // * @return
    // * @throws TeamRepositoryException
    // */
    // public IWorkItem fetchElementParId(List<String> projets, String elementId) throws TeamRepositoryException
    // {
    // for (String projetString : projets)
    // {
    // // Récupération du projet
    // IProjectArea projet = pareas.get(projetString);
    // IAuditableCommon auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
    //
    // // Attribut égal au projet
    // IQueryableAttribute attributProjet = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(projet, IWorkItem.PROJECT_AREA_PROPERTY, auditableCommon, progressMonitor);
    // Expression inProjectArea = new AttributeExpression(attributProjet, AttributeOperation.EQUALS, projet);
    //
    // // Attribut égal à l'anomalie
    // IQueryableAttribute attributId = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(projet, IWorkItem.ID_PROPERTY, auditableCommon, progressMonitor);
    // Expression isId = new AttributeExpression(attributId, AttributeOperation.EQUALS, elementId);
    //
    // // Terme poru cumuler les deux attributs
    // Term typeinProjectArea = new Term(Term.Operator.AND);
    // typeinProjectArea.add(inProjectArea);
    // typeinProjectArea.add(isId);
    //
    // // Création de la requête et appele de celle-ci pour récupérer un résultat
    // IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
    // IQueryResult<IResolvedResult<IWorkItem>> result = queryClient.getResolvedExpressionResults(projet, typeinProjectArea, IWorkItem.FULL_PROFILE);
    //
    // // Si la requête a un seul résultat on affecte l'objet de retour.
    // int nbreResultats = result.getResultSize(null).getTotal();
    // if (nbreResultats == 1)
    // return result.next(null).getItem();
    // else if (nbreResultats > 1)
    // throw new TechnicalException("Plusieurs éléments RTC remontés avec le même identifiant : projet = " + projet + " - anomalie = " + elementId, null);
    // }
    //
    // // Retourne de l'exception si aucun résultat n'a été trouvé
    // Statics.lognonlistee.warn("Aucun élément RTC remonté avec cet identifiant : anomalie = " + elementId);
    // return null;
    // }

    // /**
    // * Retourne la liste des projets RTC correspondants au projet clarity de l'anomalie
    // *
    // * @param clarity
    // * @return
    // */
    // public List<String> recupProjetRTCDepuisClarity(String clarity)
    // {
    // List<String> retour = new ArrayList<>();
    //
    // // Code clarity sans le numéro de lot.
    // String miniCLarity = clarity.length() > 6 ? clarity.substring(0, 6) : clarity;
    //
    // // Itération pour trouver les projets RTC
    // for (String projet : pareas.keySet())
    // {
    // if (projet.contains(miniCLarity))
    // retour.add(projet);
    // }
    // return retour;
    // }

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
            IAttribute someAttribute = workItemClient.findAttribute(projet, "some_attribute_ID", progressMonitor);
            workItem.setValue(someAttribute, "0");

            // Sauvegarde Workitem
            manager.save(new WorkItemWorkingCopy[] { manager.getWorkingCopy(itemhandle) }, progressMonitor);
        }
        catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur traitement RTC création de Defect", e);
        }

        return workItem.getId();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}