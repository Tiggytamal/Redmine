package junit.control.rtc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.internal.TeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.mchange.util.AssertException;

import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import junit.JunitBase;
import junit.TestUtils;
import model.Anomalie;
import model.ModelFactory;
import model.enums.Param;
import model.enums.TypeColSuivi;
import model.enums.TypeEnumRTC;
import utilities.Statics;

public class TestControlRTC extends JunitBase
{
    private ControlRTC handler;

    public TestControlRTC() throws IllegalAccessException
    {
        handler = ControlRTC.INSTANCE;
        handler.connexion();
    }

    @Test(expected = AssertException.class)
    public void createReflexion() throws InstantiationException, IllegalAccessException
    {
        // Contrôle que l'on ne peut pas instancier un deuxième controleur par réflexion
        try
        {
            Whitebox.getConstructor(ControlRTC.class).newInstance();
        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof AssertException)
            throw (AssertException) e.getCause();
        }
    }

    @Test
    public void connexionFalse() throws IllegalAccessException, TeamRepositoryException
    {
        // Test d'un retour à false de la connexion suite à une exception de l'appel à login.
        // Création mock puis remplacement repo du handler.
        TeamRepository repo = Mockito.mock(TeamRepository.class);
        Whitebox.getField(ControlRTC.class, "repo").set(handler, repo);
        PowerMockito.doThrow(new TeamRepositoryException("")).when(repo).login(Mockito.any(IProgressMonitor.class));

        // Appel méthode
        assertFalse(handler.connexion());
        Whitebox.getField(ControlRTC.class, "repo").set(handler, TeamPlatform.getTeamRepositoryService().getTeamRepository(Statics.proprietesXML.getMapParams().get(Param.URLRTC)));
        handler.connexion();
    }

    @Test
    public void recupProjetRTCDepuisWiLot() throws TeamRepositoryException
    {
        // Intialisation
        Logger logger = TestUtils.getMockLogger("logger");

        // Test avec lot absent de RTC
        assertEquals("", handler.recupProjetRTCDepuisWiLot(10));
        Mockito.verify(logger, Mockito.times(1)).warn(Mockito.anyString());

        // Test lot 278180
        assertEquals("PRJF_BFMKRT_MDP_Ma Carte", handler.recupProjetRTCDepuisWiLot(278180));

        // Test lot 279136
        assertEquals("PRJF_BF0319_CCP_Access Banking", handler.recupProjetRTCDepuisWiLot(279136));
    }

    @Test
    public void recupEtatElement() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(298846);
        if (item != null)
            assertEquals("Abandonnée", handler.recupEtatElement(item).trim());
        item = handler.recupWorkItemDepuisId(288850);
        if (item != null)
            assertEquals("Close", handler.recupEtatElement(item).trim());

        assertTrue(handler.recupEtatElement(null).isEmpty());
    }

    @Test
    public void recupWorkItemDepuisId() throws TeamRepositoryException
    {
        // Test sur la récupération des objets depuis RTC
        assertNull(handler.recupWorkItemDepuisId(10));

        // Test sur des lots
        IWorkItem item = handler.recupWorkItemDepuisId(278180);
        assertNotNull(item);
        assertEquals("fr.ca.cat.wi.lotprojet", item.getWorkItemType());
        item = handler.recupWorkItemDepuisId(279136);
        assertNotNull(item);
        assertEquals("fr.ca.cat.wi.lotprojet", item.getWorkItemType());

        // Test sur des anomalies
        item = handler.recupWorkItemDepuisId(298846);
        assertNotNull(item);
        assertEquals("defect", item.getWorkItemType());
        item = handler.recupWorkItemDepuisId(288850);
        assertNotNull(item);
        assertEquals("defect", item.getWorkItemType());
    }
    
    @Test
    public void test() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(301159);
        handler.recupDatesEtatsLot(item);
    }

    @Test
    public void recupererTousLesProjets() throws Exception
    {
        Whitebox.invokeMethod(handler, "recupererTousLesProjets");

        @SuppressWarnings("unchecked")
        Map<String, IProjectArea> pareas = (Map<String, IProjectArea>) Whitebox.getField(ControlRTC.class, "pareas").get(handler);
        assertNotNull(pareas);
        assertFalse(pareas.isEmpty());
    }

    @Test
    public void creerDefect()
    {
        String projetTest = "PRJF_T300703";
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setCpiProjet("MATHON Gregoire");
        ano.setProjetRTC(projetTest);
        ano.setLot("Lot 315765");
        ano.setEdition("E32_Fil_De_Leau");
        ano.setSecurite(Statics.SECURITEKO);
//        handler.creerDefect(ano);
    }

    @Test
    public void recupererValeurAttribut() throws TeamRepositoryException
    {
        // Test sur la récupération des valeurs des attributs
        // Intialisation d'un item.
        IWorkItem item = handler.recupWorkItemDepuisId(307396);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        List<IAttribute> attrbs = new ArrayList<>();

        // Iération sur la liste des attriuts pour révupérer les valeurs
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = handler.recupererEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            attrbs.add(attrb);
            if (attrb.getAttributeType().equals(TypeEnumRTC.IMPORTANCE.toString()))
                assertEquals("Bloquante", handler.recupererValeurAttribut(attrb, item));
            else if (attrb.getAttributeType().equals(TypeEnumRTC.ORIGINE.toString()))
                assertEquals("Qualimétrie", handler.recupererValeurAttribut(attrb, item));
        }
    }

    @Test
    public void recupContributorDepuisNom() throws TeamRepositoryException, IOException
    {
        // Appel du service pour récupérer le fichier de suivi.
        ControlSuivi control = ExcelFactory.getControlleur(TypeColSuivi.class, new File(getClass().getResource(Statics.RESOURCESTEST + "Suivi_Quality_GateTest.xlsx").getFile()));
        List<Anomalie> liste = control.recupDonneesDepuisExcel();

        // Itération sur le fichier de suivi pour vérifier que l'on remonte bien tous les contributeurs des anomalies
        for (Anomalie ano : liste)
        {
            if (ano.getCpiProjet() != null && !ano.getCpiProjet().isEmpty())
                assertNotNull(handler.recupContributorDepuisNom(ano.getCpiProjet()));
        }

        // Test sur les retour null
        assertNull(handler.recupContributorDepuisNom(""));
        assertNull(handler.recupContributorDepuisNom(null));
    }

    @Test
    public void testAttibutsItem() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(322706);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = handler.recupererEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            if (attrb.getIdentifier().equals(TypeEnumRTC.ORIGINE.toString()))
            {
                handler.recupererValeurAttribut(attrb, item);
            }
        }
    }

    @Test
    public void shutdown()
    {
        // Véfie que la plateforme est bien fermée
        handler.shutdown();
        assertTrue(!TeamPlatform.isStarted());
        TeamPlatform.startup();
    }
}