package control.rtc;

import static utilities.Statics.proprietesXML;

import java.util.Iterator;
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
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.ISubscriptions;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;

import model.Anomalie;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeEnumRTC;
import utilities.Statics;

/**
 * Classe permettant la création d'une anomalie dans RTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public final class WorkItemInitialization extends WorkItemOperation
{
    /*---------- ATTRIBUTS ----------*/

    private IWorkItemType type;
    private ICategory cat;
    private IProjectArea projet;
    private Anomalie ano;
    private int lotAno;
    private ControlRTC controlRTC;
    private IWorkItemClient client;

    /*---------- CONSTRUCTEURS ----------*/

    public WorkItemInitialization(IWorkItemType type, ICategory cat, IProjectArea projet, Anomalie ano)
    {
        super("Initializing Work Item"); 
        this.type = type;
        this.cat = cat;
        this.projet = projet;
        this.ano = ano;
        lotAno = Integer.parseInt(this.ano.getLot().substring(Statics.SBTRINGLOT));
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
        workItem.setHTMLDescription(XMLString.createFromPlainText(creerDescription()));
        workItem.setCategory(cat);

        // Environnement
        IAttribute attribut = client.findAttribute(projet, TypeEnumRTC.ENVIRONNEMENT.toString(), null);
        if (calculPariteEdition(ano.getVersion()) || calculPariteEdition(proprietesXML.getMapParams().get(Param.RTCLOTCHC)))
            workItem.setValue(attribut, recupLiteralDepuisString("Br A VMOE", attribut));
        else
            workItem.setValue(attribut, recupLiteralDepuisString("Br B VMOE", attribut));

        // Importance
        attribut = client.findAttribute(projet, TypeEnumRTC.IMPORTANCE.toString(), null);
        workItem.setValue(attribut, recupLiteralDepuisString("Bloquante", attribut));

        // Origine
        attribut = client.findAttribute(projet, TypeEnumRTC.ORIGINE.toString(), null);
        workItem.setValue(attribut, recupLiteralDepuisString("Qualimétrie", attribut));

        // Nature
        attribut = client.findAttribute(projet, TypeEnumRTC.NATURE.toString(), null);
        workItem.setValue(attribut, recupLiteralDepuisString("Qualité", attribut));

        // Entité responsable
        attribut = client.findAttribute(projet, TypeEnumRTC.ENTITERESPCORRECTION.toString(), null);
        workItem.setValue(attribut, recupLiteralDepuisString("MOE", attribut));

        // Edition
        String edition = ano.getEdition();
        attribut = client.findAttribute(projet, TypeEnumRTC.EDITION.toString(), null);
        workItem.setValue(attribut, recupLiteralDepuisString(calculEditionRTC(edition), attribut));

        // Subscriptions
        ISubscriptions subscription = workItem.getSubscriptions();
        subscription.add(controlRTC.recupContributorDepuisNom(ano.getCpiProjet()));
        subscription.add(controlRTC.getRepo().loggedInContributor());

        // Contributeurs JAVA
        if (ano.getMatieres().contains(Matiere.JAVA))
        {
            for (String nom : Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESJAVA).split(";"))
            {
                subscription.add(controlRTC.recupContributorDepuisNom(nom));
            }
        }

        // Contribureurs DATASTAGE
        if (ano.getMatieres().contains(Matiere.DATASTAGE))
        {
            for (String nom : Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESDATASTAGE).split(";"))
            {
                subscription.add(controlRTC.recupContributorDepuisNom(nom));
            }
        }

        // Creator
        workItem.setCreator(controlRTC.getRepo().loggedInContributor());

        // Owner
        workItem.setOwner(controlRTC.recupContributorDepuisNom(ano.getCpiProjet()));

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
        if (ano.getSecurite().equals(Statics.SECURITEKO))
            tags.add("sécurité");
        workItem.setTags2(tags);
    }


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
        IEnumeration<? extends ILiteral> enumeration = client.resolveEnumeration(ia, null);
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
        String retour = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEDEFECT).replace("-lot-", String.valueOf(lotAno));
        if (ano.getSecurite().equals(Statics.SECURITEKO))
            retour = retour.replace("Merci", Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTESECURITE));
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
