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
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

import model.enums.TypeParam;
import utilities.Statics;

public class ControlRTC
{
    /*---------- ATTRIBUTS ----------*/

    private ITeamRepository repo;
    private IProgressMonitor progressMonitor;
    private Map<String, IProjectArea> pareas;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de base utilisant les informations utilisateurs de l'application
     * 
     * @throws TeamRepositoryException
     */
    public ControlRTC() throws TeamRepositoryException
    {
        TeamPlatform.startup();
        repo = connect(Statics.info.getPseudo(), Statics.info.getMotDePasse(), Statics.proprietesXML.getMapParams().get(TypeParam.URLRTC));
        pareas = new HashMap<>();
        fetchAllProjectAreas();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Fermeture de la plateforme RTC
     */
    public void shutdown()
    {
        TeamPlatform.shutdown();
    }

    /**
     * Connection au repository RTC
     * 
     * @param user
     * @param password
     * @param repoUrl
     * @return
     * @throws TeamRepositoryException
     */
    private ITeamRepository connect(final String user, final String password, String repoUrl) throws TeamRepositoryException
    {
        repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoUrl);
        progressMonitor = new NullProgressMonitor();
        repo.registerLoginHandler((ITeamRepository repository) -> new UsernameAndPasswordLoginInfo(user, password));
        repo.login(progressMonitor);
        return repo;
    }

    /**
     * Récupération ed tous les projets RTC
     * 
     * @throws TeamRepositoryException
     */
    public void fetchAllProjectAreas() throws TeamRepositoryException
    {
        IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
        for (Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null))
        {
            IProjectArea parea = (IProjectArea) pareaObj;
            pareas.put(parea.getName(), parea);
        }
    }

    /**
     * Récupération d'un WorkItem depuis son identifiant et le projet RTC dont il est issu.
     * @param projetString
     * @param anomalieId
     * @return
     * @throws TeamRepositoryException
     */
    public IQueryResult<IResolvedResult<IWorkItem>> fetchItemById(String projetString, String anomalieId) throws TeamRepositoryException
    {
        IProjectArea projet = pareas.get(projetString);
        IAuditableCommon auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
        IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(projet, IWorkItem.PROJECT_AREA_PROPERTY, auditableCommon, progressMonitor);
        IQueryableAttribute id = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(projet, IWorkItem.ID_PROPERTY, auditableCommon, progressMonitor);
        Expression inProjectArea = new AttributeExpression(attribute, AttributeOperation.EQUALS, projet);
        Expression isId = new AttributeExpression(id, AttributeOperation.EQUALS, anomalieId);
        Term typeinProjectArea = new Term(Term.Operator.AND);
        typeinProjectArea.add(inProjectArea);
        typeinProjectArea.add(isId);
        IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
        return queryClient.getResolvedExpressionResults(projet, typeinProjectArea, IWorkItem.FULL_PROFILE);
    }
    
    /**
     * Calcul de l'état d'un objet RTC
     * @param item
     * @return
     * @throws TeamRepositoryException
     */
    public String getWorkItemState(IWorkItem item) throws TeamRepositoryException
    {
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IWorkflowInfo workflowInfo = workItemClient.findWorkflowInfo(item,  progressMonitor);
        return workflowInfo.getStateName(item.getState2());
    }
    
    public List<String> getProjetRTCDepuisClarity(String clarity)
    {
        List<String> retour = new ArrayList<>();
        int i = 0;
        for (String key : pareas.keySet())
        {
            if (key.contains(clarity))
            {
                if (i > 0)
                    System.out.println(retour + " autre cas trouvé " + key);
                retour.add(key);
                i++;
            }
        }
        if (i == 0)
        {
            for (String key : pareas.keySet())
            {
                if (key.contains(clarity.substring(0, 6)))
                {
                    if (i > 0)
                        System.out.println(retour + " autre cas trouvé " + key);
                    retour.add(key);
                    i++;
                }
            }
        }
        return retour;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}