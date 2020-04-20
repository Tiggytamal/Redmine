package junit.control.rtc;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;

import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.internal.TeamRepository;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;

import control.rtc.ControlRTC;
import dao.DaoComposantBase;
import dao.DaoFactory;
import dao.DaoLotRTC;
import junit.AutoDisplayName;
import junit.JunitBase;
import junit.TestUtils;
import model.EmptyTaskForTest;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EnumRTC;
import model.enums.EtatLot;
import model.enums.TypeDefautQualite;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestControlRTC extends JunitBase<ControlRTC>
{
    /*---------- ATTRIBUTS ----------*/

    private Logger logger;
    private Logger logPlantage;
    private IWorkItemClient client;
    private Map<String, IProjectArea> pareas;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlRTC() throws IllegalAccessException, TeamRepositoryException
    {
        objetTest = ControlRTC.build();

        // Mock des loggers pour vérifier les appels à ceux-ci
        logger = TestUtils.getMockLogger(ControlRTC.class, "LOGGER");
        logPlantage = TestUtils.getMockLogger(ControlRTC.class, "LOGPLANTAGE");
    }

    @Override
    @BeforeEach
    @SuppressWarnings("unchecked")
    public void init() throws Exception
    {
        initDataBase();
        objetTest.connexionSimple();
        objetTest.recupTousLesProjets();
        client = (IWorkItemClient) getField("workItemClient");
        pareas = (Map<String, IProjectArea>) getField("pareas");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreateReflexion(TestInfo testInfo) throws InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Contrôle que l'on ne peut pas instancier un deuxieme contrôleur par reflexion
        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> Whitebox.getConstructor(ControlRTC.class).newInstance());
        assertThat(e.getCause()).isNotNull();
        assertThat(e.getCause()).isInstanceOf(AssertionError.class);
        assertThat(e.getCause().getMessage()).isEqualTo("control.rtc.ControlRTC - Singleton, instanciation interdite!");
    }

    @Test
    public void testConnexion_KO(TestInfo testInfo) throws IllegalAccessException, TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test d'un retour à false de la connexion suite à une exception de l'appel à login.
        // Création mock puis remplacement repo du handler.
        TeamRepository mock = mock(TeamRepository.class);
        ITeamRepository repo = (ITeamRepository) getField("repo");
        setField("repo", mock);

        PowerMockito.doThrow(new TeamRepositoryException(EMPTY)).when(mock).login(any(IProgressMonitor.class));

        // Appel methode
        assertThat(objetTest.connexionSimple()).isFalse();
        verify(logPlantage, times(1)).error(Mockito.eq(EMPTY), any(TeamRepositoryException.class));

        // Changement repo pour appel retour vrai
        setField("repo", repo);
    }
    
    @Test
    public void testConnexion_OK(TestInfo testInfo) throws IllegalAccessException, TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.connexionSimple()).isTrue();
    }

    @Test
    public void testShutdown(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Vefie que la plateforme est bien fermée
        objetTest.shutdown();
        assertThat(TeamPlatform.isStarted()).isFalse();
        TeamPlatform.startup();
    }

    @Test
    public void testNomRecupProjetRTCDepuisWiLot(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test lot 278180
        assertThat(objetTest.recupNomProjetRTCDepuisWiLot(278180)).isEqualTo("PRJF_BFMKRT_MDP_Ma Carte");

        // Test lot 279136
        assertThat(objetTest.recupNomProjetRTCDepuisWiLot(279136)).isEqualTo("PRJF_BF0319_CCP_Access Banking");
    }
    
    @Test
    public void testNomRecupProjetRTCDepuisWiLot_Lot_absent(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec lot absent de RTC
        assertThat(objetTest.recupNomProjetRTCDepuisWiLot(10)).isEmpty();
        verify(logger, times(1)).warn(Mockito.anyString());
    }

    @Test
    public void testRecupLiteralDepuisString(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException, TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation des paramètres pour appel methode
        IProjectArea projet = pareas.get("PRJF_BF0319_CCP_Access Banking");
        IAttribute attribut = client.findAttribute(projet, EnumRTC.ENVIRONNEMENT.getValeur(), null);

        // Test methode
        Identifier<ILiteral> literal = objetTest.recupLiteralDepuisString("Br A VMOE", attribut);
        assertThat(literal.getStringIdentifier()).isEqualTo("fr.ca.cat.attribut.enumeration.environnement.brAvmoe");
    }

    @Test
    public void testRecupEtatElement_Abandonne(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test projet abandonne
        IWorkItem item = objetTest.recupWorkItemDepuisId(298846);
        assertThat(item).isNotNull();
        assertThat(objetTest.recupEtatElement(item).trim()).isEqualTo("Abandonnée");
    }
    
    @Test
    public void testRecupEtatElement_Ferme(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test projet ferme
        IWorkItem item = objetTest.recupWorkItemDepuisId(288850);
        assertThat(item).isNotNull();
        assertThat(objetTest.recupEtatElement(item).trim()).isEqualTo(Statics.ANOCLOSE);
    }
    
    @Test
    public void testRecupEtatElement_Null(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test protection null et vide
        assertThat(objetTest.recupEtatElement(null)).isEmpty();
    }

    @Test
    public void testRecupWorkItemDepuisId_Objet_inexistant(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet inexistant
        assertThat(objetTest.recupWorkItemDepuisId(10)).isNull();
    }
    
    @Test
    public void testRecupWorkItemDepuisId_Lots(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sur des anomalies
        IWorkItem item = objetTest.recupWorkItemDepuisId(298846);
        assertThat(item).isNotNull();
        assertThat(item.getWorkItemType()).isEqualTo("defect");
        item = objetTest.recupWorkItemDepuisId(288850);
        assertThat(item).isNotNull();
        assertThat(item.getWorkItemType()).isEqualTo("defect");
    }
    
    @Test
    public void testRecupWorkItemDepuisId_Anomalies(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sur des anomalies
        IWorkItem item = objetTest.recupWorkItemDepuisId(298846);
        assertThat(item).isNotNull();
        assertThat(item.getWorkItemType()).isEqualTo("defect");
        item = objetTest.recupWorkItemDepuisId(288850);
        assertThat(item).isNotNull();
        assertThat(item.getWorkItemType()).isEqualTo("defect");
    }

    @Test
    @Disabled("Impossible de supprimer en production")
    public void testSupprimerWorkItemDepuisId(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test suppression anomalie
        int code = objetTest.supprimerWorkItemDepuisId(404697);

        // Contrôle
        assertThat(code).isAnyOf(IStatus.OK, IStatus.WARNING);
        assertThat(objetTest.recupWorkItemDepuisId(404697)).isNull();
    }

    @Test
    public void testRecupValeurAttribut(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sur la récupération des valeurs des attributs
        // Intialisation d'un item.
        IWorkItem item = objetTest.recupWorkItemDepuisId(307396);
        List<IAttributeHandle> liste = item.getCustomAttributes();

        // Ieration sur la liste des attributs pour récupérer les valeurs
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = objetTest.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            if (attrb.getIdentifier().equals(EnumRTC.IMPORTANCE.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("Bloquante");
            else if (attrb.getIdentifier().equals(EnumRTC.ORIGINE.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("Qualimétrie");
            else if (attrb.getIdentifier().equals(EnumRTC.ENTITERESPCORRECTION.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("MOE");
            else if (attrb.getIdentifier().equals(EnumRTC.ENVIRONNEMENT.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("Br B VMOE");
            else if (attrb.getIdentifier().equals(EnumRTC.CODECLARITY.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("Br B VMOE");
            else if (attrb.getIdentifier().equals(EnumRTC.TROUVEDANS.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEmpty();
            else if (attrb.getIdentifier().equals(EnumRTC.EDITION.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEqualTo("E31");
            else if (attrb.getIdentifier().equals(EnumRTC.EDITIONSI.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEmpty();
            else if (attrb.getIdentifier().equals(EnumRTC.EDITIONSICIBLE.getValeur()))
                assertThat(objetTest.recupValeurAttribut(attrb, item)).isEmpty();
        }
    }

    @Test
    public void testRecupNomContributorConnecte(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String nom = objetTest.recupNomContributorConnecte();
        assertThat(nom).isEqualTo("MATHON Gregoire");
    }

    @Test
    public void testRecupContributorDepuisNom(TestInfo testInfo) throws TeamRepositoryException, IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Itération sur le fichier de suivi pour vérifier que l'on remonte bien tous les contributeurs des anomalies
        for (LotRTC lot : databaseXML.getLotsRTC())
        {
            Utilisateur cpi = lot.getCpiProjet();
            if (cpi != null && !"INCONNU".equals(cpi.getNom()))
            {
                IContributor contrib = objetTest.recupContributorDepuisNom(cpi.getNom());
                assertThat(contrib).isNotNull();
                assertThat(contrib.getName()).isNotNull();
                assertThat(contrib.getName()).isNotEmpty();
            }
        }
    }
    
    @Test
    public void testRecupContributorDepuisNom_Null(TestInfo testInfo) throws TeamRepositoryException, IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test protection null
        assertThat(objetTest.recupContributorDepuisNom(null)).isNull();
    }

    @Test
    public void testRecupContributorDepuisId_Null(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // protection null
        assertThat(objetTest.recupContributorDepuisId(null)).isNull();
        assertThat(objetTest.recupContributorDepuisId(EMPTY)).isNull();
    }
    
    @Test
    public void testRecupContributorDepuisId(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test utilisateur existant
        IContributor contrib = objetTest.recupContributorDepuisId("ETP8137");
        assertThat(contrib).isNotNull();
        assertThat(contrib.getName()).isEqualTo("MATHON Gregoire");
    }

    @Test
    public void testRecupLotsRTC(TestInfo testInfo) throws TeamRepositoryException, IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock dao - récupération numéros de lots depuis base XML
        DaoComposantBase mock = mock(DaoComposantBase.class);
        List<String> listeLots = new ArrayList<>();
        for (ComposantBase compo : databaseXML.getCompos())
        {
            if (compo.getLotRTC() != null)
                listeLots.add(compo.getLotRTC().getNumero());
        }
        when(mock.recupLotsAvecComposants()).thenReturn(listeLots);
        when(mock.persist(any(ComposantBase.class))).thenReturn(true);
        setField("daoCompo", mock);

        // Mock dao - récupération des lots RTC depuis la base XML
        DaoLotRTC mock2 = mock(DaoLotRTC.class);
        List<LotRTC> lots = new ArrayList<>();
        for (LotRTC lot : databaseXML.getLotsRTC())
        {
            lots.add(lot);
        }
        when(mock2.readAll()).thenReturn(lots);
        when(mock2.persist(any(LotRTC.class))).thenReturn(true);
        setField("daoLotRTC", mock2);

        // Appel methode avec date
        List<LotRTC> liste = objetTest.recupLotsRTC(LocalDate.of(2018, 10, 6), new EmptyTaskForTest(true));

        // Contrôle données de la liste
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        for (LotRTC lot : liste)
        {
            assertThat(lot.getNumero()).isNotNull();
            assertThat(lot.getNumero()).isNotEmpty();
            assertThat(lot.getDateMajEtat()).isNotNull();
        }

        // Nettoyage mock
        setField("daoCompo", DaoFactory.getMySQLDao(ComposantBase.class));
        setField("daoLotRTC", DaoFactory.getMySQLDao(LotRTC.class));
    }
    
    @Test
    public void testRecupLotsRTC_Sans_date(TestInfo testInfo) throws TeamRepositoryException, IllegalAccessException, TimeoutException
    {
        // Appel methode sans date
        assertThrows(TechnicalException.class, () -> objetTest.recupLotsRTC(null, new EmptyTaskForTest(true)));
    }

    @Test
    public void testMiseAJourLotsIncomplets(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock dao - récupération des lots RTC depuis la base XML qui ont une edition null
        DaoLotRTC mock = mock(DaoLotRTC.class);
        when(mock.readAll()).thenReturn(databaseXML.getLotsRTC());
        when(mock.persist(any(LotRTC.class))).thenReturn(true);
        setField("daoLotRTC", mock);

        // Appel methode
        List<LotRTC> liste = Whitebox.invokeMethod(objetTest, "miseAJourLotsIncomplets", new EmptyTaskForTest(true));
        assertWithMessage("liste retour nulle").that(liste).isNotNull();
        assertThat(liste).hasSize(13);
        for (LotRTC lot : liste)
        {
            assertWithMessage("edition null, lot : " + lot.getNumero()).that(lot.getEditionString()).isNotNull();
            assertWithMessage("libelle vide, lot : " + lot.getNumero()).that(lot.getLibelle()).isNotEmpty();
            assertWithMessage("projet clarity vide, lot : " + lot.getNumero()).that(lot.getProjetClarityString()).isNotNull();
        }

        // Nettoyage mock
        setField("daoLotRTC", DaoFactory.getMySQLDao(LotRTC.class));
    }

    @Test
    public void testRecupDatesEtatsLot(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel dates pour le lot 309138
        Map<EtatLot, LocalDate> dates = objetTest.recupDatesEtatsLot(objetTest.recupWorkItemDepuisId(309138));

        // test de chaque date avec celles trouvees dans RTC
        assertThat(dates.get(EtatLot.NOUVEAU)).isEqualTo(LocalDate.of(2018, 2, 20));
        assertThat(dates.get(EtatLot.DEVTU)).isEqualTo(LocalDate.of(2018, 3, 9));
        assertThat(dates.get(EtatLot.VMOE)).isEqualTo(LocalDate.of(2018, 5, 16));
        assertThat(dates.get(EtatLot.MOA)).isEqualTo(LocalDate.of(2018, 6, 6));
        assertThat(dates.get(EtatLot.VMOA)).isEqualTo(LocalDate.of(2018, 6, 6));
        assertThat(dates.get(EtatLot.EDITION)).isEqualTo(LocalDate.of(2018, 6, 21));
    }

    @Test
    public void testRecupTousLesProjets(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        Whitebox.invokeMethod(objetTest, "recupTousLesProjets");
        assertThat(pareas).isNotNull();
        assertThat(pareas).isNotEmpty();
    }

    @Test
    public void testRecupListeTypeWorkItem(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        List<IWorkItemType> liste = objetTest.recupListeTypeWorkItem("PRJM_FE000018_GSI PLA Titres");

        // Contrôle liste
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();

        // Verification logger appele deux fois par objet de la liste
        verify(logger, times(liste.size() * 2)).info(Mockito.anyString());
    }

    @Test
    @Disabled(value = TESTMANUEL)
    public void testRecupListeCustomAttributes(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        List<IAttributeHandle> liste = objetTest.recupListeCustomAttributes(356839);

        // Contrôle liste
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();

        // Verification logger appele deux fois par objet de la liste
        verify(logger, times(liste.size() * 2)).info(Mockito.anyString());

        IWorkItem wi = objetTest.recupWorkItemDepuisId(428708);
        for (IAttributeHandle handle : objetTest.recupListeCustomAttributes(428708))
        {
            IAttribute attr = objetTest.recupItemDepuisHandle(IAttribute.class, handle);
            System.out.println("Nom : " + attr.getDisplayName());
            System.out.println("Id : " + attr.getIdentifier());
            System.out.println("Valeur : " + objetTest.recupValeurAttribut(attr, wi));
            System.out.println("----");
        }
    }

    @Test
    @Disabled(value = "Impossible de supprimer une ano en prod")
    public void testCreerAnoRTC(TestInfo testInfo) throws TeamRepositoryException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Permet de tester creerAnoRTC - contrôleAnoRTC et supprimerAnoRTC

        // 1. Création defaut qualite
        DefautQualite dq = model.ModelFactory.build(DefautQualite.class);
        dq.setCompo(databaseXML.getMapCompos().get("Composant RESS_InternationalActivities"));
        dq.setLotRTC(databaseXML.getMapLots().get("382963"));
        dq.setTypeDefaut(TypeDefautQualite.SONAR);

        // 2. Test creerAnoRTC

        // Test avec projet inconnu
        String projetRTC = dq.getLotRTC().getProjetRTC();
        dq.getLotRTC().setProjetRTC(EMPTY);
        assertThat(objetTest.creerAnoRTC(dq)).isEqualTo(0);

        dq.getLotRTC().setProjetRTC(projetRTC);

        // Appel methode
        int anomalie = objetTest.creerAnoRTC(dq);
        System.out.println(anomalie);
        verify(logger, times(1)).info("Creation anomalie RTC numero : " + anomalie + " pour 382963");

        // 3. Test contrôleAnoRTC

        // Test sans valeur dans le defaut
        objetTest.controleAnoRTC(dq);
        assertThat(dq.getEtatAnoRTC()).isEmpty();
        assertThat(dq.getDateCreation()).isNull();
        assertThat(dq.getDateReso()).isNull();

        // Test avec numero anomalie referencee
        dq.setNumeroAnoRTC(anomalie);
        objetTest.controleAnoRTC(dq);
        assertThat(dq.getDateCreation()).isEqualTo(today);
        assertThat(dq.getDateReso()).isNull();
        assertThat(dq.getEtatAnoRTC()).isEqualTo("Nouvelle");

        // Test Exception - ajout mock temporaire pour envoyer l'exception
        IWorkItemClient workItemClient = (IWorkItemClient) getField("workItemClient");
        IWorkItemClient mock = mock(IWorkItemClient.class);
        when(mock.findWorkItemById(Mockito.eq(anomalie), Mockito.eq(IWorkItem.FULL_PROFILE), any(NullProgressMonitor.class))).thenThrow(new TeamRepositoryException(EMPTY));
        setField("workItemClient", mock);

        // Appel methode
        objetTest.controleAnoRTC(dq);

        // Contrôle et remise objet non mock
        verify(logger, times(1)).error(EMPTY, "Erreur récupération information Defect. Lot : 382963");
        verify(logPlantage, times(1)).error(ArgumentMatchers.eq(EMPTY), any(TeamRepositoryException.class));
        setField("workItemClient", workItemClient);

        // 4. Test relancerAno

        // Nombre commentaire
        IWorkItem ano = objetTest.recupWorkItemDepuisId(anomalie);
        int sizeComm = ano.getComments().getContents().length;

        // Appel methode
        assertThat(objetTest.relancerAno(anomalie)).isNotNull();

        // Contrôle création relance
        assertThat(objetTest.recupWorkItemDepuisId(anomalie).getComments().getContents().length).isEqualTo(sizeComm + 1);

        // 5. Test fermer anomalie
        assertThat(objetTest.fermerAnoRTC(anomalie)).isTrue();

        // nouveau test contrôler pour avoir la date de resolution
        objetTest.controleAnoRTC(dq);
        assertThat(dq.getDateReso()).isNotNull();

        // 6. Test rouvrir anomalie

        // Test avec workflow introuvable
        IWorkItemClient spy = Mockito.spy(workItemClient);
        setField("workItemClient", spy);
        Mockito.doReturn(null).when(spy).findWorkflowInfo(any(IWorkItem.class), any(NullProgressMonitor.class));
        assertThat(objetTest.rouvrirAnoRTC(dq)).isFalse();
        verify(logPlantage, times(1)).error("control.rtc.ControlRTC.reouvrirAnoRTC - impossible de trouver le workflow de l'anomalie : " + anomalie);
        setField("workItemClient", workItemClient);

        // Test ouverture OK
        assertThat(objetTest.rouvrirAnoRTC(dq)).isTrue();

        // 7. Nettoyage - Test suppression
        assertThat(objetTest.supprimerWorkItemDepuisId(anomalie)).isEqualTo(IStatus.OK);
        System.out.println(anomalie);

        // 8. Appel methode avec exception

        // Mock pour créer l'exception
        IAuditableClient mock2 = Mockito.mock(IAuditableClient.class);
        IAuditableClient auditableClient = (IAuditableClient) getField("auditableClient");
        when(mock2.fetchCurrentAuditable(any(IWorkItemHandle.class), Mockito.eq(WorkItem.FULL_PROFILE), any(NullProgressMonitor.class))).thenThrow(new TeamRepositoryException(EMPTY));
        setField("auditableClient", mock2);

        // Test methode
        assertThat(objetTest.creerAnoRTC(dq)).isEqualTo(0);
        verify(logger, times(1)).error("Erreur traitement RTC création de Defect. Lot : 382963");
        verify(logPlantage, times(2)).error(any(TeamRepositoryException.class));

        // Nettoyage mock
        setField("auditableClient", auditableClient);
    }

    @Test
    public void testTestSiAnoRTCClose(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        objetTest = PowerMockito.spy(objetTest);
        PowerMockito.when(objetTest.recupEtatElement(any(IWorkItem.class))).thenReturn(Statics.ANOCLOSE).thenReturn("Abandonnée").thenReturn(EMPTY);

        // Test avec anomalie à zero
        assertThat(objetTest.testSiAnoRTCClose(0)).isTrue();

        // Test anomalie close
        assertThat(objetTest.testSiAnoRTCClose(335943)).isTrue();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));

        // Test anomalie abandonnée
        assertThat(objetTest.testSiAnoRTCClose(335943)).isTrue();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));

        // Test autre retour
        assertThat(objetTest.testSiAnoRTCClose(335943)).isFalse();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));

        // Test exception
        assertThat(objetTest.testSiAnoRTCClose(241392)).isFalse();
        verify(logPlantage, times(1)).error(Mockito.eq(EMPTY), any(TeamRepositoryException.class));
    }
    
    @Test
    public void testTestSiAnoRTCClose_Sans_anomalie(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec anomalie à zero
        assertThat(objetTest.testSiAnoRTCClose(0)).isTrue();
    }
    
    @Test
    public void testTestSiAnoRTCClose_Anomalie_close(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        objetTest = PowerMockito.spy(objetTest);
        PowerMockito.when(objetTest.recupEtatElement(any(IWorkItem.class))).thenReturn(Statics.ANOCLOSE);
        
        // Test anomalie close
        assertThat(objetTest.testSiAnoRTCClose(335943)).isTrue();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));
    }
    
    @Test
    public void testTestSiAnoRTCClose_Anomalie_abandonnee(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        objetTest = PowerMockito.spy(objetTest);
        PowerMockito.when(objetTest.recupEtatElement(any(IWorkItem.class))).thenReturn("Abandonnée");
        
        // Test anomalie abandonnée
        assertThat(objetTest.testSiAnoRTCClose(335943)).isTrue();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));
    }
    
    @Test
    public void testTestSiAnoRTCClose_Anomalie_non_fermee(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation. On mock le recupererEtatElement qui renvoit les 3 valeurs possibles
        objetTest = PowerMockito.spy(objetTest);
        PowerMockito.when(objetTest.recupEtatElement(any(IWorkItem.class))).thenReturn(EMPTY);
        
        // Test autre retour
        assertThat(objetTest.testSiAnoRTCClose(335943)).isFalse();
        verify(logPlantage, Mockito.never()).error(any(TeamRepositoryException.class));
    }
    
    @Test
    public void testTestSiAnoRTCClose_Exception(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        // Test exception
        assertThat(objetTest.testSiAnoRTCClose(241392)).isFalse();
        verify(logPlantage, times(1)).error(Mockito.eq(EMPTY), any(TeamRepositoryException.class));
    }

    @Test
    public void testCreerLotSuiviRTCDepuisItem(TestInfo testInfo) throws TeamRepositoryException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération lot RTC
        IWorkItem wi = objetTest.recupWorkItemDepuisId(310979);

        // Appel methode
        LotRTC lot = objetTest.creerLotSuiviRTCDepuisItem(wi);

        assertThat(lot).isNotNull();
        assertThat(lot.getEditionString()).isNotNull();
        assertThat(lot.getProjetClarityString()).isNotNull();
    }

    @Test
    public void testGetRepo(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException, Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((ITeamRepository) Whitebox.invokeMethod(objetTest, "getRepo")).isEqualTo(Whitebox.getField(ControlRTC.class, "repo").get(objetTest));
    }

    @Test
    public void testGetClient(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException, Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((IWorkItemClient) Whitebox.invokeMethod(objetTest, "getClient")).isEqualTo(Whitebox.getField(ControlRTC.class, "workItemClient").get(objetTest));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
