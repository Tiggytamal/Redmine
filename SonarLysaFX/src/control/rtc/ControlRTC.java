package control.rtc;

import static utilities.Statics.logger;
import static utilities.Statics.proprietesXML;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
import com.ibm.team.repository.common.UUID;
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
import com.ibm.team.workitem.common.internal.model.query.BaseWorkItemQueryModel.WorkItemQueryModel;
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
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.Environnement;
import model.enums.Matiere;
import model.enums.TypeEnumRTC;
import model.enums.TypeFichier;
import model.enums.TypeParam;
import model.enums.TypeParamSpec;
import utilities.DateConvert;
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
     * Création du Defect dans RTC
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
            logger.error("Erreur traitement RTC création de Defect. Lot : " + ano.getLot());
        }

        if (workItem != null)
        {
            logger.info("Creation anomalie RTC numéro : " + workItem.getId() + " pour " + ano.getLot());
            return workItem.getId();
        }
        return 0;
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

    /**
     * Récupération de tous les lots RTC selon les paramètres fournis : <br>
     * - remiseAZero = indique si l'on prend tous les loes depuis la date de création fournie ou seulement depuis la dernière mise à jour <br>
     * - date Création = date de la dernière mise àjour du fichier <br>
     * Si la date de dernière mise àjour n'est pas connue, une demande de remise à zéero sera renvoyée.<br>
     * Une erreur sera remontée pour toute remise à zéro sans date limite de création
     * 
     * @param remiseAZero
     * @param dateCreation
     * @return
     * @throws TeamRepositoryException
     */
    @SuppressWarnings("unchecked")
    public List<?> recupLotsRTC(boolean remiseAZero, LocalDate dateCreation) throws TeamRepositoryException
    {
        // 1. Contrôle

        // On retourne une erreur si l'on souhaite une remise à zéro mais que la date de création n'est pas renseignée
        if (remiseAZero && dateCreation == null)
            throw new TechnicalException("méthode control.rts.ControlRTC.recupLotsRTC : date Creation non renseignée lors d'une remise à zéro.", null);

        // 2. Requetage sur RTC pour récupérer tous les Lots

        // Creation Query depuis ContributorQueryModel
        IItemQuery query = IItemQuery.FACTORY.newInstance(WorkItemQueryModel.ROOT);

        // Predicate avec un paramètre poru chercher depuis le nom avec un paramètre de type String
        IPredicate predicatFinal = WorkItemQueryModel.ROOT.workItemType()._eq("fr.ca.cat.wi.lotprojet");

        // Prise en compte de la date de création si elle est fournie
        if (dateCreation != null)
        {
            IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(DateConvert.convertToOldDate(dateCreation));
            predicatFinal = predicatFinal._and(predicatCreation);
        }

        // Dans le cas où l'on ne fait pas une remise à zéro du fichier, on ne prend que les lots qui ont été modifiés ou créées depuis la dernière mise à jour.
        if (!remiseAZero)
        {
            String dateMajFichierRTC = Statics.fichiersXML.getDateMaj().get(TypeFichier.LOTSRTC);
            if (dateMajFichierRTC != null && !dateMajFichierRTC.isEmpty())
            {
                // Date de la dernière mise à jour
                LocalDate lastUpdate = LocalDate.parse(dateMajFichierRTC);

                // Periode entre la dernière mise à jour et aujourd'hui
                Period periode = Period.between(lastUpdate, Statics.TODAY);

                // Prédicat des lots qui ont été modifiés depuis la dernière mise à jour
                IPredicate dateModification = WorkItemQueryModel.ROOT.modified()._gt(DateConvert.convertToOldDate(Statics.TODAY.minusDays(periode.getDays())));

                // Prédicat des lots qui ont été créés depuis la dernière mise à jour
                IPredicate predicatCreation = WorkItemQueryModel.ROOT.creationDate()._gt(DateConvert.convertToOldDate(Statics.TODAY.minusDays(periode.getDays())));

                // Ajout du contrôle Or et modification du prédicat final
                IPredicate predicatOu = dateModification._or(predicatCreation);
                predicatFinal = predicatFinal._and(predicatOu);
            }
        }

        // Utilisation du Predicate en filtre.
        IItemQuery filtered = (IItemQuery) query.filter(predicatFinal);

        // Appel Service de requêtes depuis TeamRepository et non l'interface.
        IQueryService qs = ((TeamRepository) repo).getQueryService();

        // Appel de la reqête avec le filtre
        int pageSize = 512;
        IItemQueryPage page = qs.queryItems(filtered, new Object[] {}, pageSize);

        // Liste de tous les lots trouvés.
        List<?> retour = new ArrayList<>();
        retour.addAll(page.getItemHandles());
        int nextPosition = page.getNextStartPosition();
        UUID token = page.getToken();

        // On itère sur toutes les pages de retour de la requêtes pour remplir la liste.
        while (nextPosition != -1)
        {
            IItemQueryPage nextPage = (IItemQueryPage) qs.fetchPage(token, nextPosition, pageSize);
            nextPosition = nextPage.getNextStartPosition();
            retour.addAll(nextPage.getItemHandles());
        }

        return retour;
    }

    public LotSuiviRTC creerLotSuiviRTCDepuisHandle(Object item) throws TeamRepositoryException
    {
        if (item instanceof IWorkItemHandle)
        {
            IWorkItem workItem = (IWorkItem) repo.itemManager().fetchCompleteItem((IWorkItemHandle) item, IItemManager.DEFAULT, progressMonitor);
            LotSuiviRTC retour = ModelFactory.getModel(LotSuiviRTC.class);
            retour.setLot(String.valueOf(workItem.getId()));
            retour.setLibelle(workItem.getHTMLSummary().getPlainText());
            retour.setCpiProjet(recupererItemDepuisHandle(IContributor.class, workItem.getOwner()).getName());
            retour.setProjetClarity(recupererValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.CLARITY.toString(), null), workItem));
            retour.setEdition(recupererValeurAttribut(workItemClient.findAttribute(workItem.getProjectArea(), TypeEnumRTC.EDITIONSICIBLE.toString(), null), workItem));
            retour.setEtatLot(Environnement.from(recupEtatElement(workItem)));
            retour.setProjetRTC(recupererItemDepuisHandle(IProjectArea.class, workItem.getProjectArea()).getName());
            return retour;
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
            lotAno = Integer.parseInt(this.ano.getLot().substring(4));
        }

        /*---------- METHODES PUBLIQUES ----------*/

        @Override
        protected void execute(WorkItemWorkingCopy workingCopy, IProgressMonitor monitor) throws TeamRepositoryException
        {
            IWorkItem workItem = workingCopy.getWorkItem();
            workItem.setHTMLSummary(XMLString.createFromPlainText(RECAPITULATIF));
            workItem.setHTMLDescription(XMLString.createFromPlainText(creerDescription()));
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
            workItem.setValue(attribut, recupLiteralDepuisString("Qualité", attribut));

            // Entité responsable
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.ENTITERESPCORRECTION.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString("MOE", attribut));

            // Edition
            String edition = ano.getEdition();
            attribut = workItemClient.findAttribute(projet, TypeEnumRTC.EDITION.toString(), null);
            workItem.setValue(attribut, recupLiteralDepuisString(calculEditionRTC(edition), attribut));

            // Edition SI Cible
            if (edition.contains("CHC") || edition.contains("CDM"))
            {
                attribut = workItemClient.findAttribute(projet, TypeEnumRTC.EDITIONSICIBLE.toString(), null);
                workItem.setValue(attribut, recupLiteralDepuisString(edition, attribut));
            }

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

        /**
         * Calcul la valeur de l'édition de l'anomalie
         * 
         * @param edition
         * @return
         */
        private String calculEditionRTC(String edition)
        {
            // Si on a une édition CHC, on va chercher la version dans les paramètres
            if (edition.contains("CHC") || edition.contains("CDM"))
                return proprietesXML.getMapParams().get(TypeParam.RTCLOTCHC);

            String versionRegex = "^E[2-9][0-9](\\.[0-1]){0,1}";
            String fdlregex = "Fil_De_Leau";
            String retour = "";
            Matcher matcher = Pattern.compile(versionRegex).matcher(edition);
            if (matcher.find())
                retour = matcher.group(0);

            // Ajout du Fil de l'eau.
            if (edition.contains(fdlregex))
                if (retour.matches("^.*\\.[0-9]$"))
                    retour = retour + ".FDL";
                else
                    retour = retour + ".0.FDL";
            return retour;
        }

        private boolean calculPariteVersion(String version)
        {
            Matcher matcher = Pattern.compile("^E[2-9][0-9]").matcher(version);
            int i = 0;
            if (matcher.find())
                i = Integer.parseInt(matcher.group(0).substring(1));
            return (i % 2 == 0);

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

        private String creerDescription()
        {
            String retour = Statics.proprietesXML.getMapParamsSpec().get(TypeParamSpec.TEXTEDEFECT).replace("-lot-", String.valueOf(lotAno));
            if (ano.getSecurite().equals(Statics.SECURITEKO))
                retour = retour.replace("Merci", Statics.proprietesXML.getMapParamsSpec().get(TypeParamSpec.TEXTESECURITE));
            return retour;
        }

        /*---------- ACCESSEURS ----------*/
    }
}