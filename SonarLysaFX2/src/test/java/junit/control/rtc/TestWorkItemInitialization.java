package junit.control.rtc;

import static com.google.common.truth.Truth.assertThat;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;

import control.rtc.ControlRTC;
import control.rtc.WorkItemInitialization;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.bdd.DefautQualite;
import model.enums.EnumRTC;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeDefautQualite;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestWorkItemInitialization extends JunitBase<WorkItemInitialization>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualite dq;
    private IWorkItemClient client;
    private ControlRTC controlRTC;
    private List<ICategory> categories;
    private IProjectArea projet;
    private WorkItemWorkingCopy workingCopy;
    private IWorkItem workItem;

    /*---------- CONSTRUCTEURS ----------*/

    public TestWorkItemInitialization() throws IllegalArgumentException, IllegalAccessException, TeamRepositoryException
    {
        controlRTC = ControlRTC.getInstance();
        client = (IWorkItemClient) Whitebox.getField(ControlRTC.class, "workItemClient").get(controlRTC);
    }

    @Override
    @BeforeEach
    public void init() throws IllegalAccessException, TeamRepositoryException, JAXBException
    {
        controlRTC.connexionSimple();
        controlRTC.recupTousLesProjets();

        // Initialisation DefautQualite
        initDataBase();
        dq = model.ModelFactory.build(DefautQualite.class);
        dq.setCompo(databaseXML.getMapCompos().get("Composant RESS_InternationalActivities"));
        dq.getCompo().setSecurityRating("E");
        dq.setLotRTC(databaseXML.getMapLots().get("421362"));
        dq.setTypeDefaut(TypeDefautQualite.SONAR);

        // Initialisation client, projet, itemType et categories
        @SuppressWarnings("unchecked")
        Map<String, IProjectArea> pareas = (Map<String, IProjectArea>) Whitebox.getField(ControlRTC.class, "pareas").get(controlRTC);
        projet = pareas.get(dq.getLotRTC().getProjetRTC());
        IWorkItemType itemType = client.findWorkItemType(projet, "defect", null);
        categories = client.findCategories(projet, ICategory.FULL_PROFILE, null);

        objetTest = new WorkItemInitialization(itemType, categories.get(0), projet, dq);

        // Création workItemCopy
        workItem = controlRTC.recupWorkItemDepuisId(327574);
        client.getWorkItemWorkingCopyManager().connect(workItem, IWorkItem.FULL_PROFILE, null);
        workingCopy = client.getWorkItemWorkingCopyManager().getWorkingCopy(workItem);
    }

    @AfterEach
    public void end()
    {
        client.getWorkItemWorkingCopyManager().revert(workItem);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Disabled
    public void testExecute1(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation données anomalie
        dq.getLotRTC().setProjetRTC("PRJF_T300703");

        testExecute(dq);
    }

    @Test
    @Disabled
    public void testExecute2(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation données anomalie
        dq.getCompo().setMatiere(Matiere.DATASTAGE);
        dq.getCompo().setSecurityRating("A");

        testExecute(dq);
    }

    @Test
    public void testCalculEditionRTC(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "E32")).isEqualTo("E32");
        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "CHC_CDM2018-S34")).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC));
        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "CHC_2018-S34")).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC));
        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "CDM_2018-S34")).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC));
        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "E31_Fil_De_Leau")).isEqualTo("E31.0.FDL");
        assertThat((String) invokeMethod(objetTest, "calculEditionRTC", "E30.1_Fil_De_Leau")).isEqualTo("E30.1.FDL");
    }

    @Test
    public void testCalculPariteEdition(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test parite selon les editions
        assertThat((boolean) invokeMethod(objetTest, "calculPariteEdition", "E32")).isTrue();
        assertThat((boolean) invokeMethod(objetTest, "calculPariteEdition", "E31")).isFalse();
        assertThat((boolean) invokeMethod(objetTest, "calculPariteEdition", "E30")).isTrue();
        assertThat((boolean) invokeMethod(objetTest, "calculPariteEdition", "E29")).isFalse();
        assertThat((boolean) invokeMethod(objetTest, "calculPariteEdition", "E31.0.FDL")).isFalse();
    }

    @Test
    public void testCreerTags(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        IWorkItem workItem = workingCopy.getWorkItem();
        invokeMethod(objetTest, "creerTags", workItem);

        // Contrôle des tags avec composant securite
        assertThat(workItem.getTags2().get(0)).isEqualTo("lot=421362");
        assertThat(workItem.getTags2().get(1)).isEqualTo("securite");
        assertThat(workItem.getTags2()).hasSize(2);

        // Contrôle sans securite
        dq.getCompo().setSecurityRating("A");
        invokeMethod(objetTest, "creerTags", workItem);

        // Contrôle des tags avec composant non securite
        assertThat(workItem.getTags2().get(0)).isEqualTo("lot=421362");
        assertThat(workItem.getTags2()).hasSize(3);
    }

    @Test
    public void testCreerDescription(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test defaut sonar avec securite
        dq.getCompo().setSecurityRating("F");
        dq.setTypeDefaut(TypeDefautQualite.SONAR);
        String texte = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANO).replace("-lot-", dq.getLotRTC().getNumero()).replace("xxxxx", dq.getCompo().getNom())
                .replace("key", dq.getCompo().getKey()).replace("-serveur-", Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS))
                + Statics.NL + Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOSECURITE);

        String desc = invokeMethod(objetTest, "creerDescription", dq);
        assertThat(desc).isEqualTo(texte);

        // Test defaut sonar sans securite
        dq.getCompo().setSecurityRating("A");
        texte = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANO).replace("-lot-", dq.getLotRTC().getNumero()).replace("xxxxx", dq.getCompo().getNom())
                .replace("key", dq.getCompo().getKey()).replace("-serveur-", Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS));

        desc = invokeMethod(objetTest, "creerDescription", dq);
        assertThat(desc).isEqualTo(texte);

        // Test defaut mixte
        dq.setTypeDefaut(TypeDefautQualite.MIXTE);
        texte = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANO).replace("-lot-", dq.getLotRTC().getNumero()).replace("xxxxx", dq.getCompo().getNom())
                .replace("key", dq.getCompo().getKey()).replace("-serveur-", Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS))
                + Statics.NL + Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEANOAPPLI).replace("xxxxx", dq.getCompo().getNom());

        desc = invokeMethod(objetTest, "creerDescription", dq);
        assertThat(desc).isEqualTo(texte);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void testAttribut(IProjectArea projet, EnumRTC type, IWorkItem workItemTest, String valeurTest) throws TeamRepositoryException
    {
        IAttribute attribut = client.findAttribute(projet, type.getValeur(), null);
        assertThat(workItemTest.getValue(attribut)).isEqualTo(controlRTC.recupLiteralDepuisString(valeurTest, attribut));
    }

    private void testExecute(DefautQualite dq) throws Exception
    {

        // Initialisation objetTest ave données non nulles et appel methode
        invokeMethod(objetTest, "execute", workingCopy, null);

        // Test des données - Récupération du wirkItem modifie
        IWorkItem workItemTest = workingCopy.getWorkItem();

        // Categorie
        assertThat(controlRTC.recupItemDepuisHandle(ICategory.class, workItemTest.getCategory()).getName()).isEqualTo(controlRTC.recupItemDepuisHandle(ICategory.class, categories.get(0)).getName());

        // Titre
        assertThat(workingCopy.getWorkItem().getHTMLSummary()).isEqualTo(XMLString.createFromPlainText(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEPANORECAP)));

        // Details
        assertThat(workingCopy.getWorkItem().getHTMLDescription().toString().split("\\.")[0].trim())
                .isEqualTo("Bonjour,<br/>L'analyse SonarQube de ce jour du lot projet 382963 fait apparaitre un quality Gate non conforme au niveau du composant Composant COBMF_Test_E 14");

        // Environnement
        if ("E32".equals(dq.getCompo().getVersion()))
            testAttribut(projet, EnumRTC.ENVIRONNEMENT, workItemTest, "Br A VMOE");
        else
            testAttribut(projet, EnumRTC.ENVIRONNEMENT, workItemTest, "Br B VMOE");

        // Importance
        testAttribut(projet, EnumRTC.IMPORTANCE, workItemTest, "Bloquante");

        // Origine
        testAttribut(projet, EnumRTC.ORIGINE, workItemTest, "Qualimetrie");

        // Nature
        testAttribut(projet, EnumRTC.NATURE, workItemTest, "Qualite");

        // Entite responsable
        testAttribut(projet, EnumRTC.ENTITERESPCORRECTION, workItemTest, "MOE");

        // Subscriptions
        assertThat(workItemTest.getSubscriptions().contains(controlRTC.recupContributorDepuisNom(dq.getLotRTC().getCpiProjet().getNom()))).isTrue();
        assertThat(workItemTest.getSubscriptions().contains(controlRTC.recupContributorDepuisNom("MATHON Gregoire"))).isTrue();

        // Creator
        assertThat(controlRTC.recupContributorDepuisNom("MATHON Gregoire")).isEqualTo(workItemTest.getCreator());

        // Owner
        assertThat(controlRTC.recupContributorDepuisNom(dq.getLotRTC().getCpiProjet().getNom())).isEqualTo(workItemTest.getOwner());

        // Tags
        assertThat(workItemTest.getTags2().contains("lot=" + dq.getLotRTC().getNumero())).isTrue();

        if (dq.getCompo().isSecurite())
            assertThat(workItemTest.getTags2().contains("securite")).isTrue();
        else
            assertThat(workItemTest.getTags2()).hasSize(1);
    }

    /*---------- ACCESSEURS ----------*/

}
