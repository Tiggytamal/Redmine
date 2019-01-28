package junit.control.rtc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
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

import control.rtc.ControlRTC;
import control.task.MajLotsRTCTask;
import dao.DaoDefautQualite;
import dao.DaoFactory;
import junit.JunitBase;
import junit.TestUtils;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.EtatAnoRTC;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.TypeDefaut;
import model.enums.TypeEnumRTC;
import utilities.Statics;
import utilities.TechnicalException;

public class TestControlRTC extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlRTC controlTest;

    /*---------- CONSTRUCTEURS ----------*/
    public TestControlRTC() throws IllegalAccessException
    {
        controlTest = ControlRTC.INSTANCE;
        controlTest.connexion();
    }

    @Override
    public void init() throws Exception
    {
        // Pas d'initialisation nécessaire

    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = AssertException.class)
    public void testCreateReflexion() throws InstantiationException, IllegalAccessException
    {
        // Contrôle que l'on ne peut pas instancier un deuxième controleur par réflexion
        try
        {
            Whitebox.getConstructor(ControlRTC.class).newInstance();
        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof AssertException)
                throw (AssertException) e.getCause();
        }
    }

    @Test
    public void testConnexionFalse() throws IllegalAccessException, TeamRepositoryException
    {
        // Test d'un retour à false de la connexion suite à une exception de l'appel à login.
        // Création mock puis remplacement repo du handler.
        TeamRepository repo = Mockito.mock(TeamRepository.class);
        Whitebox.getField(ControlRTC.class, "repo").set(controlTest, repo);
        PowerMockito.doThrow(new TeamRepositoryException(EMPTY)).when(repo).login(Mockito.any(IProgressMonitor.class));

        // Appel méthode
        assertFalse(controlTest.connexion());
        Whitebox.getField(ControlRTC.class, "repo").set(controlTest, TeamPlatform.getTeamRepositoryService().getTeamRepository(Statics.proprietesXML.getMapParams().get(Param.URLRTC)));
        controlTest.connexion();
    }

    @Test
    public void testRecupProjetRTCDepuisWiLot() throws TeamRepositoryException
    {
        // Intialisation
        Logger logger = TestUtils.getMockLogger(ControlRTC.class, "LOGGER");

        // Test avec lot absent de RTC
        assertEquals(EMPTY, controlTest.recupProjetRTCDepuisWiLot(10));
        Mockito.verify(logger, Mockito.times(1)).warn(Mockito.anyString());

        // Test lot 278180
        assertEquals("PRJF_BFMKRT_MDP_Ma Carte", controlTest.recupProjetRTCDepuisWiLot(278180));

        // Test lot 279136
        assertEquals("PRJF_BF0319_CCP_Access Banking", controlTest.recupProjetRTCDepuisWiLot(279136));
    }

    @Test
    public void testRecupEtatElement() throws TeamRepositoryException
    {
        IWorkItem item = controlTest.recupWorkItemDepuisId(298846);
        if (item != null)
            assertEquals("Abandonnée", controlTest.recupEtatElement(item).trim());
        item = controlTest.recupWorkItemDepuisId(288850);
        if (item != null)
            assertEquals("Close", controlTest.recupEtatElement(item).trim());

        assertEquals(EMPTY, controlTest.recupEtatElement(null));
    }

    @Test
    public void testRecupWorkItemDepuisId() throws TeamRepositoryException
    {
        // Test sur la récupération des objets depuis RTC
        assertNull(controlTest.recupWorkItemDepuisId(10));

        // Test sur des lots
        IWorkItem item = controlTest.recupWorkItemDepuisId(278180);
        assertNotNull(item);
        assertEquals("fr.ca.cat.wi.lotprojet", item.getWorkItemType());
        item = controlTest.recupWorkItemDepuisId(279136);
        assertNotNull(item);
        assertEquals("fr.ca.cat.wi.lotprojet", item.getWorkItemType());

        // Test sur des anomalies
        item = controlTest.recupWorkItemDepuisId(298846);
        assertNotNull(item);
        assertEquals("defect", item.getWorkItemType());
        item = controlTest.recupWorkItemDepuisId(288850);
        assertNotNull(item);
        assertEquals("defect", item.getWorkItemType());
    }

    @Test
    // @Ignore("Suppression WorkItem - A utiliser en test manuel")
    public void testSupprimerWorkItemDepuisId() throws TeamRepositoryException
    {
        controlTest.supprimerWorkItemDepuisId(326691);
        assertNull(controlTest.recupWorkItemDepuisId(326691));
    }

    @Test
    public void testRecupererTousLesProjets() throws Exception
    {
        Whitebox.invokeMethod(controlTest, "recupererTousLesProjets");

        @SuppressWarnings("unchecked")
        Map<String, IProjectArea> pareas = (Map<String, IProjectArea>) Whitebox.getField(ControlRTC.class, "pareas").get(controlTest);
        assertNotNull(pareas);
        assertFalse(pareas.isEmpty());
    }

    @Test
    public void testCreerAnoRTC() throws TeamRepositoryException
    {
        DefautQualite dq = ModelFactory.build(DefautQualite.class);
        dq.setLotRTC(DaoFactory.getDao(LotRTC.class).recupEltParIndex("310979"));
        dq.setSecurite(true);
        dq.setTypeDefaut(TypeDefaut.APPLI);
        DefautAppli da = ModelFactory.build(DefautAppli.class);
        da.setCompo(ComposantSonar.build("id1", "key1", "Composant 1"));
        DefautAppli da2 = ModelFactory.build(DefautAppli.class);
        da2.setCompo(ComposantSonar.build("id2", "key2", "Composant 2"));
        dq.getDefautsAppli().add(da);
        dq.getDefautsAppli().add(da2);
        int numero = controlTest.creerAnoRTC(dq);

        // Test que le defect a bien été créé
        assertTrue(numero > 0);
        System.out.println(numero);

        // Suppression defect
        // assertEquals(IStatus.OK, controlTest.supprimerWorkItemDepuisId(numero).getCode());

        // Test de la suppression
        // assertNull(controlTest.recupWorkItemDepuisId(numero));
    }

    @Test
    public void testRecupererValeurAttribut() throws TeamRepositoryException
    {
        // Test sur la récupération des valeurs des attributs
        // Intialisation d'un item.
        IWorkItem item = controlTest.recupWorkItemDepuisId(307396);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        List<IAttribute> attrbs = new ArrayList<>();

        // Iération sur la liste des attriuts pour révupérer les valeurs
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = controlTest.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            attrbs.add(attrb);
            if (attrb.getAttributeType().equals(TypeEnumRTC.IMPORTANCE.toString()))
                assertEquals("Bloquante", controlTest.recupValeurAttribut(attrb, item));
            else if (attrb.getAttributeType().equals(TypeEnumRTC.ORIGINE.toString()))
                assertEquals("Qualimétrie", controlTest.recupValeurAttribut(attrb, item));
        }
    }

    @Test
    public void testRecupNomContributorConnecte()
    {
        String nom = controlTest.recupNomContributorConnecte();
        assertEquals("MATHON Gregoire", nom);
    }

    @Test
    public void testRecupContributorDepuisNom() throws TeamRepositoryException, IOException
    {
        // // Appel du service pour récupérer le fichier de suivi.
        // ControlSuivi control = ExcelFactory.getReader(TypeColSuivi.class, new File(getClass().getResource(Statics.ROOT + "Suivi_Quality_GateTest.xlsx").getFile()));
        // List<Anomalie> liste = control.recupDonneesDepuisExcel();
        //
        // // Itération sur le fichier de suivi pour vérifier que l'on remonte bien tous les contributeurs des anomalies
        // for (Anomalie ano : liste)
        // {
        // if (ano.getCpiProjet() != null && !ano.getCpiProjet().isEmpty())
        // assertNotNull(handler.recupContributorDepuisNom(ano.getCpiProjet()));
        // }
        //
        // // Test sur les retour null
        // assertNull(handler.recupContributorDepuisNom(EMPTY));
        // assertNull(handler.recupContributorDepuisNom(null));
    }

    @Test
    public void testRecupContributorDepuisId() throws TeamRepositoryException
    {
        assertNull(controlTest.recupContributorDepuisId(null));
        assertNull(controlTest.recupContributorDepuisId("000001"));
    }

    @Test
    public void testRecupLotsRTC() throws TeamRepositoryException
    {
        List<LotRTC> liste = controlTest.recupLotsRTC(LocalDate.of(2016, 1, 1), new MajLotsRTCTask());
        assertNotNull(liste);
        assertFalse(liste.isEmpty());

        List<LotRTC> liste2 = controlTest.recupLotsRTC(null, new MajLotsRTCTask());
        assertNotNull(liste2);
        assertFalse(liste2.isEmpty());
    }

    @Test(expected = TechnicalException.class)
    public void testRecupLotsRTCException() throws TeamRepositoryException
    {
        controlTest.recupLotsRTC(null, new MajLotsRTCTask());
    }

    @Test
    public void testCreerLotSuiviRTCDepuisHandle() throws TeamRepositoryException
    {
        List<LotRTC> liste = controlTest.recupLotsRTC(LocalDate.of(2016, 1, 1), new MajLotsRTCTask());
        LotRTC lot = liste.get(0);
        assertNotNull(lot.getEdition());
        assertNotNull(lot.getEdition());
        assertNotNull(lot.getProjetClarity());
        assertFalse(lot.getProjetClarity().getCode().isEmpty());
    }

    @Test
    public void testRecupDatesEtatsLot() throws TeamRepositoryException
    {
        // Appel dates pour le lot 309138
        Map<EtatLot, LocalDate> dates = controlTest.recupDatesEtatsLot(controlTest.recupWorkItemDepuisId(309138));

        // test de chaque date avec celles trouvées dans RTC
        assertEquals(LocalDate.of(2018, 2, 20), dates.get(EtatLot.NOUVEAU));
        assertEquals(LocalDate.of(2018, 3, 9), dates.get(EtatLot.DEVTU));
        assertEquals(LocalDate.of(2018, 5, 16), dates.get(EtatLot.VMOE));
        assertEquals(LocalDate.of(2018, 6, 6), dates.get(EtatLot.MOA));
        assertEquals(LocalDate.of(2018, 6, 6), dates.get(EtatLot.VMOA));
        assertEquals(LocalDate.of(2018, 6, 21), dates.get(EtatLot.EDITION));
    }

    @Test
    public void testRecupEtatsAnoRTC()
    {
        DaoDefautQualite dao = DaoFactory.getDao(DefautQualite.class);
        List<DefautQualite> dqs = dao.readAll();
        int size = dqs.size();
        int i = 0;
        int j = 0;
        for (DefautQualite dq : dqs)
        {
            i++;
            System.out.println(i + " - " + size);
            if (dq.getNumeroAnoRTC() == 0)
                continue;

            ControlRTC.INSTANCE.connexion();
            IWorkItem wi = null;
            try
            {
                wi = controlTest.recupWorkItemDepuisId(dq.getNumeroAnoRTC());
            }
            catch (TeamRepositoryException e)
            {
                System.out.println(dq.getNumeroAnoRTC());
                continue;
            }
            Map<EtatAnoRTC, LocalDate> etats;
            try
            {
                etats = ControlRTC.INSTANCE.recupDatesEtatsAnoRTC(wi);
            }
            catch (TeamRepositoryException e)
            {
                System.out.println(wi.getId());
                continue;
            }

            if (etats.containsKey(EtatAnoRTC.REOUVERTE))
            {
                System.out.println(dq.getNumeroAnoRTC());
                dq.setDateReouv(etats.get(EtatAnoRTC.REOUVERTE));
                j++;
            }
        }

        System.out.println(j);
    }

    @Test
    public void testTestSiAnomalieClose() throws TeamRepositoryException
    {
        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        controlTest = PowerMockito.spy(controlTest);
        PowerMockito.when(controlTest.recupEtatElement(Mockito.any(IWorkItem.class))).thenReturn("Close").thenReturn("Abandonnée").thenReturn(EMPTY);
        Logger logMock = TestUtils.getMockLogger(ControlRTC.class, "LOGPLANTAGE");

        // Test de la méthode

        // Test avec anomalie à zéro
        assertTrue(controlTest.testSiAnoRTCClose(0));

        // test close et abandonnée
        assertTrue(controlTest.testSiAnoRTCClose(335943));
        assertTrue(controlTest.testSiAnoRTCClose(335943));
        Mockito.verify(logMock, Mockito.never()).error(Mockito.any(TeamRepositoryException.class));

        // test autre retour
        assertFalse(controlTest.testSiAnoRTCClose(335943));
        Mockito.verify(logMock, Mockito.never()).error(Mockito.any(TeamRepositoryException.class));

        // Test exception
        assertFalse(controlTest.testSiAnoRTCClose(241392));
        Mockito.verify(logMock, Mockito.times(1)).error(Mockito.any(TeamRepositoryException.class));
    }

    @Test
    public void testAttibutsItem() throws TeamRepositoryException
    {
        IWorkItem item = controlTest.recupWorkItemDepuisId(322706);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = controlTest.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            if (attrb.getIdentifier().equals(TypeEnumRTC.ORIGINE.toString()))
            {
                controlTest.recupValeurAttribut(attrb, item);
            }
        }
    }

    @Test
    public void testShutdown()
    {
        // Véfie que la plateforme est bien fermée
        controlTest.shutdown();
        assertTrue(!TeamPlatform.isStarted());
        TeamPlatform.startup();
    }

    @Test
    public void testRecupListeTypeWorkItem() throws TeamRepositoryException
    {
        controlTest.recupListeTypeWorkItem("PRJM_FE000018_GSI PLA Titres");
    }

    @Test
    public void testRecupListeCustomAttributes() throws TeamRepositoryException
    {
        controlTest.recupListeCustomAttributes(356839);
    }

    @Test
    public void testFermerAnoRTC() throws TeamRepositoryException
    {
        controlTest.fermerAnoRTC(356839);
    }

    @Test
    public void testRelancerAno() throws TeamRepositoryException
    {
        controlTest.relancerAno(356839);
    }

    @Test
    public void testGetRepo() throws IllegalArgumentException, IllegalAccessException, Exception
    {
        assertEquals(Whitebox.getField(ControlRTC.class, "repo").get(controlTest), Whitebox.invokeMethod(controlTest, "getRepo"));
    }

    @Test
    public void testGetClient() throws IllegalArgumentException, IllegalAccessException, Exception
    {
        assertEquals(Whitebox.getField(ControlRTC.class, "workItemClient").get(controlTest), Whitebox.invokeMethod(controlTest, "getClient"));
    }

    @Test
    // @Ignore("Méthode pour tests manuels")
    public void testTest() throws TeamRepositoryException
    {

    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
