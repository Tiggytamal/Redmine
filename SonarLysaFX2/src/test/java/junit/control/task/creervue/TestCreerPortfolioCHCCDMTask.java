package junit.control.task.creervue;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.portfolio.CreerPortfolioCHCCDMTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import model.enums.OptionGestionErreur;
import model.enums.TypeEdition;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.FunctionalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerPortfolioCHCCDMTask extends TestAbstractTask<CreerPortfolioCHCCDMTask>
{
    /*---------- ATTRIBUTS ----------*/

    private List<String> annees;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws IllegalAccessException
    {
        annees = new ArrayList<>();
        annees.add("2019");
        objetTest = new CreerPortfolioCHCCDMTask(annees, TypeEdition.CDM);
        initAPI(CreerPortfolioCHCCDMTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreerVueCHCCDMTaskException1(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThrows(FunctionalException.class, () -> new CreerPortfolioCHCCDMTask(null, TypeEdition.CDM));
        assertThrows(FunctionalException.class, () -> new CreerPortfolioCHCCDMTask(new ArrayList<>(), TypeEdition.CDM));
    }

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test du retour avec les methode mockees. On appel par la methode call
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();
    }

    @Test
    public void testSuppressionVuesMaintenance_CHC(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode
        Whitebox.invokeMethod(objetTest, "suppressionPortfoliosMaintenance", TypeEdition.CHC, annees);
        
        // Contrôle du nombre d'obejts supprimés (52).
        Mockito.verify(api, Mockito.times(52)).supprimerObjetSonar(Mockito.anyString(), Mockito.eq(TypeObjetSonar.PORTFOLIO), Mockito.eq(OptionGestionErreur.NON));
    }
    
    @Test
    public void testSuppressionVuesMaintenance_CDM(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        // Appel méthode
        Whitebox.invokeMethod(objetTest, "suppressionPortfoliosMaintenance", TypeEdition.CDM, annees);
        
        // Contrôle du nombre d'obejts supprimés (52).
        Mockito.verify(api, Mockito.times(52)).supprimerObjetSonar(Mockito.anyString(), Mockito.eq(TypeObjetSonar.PORTFOLIO), Mockito.eq(OptionGestionErreur.NON));
    }

    @Test
    public void testPreparerMapVuesMaintenance(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode
        Map<String, Set<String>> retour = Whitebox.invokeMethod(objetTest, "preparerMapPortfoliosMaintenance");
        
        // Contrôles
        assertThat(retour).isNotNull();
        assertWithMessage("Map retour vide").that(retour).isNotEmpty();

        // Contrôle des clefs
        for (String key : retour.keySet())
        {
            assertThat(key).contains(TypeEdition.CDM.toString());
        }
    }

    @Test
    public void testCreerVuesMaintenance(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation mock et map d'entree
        Map<String, Set<String>> mapVuesACreer = new HashMap<>();
        Set<String> hashSet = new HashSet<>();
        hashSet.add("set1");
        hashSet.add("set2");
        mapVuesACreer.put("key1", hashSet);

        // Appel methode
        Whitebox.invokeMethod(objetTest, "creerPortfoliosMaintenance", mapVuesACreer);

        // Vérifications
        Mockito.verify(api, Mockito.times(1)).creerObjetSonar(Mockito.any(ObjetSonar.class));
        Mockito.verify(api, Mockito.times(2)).ajouterSousProjet(Mockito.any(ObjetSonar.class), Mockito.anyString());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
