package junit.control.task.creervue;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import control.task.portfolio.CreerPortfolioDirectionTask;
import dao.DaoApplication;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import model.bdd.Application;
import model.enums.OptionGestionErreur;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.ObjetSonar;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerPortfolioDirectionTask extends TestAbstractTask<CreerPortfolioDirectionTask>
{
    /*---------- ATTRIBUTS ----------*/

    String direction1 = "Direction1";
    String direction2 = "Direction2";

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    protected void initImpl() throws Exception
    {
        objetTest = new CreerPortfolioDirectionTask();
        initAPI(CreerPortfolioDirectionTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((boolean) invokeMethod(objetTest, "call")).isTrue();
    }

    @Test
    void testSuppressionPortfolios(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        ComposantSonar compo = new ComposantSonar();
        compo.setKey(KEY);
        List<ComposantSonar> liste = new ArrayList<>();
        liste.add(compo);
        liste.add(compo);

        // Mock de l'appel pour récupérer la liste des portfolios direction
        when(api.getObjetSonarParNomOuType("Direction", TypeObjetSonar.PORTFOLIO)).thenReturn(liste);

        // Appel méthode
        invokeMethod(objetTest, "suppressionPortfolios");

        // Contrôle
        verify(api, times(2)).supprimerObjetSonar(Mockito.anyString(), eq(TypeObjetSonar.PORTFOLIO), Mockito.any(OptionGestionErreur.class));
    }

    @Test
    void testPreparerPortfolios(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock dao
        List<Application> liste = new ArrayList<>();
        Application appli1 = Application.getApplication("DOC1", true);
        liste.add(appli1);
        Application appli2 = Application.getApplication("ABCD", true);
        appli2.setDirection(direction1);
        liste.add(appli2);
        Application appli3 = Application.getApplication("ABCE", true);
        appli3.setDirection(direction1);
        liste.add(appli3);
        Application appli4 = Application.getApplication("QSDF", true);
        appli4.setDirection(direction2);
        liste.add(appli4);
        DaoApplication mock = Mockito.mock(DaoApplication.class);
        when(mock.readAll()).thenReturn(liste);
        setField("dao", mock);

        // Appel méthode
        Map<String, List<String>> map = invokeMethod(objetTest, "preparerPortfolios");

        // Contrôles
        assertThat(map).isNotNull();
        assertThat(map).isNotEmpty();
        List<String> liste1 = map.get(direction1);
        assertThat(liste1).isNotNull();
        assertThat(liste1).hasSize(2);
        List<String> liste2 = map.get(direction2);
        assertThat(liste2).isNotNull();
        assertThat(liste2).hasSize(1);
    }

    @Test
    void testCreerPortfolioDirection(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création paramètre méthode
        Map<String, List<String>> map = new HashMap<>();
        map.put(direction1, Arrays.asList("ABCD", "QSDF"));
        map.put(direction2, Arrays.asList("DOC1"));
        map.put("vide", new ArrayList<>());
        map.put("Testéèê ô &", new ArrayList<>());

        // Appel méthode
        invokeMethod(objetTest, "creerPortfoliosDirection", map);

        // Contrôles

        // Contrôle direction avec 1 élément
        ObjetSonar objet1 = new ObjetSonar("direction_direction1", "Direction Direction1", "Portfolio des applications de la direction Direction1", TypeObjetSonar.PORTFOLIO);
        verify(api, times(1)).creerObjetSonar(eq(objet1));
        verify(api, times(2)).ajouterSousPortfolio(eq(objet1), any(ObjetSonar.class));

        // Contrôle direction avec 2 éléments
        ObjetSonar objet2 = new ObjetSonar("direction_direction2", "Direction Direction2", "Portfolio des applications de la direction Direction2", TypeObjetSonar.PORTFOLIO);
        verify(api, times(1)).creerObjetSonar(eq(objet2));
        verify(api, times(1)).ajouterSousPortfolio(eq(objet2), any(ObjetSonar.class));

        // Contrôle direction sans élément
        ObjetSonar objet3 = new ObjetSonar("direction_vide", "Direction vide", "Portfolio des applications de la direction vide", TypeObjetSonar.PORTFOLIO);
        verify(api, times(1)).creerObjetSonar(eq(objet3));
        verify(api, Mockito.never()).ajouterSousPortfolio(eq(objet3), any(ObjetSonar.class));

        // Contrôle de la correction des accents
        ObjetSonar objet4 = new ObjetSonar("direction_testeee_o_et", "Direction Testéèê ô &", "Portfolio des applications de la direction Testéèê ô &", TypeObjetSonar.PORTFOLIO);
        verify(api, times(1)).creerObjetSonar(eq(objet4));
        verify(api, Mockito.never()).ajouterSousPortfolio(eq(objet4), any(ObjetSonar.class));

        // Contrôle que l'on calcule bien 4 portfolios
        verify(api, times(4)).calculObjetSonar(any(ObjetSonar.class));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
