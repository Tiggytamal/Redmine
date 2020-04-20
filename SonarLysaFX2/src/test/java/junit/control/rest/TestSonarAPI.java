package junit.control.rest;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;

import control.parsing.ControlJSON;
import control.rest.SonarAPI;
import control.task.maj.ExceptionTraiterCompo;
import junit.AutoDisplayName;
import junit.JunitBase;
import junit.TestUtils;
import model.EmptyTaskForTest;
import model.Info;
import model.ModelFactory;
import model.bdd.ComposantBase;
import model.bdd.IssueBase;
import model.bdd.Utilisateur;
import model.enums.InstanceSonar;
import model.enums.Metrique;
import model.enums.OptionGestionErreur;
import model.enums.OptionGetCompos;
import model.enums.Param;
import model.enums.TypeDefautSonar;
import model.enums.TypeObjetSonar;
import model.parsing.ComposantJSON;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.DetailRegle;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Message;
import model.rest.sonarapi.Mesure;
import model.rest.sonarapi.ObjetSonar;
import model.rest.sonarapi.ParamAPI;
import model.rest.sonarapi.QualityGate;
import model.rest.sonarapi.QualityProfile;
import model.rest.sonarapi.Regle;
import model.rest.sonarapi.Retour;
import model.rest.sonarapi.StatutQGProjet;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestSonarAPI extends TestAbstractControlRest<SonarAPI>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String METHODEFORMAT = "format";
    private static final String METHODEFORMATZONED = "formatZoned";
    private static final String METHODEGESTIONERREUR = "gestionErreur";
    private static final String METHODEERREURAPI = "erreurAPI";

    private Logger logPlantageMock;

    /*---------- CONSTRUCTEURS ----------*/

    public TestSonarAPI() throws Exception
    {
        // Mock du logger pour vérifier les appels à celui-ci
        loggerMock = TestUtils.getMockLogger(SonarAPI.class, "LOGGER");
        logPlantageMock = TestUtils.getMockLogger(SonarAPI.class, "LOGPLANTAGE");
    }

    @Override
    @BeforeEach
    public void init()
    {
        objetTest = SonarAPI.build();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild_Standard(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test build normal
        assertThat(((WebTarget) getField("webTarget")).getUri().toString()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
    }

    @Test
    public void testBuild_LEGACY(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test build avec InstanceSonar.LEGACY
        objetTest = SonarAPI.build(InstanceSonar.LEGACY);
        assertThat(((WebTarget) getField("webTarget")).getUri().toString()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
    }

    @Test
    public void testBuild_MOBILECENTER(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test build avec InstanceSonar.MOBILECENTER
        objetTest = SonarAPI.build(InstanceSonar.MOBILECENTER);
        assertThat(((WebTarget) getField("webTarget")).getUri().toString()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONARMC));
    }

    @Test
    public void testBuild_Null(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test build avec InstanceSonar null
        objetTest = SonarAPI.build(null);
        assertThat(((WebTarget) getField("webTarget")).getUri().toString()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
    }

    @Test
    public void testGetComposants(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel de la methode - test avec option minimale pour reduire les temps de test.
        List<ComposantSonar> liste = objetTest.getComposants(OptionGetCompos.MINIMALE, new EmptyTaskForTest(true));

        // Contrôle de la liste
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();

        // Test que l'on a bien que des composants sur branches "master"
        for (ComposantSonar compo : liste)
        {
            assertThat(compo.getKey()).isNotNull();
            assertThat(compo.getKey()).isNotEmpty();
            assertThat(compo.getNom()).isNotNull();
            assertThat(compo.getNom()).isNotEmpty();
            assertThat(compo.getQualityGate()).isNotNull();
            assertThat(compo.getDateAnalyse()).isNotNull();
            assertThat(compo.getBranche()).isEqualTo("master");
        }
    }

    @Test
    public void testGetComposantsSansBranche(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel de la methode
        List<ComposantSonar> liste = objetTest.getComposantsSansBranche(new EmptyTaskForTest(true));

        // Test sans erreur d'API
        verify(loggerMock, never()).error(anyString());

        // Contrôle de la liste
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();

        // Test que l'on a bien que des composants sur branches "master"
        for (ComposantSonar compo : liste)
        {
            assertThat(compo.getKey()).isNotNull();
            assertThat(compo.getKey()).isNotEmpty();
            assertThat(compo.getNom()).isNotNull();
            assertThat(compo.getNom()).isNotEmpty();
            assertThat(compo.getQualityGate()).isNotNull();
            assertThat(compo.getBranche()).isEqualTo("master");
        }
    }

    @Test
    public void testGetNbreComposQGKO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getNbreComposQGKO()).isGreaterThan(-1);
    }

    @Test
    public void testGetComposantsSansBranche_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test retour en erreur de l'API
        webServiceKO();
        List<ComposantSonar> liste = objetTest.getComposantsSansBranche(new EmptyTaskForTest(true));

        // Contrôle liste
        assertThat(liste).isNotNull();
        assertThat(liste).isEmpty();
        verify(loggerMock, times(1)).error(anyString());
    }

    @Test
    public void testGetDetailsComposant(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String branche = "master";
        String key = "fr.ca.cat.webapp:WebApp_CELS_FAS_Build";

        // Test composant connu
        ComposantSonar compo = objetTest.getDetailsComposant(key, branche);

        // Verification pas de logger d'erreur et instanciation informations composant
        assertThat(compo).isNotNull();
        verify(loggerMock, never()).error(anyString());
        assertThat(compo.getType()).isEqualTo(TypeObjetSonar.PROJECT);
        assertThat(compo.getQualityGate()).isNotNull();
        assertThat(compo.getKey()).isEqualTo(key);
        assertThat(compo.getBranche()).isEqualTo(branche);
        assertThat(compo.getDateAnalyse()).isNotNull();
        assertThat(compo.getDateLeakPeriod()).isNotNull();
    }

    @Test
    public void testGetDetailsComposant_Inconnu(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test composant inconnu - objet null et utilisation logger
        assertThat(objetTest.getDetailsComposant(EMPTY, EMPTY)).isNull();
        verify(logPlantageMock, times(1)).error(anyString());
    }

    @Test
    public void testGetMesuresComposant(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String key = "fr.ca.cat.npc:CMS_NPC0_Build";
        String branche = "master";
        String[] metricKeys = new String[]
        { Metrique.BLOQUANT.getValeur(), Metrique.CRITIQUE.getValeur(), Metrique.SECURITY.getValeur(), Metrique.EDITION.getValeur() };

        // Test composant OK
        ComposantSonar compo = objetTest.getMesuresComposant(key, branche, metricKeys);

        // Verification pas de logger et informations du composant
        assertThat(compo).isNotNull();
        assertThat(compo.getType()).isEqualTo(TypeObjetSonar.PROJECT);
        assertThat(compo.getKey()).isEqualTo(key);
        assertThat(compo.getBranche()).isEqualTo(branche);
        assertThat(compo.getQualityGate()).isNotNull();
        List<Mesure> liste = compo.getMesures();
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(4);

        for (Mesure mesure : liste)
        {
            assertThat(mesure.getType()).isAnyOf(Metrique.BLOQUANT, Metrique.CRITIQUE, Metrique.SECURITY, Metrique.EDITION);
            assertThat(mesure.getListePeriodes()).isNotNull();
        }

        // Test composant KO
        assertThat(objetTest.getMesuresComposant(EMPTY, EMPTY, metricKeys)).isNull();
        verify(logPlantageMock, times(1)).error(anyString());
    }

    @Test
    public void testGetMesuresComposant_Composant_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String[] metricKeys = new String[]
        { Metrique.BLOQUANT.getValeur(), Metrique.CRITIQUE.getValeur(), Metrique.SECURITY.getValeur(), Metrique.EDITION.getValeur() };

        // Test composant KO
        assertThat(objetTest.getMesuresComposant(EMPTY, EMPTY, metricKeys)).isNull();
        verify(logPlantageMock, times(1)).error(anyString());
    }

    @Test
    public void testGetVersionComposant(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        initDataBase();
        ComposantBase compo = databaseXML.getCompos().get(0);

        // Test Composant OK
        String version = objetTest.getVersionComposant(compo);
        assertThat(version).isNotNull();
        assertThat(version).isEqualTo(compo.getVersion());
    }

    @Test
    public void testGetVersionComposant_Composant_KO(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        initDataBase();
        Logger logPlantage = TestUtils.getMockLogger(SonarAPI.class, "LOGPLANTAGE");
        ComposantBase compo1 = ComposantBase.build(KEY, NOM);

        // Test composant KO
        assertThat(objetTest.getVersionComposant(compo1)).isEqualTo(EMPTY);
        verify(logPlantage, times(1)).error("Erreur API : api/project_analyses/search - Composant : ");
    }

    @Test
    public void testGetVersionComposant_Analyse_KO(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        initDataBase();
        ComposantBase compo = databaseXML.getCompos().get(0);
        Logger logPlantage = TestUtils.getMockLogger(SonarAPI.class, "LOGPLANTAGE");
        ComposantBase compo1 = ComposantBase.build(KEY, NOM);

        // Test composant avec date de dernière analyse KO
        compo1.setNom(compo.getNom());
        compo1.setKey(compo.getKey());
        compo1.setBranche("master");
        compo1.setDerniereAnalyse(LocalDateTime.now());
        assertThat(objetTest.getVersionComposant(compo1)).isEqualTo(EMPTY);
        verify(logPlantage, times(1))
                .error("control.rest.SonarAPI.getVersionComposant - Impossible de trouver la version à la date de dernière analyse : " + compo1.getNom() + " - " + compo1.getDerniereAnalyse());
    }

    @Test
    public void testGetIssuesGenerique(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        List<ParamAPI> params = prepareGetIssuegenerique();

        // Appel methode
        List<Issue> liste = objetTest.getIssuesGenerique(params);

        // Vontrôle valeurs de retour
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        for (Issue issue : liste)
        {
            assertThat(issue.getType()).isEqualTo(TypeDefautSonar.VULNERABILITY);
            assertThat(issue.getKey()).isNotNull();
        }
    }

    @Test
    public void testGetIssuesGenerique_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        List<ParamAPI> params = prepareGetIssuegenerique();

        // Mock appel webservice pour retourner une erreur
        webServiceKO();
        List<Issue> liste = objetTest.getIssuesGenerique(params);

        // Contrôle de la liste vide et de l'appel au logger.
        assertThat(liste).isNotNull();
        assertThat(liste).isEmpty();
        verify(logPlantageMock, times(1)).error(anyString());
    }

    @Test
    public void testAssignerIssue(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création Issue en utlisation une liste d'issues non assignées
        IssueBase issue = prepareAssignerIssue();

        initDataBase();
        Utilisateur user = databaseXML.getMapUsers().get("ETP8137");
        issue.setUtilisateur(user);

        // Appel méthode
        assertThat(objetTest.assignerIssue(issue)).isTrue();

        // Récupération de l'issue depuis SonarQube
        List<ParamAPI> params = new ArrayList<>();
        params.add(new ParamAPI("issues", issue.getKey()));
        Issue issueTest = objetTest.getIssuesGenerique(params).get(0);

        // Contrôle de l'assignation
        assertThat(issueTest).isNotNull();
        assertThat(issueTest.getAssigne()).isEqualTo("ETP8137");
    }

    @Test
    public void testAssignerIssue_Appel_KO(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création Issue en utlisation une liste d'issues non assignées
        IssueBase issue = prepareAssignerIssue();

        // Mock appel webservice pour retourner une erreur
        webServiceKO();
        assertThat(objetTest.assignerIssue(issue)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings =
    { "Crédit Agricole (SECAPI) Final", "Full Security SECAPI" })
    public void testGetQualityProfileByName(String nomQp, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        QualityProfile qp = objetTest.getQualityProfileByName(nomQp);
        assertThat(qp).isNotNull();
        assertThat(qp.getNom()).isEqualTo(nomQp);
    }

    @Test
    public void testGetQualityProfileByName_Erreur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel KO
        QualityProfile qp = objetTest.getQualityProfileByName("erreur");
        verify(logPlantageMock, times(1)).error(anyString());
        assertThat(qp).isNull();
    }

    @Test
    public void testGetQualityProfileByName_Plantage(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel plante
        webServiceKO();
        QualityProfile qp = objetTest.getQualityProfileByName("erreur");
        verify(logPlantageMock, times(1)).error(anyString());
        assertThat(qp).isNull();
    }

    @Test
    public void testGetRulesByQualityProfile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel OK
        QualityProfile qp = objetTest.getQualityProfileByName("Crédit Agricole (SECAPI) Final");
        List<Regle> liste = objetTest.getRulesByQualityProfile(qp);
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        verify(loggerMock, never()).error(anyString());
    }

    @Test
    public void testGetRulesByQualityProfile_Appel_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel KO
        List<Regle> liste = objetTest.getRulesByQualityProfile("erreur");
        assertThat(liste).isNotNull();
        assertThat(liste).isEmpty();
        verify(logPlantageMock, times(1)).error(anyString());
    }

    @Test
    public void testGetReglesGenerique(TestInfo testInfo)
    {
        // Appel méthode
        List<Regle> regles = prepareEtTestGetReglesGenerique();

        // Contrôles
        assertThat(regles).isNotNull();
        assertThat(regles).isNotEmpty();
        assertThat(regles).hasSize(18);

        for (Regle regle : regles)
        {
            assertThat(regle.getRepo()).isEqualTo("AEM Rules");
            assertThat(regle.getLangage()).isEqualTo("java");
        }
    }

    @Test
    public void testGetReglesGenerique_Appel_KO(TestInfo testInfo)
    {
        // Test appel KO
        webServiceKO();
        List<Regle> regles = prepareEtTestGetReglesGenerique();
        assertThat(regles).isNotNull();
        assertThat(regles).isEmpty();
        verify(loggerMock, times(1)).error(anyString());
    }

    @Test
    public void testGetDetailsRegles(TestInfo testInfo)
    {
        // Appel méthode
        DetailRegle detail = objetTest.getDetailsRegle("squid:S1161");

        // Contrôle données
        assertThat(detail).isNotNull();
        Regle regle = detail.getRegle();
        assertThat(regle).isNotNull();
        assertThat(regle.getRepo()).isEqualTo("squid");
        assertThat(regle.getKey()).isEqualTo("squid:S1161");
        assertThat(regle.getType()).isEqualTo(TypeDefautSonar.CODE_SMELL);
    }

    @Test
    public void testGetDetailsRegles_API_KO(TestInfo testInfo)
    {
        // Test api KO
        assertThat(objetTest.getDetailsRegle("erreur")).isNull();
        verify(loggerMock, times(1)).error(anyString());
    }

    @Test
    public void testGetQualtityGate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode OK
        QualityGate qg = objetTest.getQualityGate("QG JAVA Niveau 1");
        assertThat(qg).isNotNull();
        assertThat(qg.getId()).isNotEmpty();
        assertThat(qg.getNom()).isNotEmpty();;
    }

    @Test
    public void testGetQualtityGate_Appel_API_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel avec erreur API
        webServiceKO();
        FunctionalException e = assertThrows(FunctionalException.class, () -> objetTest.getQualityGate("QG JAVA Niveau 1"));
        String erreur = "Erreur au moment de l'appel de l'API : api/qualitygates/show";
        verify(loggerMock, times(1)).error(Mockito.eq(erreur));
        assertThat(e.getMessage()).isEqualTo(erreur);
    }

    @Test
    public void testGetQualtityGate_Exception()
    {
        // Test exception avec non de Gate inconnu
        FunctionalException e = assertThrows(FunctionalException.class, () -> objetTest.getQualityGate("erreur"));
        assertThat(e.getMessage()).isEqualTo("impossible de trouver la Qualitygate avec le nom donne : erreur");
    }

    @Test
    public void testGetListQualitygate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test methode OK
        List<QualityGate> liste = objetTest.getListQualitygate();

        // Contrôle données
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        for (QualityGate qg : liste)
        {
            assertThat(qg.getId()).isNotEmpty();
            assertThat(qg.getNom()).isNotEmpty();
        }
    }

    @Test
    public void testGetListQualitygate_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test methode KO avec exception
        objetTest = Mockito.spy(objetTest);
        Mockito.doReturn(responseMock).when(objetTest).appelWebserviceGET(anyString());
        FunctionalException e = assertThrows(FunctionalException.class, () -> objetTest.getListQualitygate());
        String erreur = "Erreur au moment de l'appel de l'API : api/qualitygates/list";
        verify(loggerMock, times(1)).error(Mockito.eq(erreur));
        assertThat(e.getMessage()).isEqualTo(erreur);
    }

    @Test
    public void testGetAnalyseQGInfos(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel OK
        StatutQGProjet statut = objetTest.getAnalyseQGInfos("AWnh9PsZ_D4z6XhLuql0");
        assertThat(statut).isNotNull();
        assertThat(statut.getStatus()).isNotEmpty();
    }

    @Test
    public void testGetAnalyseQGInfos_Appel_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test appel KO
        webServiceKO();
        objetTest.getAnalyseQGInfos("AWsioBNUrIfSkw1sdfMj");
        verify(logPlantageMock, times(1)).error("Erreur au moment de retrouver les infos d'un composant depuis l'analyse donnée : AWsioBNUrIfSkw1sdfMj");
    }

    @ParameterizedTest
    @ValueSource(strings =
    { "/AWwY97DG7zaYOiyfice4.json", "/AWxC00xEjS-EPYcwiV7b.json" })
    void testGetDateAnalyse(String fichier, TestInfo testInfo) throws JAXBException, ExceptionTraiterCompo
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation compo
        ComposantJSON compoJSON = new ControlJSON().parsingCompoJSON(new File(JunitBase.class.getResource(fichier).getFile()));

        // Appel méthode et contrôle du retour
        ZonedDateTime time = objetTest.getDateAnalyse(compoJSON);
        assertThat(time).isNotNull();
        assertThat(LocalDateTime.from(time)).isGreaterThan(LocalDateTime.of(2019, 1, 1, 00, 00));
        verify(loggerMock, never()).error(anyString());
    }

    @Test
    void testGetDateAnalyse_Exception_Pas_de_Date(TestInfo testInfo) throws JAXBException, ExceptionTraiterCompo
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création compo de test
        ComposantJSON compoJSON = new ControlJSON().parsingCompoJSON(new File(JunitBase.class.getResource("/AWwgaAlf7zaYOiyfj0dS.json").getFile()));

        // Mock logger
        Logger logPlantage = TestUtils.getMockLogger(SonarAPI.class, "LOGPLANTAGE");

        // Contrôles
        ExceptionTraiterCompo e = assertThrows(ExceptionTraiterCompo.class, () -> objetTest.getDateAnalyse(compoJSON));
        assertThat(e.getMessage()).isEqualTo("Impossible de trouver la date de l'analyse (Analyse obsolète) : " + compoJSON.getProjet().getKey());
        verify(logPlantage, times(1)).error(anyString());
    }

    @Test
    void testGetDateAnalyse_Exception_API(TestInfo testInfo) throws JAXBException, ExceptionTraiterCompo
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création compo de test
        ComposantJSON compoJSON = new ControlJSON().parsingCompoJSON(new File(JunitBase.class.getResource("/AWwgaAlf7zaYOiyfj0dS.json").getFile()));

        // Test appel API KO
        webServiceKO();
        Logger logPlantage = TestUtils.getMockLogger(SonarAPI.class, "LOGPLANTAGE");

        // Contrôles
        ExceptionTraiterCompo e = assertThrows(ExceptionTraiterCompo.class, () -> objetTest.getDateAnalyse(compoJSON));
        assertThat(e.getMessage()).isEqualTo("Impossible de trouver la date de l'analyse (plantage API) : " + compoJSON.getProjet().getKey());
        verify(logPlantage, times(1)).error(anyString());
    }

    @Test
    public void testGetObjetSonarParNomOuType_Appel_par_type(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel avec le type d'objet
        List<ComposantSonar> liste = objetTest.getObjetSonarParNomOuType(null, TypeObjetSonar.PORTFOLIO);

        // Contrôle liste et pas d'erreur
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        verify(loggerMock, never()).error(anyString());
    }

    @Test
    public void testGetObjetSonarParNomOuType_Appel_par_nom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel avec le nom de l'objet
        List<ComposantSonar> liste = objetTest.getObjetSonarParNomOuType("Composant Fwk_EnrichissementPddPourBtp", TypeObjetSonar.PROJECT);

        // Contrôle liste et pas d'erreur
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(1);
        verify(loggerMock, never()).error(anyString());
        ComposantSonar compo = liste.get(0);
        assertThat(compo).isNotNull();
        assertThat(compo.getKey()).isNotNull();
        assertThat(compo.getKey()).isNotEmpty();
        assertThat(compo.getNom()).isNotNull();
        assertThat(compo.getNom()).isNotEmpty();
        assertThat(compo.getType()).isNotNull();
    }

    @Test
    public void testGetObjetSonarParNomOuType_Appel_API_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec appel webservice KO
        webServiceKO();
        List<ComposantSonar> liste = objetTest.getObjetSonarParNomOuType(EMPTY, TypeObjetSonar.PORTFOLIO);

        // Contrôle liste avec erreur
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(0);
        verify(loggerMock, times(1)).error(anyString());
    }

    @Test
    public void testGetObjetSonarParNomOuType_Null(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Contrôle type null
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.getObjetSonarParNomOuType(NOM, null));
        assertThat(e.getMessage()).isEqualTo("control.rest.SonarAPI.getObjetSonarParNomOuType - type null");
    }

    @Test
    public void testAssocierQualitygate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Rcuperation QG
        QualityGate qg = objetTest.getQualityGate("QG JAVA Niveau 1");

        // Appel methode OK
        ComposantBase compo = ComposantBase.build("fr.ca.cat.apimanager.resources:RESS_EligibiliteEI_Build", "Composant RESS_EligibiliteEI");
        assertThat(objetTest.associerQualitygate(compo, qg)).isTrue();
        verify(loggerMock, times(1)).info("projet " + compo.getNom() + " - Association QualityGate Id " + qg.getId() + ": HTTP 204");
    }

    @Test
    public void testAssocierQualitygate_Appel_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Rcuperation QG
        QualityGate qg = objetTest.getQualityGate("QG JAVA Niveau 1");

        // Appel methode KO
        ComposantBase compo = ComposantBase.build("erreur", "erreur");
        assertThat(objetTest.associerQualitygate(compo, qg)).isFalse();
        verify(loggerMock, times(1)).info("projet " + compo.getNom() + " - Association QualityGate Id " + qg.getId() + ": HTTP 404");
    }

    @Test
    public void testCreerObjetSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String key = "APPLITESTKey";
        String nom = "APPLI TEST1";
        String desc = "Description";

        // Suppression initiale pour éviter les faux fail
        objetTest.supprimerObjetSonar(key, TypeObjetSonar.PORTFOLIO, OptionGestionErreur.NON);
        objetTest.supprimerObjetSonar(key, TypeObjetSonar.APPLI, OptionGestionErreur.NON);

        // test contrôle objet
        ObjetSonar objet = new ObjetSonar(key, nom, desc, TypeObjetSonar.PORTFOLIO);
        objet.setKey(EMPTY);
        assertThat(objetTest.creerObjetSonar(objet)).isEqualTo(Status.BAD_REQUEST);

        // Création du portfolio
        objet = new ObjetSonar(key, nom, desc, TypeObjetSonar.PORTFOLIO);

        // Appel API Sonar
        Status status = objetTest.creerObjetSonar(objet);
        assertThat(status).isEqualTo(Status.OK);

        List<ComposantSonar> liste = objetTest.getObjetSonarParNomOuType(nom, TypeObjetSonar.PORTFOLIO);
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(1);
        assertThat(liste.get(0).getKey()).isEqualTo(key);
        assertThat(liste.get(0).getNom()).isEqualTo(nom);

        // Suppression objet de test
        objetTest.supprimerObjetSonar(key, TypeObjetSonar.PORTFOLIO, OptionGestionErreur.NON);

        // Test création appli
        objet = new ObjetSonar(key, nom, desc, TypeObjetSonar.APPLI);

        // Appel API Sonar
        status = objetTest.creerObjetSonar(objet);
        assertThat(status).isEqualTo(Status.OK);

        liste = objetTest.getObjetSonarParNomOuType(nom, TypeObjetSonar.APPLI);
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(1);
        assertThat(liste.get(0).getKey()).isEqualTo(key);
        assertThat(liste.get(0).getNom()).isEqualTo(nom);

        // Suppression objet de test
        objetTest.supprimerObjetSonar(key, TypeObjetSonar.APPLI, OptionGestionErreur.NON);
    }

    @Test
    public void testCreerObjetSonar_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String key = "APPLITESTKey";
        String nom = "APPLI TEST1";
        String desc = "Description";

        // Test remontée exception
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.creerObjetSonar(new ObjetSonar(key, nom, desc, TypeObjetSonar.PROJECT)));
        assertThat(e.getMessage()).isEqualTo("control.rest.SonarAPI.creerObjetSonar - type objet non gere : PROJECT");
    }

    @Test
    public void testCalculObjetSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        ObjetSonar pf = prepareCalculObjetSonar("view_application_ADCQ");

        // Test methode OK
        assertThat(objetTest.calculObjetSonar(pf)).isEqualTo(Status.NO_CONTENT);
        verify(loggerMock, times(1)).info("Calcul objet : " + pf.getKey() + " - nom : " + pf.getNom() + ": HTTP 204");
    }

    @Test
    public void testCalculObjetSonar_Retour_HTTP_404(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        ObjetSonar pf = prepareCalculObjetSonar("erreur");

        // Test methode KO
        assertThat(objetTest.calculObjetSonar(pf)).isEqualTo(Status.NOT_FOUND);
        verify(loggerMock, times(1)).info("Calcul objet : " + pf.getKey() + " - nom : " + pf.getNom() + ": HTTP 404");
    }

    @Test
    public void testCalculObjetSonar_Retour_BAD_REQUEST(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        ObjetSonar pf = prepareCalculObjetSonar("erreur");
        pf.setKey(EMPTY);

        // Test portfolio KO
        assertThat(objetTest.calculObjetSonar(pf)).isEqualTo(Status.BAD_REQUEST);
    }

    @Test
    public void testSupprimerObjetSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test contrôle Portfolio
        ObjetSonar pf = new ObjetSonar("erreur", "erreur", "", TypeObjetSonar.APPLI);
        pf.setKey(EMPTY);
        assertThat(objetTest.supprimerObjetSonar(pf, OptionGestionErreur.OUI)).isEqualTo(Status.BAD_REQUEST);
    }

    @Test
    public void testSupprimerObjetSonar_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test exception
        assertThrows(TechnicalException.class, () -> objetTest.supprimerObjetSonar(new ObjetSonar("erreur", "erreur", "", TypeObjetSonar.DIRECTORY), OptionGestionErreur.OUI));
    }

    @Test
    public void testAjouterSousProjet(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // création objets initiaux
        ObjetSonar pf = new ObjetSonar("keyPortfolio", "name", Statics.EMPTY, TypeObjetSonar.PORTFOLIO);
        objetTest.supprimerObjetSonar(pf, OptionGestionErreur.NON);
        objetTest.creerObjetSonar(pf);

        ObjetSonar appli = new ObjetSonar("keyAppli", "name", Statics.EMPTY, TypeObjetSonar.APPLI);
        objetTest.supprimerObjetSonar(appli, OptionGestionErreur.NON);
        objetTest.creerObjetSonar(appli);

        String key = "fr.ca.cat.soa.dao:DAO_Accounts_Build";

        // Appel méthode et contrôle
        try
        {
            assertThat(objetTest.ajouterSousProjet(pf, key)).isEqualTo(Status.NO_CONTENT);
            assertThat(objetTest.ajouterSousProjet(appli, key)).isEqualTo(Status.NO_CONTENT);
        }
        finally
        {
            // Nettoyage
            objetTest.supprimerObjetSonar(pf, OptionGestionErreur.NON);
            objetTest.supprimerObjetSonar(appli, OptionGestionErreur.NON);
        }
    }

    @Test
    public void testAjouterSousProjet_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String key = "fr.ca.cat.soa.dao:DAO_Accounts_Build:13";

        // Appel exception
        ObjetSonar exception = new ObjetSonar("keyPortfolio", "name", Statics.EMPTY, TypeObjetSonar.PROJECT);
        assertThrows(TechnicalException.class, () -> objetTest.ajouterSousProjet(exception, key));
    }

    @Test
    public void testAjouterSousPortfolio(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // création objets initiaux
        ObjetSonar pf = new ObjetSonar("keyPortfolio", "name", Statics.EMPTY, TypeObjetSonar.PORTFOLIO);
        objetTest.supprimerObjetSonar(pf, OptionGestionErreur.NON);
        objetTest.creerObjetSonar(pf);
        ObjetSonar pf2 = new ObjetSonar("keyPortfolio2", "name", Statics.EMPTY, TypeObjetSonar.PORTFOLIO);
        objetTest.supprimerObjetSonar(pf2, OptionGestionErreur.NON);
        objetTest.creerObjetSonar(pf2);

        // Appel méthode et contrôle
        assertThat(objetTest.ajouterSousPortfolio(pf, pf2)).isEqualTo(Status.OK);

        // Nettoyage
        objetTest.supprimerObjetSonar(pf, OptionGestionErreur.NON);
        objetTest.supprimerObjetSonar(pf2, OptionGestionErreur.NON);
    }

    @Test
    public void testGetVuesParNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        List<ComposantSonar> liste = objetTest.getObjetSonarParNomOuType("Branche", TypeObjetSonar.PROJECT);
        assertThat(liste).isNotNull();
    }

    @Test
    public void testConnexionUtilisateur_OK(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test utilisateur OK
        assertThat(objetTest.connexionUtilisateur()).isTrue();
    }

    @Test
    public void testConnexionUtilisateur_KO(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test utilisateur KO
        Info save = Statics.info;
        Info info = ModelFactory.build(Info.class);
        info.setMotDePasse("error");
        info.setPseudo("error");
        Whitebox.setInternalState(Statics.class, info);
        assertThat(objetTest.connexionUtilisateur()).isFalse();

        // Remise des infos OK
        Whitebox.setInternalState(Statics.class, save);
    }

    @Test
    public void testFormat_Avec_nanosecondes(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test format avec nanosecondes
        assertThat((LocalDateTime) invokeMethod(objetTest, METHODEFORMAT, "2018-01-01T10:10:10+0200")).isEqualTo(LocalDateTime.of(2018, 1, 1, 10, 10, 10));
    }

    @Test
    public void testFormat_Sans_nanosecondes(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test format sans nanosecondes
        assertThat((LocalDateTime) invokeMethod(objetTest, METHODEFORMAT, "2018-02-02T12:13:14")).isEqualTo(LocalDateTime.of(2018, 2, 2, 12, 13, 14));
    }

    @Test
    public void testFormat_Exception(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test retour exception
        assertThrows(TechnicalException.class, () -> invokeMethod(objetTest, METHODEFORMAT, "erreur"));
    }

    @Test
    public void testFormatZoned(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test format avec nanosecondes
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getYear()).isEqualTo(2018);
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getMonth()).isEqualTo(Month.JANUARY);
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getDayOfMonth()).isEqualTo(1);
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getHour()).isEqualTo(10);
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getMinute()).isEqualTo(10);
        assertThat(((ZonedDateTime) invokeMethod(objetTest, METHODEFORMATZONED, "2018-01-01T10:10:10+0100")).getSecond()).isEqualTo(10);
    }

    @Test
    public void testFormatZoned_Exception(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test retour exception
        assertThrows(TechnicalException.class, () -> invokeMethod(objetTest, METHODEFORMATZONED, "erreur"));
    }

    @Test
    void testGetComposPlantes(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode
        List<ComposantSonar> liste = objetTest.getComposPlantes(null);

        // Contrôle liste non null et éléments sans date analyse
        assertThat(liste).isNotNull();

        for (ComposantSonar compo : liste)
        {
            assertThat(compo.getDateAnalyse()).isNull();
        }
    }

    @Test
    public void testTraitementBranches(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode et contrôle
        List<ComposantSonar> liste = prepareEtTestTraitementBranches();
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(2);
    }

    @Test
    public void testTraitementBranches_Erreur_API(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test webservice plante
        webServiceKO();
        List<ComposantSonar> liste = prepareEtTestTraitementBranches();
        verify(logPlantageMock, times(1)).error("Erreur API : api/project_branches/list - Composant : nom");
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(1);
    }

    @Test
    public void testGestionErreur_Avec_erreur(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        Response response = prepareGestionErreur();
        Mockito.when(response.getStatus()).thenReturn(Status.FORBIDDEN.getStatusCode());

        // Test avec erreur
        assertThat((boolean) invokeMethod(objetTest, METHODEGESTIONERREUR, response)).isFalse();
        verify(logPlantageMock, times(1)).error("message");
    }

    @Test
    public void testGestionErreur_Sans_erreur(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        Response response = prepareGestionErreur();
        Mockito.when(response.getStatus()).thenReturn(Status.OK.getStatusCode()).thenReturn(Status.NO_CONTENT.getStatusCode());

        // Tests OK
        assertThat((boolean) invokeMethod(objetTest, METHODEGESTIONERREUR, response)).isTrue();
        verify(logPlantageMock, never()).error(Mockito.anyString());
        assertThat((boolean) invokeMethod(objetTest, METHODEGESTIONERREUR, response)).isTrue();
        verify(logPlantageMock, never()).error(Mockito.anyString());
    }

    @Test
    public void testErreurAPI_OK(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test OK
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, "api/project_branches/list")).isEqualTo("Erreur API : api/project_branches/list - Composant : ");
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, "api/test")).isEqualTo("Erreur API : api/test - Composant : ");
    }

    @Test
    public void testErreurAPI_KO(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec mauvaises valeurs
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, "test")).isEqualTo("Erreur API : inconnue - Composant : ");
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, EMPTY)).isEqualTo("Erreur API : inconnue - Composant : ");
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, new Class<?>[]
        { String.class }, new Object[]
        { null })).isEqualTo("Erreur API : inconnue - Composant : ");
        assertThat((String) invokeMethod(objetTest, METHODEERREURAPI, "apitest")).isEqualTo("Erreur API : inconnue - Composant : ");
    }

    /*---------- METHODES PRIVEES ----------*/

    private void webServiceKO()
    {
        objetTest = Mockito.spy(objetTest);
        Mockito.doReturn(responseMock).when(objetTest).appelWebserviceGET(anyString(), any(ParamAPI[].class));
        Mockito.doReturn(responseMock).when(objetTest).appelWebservicePOST(any(WebTarget.class));
    }

    private List<ParamAPI> prepareGetIssuegenerique()
    {
        List<ParamAPI> retour = new ArrayList<>();
        retour.add(new ParamAPI("severities", "CRITICAL, BLOCKER"));
        retour.add(new ParamAPI("resolved", "true"));
        retour.add(new ParamAPI("tags", "cve"));
        retour.add(new ParamAPI("types", "VULNERABILITY"));
        retour.add(new ParamAPI("additionalFields", "_all"));
        return retour;
    }

    private IssueBase prepareAssignerIssue()
    {
        List<ParamAPI> params = new ArrayList<>();
        params.add(new ParamAPI("severities", "MINOR"));
        params.add(new ParamAPI("resolved", "true"));
        params.add(new ParamAPI("createdBefore", "2018-01-20"));
        params.add(new ParamAPI("createdAfter", "2018-01-01"));
        params.add(new ParamAPI("statuses", "CLOSED"));
        params.add(new ParamAPI("ps", "500"));
        params.add(new ParamAPI("assigned", "false"));

        List<Issue> liste = objetTest.getIssuesGenerique(params);
        return IssueBase.build(liste.get(0));
    }

    private List<Regle> prepareEtTestGetReglesGenerique()
    {
        // Création paramètres - Règles AEM
        List<ParamAPI> params = new ArrayList<>();
        params.add(new ParamAPI("languages", "java"));
        params.add(new ParamAPI("repositories", "AEM Rules"));

        // Appel méthode
        return objetTest.getReglesGenerique(params);
    }

    private ObjetSonar prepareCalculObjetSonar(String key)
    {
        return new ObjetSonar(key, "Application ADCQ", "", TypeObjetSonar.PORTFOLIO);
    }

    private List<ComposantSonar> prepareEtTestTraitementBranches() throws Exception
    {
        // Préparation liste
        ComposantSonar compo = new ComposantSonar();
        compo.setKey("fr.ca.cat.apimanager.resources:RESS_MPE_Synchrone_Build");
        compo.setNom(NOM);
        List<ComposantSonar> retour = new ArrayList<>();
        retour.add(compo);

        // Appel méthode
        invokeMethod(objetTest, "traitementBranches", retour, new EmptyTaskForTest(true));

        return retour;
    }

    private Response prepareGestionErreur()
    {
        Retour retour = new Retour();
        Message message = new Message();
        message.setMsg("message");
        retour.getErreurs().add(message);
        Response response = Mockito.mock(Response.class);
        Mockito.when(response.readEntity(Mockito.eq(Retour.class))).thenReturn(retour);
        return response;
    }

    /*---------- ACCESSEURS ----------*/

}
