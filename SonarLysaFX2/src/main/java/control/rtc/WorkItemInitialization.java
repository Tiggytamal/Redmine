package control.rtc;

import static utilities.Statics.EMPTY;
import static utilities.Statics.proprietesXML;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemOperation;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.ISubscriptions;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;

import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EnumRTC;
import model.enums.EtatCodeAppli;
import model.enums.InstanceSonar;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.QG;
import model.enums.TypeDefautQualite;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe permettant la création d'une anomalie dans RTC. Modification d'une WorkItemOperation.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class WorkItemInitialization extends WorkItemOperation
{
    /*---------- ATTRIBUTS ----------*/

    // Pattern regex
    private static final Pattern VERSIONFDL = Pattern.compile("^.*\\.[0-9]$");
    private static final Pattern EDITION = Pattern.compile("^E[2-9][0-9]");
    private static final Pattern VERSIONREGEX = Pattern.compile("^E[2-9][0-9](\\.[0-1]){0,1}");

    // Constantes statiques
    private static final String XXXXX = "xxxxx";
    private static final String CODE = "-code-";
    private static final String LOT = "-lot-";

    private static final int DURATION = 43_200_000;
    private IWorkItemType type;
    private ICategory cat;
    private IProjectArea projet;
    private DefautQualite dq;
    private int lotAno;
    private ControlRTC controlRTC;
    private IWorkItemClient client;

    /*---------- CONSTRUCTEURS ----------*/

    public WorkItemInitialization(IWorkItemType type, ICategory cat, IProjectArea projet, DefautQualite dq)
    {
        super("Initializing Work Item");
        this.type = type;
        this.cat = cat;
        this.projet = projet;
        this.dq = dq;
        lotAno = Integer.parseInt(this.dq.getLotRTC().getNumero());
        controlRTC = ControlRTC.getInstance();
        client = controlRTC.getClient();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void execute(WorkItemWorkingCopy wc, IProgressMonitor monitor) throws TeamRepositoryException
    {
        IWorkItem workItem = wc.getWorkItem();
        workItem.setHTMLSummary(XMLString.createFromPlainText(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEPANORECAP)));
        workItem.setHTMLDescription(XMLString.createFromPlainText(creerDescription(dq)));
        workItem.setCategory(cat);

        LotRTC lotRTC = dq.getLotRTC();

        // Test si l'espace projet est un espace produit ou non
        if (projet.getProcessName().contains(ControlRTC.ESPACEPRODUIT))
            ajoutInfoEspaceProduit(workItem, monitor);
        else
            ajoutInfoHorsEspaceProduit(workItem, lotRTC, monitor);

        // Estimation
        workItem.setDuration(DURATION);

        // Subscriptions
        Set<Utilisateur> createurs = dq.getCreateurIssues();

        ISubscriptions subscription = workItem.getSubscriptions();
        subscription.add(controlRTC.recupContributorDepuisNom(lotRTC.getCpiProjet().getNom()));
        subscription.add(controlRTC.getRepo().loggedInContributor());

        if (!createurs.isEmpty())
        {
            for (Utilisateur createur : dq.getCreateurIssues())
            {
                subscription.add(controlRTC.recupContributorDepuisId(createur.getIdentifiant()));
            }
        }

        // Membres AQP
        for (String nom : Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESAQP).split(";"))
        {
            subscription.add(controlRTC.recupContributorDepuisNom(nom));
        }

        // Creator
        workItem.setCreator(controlRTC.getRepo().loggedInContributor());

        // Owner
        workItem.setOwner(controlRTC.recupContributorDepuisNom(lotRTC.getCpiProjet().getNom()));

        // Maj item
        client.updateWorkItemType(workItem, type, type, monitor);

        // Set tags
        creerTags(workItem);
    }

    /**
     * Ajout des informations obligatoires pour un espace produit pour que l'anomalie puisse être enregistrée.
     * 
     * @param wi
     *                Objet à traiter.
     * @param monitor
     *                Moniteur du traitement.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    private void ajoutInfoEspaceProduit(IWorkItem wi, IProgressMonitor monitor) throws TeamRepositoryException
    {
        // Importance
        IAttribute attribut = client.findAttribute(projet, EnumRTC.CRITICITE.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Bloquant", attribut));

        // Origine
        attribut = client.findAttribute(projet, EnumRTC.ORIGINE2.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString("MOE", attribut));

    }

    /**
     * Ajout des informations obligatoires hors espace produit pour que l'anomalie puisse être enregistrée.
     * 
     * @param wi
     *                Objet à traiter.
     * @param lotRTC
     *                Lot RTC lié à l'anomalie.
     * @param monitor
     *                Moniteur du traitement.
     * @throws TeamRepositoryException
     *                                 Exception renvoyée par RTC en cas d'erreur.
     */
    private void ajoutInfoHorsEspaceProduit(IWorkItem wi, LotRTC lotRTC, IProgressMonitor monitor) throws TeamRepositoryException
    {
        // Environnement
        IAttribute attribut = client.findAttribute(wi.getProjectArea(), EnumRTC.ENVIRONNEMENT.getValeur(), monitor);
        if (calculPariteEdition(lotRTC.getEdition().getNom()) || calculPariteEdition(proprietesXML.getMapParams().get(Param.RTCLOTCHC)))
            wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Br A VMOE", attribut));
        else
            wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Br B VMOE", attribut));

        // Importance
        attribut = client.findAttribute(projet, EnumRTC.IMPORTANCE.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Bloquante", attribut));

        // Origine
        attribut = client.findAttribute(projet, EnumRTC.ORIGINE.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Qualimétrie", attribut));

        // Trouve dans - Itération de plannification
        attribut = client.findAttribute(projet, EnumRTC.TROUVEDANS.getValeur(), monitor);
        IWorkItem lotInitial = controlRTC.recupWorkItemDepuisId(Integer.parseInt(dq.getLotRTC().getNumero()));
        wi.setValue(attribut, lotInitial.getItemHandle());

        // Nature
        attribut = client.findAttribute(projet, EnumRTC.NATURE.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString("Qualité", attribut));

        // Edition
        String edition = lotRTC.getEdition().getNom();
        attribut = client.findAttribute(projet, EnumRTC.EDITION.getValeur(), monitor);
        wi.setValue(attribut, controlRTC.recupLiteralDepuisString(calculEditionRTC(edition), attribut));
    }

    /**
     * Calcul la valeur de l'édition de l'anomalie.
     * 
     * @param edition
     *                Edition depuis la base de données.
     * @return
     *         Edition transformée compatible avec RTC.
     */
    private String calculEditionRTC(String edition)
    {
        // Si on a une edition CHC, on va chercher la version dans les paramètres
        if (edition.contains("CHC") || edition.contains("CDM"))
            return proprietesXML.getMapParams().get(Param.RTCLOTCHC);

        String fdlregex = "Fil_De_Leau";
        String retour = EMPTY;
        Matcher matcher = VERSIONREGEX.matcher(edition);
        if (matcher.find())
            retour = matcher.group(0);

        // Ajout du Fil de l'eau.
        if (edition.contains(fdlregex))
            if (VERSIONFDL.matcher(retour).matches())
                retour = retour + ".FDL";
            else
                retour = retour + ".0.FDL";
        return retour;
    }

    /**
     * Calvul la parité du numéro de l'édition pour pouvoir sélectionner la branche de développement.
     * 
     * @param edition
     *                Edition de l'anomalie.
     * @return
     *         Vrai si l'édition est paire.
     */
    private boolean calculPariteEdition(String edition)
    {
        Matcher matcher = EDITION.matcher(edition);
        int i = 0;
        if (matcher.find())
        {
            i = Integer.parseInt(matcher.group(0).substring(1));
            return (i % 2 == 0);
        }
        return false;
    }

    /**
     * Rajoute les tags à l'anomalie (numéro de lot et sécurité).
     * 
     * @param wi
     *           Anomalie à traiter.
     */
    private void creerTags(IWorkItem wi)
    {
        List<String> tags = wi.getTags2();
        tags.add("lot=" + lotAno);
        if (dq.getCompo().isSecurite())
            tags.add("securite");
        wi.setTags2(tags);
    }

    /**
     * Création du texte de l'anomalie, avec paramétrage du nom et de la clef du composant, du numéro du lot, du code application et du serveur SonarQube.
     * 
     * @param dq
     *           DefautQualite à traiter.
     * @return
     *         Description complète traitée.
     */
    private String creerDescription(DefautQualite dq)
    {

        StringBuilder retour = new StringBuilder();
        if (dq.getTypeDefaut() != TypeDefautQualite.APPLI)
        {
            String texte = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANO).replace(LOT, String.valueOf(lotAno)).replace(XXXXX, dq.getCompo().getNom()).replace("key",
                    dq.getCompo().getKey());

            // Calcul url serveur SonarQube
            if (dq.getCompo().getInstance() == InstanceSonar.LEGACY)
                texte = texte.replace("-serveur-", Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS));
            else if (dq.getCompo().getInstance() == InstanceSonar.MOBILECENTER)
                texte = texte.replace("-serveur-", Statics.proprietesXML.getMapParams().get(Param.URLSONARMC) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS));
            else
                throw new TechnicalException("control.rtc.WorkItemInitialization.creerDescription - InstanceSonar inconnue : " + dq.getCompo().getInstance());
            retour.append(texte);

            // Texte défaut de sécurité
            if (dq.getCompo().isSecurite())
                retour.append(Statics.NL).append(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOSECURITE));
        }
        if (dq.getTypeDefaut() != TypeDefautQualite.SONAR)
        {
            // ajout du texte pour les anomalies concernants les codes applications sur le composant.
            if (retour.length() != 0)
                retour.append(Statics.NL);

            // Ajout du message de correction du code appli si celui est bon pour SonarQube mais quand même faux
            if (dq.getCompo().getAppli() != null && dq.getCompo().getQualityGate() == QG.OK && dq.getEtatCodeAppli() == EtatCodeAppli.ERREUR)
                retour.append(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOAPPLIFAUX).replace(CODE, dq.getCompo().getAppli().getCode()).replace(XXXXX, dq.getCompo().getNom()));
            else
                retour.append(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOAPPLI).replace(XXXXX, dq.getCompo().getNom()));
        }

        return retour.toString();
    }

    /*---------- ACCESSEURS ----------*/
}
