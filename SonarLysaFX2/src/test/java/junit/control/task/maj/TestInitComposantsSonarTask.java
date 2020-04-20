package junit.control.task.maj;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import control.rest.ControlAppCATS;
import control.rest.ControlRepack;
import control.task.maj.InitComposantsSonarTask;
import dao.DaoStatistiqueCompo;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.EmptyTaskForTest;
import model.OptionInitAPI;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.enums.Metrique;
import model.enums.OptionGetCompos;
import model.rest.repack.RepackREST;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Mesure;
import utilities.TechnicalException;

@DisplayNameGeneration (AutoDisplayName.class)
public class TestInitComposantsSonarTask extends TestAbstractTask<InitComposantsSonarTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws IllegalAccessException
    {
        objetTest = new InitComposantsSonarTask();
        initAPI(InitComposantsSonarTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur() throws IllegalAccessException
    {
        objetTest = new InitComposantsSonarTask(new EmptyTaskForTest());
        assertThat(objetTest).isNotNull();
        assertThat(getField("tacheParente")).isNotNull();
    }

    @Test
    @Disabled (TESTMANUEL)
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Reinitialisation de l'API
        initAPI(InitComposantsSonarTask.class, OptionInitAPI.NOMOCK);

        // Appel de la methode
        assertThat((boolean) invokeMethod(objetTest, "call")).isTrue();
    }

    @Test
    public void testTraiterListeComposants(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation des Mocks
        mockAPIGetSomething(() -> api.getComposants(Mockito.eq(OptionGetCompos.MINIMALE), any()));
        mockAPIGetSomething(() -> api.getComposantsSansBranche(any()));
        List<ComposantSonar> listeApi = api.getComposants(OptionGetCompos.MINIMALE, null);
        List<ComposantSonar> retour = new ArrayList<>();
        for (ComposantSonar compo : listeApi)
        {
            retour.add(compo);
            if (retour.size() > 99)
                break;
        }

        when(api.getComposants(Mockito.eq(OptionGetCompos.MINIMALE), any())).thenReturn(retour);
        DaoStatistiqueCompo daoStatMock = Mockito.mock(DaoStatistiqueCompo.class);
        setField("daoStat", daoStatMock);

        // Autres apis
        when(api.getMesuresComposant(anyString(), anyString(), Mockito.any(String[].class))).thenReturn(new ComposantSonar());
        when(api.getDetailsComposant(anyString(), anyString())).thenReturn(new ComposantSonar());
        mockAPIGetSomething(() -> api.getComposantsSansBranche(any()));

        // Appel de la methode
        List<ComposantBase> liste = invokeMethod(objetTest, "traiterListeComposants");

        // Contrôles
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        assertThat(liste).hasSize(retour.size());
    }

    @Test
    public void testInitDateRepack(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        final String nomGc = "nomGC";

        // Préparation variables
        ComposantBase compo = ComposantBase.build("key", "composantTest");
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
        ControlRepack mock = Mockito.mock(ControlRepack.class);
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
        ComposantBase compo = ComposantBase.build(KEY, EMPTY);
        ComposantSonar compoMetrique = new ComposantSonar();
        compoMetrique.getMesures().add(new Mesure(Metrique.APPLI, appli));
        Map<String, Application> mapAppli = new HashMap<>();
        ControlAppCATS mock = Mockito.mock(ControlAppCATS.class);
        when(mock.testApplicationExiste(anyString())).thenReturn(false).thenReturn(true);
        setField("controlAppCATS", mock);

        // Premier appel avec appli qui n'existe pas et qui n'est pas dans la HashMap
        invokeMethod(objetTest, "initCodeAppli", compo, compoMetrique, mapAppli);

        // Contrôles
        assertThat(mapAppli).hasSize(1);
        assertThat(mapAppli).containsKey(appli);
        assertThat(mapAppli.get(appli).isReferentiel()).isFalse();

        // Deuxième appel avec appli qui existe et qui est dans la HashMap
        invokeMethod(objetTest, "initCodeAppli", compo, compoMetrique, mapAppli);

        // Contrôles
        assertThat(mapAppli).hasSize(1);
        assertThat(mapAppli).containsKey(appli);
        assertThat(mapAppli.get(appli).isReferentiel()).isTrue();
    }

    @Test
    public void testSauvegarde(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock du persist pour retourner vrai
        when(daoCompoMock.persist(Mockito.any(ComposantBase.class))).thenReturn(true).thenThrow(IllegalStateException.class);

        // test ok
        ComposantBase compo = databaseXML.getCompos().get(0);
        assertThat((boolean) invokeMethod(objetTest, "sauvegarde", daoCompoMock, compo)).isTrue();

        // Test ko
        assertThrows(IllegalStateException.class, () -> invokeMethod(objetTest, "sauvegarde", daoCompoMock, compo));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
