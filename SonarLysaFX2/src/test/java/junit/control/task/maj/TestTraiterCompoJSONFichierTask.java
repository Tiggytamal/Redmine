package junit.control.task.maj;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import application.MainScreen;
import control.rest.ControlAppCATS;
import control.rest.ControlRepack;
import control.rtc.ControlRTC;
import control.task.maj.ExceptionTraiterCompo;
import control.task.maj.TraiterCompoJSONFichierTask;
import dao.DaoApplication;
import dao.DaoDefautQualite;
import dao.DaoIssueBase;
import javafx.scene.Scene;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.ModelFactory;
import model.OptionInitAPI;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.IssueBase;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EtatCodeAppli;
import model.enums.Metrique;
import model.enums.Param;
import model.enums.TypeDefautQualite;
import model.parsing.ComposantJSON;
import model.parsing.ProjetJSON;
import model.rest.repack.RepackREST;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Mesure;
import model.rest.sonarapi.StatutQGCondition;
import model.rest.sonarapi.StatutQGProjet;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTraiterCompoJSONFichierTask extends TestAbstractTask<TraiterCompoJSONFichierTask>
{
    /*---------- ATTRIBUTS ----------*/

    private DaoIssueBase daoIBMock;
    private DaoDefautQualite daoDQ;
    private ComposantBase compo;
    private File copie;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws Exception
    {
        if (MainScreen.ROOT.getScene() == null)
            new Scene(MainScreen.ROOT);
        File file = new File(Statics.RESSTEST + "JSON/testJSON.json");
        copie = new File(Statics.RESSTEST + "testJSONCopie.json");
        if (!copie.exists())
            FileUtils.copyFile(file, copie);
        objetTest = new TraiterCompoJSONFichierTask(copie);
        compo = ComposantBase.build(KEY, TESTSTRING);

        // Mock du dao
        daoIBMock = mock(DaoIssueBase.class);
        setField("daoIssueBase", daoIBMock);
        daoDQ = mock(DaoDefautQualite.class);
        setField("daoDQ", daoDQ);
        initAPI(TraiterCompoJSONFichierTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testTraiterComposantDepuisFichier(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

    }

    @Test
    public void testInitDepuisJSON(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

    }

    @Test
    public void testInitLot(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

    }

    @Test
    public void testInitDateRepack(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        final String nomGc = "nomGC";

        // Préparation variables
        compo.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        List<RepackREST> liste = new ArrayList<>();
        RepackREST repack = new RepackREST();
        repack.setNomGc("BOREAL_Packaging");
        liste.add(repack);
        repack = new RepackREST();
        repack.setNomGc(nomGc);
        liste.add(repack);
        repack = new RepackREST();
        repack.setNomGc(nomGc);
        repack.setDateCreation("mauvaiseDate");
        liste.add(repack);
        ControlRepack mock = mock(ControlRepack.class);
        when(mock.getRepacksComposant(compo)).thenReturn(liste);
        setField("cr", mock);

        // Appel méthode sans date repack correct
        invokeMethod(objetTest, "initDateRepack", compo);

        // Contrôle date null
        assertThat(compo.getDateRepack()).isNull();

        // AJout repack avec date correcte
        repack = new RepackREST();
        repack.setNomGc(nomGc);
        repack.setDateCreation("1234567891234");
        liste.add(repack);

        // Appel méthode
        invokeMethod(objetTest, "initDateRepack", compo);

        // Contrôle date bien enregistrée
        assertThat(compo.getDateRepack()).isEqualTo(LocalDate.of(2009, 2, 14));
        assertThat(compo.getLotRTC().getDateRepack()).isEqualTo(LocalDate.of(2009, 2, 14));
    }

    @Test
    public void testInitCodeAppli(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Intialisation variables
        String appli = "V360";
        ComposantSonar compoMetrique = new ComposantSonar();
        compoMetrique.getMesures().add(new Mesure(Metrique.APPLI, appli));

        // Mock appel AppCATS
        ControlAppCATS mock = mock(ControlAppCATS.class);
        when(mock.testApplicationExiste(anyString())).thenReturn(false).thenReturn(true);
        setField("controlAppCATS", mock);

        // Mock appel base Application
        DaoApplication mockAppli = mock(DaoApplication.class);
        when(mockAppli.recupEltParIndex(anyString())).thenReturn(null).thenReturn(Application.getApplication(appli, false));
        setField("daoAppli", mockAppli);

        // Premier test avec applicaiton null
        assertThat(compo.getAppli()).isNull();

        // Premier appel avec appli qui n'existe pas et qui n'est pas dans la base de donnée
        invokeMethod(objetTest, "initCodeAppli", compo, compoMetrique);

        // Contrôles
        assertThat(compo.getAppli()).isNotNull();
        assertThat(compo.getAppli().isReferentiel()).isFalse();
        assertThat(compo.getAppli().getCode()).isEqualTo(appli);

        // Deuxième appel avec appli qui existe et qui est dans la bse de données, mais avec un referentiel à faux qui devrait être vrai
        invokeMethod(objetTest, "initCodeAppli", compo, compoMetrique);

        // Contrôles
        assertThat(compo.getAppli()).isNotNull();
        assertThat(compo.getAppli().isReferentiel()).isTrue();
        assertThat(compo.getAppli().getCode()).isEqualTo(appli);
    }

    @Test
    public void testTraiterDefauts(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

    }

    @Test
    public void testCreerDefaut(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        compo.setBranche("master");
        LotRTC lot = LotRTC.getLotRTCInconnu(LOT123456);
        Edition edition = Edition.build("E32", LOT123456);
        edition.setDateMEP(today);
        lot.setEdition(edition);
        compo.setLotRTC(lot);
        List<IssueBase> liste = new ArrayList<>();
        IssueBase issue = ModelFactory.build(IssueBase.class);
        issue.setUtilisateur(Statics.USERINCONNU);
        liste.add(issue);
        issue = ModelFactory.build(IssueBase.class);
        Utilisateur user = ModelFactory.build(Utilisateur.class);
        issue.setUtilisateur(user);
        liste.add(issue);

        // Test avec deta repack null
        DefautQualite retour = invokeMethod(objetTest, "creerDefaut", compo, liste);
        assertThat(retour).isNotNull();
        assertThat(retour.getLotRTC()).isEqualTo(lot);
        assertThat(retour.getCompo()).isEqualTo(compo);
        assertThat(retour.getDateMepPrev()).isEqualTo(today);

        // Test avec date de repack non null
        lot.setDateRepack(LocalDate.of(2019, 2, 2));
        retour = invokeMethod(objetTest, "creerDefaut", compo, liste);
        assertThat(retour).isNotNull();
        assertThat(retour.getLotRTC()).isEqualTo(lot);
        assertThat(retour.getCompo()).isEqualTo(compo);
        assertThat(retour.getDateMepPrev()).isEqualTo(LocalDate.of(2019, 2, 2));

    }

    @Test
    public void testMiseAJourDefaut(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String idAnalyse = "idAnalyse";
        ComposantJSON compo = new ComposantJSON();
        ProjetJSON projet = new ProjetJSON();
        projet.setNom("Projet");
        compo.setProjet(projet);
        compo.setIdAnalyse(idAnalyse);
        DefautQualite dq = ModelFactory.build(DefautQualite.class);

        // Créationde l'objet de retour de l'api pour tester toutes les conditions de la boucle.
        StatutQGProjet sp = new StatutQGProjet();
        List<StatutQGCondition> liste = new ArrayList<>();
        StatutQGCondition cond = new StatutQGCondition();
        cond.setStatus("OK");
        cond.setMetricKey("notecodeappli");
        liste.add(cond);
        cond = new StatutQGCondition();
        cond.setStatus("OK");
        cond.setMetricKey("QG");
        liste.add(cond);
        cond = new StatutQGCondition();
        cond.setStatus("ERROR");
        cond.setMetricKey("notecodeappli");
        liste.add(cond);
        cond = new StatutQGCondition();
        cond.setStatus("ERROR");
        cond.setMetricKey("QG");
        liste.add(cond);
        sp.setConditions(liste);

        // Mock des api
        when(api.getAnalyseQGInfos(anyString())).thenReturn(null).thenReturn(sp);
        ControlRTC mockRTC = mock(ControlRTC.class);
        setField("controlRTC", mockRTC);

        // Test retour exception sur retour api null
        ExceptionTraiterCompo e = assertThrows(ExceptionTraiterCompo.class, () -> appelMiseAJourDefaut(dq, compo, EtatCodeAppli.OK));
        assertThat(e.getMessage()).isEqualTo("Impossible de retrouver l'analyse du composant donne : Projet - analyse : idAnalyse");

        // Pour chaque test, on regarde l'id de l'analyse, le type de défaut l'appel à RTC. Le nombre d'appel à RTC n'est pas remis à zéro entre chaque.
        // On test donc pour 1 pui 2 puis 3

        // Test TypeDefautQualite mixte
        dq.setAnalyseId(Statics.EMPTY);
        appelMiseAJourDefaut(dq, compo, EtatCodeAppli.ERREUR);
        assertThat(dq.getAnalyseId()).isEqualTo(idAnalyse);
        assertThat(dq.getTypeDefaut()).isEqualTo(TypeDefautQualite.MIXTE);
        Mockito.verify(mockRTC, Mockito.times(1)).controleAnoRTC(dq);

        // Test TypeDefautQualite sonar
        dq.setAnalyseId(Statics.EMPTY);
        appelMiseAJourDefaut(dq, compo, EtatCodeAppli.OK);
        assertThat(dq.getAnalyseId()).isEqualTo(idAnalyse);
        assertThat(dq.getTypeDefaut()).isEqualTo(TypeDefautQualite.SONAR);
        Mockito.verify(mockRTC, Mockito.times(2)).controleAnoRTC(dq);

        // Test TypeDefautQualite appli
        sp.getConditions().clear();
        appelMiseAJourDefaut(dq, compo, EtatCodeAppli.ERREUR);
        assertThat(dq.getAnalyseId()).isEqualTo(idAnalyse);
        assertThat(dq.getTypeDefaut()).isEqualTo(TypeDefautQualite.APPLI);
        Mockito.verify(mockRTC, Mockito.times(3)).controleAnoRTC(dq);

        // Test sans modification de l'etat
        dq.setTypeDefaut(TypeDefautQualite.INCONNU);
        appelMiseAJourDefaut(dq, compo, EtatCodeAppli.NA);
        assertThat(dq.getAnalyseId()).isEqualTo(idAnalyse);
        assertThat(dq.getTypeDefaut()).isEqualTo(TypeDefautQualite.INCONNU);
        Mockito.verify(mockRTC, Mockito.times(4)).controleAnoRTC(dq);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAssignerAnoSonar(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        ComposantJSON compoJSON = new ComposantJSON();
        compoJSON.setIdAnalyse("IDAnalyse");
        List<Issue> liste = new ArrayList<>();
        Issue issue = new Issue();
        issue.setAuteur("MATHON Gregoire");
        liste.add(issue);
        issue = new Issue();
        issue.setAuteur("MATHON Gregoire");
        liste.add(issue);
        issue = new Issue();
        issue.setAuteur("INCONNU");
        liste.add(issue);
        when(api.getDateAnalyse(compoJSON)).thenReturn(ZonedDateTime.of(LocalDateTime.of(2019, 6, 1, 12, 20), ZoneId.systemDefault()));
        when(api.assignerIssue(Mockito.any(IssueBase.class))).thenReturn(true).thenReturn(false);
        when(api.getIssuesGenerique(Mockito.any(Collection.class))).thenReturn(liste);

        // Appel méthode
        List<IssueBase> retour = invokeMethod(objetTest, "assignerAnoSonar", compoJSON);
        assertThat(retour).isNotNull();
        assertThat(retour).hasSize(2);
        assertThat(retour.get(0).getUtilisateur().getIdentifiant()).isEqualTo("ETP8137");
        assertThat(retour.get(0).getAnalyseId()).isEqualTo("IDAnalyse");
        assertThat(retour.get(1).getUtilisateur()).isEqualTo(Statics.USERINCONNU);
    }

    @Test
    public void testSuppressionFichier(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans sauvegarde
        invokeMethod(objetTest, "suppressionFichier", false);
        assertThat(copie.exists()).isFalse();
    }

    @Test
    public void testSuppressionFichier_avec_sauvegarde(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec sauvegarde
        invokeMethod(objetTest, "suppressionFichier", true);
        assertThat(copie.exists()).isFalse();
        File newFile = new File(Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATHRAPPORT) + copie.getName());
        assertThat(newFile.exists()).isTrue();

        // Nettoyage
        Files.delete(newFile.toPath());
    }

    @Test
    public void testPreparationNomAuteur(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test nom moins de 2 caractères
        assertThat(appelPreparationNomAuteur("a")).isEmpty();

        // Tests bonne correction des noms
        assertThat(appelPreparationNomAuteur("mathon grégoire")).isEqualTo("MATHON Grégoire");
        assertThat(appelPreparationNomAuteur("MAThon gréGOIre")).isEqualTo("MATHON Grégoire");
        assertThat(appelPreparationNomAuteur("mathON gregoiRE")).isEqualTo("MATHON Gregoire");
        assertThat(appelPreparationNomAuteur("dupont george")).isEqualTo("DUPONT George");
    }

    /*---------- METHODES PRIVEES ----------*/

    private String appelPreparationNomAuteur(String auteur) throws Exception
    {
        return Whitebox.invokeMethod(objetTest, "preparationNomAuteur", auteur);
    }

    private void appelMiseAJourDefaut(DefautQualite dq, ComposantJSON compo, EtatCodeAppli etat) throws Exception
    {
        Whitebox.invokeMethod(objetTest, "miseAJourDefaut", dq, compo, etat);
    }

    /*---------- ACCESSEURS ----------*/
}
