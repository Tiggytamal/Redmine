package junit.control.rtc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
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
import junit.JunitBase;
import model.Anomalie;
import model.ModelFactory;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeEnumRTC;
import utilities.Statics;

public class TestWorkItemInitialization extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private WorkItemInitialization handler;
    private Anomalie ano;
    private IWorkItemClient client;
    private ControlRTC controlRTC;
    private NullProgressMonitor monitor;
    private final static String NUMEROLOT1 = "Lot 123456";

    /*---------- CONSTRUCTEURS ----------*/

    public TestWorkItemInitialization() throws IllegalArgumentException, IllegalAccessException
    {
        controlRTC = ControlRTC.INSTANCE;
        controlRTC.connexion();
        client = (IWorkItemClient) Whitebox.getField(ControlRTC.class, "workItemClient").get(controlRTC);
        monitor = new NullProgressMonitor();
    }

    @Override
    public void init()
    {
        ano = ModelFactory.getModel(Anomalie.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testExecute1() throws Exception
    {
        // Initialisation données anomalie
        ano.setProjetRTC("PRJF_T300703");
        ano.setLot(NUMEROLOT1);
        ano.setCpiProjet("TRICOT Nicolas");
        ano.setSecurite(Statics.X);
        ano.setMatieresString(Matiere.JAVA.getValeur());

        testExecute(ano);
    }

    @Test
    public void testExecute2() throws Exception
    {
        // Initialisation données anomalie
        ano.setProjetRTC("PRJF_T300703");
        ano.setLot("Lot 654321");
        ano.setCpiProjet("TRICOT Nicolas");
        ano.setVersion("E32");
        ano.setMatieresString(Matiere.DATASTAGE.getValeur());
        ano.setSecurite(Statics.EMPTY);

        testExecute(ano);
    }

    @Test
    public void testCalculEditionRTC() throws Exception
    {
        ano.setLot(NUMEROLOT1);
        handler = new WorkItemInitialization(null, null, null, ano);
        assertEquals("E32", Whitebox.invokeMethod(handler, "calculEditionRTC", "E32"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CHC_CDM2018-S34"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CHC_2018-S34"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CDM_2018-S34"));
        assertEquals("E31.0.FDL", Whitebox.invokeMethod(handler, "calculEditionRTC", "E31_Fil_De_Leau"));
        assertEquals("E30.1.FDL", Whitebox.invokeMethod(handler, "calculEditionRTC", "E30.1_Fil_De_Leau"));

    }

    @Test
    public void testCalculPariteEdition() throws Exception
    {
        ano.setLot(NUMEROLOT1);
        handler = new WorkItemInitialization(null, null, null, ano);
        assertTrue(Whitebox.invokeMethod(handler, "calculPariteEdition", "E32"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E31"));
        assertTrue(Whitebox.invokeMethod(handler, "calculPariteEdition", "E30"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E29"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E31.0.FDL"));
    }

    /*---------- METHODES PRIVEES ----------*/

    private void testAttribut(IProjectArea projet, TypeEnumRTC type, IWorkItem workItemTest, String valeurTest) throws TeamRepositoryException
    {
        IAttribute attribut = client.findAttribute(projet, type.getValeur(), null);
        assertEquals(controlRTC.recupLiteralDepuisString(valeurTest, attribut), workItemTest.getValue(attribut));
    }

    private void testExecute(Anomalie ano) throws Exception
    {
        // Initialisation client, projet, itemType et categories
        @SuppressWarnings("unchecked")
        Map<String, IProjectArea> pareas = (Map<String, IProjectArea>) Whitebox.getField(ControlRTC.class, "pareas").get(controlRTC);
        IProjectArea projet = pareas.get(ano.getProjetRTC());
        IWorkItemType itemType = client.findWorkItemType(projet, "defect", monitor);
        List<ICategory> categories = client.findCategories(projet, ICategory.FULL_PROFILE, monitor);

        // Création workItemCopy - depuis defect 328660 - defect de test projet PRJF_T300703
        IWorkItem workItem = controlRTC.recupWorkItemDepuisId(327574);
        client.getWorkItemWorkingCopyManager().connect(workItem, IWorkItem.FULL_PROFILE, monitor);
        WorkItemWorkingCopy workingCopy = client.getWorkItemWorkingCopyManager().getWorkingCopy(workItem);

        // Initialisation handler ave données non nulles et appel méthode
        handler = new WorkItemInitialization(itemType, categories.get(0), projet, ano);
        Whitebox.invokeMethod(handler, "execute", workingCopy, monitor);

        // Test des données - Récupération du wirkItem modifié
        IWorkItem workItemTest = workingCopy.getWorkItem();

        // Catégorie
        assertEquals(controlRTC.recupererItemDepuisHandle(ICategory.class, categories.get(0)).getName(), controlRTC.recupererItemDepuisHandle(ICategory.class, workItemTest.getCategory()).getName());

        // Titre
        assertEquals(XMLString.createFromPlainText(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.RECAPDEFECT)), workingCopy.getWorkItem().getHTMLSummary());

        // Détails
        assertTrue(
                workingCopy.getWorkItem().getHTMLDescription().toString().contains("Bonjour,<br/>L'analyse SonarQube de ce jour du lot projet " + ano.getLot().substring(Statics.SBTRINGLOT) + " fait apparaitre un quality Gate non conforme."));

        // Environnement
        if ("E32".equals(ano.getVersion()))
            testAttribut(projet, TypeEnumRTC.ENVIRONNEMENT, workItemTest, "Br A VMOE");
        else
            testAttribut(projet, TypeEnumRTC.ENVIRONNEMENT, workItemTest, "Br B VMOE");

        // Importance
        testAttribut(projet, TypeEnumRTC.IMPORTANCE, workItemTest, "Bloquante");

        // Origine
        testAttribut(projet, TypeEnumRTC.ORIGINE, workItemTest, "Qualimétrie");

        // Nature
        testAttribut(projet, TypeEnumRTC.NATURE, workItemTest, "Qualité");

        // Entité responsable
        testAttribut(projet, TypeEnumRTC.ENTITERESPCORRECTION, workItemTest, "MOE");

        // Subscriptions
        assertTrue(workItemTest.getSubscriptions().contains(controlRTC.recupContributorDepuisNom(ano.getCpiProjet())));
        assertTrue(workItemTest.getSubscriptions().contains(controlRTC.recupContributorDepuisNom("MATHON Gregoire")));

        // Creator
        assertEquals(workItemTest.getCreator(), controlRTC.recupContributorDepuisNom("MATHON Gregoire"));

        // Owner
        assertEquals(workItemTest.getOwner(), controlRTC.recupContributorDepuisNom(ano.getCpiProjet()));

        // Tags
        assertTrue(workItemTest.getTags2().contains("lot=" + ano.getLot().substring(Statics.SBTRINGLOT)));
        
        if (!ano.getSecurite().isEmpty())
            assertTrue(workItemTest.getTags2().contains("sécurité"));
        else
            assertEquals(3, workItemTest.getTags2().size());   
    }

    /*---------- ACCESSEURS ----------*/

}
