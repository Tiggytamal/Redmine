package control.rtc;

import static utilities.Statics.EMPTY;
import static utilities.Statics.proprietesXML;

import java.util.List;
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

import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeDefaut;
import model.enums.TypeEnumRTC;
import utilities.Statics;

/**
 * Classe permettant la création d'une anomalie dans RTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class WorkItemInitialization extends WorkItemOperation
{
    /*---------- ATTRIBUTS ----------*/

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
        lotAno = Integer.parseInt(this.dq.getLotRTC().getLot());
        controlRTC = ControlRTC.INSTANCE;
        client = controlRTC.getClient();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void execute(WorkItemWorkingCopy workingCopy, IProgressMonitor monitor) throws TeamRepositoryException
    {
        IWorkItem workItem = workingCopy.getWorkItem();
        workItem.setHTMLSummary(XMLString.createFromPlainText(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.RECAPDEFECT)));
        workItem.setHTMLDescription(XMLString.createFromPlainText(creerDescription(dq)));
        workItem.setCategory(cat);

        LotRTC lotRTC = dq.getLotRTC();

        // Environnement
        IAttribute attribut = client.findAttribute(projet, TypeEnumRTC.ENVIRONNEMENT.getValeur(), monitor);
        if (calculPariteEdition(lotRTC.getEdition().getNom()) || calculPariteEdition(proprietesXML.getMapParams().get(Param.RTCLOTCHC)))
            workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("Br A VMOE", attribut));
        else
            workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("Br B VMOE", attribut));

        // Importance
        attribut = client.findAttribute(projet, TypeEnumRTC.IMPORTANCE.getValeur(), monitor);
        workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("Bloquante", attribut));
        
        // Estimation
        workItem.setDuration(43200000);
        
        // Origine
        attribut = client.findAttribute(projet, TypeEnumRTC.ORIGINE.getValeur(), monitor);
        workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("Qualimétrie", attribut));
        
        // Trouvé dans - Itération de plannification
        attribut = client.findAttribute(projet, TypeEnumRTC.TROUVEDANS.getValeur(), monitor);
        IWorkItem lotInitial = controlRTC.recupWorkItemDepuisId(Integer.parseInt(dq.getLotRTC().getLot()));        
        workItem.setValue(attribut, lotInitial.getTarget());

        // Nature
        attribut = client.findAttribute(projet, TypeEnumRTC.NATURE.getValeur(), monitor);
        workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("Qualité", attribut));

        // Entité responsable
        attribut = client.findAttribute(projet, TypeEnumRTC.ENTITERESPCORRECTION.getValeur(), monitor);
        workItem.setValue(attribut, controlRTC.recupLiteralDepuisString("MOE", attribut));

        // Edition
        String edition = lotRTC.getEdition().getNom();
        attribut = client.findAttribute(projet, TypeEnumRTC.EDITION.getValeur(), monitor);
        workItem.setValue(attribut, controlRTC.recupLiteralDepuisString(calculEditionRTC(edition), attribut));

        // Subscriptions
        ISubscriptions subscription = workItem.getSubscriptions();
        subscription.add(controlRTC.recupContributorDepuisNom(lotRTC.getCpiProjet()));
        subscription.add(controlRTC.getRepo().loggedInContributor());

        // Contributeurs JAVA
        if (lotRTC.getMatieres().contains(Matiere.JAVA))
        {
            for (String nom : Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESJAVA).split(";"))
            {
                subscription.add(controlRTC.recupContributorDepuisNom(nom));
            }
        }

        // Contribureurs DATASTAGE
        if (lotRTC.getMatieres().contains(Matiere.DATASTAGE))
        {
            for (String nom : Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESDATASTAGE).split(";"))
            {
                subscription.add(controlRTC.recupContributorDepuisNom(nom));
            }
        }

        // Creator
        workItem.setCreator(controlRTC.getRepo().loggedInContributor());

        // Owner
        workItem.setOwner(controlRTC.recupContributorDepuisNom(lotRTC.getCpiProjet()));

        // Maj item
        client.updateWorkItemType(workItem, type, type, monitor);

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
            return proprietesXML.getMapParams().get(Param.RTCLOTCHC);

        String versionRegex = "^E[2-9][0-9](\\.[0-1]){0,1}";
        String fdlregex = "Fil_De_Leau";
        String retour = EMPTY;
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

    private boolean calculPariteEdition(String edition)
    {
        Matcher matcher = Pattern.compile("^E[2-9][0-9]").matcher(edition);
        int i = 0;
        if (matcher.find())
        {
            i = Integer.parseInt(matcher.group(0).substring(1));
            return (i % 2 == 0);
        }
        return false;
    }

    private void creerTags(IWorkItem workItem)
    {
        List<String> tags = workItem.getTags2();
        tags.add("lot=" + lotAno);
        if (dq.isSecurite())
            tags.add("sécurité");
        workItem.setTags2(tags);
    }

    private String creerDescription(DefautQualite dq)
    {
        String retour;
        if (dq.getTypeDefaut() != TypeDefaut.APPLI)
        {
            retour = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEDEFECT).replace("-lot-", String.valueOf(lotAno));
            if (dq.isSecurite())
                retour = retour.replace("Merci", Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTESECURITE));
        }
        else
        {
            // Ajout de tous le snoms de composants à la liste
            StringBuilder builder = new StringBuilder();
            for (DefautAppli da : dq.getDefautsAppli())
            {
                builder.append(Statics.TIRET).append(da.getCompo().getNom()).append(Statics.NL);
            }
            
            // ajout des noms de composants à la phrase de l'anomalie
            retour = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEDEFECTAPPLI).replaceAll("xxxxx", builder.toString());            
            if (!dq.getNewCodeAppli().isEmpty())
                retour += Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTENEWAPPLI).replaceAll("-code-", dq.getNewCodeAppli());
        }
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
