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
import junit.JunitBase;
import junit.TestUtils;
import model.bdd.LotRTC;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.TypeEnumRTC;
import utilities.Statics;
import utilities.TechnicalException;

public class TestControlRTC extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlRTC handler;

    /*---------- CONSTRUCTEURS ----------*/
    public TestControlRTC() throws IllegalAccessException
    {
        handler = ControlRTC.INSTANCE;
        handler.connexion();
    }

    @Override
    public void init() throws Exception
    {
        // Pas d'initialisation n�cessaire

    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = AssertException.class)
    public void testCreateReflexion() throws InstantiationException, IllegalAccessException
    {
        // Contr�le que l'on ne peut pas instancier un deuxi�me controleur par r�flexion
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
        // Test d'un retour � false de la connexion suite � une exception de l'appel � login.
        // Cr�ation mock puis remplacement repo du handler.
        TeamRepository repo = Mockito.mock(TeamRepository.class);
        Whitebox.getField(ControlRTC.class, "repo").set(handler, repo);
        PowerMockito.doThrow(new TeamRepositoryException(EMPTY)).when(repo).login(Mockito.any(IProgressMonitor.class));

        // Appel m�thode
        assertFalse(handler.connexion());
        Whitebox.getField(ControlRTC.class, "repo").set(handler, TeamPlatform.getTeamRepositoryService().getTeamRepository(Statics.proprietesXML.getMapParams().get(Param.URLRTC)));
        handler.connexion();
    }

    @Test
    public void testRecupProjetRTCDepuisWiLot() throws TeamRepositoryException
    {
        // Intialisation
        Logger logger = TestUtils.getMockLogger(ControlRTC.class, "LOGGER");

        // Test avec lot absent de RTC
        assertEquals(EMPTY, handler.recupProjetRTCDepuisWiLot(10));
        Mockito.verify(logger, Mockito.times(1)).warn(Mockito.anyString());

        // Test lot 278180
        assertEquals("PRJF_BFMKRT_MDP_Ma Carte", handler.recupProjetRTCDepuisWiLot(278180));

        // Test lot 279136
        assertEquals("PRJF_BF0319_CCP_Access Banking", handler.recupProjetRTCDepuisWiLot(279136));
    }

    @Test
    public void testRecupEtatElement() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(298846);
        if (item != null)
            assertEquals("Abandonn�e", handler.recupEtatElement(item).trim());
        item = handler.recupWorkItemDepuisId(288850);
        if (item != null)
            assertEquals("Close", handler.recupEtatElement(item).trim());

        assertEquals(EMPTY, handler.recupEtatElement(null));
    }

    @Test
    public void testRecupWorkItemDepuisId() throws TeamRepositoryException
    {
        // Test sur la r�cup�ration des objets depuis RTC
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
//    @Ignore("Suppression WorkItem - A utiliser en test manuel")
    public void testSupprimerWorkItemDepuisId() throws TeamRepositoryException
    {
        handler.supprimerWorkItemDepuisId(326691);
        assertNull(handler.recupWorkItemDepuisId(326691));
    }

    @Test
    public void testRecupererTousLesProjets() throws Exception
    {
        Whitebox.invokeMethod(handler, "recupererTousLesProjets");

        @SuppressWarnings("unchecked")
        Map<String, IProjectArea> pareas = (Map<String, IProjectArea>) Whitebox.getField(ControlRTC.class, "pareas").get(handler);
        assertNotNull(pareas);
        assertFalse(pareas.isEmpty());
    }

    @Test
    public void testCreerDefect() throws TeamRepositoryException
    {
        // String projetTest = "PRJF_T300703";
        // Anomalie ano = ModelFactory.getModel(Anomalie.class);
        // ano.setCpiProjet("MATHON Gregoire");
        // ano.setProjetRTC(projetTest);
        // ano.setLot("Lot 315765");
        // ano.setEdition("E32_Fil_De_Leau");
        // ano.setSecurite(Statics.X);
        // int numero = handler.creerDefect(ano);
        //
        // // Test que le defect a bien �t� cr��
        // assertTrue(numero > 0);
        //
        // // Suppression defect
        // assertEquals(IStatus.OK, handler.supprimerWorkItemDepuisId(numero).getCode());
        //
        // // Test de la suppression
        // assertNull(handler.recupWorkItemDepuisId(numero));
    }

    @Test
    public void testRecupererValeurAttribut() throws TeamRepositoryException
    {
        // Test sur la r�cup�ration des valeurs des attributs
        // Intialisation d'un item.
        IWorkItem item = handler.recupWorkItemDepuisId(307396);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        List<IAttribute> attrbs = new ArrayList<>();

        // I�ration sur la liste des attriuts pour r�vup�rer les valeurs
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = handler.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            attrbs.add(attrb);
            if (attrb.getAttributeType().equals(TypeEnumRTC.IMPORTANCE.toString()))
                assertEquals("Bloquante", handler.recupValeurAttribut(attrb, item));
            else if (attrb.getAttributeType().equals(TypeEnumRTC.ORIGINE.toString()))
                assertEquals("Qualim�trie", handler.recupValeurAttribut(attrb, item));
        }
    }

    @Test
    public void testRecupNomContributorConnecte()
    {
        String nom = handler.recupNomContributorConnecte();
        assertEquals("MATHON Gregoire", nom);
    }

    @Test
    public void testRecupContributorDepuisNom() throws TeamRepositoryException, IOException
    {
        // // Appel du service pour r�cup�rer le fichier de suivi.
        // ControlSuivi control = ExcelFactory.getReader(TypeColSuivi.class, new File(getClass().getResource(Statics.ROOT + "Suivi_Quality_GateTest.xlsx").getFile()));
        // List<Anomalie> liste = control.recupDonneesDepuisExcel();
        //
        // // It�ration sur le fichier de suivi pour v�rifier que l'on remonte bien tous les contributeurs des anomalies
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
        assertNull(handler.recupContributorDepuisId(null));
        assertNull(handler.recupContributorDepuisId("000001"));
    }

    @Test
    public void testRecupLotsRTC() throws TeamRepositoryException
    {
        List<LotRTC> liste = handler.recupLotsRTC(LocalDate.of(2016, 01, 01), new MajLotsRTCTask());
        assertNotNull(liste);
        assertFalse(liste.isEmpty());

        List<LotRTC> liste2 = handler.recupLotsRTC(null, new MajLotsRTCTask());
        assertNotNull(liste2);
        assertFalse(liste2.isEmpty());
    }

    @Test(expected = TechnicalException.class)
    public void testRecupLotsRTCException() throws TeamRepositoryException
    {
        handler.recupLotsRTC(null, new MajLotsRTCTask());
    }

    @Test
    public void testCreerLotSuiviRTCDepuisHandle() throws TeamRepositoryException
    {
        List<LotRTC> liste = handler.recupLotsRTC(LocalDate.of(2016, 01, 01), new MajLotsRTCTask());
        LotRTC lot = liste.get(0);
        assertNotNull(lot.getEdition());
        assertFalse(lot.getEdition().isEmpty());
        assertNotNull(lot.getProjetClarity());
        assertFalse(lot.getProjetClarity().getCode().isEmpty());
    }

    @Test
    public void testRecupDatesEtatsLot() throws TeamRepositoryException
    {
        // Appel dates pour le lot 309138
        Map<EtatLot, LocalDate> dates = handler.recupDatesEtatsLot(handler.recupWorkItemDepuisId(309138));

        // test de chaque date avec celles trouv�es dans RTC
        assertEquals(LocalDate.of(2018, 2, 20), dates.get(EtatLot.NOUVEAU));
        assertEquals(LocalDate.of(2018, 3, 9), dates.get(EtatLot.DEVTU));
        assertEquals(LocalDate.of(2018, 5, 16), dates.get(EtatLot.VMOE));
        assertEquals(LocalDate.of(2018, 6, 6), dates.get(EtatLot.MOA));
        assertEquals(LocalDate.of(2018, 6, 6), dates.get(EtatLot.VMOA));
        assertEquals(LocalDate.of(2018, 6, 21), dates.get(EtatLot.EDITION));
    }

    @Test
    public void testTestSiAnomalieClose() throws TeamRepositoryException
    {
        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        handler = PowerMockito.spy(handler);
        PowerMockito.when(handler.recupEtatElement(Mockito.any(IWorkItem.class))).thenReturn("Close").thenReturn("Abandonn�e").thenReturn(EMPTY);
        Logger logMock = TestUtils.getMockLogger(ControlRTC.class, "LOGPLANTAGE");

        // Test de la m�thode

        // Test avec anomalie � z�ro
        assertTrue(handler.testSiAnomalieClose(0));

        // test close et abandonn�e
        assertTrue(handler.testSiAnomalieClose(335943));
        assertTrue(handler.testSiAnomalieClose(335943));
        Mockito.verify(logMock, Mockito.never()).error(Mockito.any(TeamRepositoryException.class));

        // test autre retour
        assertFalse(handler.testSiAnomalieClose(335943));
        Mockito.verify(logMock, Mockito.never()).error(Mockito.any(TeamRepositoryException.class));

        // Test exception
        assertFalse(handler.testSiAnomalieClose(241392));
        Mockito.verify(logMock, Mockito.times(1)).error(Mockito.any(TeamRepositoryException.class));
    }

    @Test
    public void testAttibutsItem() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(322706);
        List<IAttributeHandle> liste = item.getCustomAttributes();
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = handler.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            if (attrb.getIdentifier().equals(TypeEnumRTC.ORIGINE.toString()))
            {
                handler.recupValeurAttribut(attrb, item);
            }
        }
    }

    @Test
    public void testShutdown()
    {
        // V�fie que la plateforme est bien ferm�e
        handler.shutdown();
        assertTrue(!TeamPlatform.isStarted());
        TeamPlatform.startup();
    }
    
    @Test
    public void testRecupListeTypeWorkItem() throws TeamRepositoryException
    {
        handler.recupListeTypeWorkItem("PRJM_FE000018_GSI PLA Titres");
    }
    
    @Test
    public void testRecupListeCustomAttributes() throws TeamRepositoryException
    {
        handler.recupListeCustomAttributes(356839);
    }
    
    @Test
    public void testFermerAnoRTC() throws TeamRepositoryException
    {
        handler.fermerAnoRTC(356839);
    }
    
    @Test
    public void testRelancerAno() throws TeamRepositoryException
    {
        handler.relancerAno(356839);
    }

    @Test
    public void testGetRepo() throws IllegalArgumentException, IllegalAccessException, Exception
    {
        assertEquals(Whitebox.getField(ControlRTC.class, "repo").get(handler), Whitebox.invokeMethod(handler, "getRepo"));
    }

    @Test
    public void testGetClient() throws IllegalArgumentException, IllegalAccessException, Exception
    {
        assertEquals(Whitebox.getField(ControlRTC.class, "workItemClient").get(handler), Whitebox.invokeMethod(handler, "getClient"));
    }
    
    @Test
    public void testTest() throws TeamRepositoryException
    {
        IWorkItem wi = handler.recupWorkItemDepuisId(288016);
        System.out.println(wi.getWorkItemType());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
