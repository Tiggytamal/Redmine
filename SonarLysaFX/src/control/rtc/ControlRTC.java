package control.rtc;

import static utilities.Statics.logger;
import static utilities.Statics.proprietesXML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ibm.team.workitem.common.model.ISubscriptions;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import com.mchange.util.AssertException;

import model.Anomalie;
import model.enums.Matiere;
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

    private static final String RECAPITULATIF = "Anomalie Qualimétrie : Quality Gate non conforme";

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
        if (workItem == null)
        {
            logger.warn("Récupération projetRTC - Lot introuvable : " + lot);
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
            WorkItemInitialization init = new WorkItemInitialization(itemType, cat, projet, ano);
            IWorkItemHandle handle = init.run(itemType, progressMonitor);
            workItem = auditableClient.fetchCurrentAuditable(handle, WorkItem.FULL_PROFILE, progressMonitor);

        } catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Erreur traitement RTC création de Defect", e);
        }
        return workItem.getId();
    }

    /**
     * Retourne la valeur d'un attribut d'un WorkItem RTC sous forme d'une chaine de caractères.
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
                System.out.println(iLiteral.getName());
                if (iLiteral.getIdentifier2().equals(literalID))
                    return iLiteral.getName();
            }
        }
        else if (objet instanceof String)
            return (String) objet;

        return null;
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
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class WorkItemInitialization extends WorkItemOperation
    {
        /*---------- ATTRIBUTS ----------*/

        private IWorkItemType type;
        private ICategory cat;
        private IProjectArea projet;
        private Anomalie ano;
        private int lotAno;

        /*---------- CONSTRUCTEURS ----------*/

        public WorkItemInitialization(IWorkItemType type, ICategory cat, IProjectArea projet, Anomalie ano)
        {
            super("Initializing Work Item");
            this.type = type;
            this.cat = cat;
            this.projet = projet;
            this.ano = ano;
            lotAno = Integer.parseInt(ano.getLot().substring(4));
        }

        /*---------- METHODES PUBLIQUES ----------*/

        @Override
        protected void execute(WorkItemWorkingCopy workingCopy, IProgressMonitor monitor) throws TeamRepositoryException
        {
            IWorkItem workItem = workingCopy.getWorkItem();
            workItem.setHTMLSummary(XMLString.createFromPlainText(RECAPITULATIF));
            workItem.setHTMLDescription(XMLString.createFromPlainText(creerDescription(lotAno)));
            workItem.setCategory(cat);

            // Environnement
            IAttribute attribut = workItemClient.findAttribute(projet, TypeEnumRTC.ENVIRONNEMENT.toString(), null);
            if (calculPariteVersion(ano.getVersion()))
                workItem.setValue(attribut, recupLiteralDepuisString("Br A VMOE", attribut));
            else
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

            // Entité responsable
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.ENTITERESPCORRECTION.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("MOE", attribut));

            // Edition
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.EDITION.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString(calculVersionRTC(ano.getVersion()), attribut));

            // Subscriptions
            ISubscriptions subscription = workItem.getSubscriptions();
            subscription.add(recupContributorDepuisNom(ano.getCpiProjet()));
            subscription.add(repo.loggedInContributor());

            // Contributeurs JAVA
            if (ano.getMatieres().contains(Matiere.JAVA))
            {
                subscription.add(recupContributorDepuisNom("PRUDENT Alain"));
                subscription.add(recupContributorDepuisNom("TRICOT Nicolas"));
                subscription.add(recupContributorDepuisNom("MATHON Gregoire"));
            }

            // Contribureurs DATASTAGE
            if (ano.getMatieres().contains(Matiere.DATASTAGE))
            {
                subscription.add(recupContributorDepuisNom("BONORIS Jean-Louis"));
            }

            // Creator
            workItem.setCreator(repo.loggedInContributor());

            // Owner
            workItem.setOwner(recupContributorDepuisNom(ano.getCpiProjet()));

            // Maj item
            workItemClient.updateWorkItemType(workItem, type, type, monitor);

            // Set tags
            creerTags(workItem);
        }

        private String calculVersionRTC(String version)
        {
            if (version.contains("CHC") || version.contains("CDM"))
                return proprietesXML.getMapParams().get(TypeParam.RTCLOTCHC);
            String versionRegex = "^E[2-9][0-9](\\.[0-1]) {0,1}";
            String fdlregex = "Fil_De_Leau";
            String retour = "";            
            Matcher matcher = Pattern.compile(versionRegex).matcher(version);
            if (matcher.find())
                retour = matcher.group(0);
            if (version.contains(fdlregex))
                retour = retour + ".FDL";
            return retour;
        }
        
        private boolean calculPariteVersion(String version)
        {
            Matcher matcher = Pattern.compile("^E[2-9][0-9]").matcher(version);
            int i = 0;
            if (matcher.find())
                i = Integer.parseInt(matcher.group(0).substring(1));
            return (i %2 == 0);
                
        }

        private void creerTags(IWorkItem workItem)
        {
            List<String> tags = workItem.getTags2();
            tags.add("lot=" + lotAno);
            if (ano.getSecurite().equals(Statics.SECURITEKO))
                tags.add("sécurité");
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

        private String creerDescription(int lot)
        {
            return "Bonjour,\nL'analyse SonarQube de ce jour du lot projet " + lot + " fait apparaitre un quality Gate non conforme.\n"
                    + "Veuillez trouver ci après le lien vers l'analyse du lot pour prise en compte et correction :\n" + "http://ttp10-snar.ca-technologies.fr/governance?id=view_lot_" + lot
                    + "\nMerci";
        }

        /*---------- ACCESSEURS ----------*/
    }
}