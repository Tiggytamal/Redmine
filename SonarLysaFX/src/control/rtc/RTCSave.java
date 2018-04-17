package control.rtc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.filesystem.common.internal.process.config.DisinterestedApproverWorkItemSaveAdvisorConfig;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.IItem;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.UUID;
import com.ibm.team.repository.common.query.ast.IPredicate;
import com.ibm.team.workitem.api.common.WorkItem;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.IWorkItemWorkingCopyManager;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.internal.model.query.BaseWorkItemQueryModel.WorkItemQueryModel;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.query.IQueryDescriptor;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

@SuppressWarnings ("unused")
public class RTCSave
{
    private ITeamRepository repo;
    private IProgressMonitor progressMonitor;
    private IQueryDescriptor descriptor;

    private Map<String, IProjectArea> pareas;
    private Expression expression;

    public IQueryResult<IResolvedResult<IWorkItem>> resultsResolvedByExpression(IProjectArea projectArea)
    {
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IQueryClient queryClient = workItemClient.getQueryClient();
        IQueryResult<IResolvedResult<IWorkItem>> results = queryClient.getResolvedExpressionResults(projectArea, expression, IWorkItem.FULL_PROFILE);
        return results;
    }

    public IQueryResult<IResolvedResult<IWorkItem>> test2()
    {
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IQueryClient queryClient = workItemClient.getQueryClient();
        // Set the load profile
        ItemProfile<IWorkItem> loadProfile = IWorkItem.SMALL_PROFILE;
        return queryClient.getResolvedQueryResults(descriptor, loadProfile);
    }

    private ICategoryHandle toCategory(String allValuesAsString)
    {
        return null;
    }

    private IProjectAreaHandle toProjectArea(String allValuesAsString)
    {
        return null;
    }

    public RTCSave() throws TeamRepositoryException
    {
        TeamPlatform.startup();
        repo = connect("ETP8137", "28H02m89,;:!", "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm");
    }

    public void shutdown()
    {
        TeamPlatform.shutdown();
    }

    private ITeamRepository connect(final String user, final String password, String repoUrl) throws TeamRepositoryException
    {
        ITeamRepository repository = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoUrl);
        progressMonitor = new NullProgressMonitor();
        repository.registerLoginHandler((ITeamRepository repo) -> new UsernameAndPasswordLoginInfo("ETP8137", "28H02m89,;:!"));
        repository.login(progressMonitor);
        return repository;
    }

    public Map<String, IProjectArea> fetchAllProjectAreas() throws TeamRepositoryException
    {

        Map<String, IProjectArea> pareas = new HashMap<>();
        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
        this.pareas = pareas;
        return pareas;
    }

    public void createQuery(IProjectArea parea) throws TeamRepositoryException
    {
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IAttribute built_in_duration = workItemClient.findAttribute(parea, IWorkItem.ID_PROPERTY, progressMonitor);
        IAuditableCommon auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
        IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(parea, IWorkItem.PROJECT_AREA_PROPERTY, auditableCommon, progressMonitor);
        IQueryableAttribute typeType = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(parea, IWorkItem.TYPE_PROPERTY, auditableCommon, progressMonitor);
        IQueryableAttribute id = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(parea, IWorkItem.ID_PROPERTY, auditableCommon, progressMonitor);

        Expression inProjectArea = new AttributeExpression(attribute, AttributeOperation.EQUALS, parea);

        Expression isId = new AttributeExpression(id, AttributeOperation.EQUALS, "309174");
        expression = inProjectArea;
        Term typeinProjectArea = new Term(Term.Operator.AND);
        typeinProjectArea.add(inProjectArea);
        typeinProjectArea.add(isId);
        expression = typeinProjectArea;
    }

    public IQueryResult<IResolvedResult<IWorkItem>> fetchAllItems(IProjectArea parea) throws TeamRepositoryException
    {
        IAuditableClient auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);

        IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);

        IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(parea, IWorkItem.PROJECT_AREA_PROPERTY, auditableClient, progressMonitor);

        Expression expression = new AttributeExpression(attribute, AttributeOperation.EQUALS, parea);

        return queryClient.getResolvedExpressionResults(parea, expression, IWorkItem.SMALL_PROFILE);
    }

    public void test() throws TeamRepositoryException
    {
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IWorkItem workItem = workItemClient.findWorkItemById(319741, IWorkItem.FULL_PROFILE, new NullProgressMonitor());

        System.out.println("Listing all attributes....");
        List<IAttribute> allAttributes = workItemClient.findAttributes(workItem.getProjectArea(), new NullProgressMonitor());
        for (Iterator<IAttribute> iterator = allAttributes.iterator(); iterator.hasNext();)
        {

            IAttribute iAttributeHandle = iterator.next();
            IAttribute iAttribute = (IAttribute) repo.itemManager().fetchCompleteItem(iAttributeHandle, IItemManager.DEFAULT, new NullProgressMonitor());
            System.out.println("Attribute Name: " + iAttribute.getDisplayName() + ", Type: " + iAttribute.getAttributeType());

            if (iAttribute.getDisplayName() == "Actual Spent Hours")
            {
                System.out.println("Setting a value to the attribute Actual Spent Hours....");
                IWorkItemWorkingCopyManager copyManager = workItemClient.getWorkItemWorkingCopyManager();
                copyManager.connect(workItem, IWorkItem.FULL_PROFILE, new NullProgressMonitor());
                WorkItemWorkingCopy workItemCopy = copyManager.getWorkingCopy(workItem);
                IWorkItem workItem_to_edit = (IWorkItem) workItemCopy.getWorkItem();
                Integer inter = new Integer("4");
                workItem_to_edit.setValue(iAttribute, inter);

            }
        }

    }

}
