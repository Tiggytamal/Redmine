package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueParEditionTask;
import model.bdd.ComposantSonar;
import model.sonarapi.Vue;

public class TestCreerVueParEditionTask extends AbstractTestTask<CreerVueParEditionTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @Before
    public void init() throws IllegalAccessException
    {
        handler = new CreerVueParEditionTask();
        initAPI(CreerVueParEditionTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    public void testCreerVueParEdition() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
        Mockito.verify(api, Mockito.atLeast(100)).ajouterProjet(Mockito.any(ComposantSonar.class), Mockito.any(Vue.class));
    }

    @Test
    public void testAnnuler()
    {
        // Vérification que l'on n'annule pas la tâche
        handler.annuler();
        assertFalse(handler.isCancelled());
    }
}
