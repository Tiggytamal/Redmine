package junit.control.task;

import static com.google.common.truth.Truth.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.reflect.Whitebox;

import control.task.AbstractTask;
import control.task.AffichageTempsTask;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.EmptyTaskForTest;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAffichageTempsTask extends JunitBase<AffichageTempsTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        try
        {
            Constructor<AffichageTempsTask> constructor = AffichageTempsTask.class.getDeclaredConstructor(AbstractTask.class);
            constructor.setAccessible(true);
            objetTest = constructor.newInstance(new Object[]
            { null });
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : control.task.AffichageTempsTask", e);
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnnuler(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Booleen initialement à faux
        assertThat((boolean) getField("stop")).isFalse();

        // Appel methode
        objetTest.annuler();

        // Booleen à vrai
        assertThat((boolean) getField("stop")).isTrue();
    }

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test de la methode avec la tache parente null
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isFalse();

        // Mock de la tâche parente pour permettre d'arreter la boucle infinie.
        EmptyTaskForTest mock = Mockito.spy(EmptyTaskForTest.class);

        // Mock qui renvoie d'abord la methode normale puis arrête la boucle en mettant stop à vrai.
        Mockito.doCallRealMethod().doAnswer((InvocationOnMock invocation) -> { setField("stop", true); return true; }).when(mock).setAffTimer(Mockito.anyString(), Mockito.anyString());

        setField("tacheParente", mock);

        // Appel methode
        Whitebox.invokeMethod(objetTest, "call");

    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
