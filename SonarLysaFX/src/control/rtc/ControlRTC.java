package control.rtc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
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
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.model.query.BaseContributorQueryModel.ContributorQueryModel;
import com.ibm.team.repository.common.query.IItemQuery;
import com.ibm.team.repository.common.query.IItemQueryPage;
import com.ibm.team.repository.common.query.ast.IPredicate;
import com.ibm.team.repository.common.service.IQueryService;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemOperation;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.internal.model.WorkItem;
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
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import model.Anomalie;
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
    public String recupProjetRTCDepuisWiLot(int lot) throws TeamRepositoryException
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
    public int creerDefect(Anomalie ano)
    {
        IWorkItem workItem = null;
        try
        {           
            IProjectArea projet = pareas.get(ano.getProjetRTC());

            // Type de l'objet
            IWorkItemType itemType = workItemClient.findWorkItemType(projet, "defect", progressMonitor);

            List<ICategory> liste2 = workItemClient.findCategories(projet, ICategory.FULL_PROFILE, progressMonitor);
            ICategory cat = null;
            for (ICategory iCategory : liste2)
            {
                if (iCategory.getName().equals("Projet"))
                    cat = iCategory;
            }
            
            // Création
            WorkItemInitialization init = new WorkItemInitialization("testSummary", itemType, cat, projet, ano);
            IWorkItemHandle handle = init.run(itemType, progressMonitor);
            workItem = auditableClient.fetchCurrentAuditable(handle, WorkItem.FULL_PROFILE, progressMonitor);

        } catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur traitement RTC création de Defect", e);
        }
        return workItem.getId();
    }

    public String recupererValeurAttribut(IAttribute attrb, IWorkItem item) throws TeamRepositoryException
    {
        Object objet = attrb.getValue(auditableCommon, item, progressMonitor);
        if (objet instanceof Identifier)
        {
            @SuppressWarnings ("unchecked")
            Identifier<? extends ILiteral> literalID = (Identifier<? extends ILiteral>) objet;
            List<? extends ILiteral> literals = workItemClient.resolveEnumeration(attrb, progressMonitor).getEnumerationLiterals();

            for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
            {
                ILiteral iLiteral = iterator.next();

                if (iLiteral.getIdentifier2().equals(literalID))
                {
                    System.out.println(attrb.getDisplayName() + " - valeur = " + iLiteral.getName() + " - identifiant : " + literalID);
                    return iLiteral.getName();
                }
                
            }
        }
        else if (objet instanceof String)
            return (String) objet;
        
        return null;
    }


    public void test() throws TeamRepositoryException
    {
        recupererTousLesProjets();
        List<IWorkItemType> liste = workItemClient.findWorkItemTypes(pareas.get("PRJF_T300703"), progressMonitor);
        for (IWorkItemType iWorkItemType : liste)
        {
            System.out.println(iWorkItemType.getIdentifier() + " - " + iWorkItemType.getDisplayName());
        }
        List<ICategory> liste2 = workItemClient.findCategories(pareas.get("PRJF_T300703"), ICategory.FULL_PROFILE, progressMonitor);
        for (ICategory iCategory : liste2)
        {
            System.out.println(iCategory.getName() + " - " + iCategory.getCategoryId().toString());
        }
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
        // Creation Query depuis ContributorQueryModel
        final IItemQuery query = IItemQuery.FACTORY.newInstance(ContributorQueryModel.ROOT);

        // Predicate avec un paramètre poru chercher depuis le nom avec un paramètre de type String
        final IPredicate predicate = ContributorQueryModel.ROOT.name()._eq(query.newStringArg());

        // Utilisation du Predicate en filtre.
        final IItemQuery filtered = (IItemQuery) query.filter(predicate);

        // Appel Service de requêtes depuis TeamRepository et non l'interface.
        final IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la reqête avec le filtre
        final IItemQueryPage page = qs.queryItems(filtered, new Object[] { nom }, 1);

        // Retour de l'objet
        final List<?> handles = page.getItemHandles();
        if (!handles.isEmpty())
        {
            return (IContributor) repo.itemManager().fetchCompleteItem((IContributorHandle) handles.get(0), IItemManager.DEFAULT, progressMonitor);
        }

        return null;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    /**
     * Classe privée permettant la création d'une anomalie dans SonarQube
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class WorkItemInitialization extends WorkItemOperation
    {
        /*---------- ATTRIBUTS ----------*/

        private String summary;
        private IWorkItemType type;
        private ICategory cat;
        private IProjectArea projet;
        private Anomalie ano;
        
        /*---------- CONSTRUCTEURS ----------*/

        public WorkItemInitialization(String summary, IWorkItemType type, ICategory cat, IProjectArea projet, Anomalie ano)
        {
            super("Initializing Work Item");
            this.summary = summary;
            this.type = type;
            this.cat = cat;
            this.projet = projet;
            this.ano = ano;
        }
        
        /*---------- METHODES PUBLIQUES ----------*/

        @Override
        protected void execute(WorkItemWorkingCopy workingCopy, IProgressMonitor monitor) throws TeamRepositoryException
        {
            IWorkItem workItem = workingCopy.getWorkItem();
            workItem.setHTMLSummary(XMLString.createFromPlainText(summary));
            workItem.setHTMLDescription(XMLString.createFromPlainText(summary));
            workItem.setCategory(cat);

            // Environnement
            IAttribute attribut = workItemClient.findAttribute(projet, TypeEnumRTC.ENVIRONNEMENT.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("Br B VMOE", attribut));

            // Importance
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.IMPORTANCE.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("Bloquante", attribut));
            
            // Origine
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.ORIGINE.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("Qualimétrie", attribut));
            
            // Nature
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.NATURE.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("Développement", attribut));

            // Creator
            IContributor iContributor = repo.loggedInContributor();
            workItem.setCreator(iContributor);

            // Owner
            workItem.setOwner(recupContributorDepuisNom(ano.getCpiProjet()));

            // Maj item
            workItemClient.updateWorkItemType(workItem, type, null, monitor);

            // Set tags
            List<String> tags = workItem.getTags2();
            tags.add("NewTag");
            workItem.setTags2(tags);
        }
        
        /*---------- METHODES PRIVEES ----------*/
        
        /**
         * 
         * @param name
         * @param ia
         * @return
         * @throws TeamRepositoryException
         */
        private Identifier<? extends ILiteral> recupLiteralDepuisString(String name, IAttributeHandle ia) throws TeamRepositoryException
        {
            Identifier<? extends ILiteral> literalID = null;
            IEnumeration<? extends ILiteral> enumeration = workItemClient.resolveEnumeration(ia, null);
            List<? extends ILiteral> literals = enumeration.getEnumerationLiterals();
            for (Iterator<? extends ILiteral> iterator = literals.iterator(); iterator.hasNext();)
            {
                ILiteral iLiteral = iterator.next();
                if (iLiteral.getName().equals(name))
                {
                    literalID = iLiteral.getIdentifier2();
                    break;
                }
            }
            return literalID;
        }
        
        /*---------- ACCESSEURS ----------*/
    }
}