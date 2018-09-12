package junit.control.task;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.task.CreerExtractComposantsSonarTask;
import utilities.Statics;

public class TestCreerExtractComposantsSonarTask extends AbstractTestTask<CreerExtractComposantsSonarTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new CreerExtractComposantsSonarTask(new File(Statics.RESSTEST + "testExtractCompo.xlsx"));
        initAPI(CreerExtractComposantsSonarTask.class, true);        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testAnnuler() throws IllegalAccessException
    {
        // Test cancel de la méthode
        handler.annuler();
        assertTrue(handler.isCancelled());
    }
    
    @Test
    public void testRecupererComposantsSonar() throws Exception
    {
        // Appel méthode, creerVuePatrimoine depuis la méthode call et test du retour à true
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
