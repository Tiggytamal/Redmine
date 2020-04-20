package junit.control.task.creervue;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.portfolio.CreerPortfolioDataStageTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import model.enums.OptionGestionErreur;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerPortfolioDataStageTask extends TestAbstractTask<CreerPortfolioDataStageTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    
    @Override
    public void initImpl() throws IllegalArgumentException, IllegalAccessException
    {
        objetTest = new CreerPortfolioDataStageTask();
        initAPI(CreerPortfolioDataStageTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testAnnuler(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test simple avec la vue nulle
        objetTest.annuler();

        // On verifie que l'on appelle pas supprimer
        Mockito.verify(api, Mockito.never()).supprimerObjetSonar(Mockito.any(ObjetSonar.class), Mockito.any(OptionGestionErreur.class));

        // Instanciation de vue et appel methode
        ObjetSonar vue = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        setField("pf", vue);
        objetTest.annuler();

        // On verifie que l'on appelle les methodes de suppression 1 fois
        Mockito.verify(api, Mockito.times(1)).supprimerObjetSonar(Mockito.any(ObjetSonar.class), Mockito.any(OptionGestionErreur.class));
    }

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // On invoque la methode call, ce qui correspond à la methode creerVueDataStage
        // Premier appel simple pour voir que l'on retourne bien true à la fin de la methode
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();

        objetTest = Mockito.spy(objetTest);

        // Test sur le premier appel de isCancelled pour finir la methode
        Mockito.when(objetTest.isCancelled()).thenReturn(true);
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isFalse();

        // Test sur le deuxieme appel de isCancelled pour finir la methode
        Mockito.when(objetTest.isCancelled()).thenReturn(false).thenReturn(true);
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
