package junit.control.task.creervue;

import static com.google.common.truth.Truth.assertThat;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import control.task.portfolio.CreerPortfolioTrimestrielTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.KeyDateMEP;
import model.OptionInitAPI;
import model.enums.OptionGestionErreur;
import model.enums.OptionPortfolioTrimestriel;
import model.enums.Param;
import model.enums.TypeObjetSonar;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerPortfolioTrimestrielTask extends TestAbstractTask<CreerPortfolioTrimestrielTask>
{
    /*---------- ATTRIBUTS ----------*/

    private static final LocalDate date = LocalDate.of(2019, 1, 1);

    /*---------- CONSTRUCTEURS ----------*/

    
    @Override
    public void initImpl() throws Exception
    {
        objetTest = new CreerPortfolioTrimestrielTask(date, OptionPortfolioTrimestriel.ALL);
        initAPI(CreerPortfolioTrimestrielTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testAnnuler(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Instanciation de vueKey non vide
        setField("vueKey", "vueKey");
        objetTest.annuler();

        // On verifie que l'on appelle les methodes de suppression 1 fois
        Mockito.verify(api, Mockito.times(1)).supprimerObjetSonar(Mockito.anyString(), Mockito.eq(TypeObjetSonar.PORTFOLIO), Mockito.any(OptionGestionErreur.class));
    }
    
    @Test
    public void testAnnuler_Null(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test simple avec la vueKey null
        objetTest.annuler();

        // On verifie que l'on appelle pas supprimer
        Mockito.verify(api, Mockito.never()).supprimerObjetSonar(Mockito.anyString(), Mockito.eq(TypeObjetSonar.PORTFOLIO), Mockito.any(OptionGestionErreur.class));
    }
    
    @Test
    public void testAnnuler_Vide(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Instanciation de vueKey à vide
        setField("vueKey", EMPTY);
        objetTest.annuler();

        // On verifie que l'on appelle pas les methodes de suppression
        Mockito.verify(api, Mockito.never()).supprimerObjetSonar(Mockito.anyString(), Mockito.eq(TypeObjetSonar.PORTFOLIO), Mockito.any(OptionGestionErreur.class));
    }

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((boolean) invokeMethod(objetTest, "call")).isTrue();
    }

    @Test
    public void testTrierComposPourTEP(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création objets pour test
        KeyDateMEP objet1 = KeyDateMEP.build(KEY, NOM, LocalDate.of(2019, 1, 12));
        KeyDateMEP objet2 = KeyDateMEP.build(KEY, NOM, LocalDate.of(2019, 2, 23));
        KeyDateMEP objet3 = KeyDateMEP.build(KEY, NOM, LocalDate.of(2019, 3, 6));
        List<KeyDateMEP> liste = new ArrayList<>();
        liste.add(objet1);
        liste.add(objet2);
        liste.add(objet3);

        // Appel methode
        Map<LocalDate, List<KeyDateMEP>> map = invokeMethod(objetTest, "trierComposPourTEP", liste);

        // Contrôle données
        assertThat(map).isNotNull();
        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(3);
        assertThat(map.get(date)).isNotNull();
        assertThat(map.get(date.plusMonths(1))).isNotNull();
        assertThat(map.get(date.plusMonths(2))).isNotNull();

    }

    @Test
    public void testCreerObjetSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // TODO

    }

    @Test
    public void testRecupKeyCompos(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        List<KeyDateMEP> liste = invokeMethod(objetTest, "recupKeyCompos");

        // Contrôle sur la liste : non nulle, non vide, et objets non vide avec dates cohérentes
        assertThat(liste).isNotNull();;
        assertThat(liste).isNotEmpty();
        for (KeyDateMEP compo : liste)
        {
            assertThat(compo.getCle()).isNotEmpty();
            assertThat(compo.getNom()).isNotEmpty();
            assertThat(compo.getDate()).isGreaterThan(date.minusDays(1));
            assertThat(compo.getDate()).isLessThan(date.plusMonths(3));
        }
    }

    @Test
    public void testRecupKeyComposDataStage(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        List<KeyDateMEP> liste = invokeMethod(objetTest, "recupKeyComposDataStage");

        // Contrôle sur la liste : non nulle, non vide, et objets non vide avec date coherente
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        for (KeyDateMEP compo : liste)
        {
            assertThat(compo.getCle()).isNotEmpty();
            assertThat(compo.getNom()).isNotEmpty();
            assertThat(compo.getDate()).isGreaterThan(date.minusDays(1));
            assertThat(compo.getDate()).isLessThan(date.plusMonths(3));
            assertThat(compo.getNom()).startsWith(Statics.proprietesXML.getMapParams().get(Param.FILTREDATASTAGE));
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
