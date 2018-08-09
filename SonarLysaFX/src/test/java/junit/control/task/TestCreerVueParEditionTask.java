package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.CreerVueParEditionTask;
import de.saxsys.javafx.test.JfxRunner;
import model.ComposantSonar;
import model.sonarapi.Vue;

@RunWith(JfxRunner.class)
public class TestCreerVueParEditionTask extends AbstractTestTask<CreerVueParEditionTask>
{
    /*---------- ATTRIBUTS ----------*/
    
    private SonarAPI api;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    @Before
    public void init() throws IllegalAccessException
    {   
        handler = new CreerVueParEditionTask();
        api = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueParEditionTask.class, "api").set(handler, api);
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
