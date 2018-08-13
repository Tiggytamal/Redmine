package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerVuePatrimoineTask;
import model.ComposantSonar;
import model.sonarapi.Vue;

public class TestCreerVuePatrimoineTask extends AbstractTestTask<CreerVuePatrimoineTask>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new CreerVuePatrimoineTask();
        initAPI(CreerVuePatrimoineTask.class, true);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerVuePatrimoine() throws Exception
    {
        // Appel m�thode, creerVuePatrimoine depuis la m�thode call et test du retour � true
        assertTrue(Whitebox.invokeMethod(handler, "call"));
        
        // V�rification que la m�thode d'ajout des projet a �t� appel�e.
        Mockito.verify(api, Mockito.atLeast(2)).ajouterProjet(Mockito.any(ComposantSonar.class), Mockito.any(Vue.class));
        
        // Appel m�thode, creerVuePatrimoine avec t�che annul�e
        init();
        handler.annuler();
        assertFalse(Whitebox.invokeMethod(handler, "call"));
        
        // V�rification que la m�thode d'ajout des projet a �t� appel�e.
        Mockito.verify(api, Mockito.never()).ajouterProjet(Mockito.any(ComposantSonar.class), Mockito.any(Vue.class));
    }
    
    @Test
    @Override
    public void testAnnuler() throws IllegalAccessException
    {
        // 1. Test avec key null
        Whitebox.getField(CreerVuePatrimoineTask.class, "key").set(handler, null);
        
        // Appel m�thode
        handler.annuler();
        
        // Test cancel et supprimerProjet
        assertTrue(handler.isCancelled());
        Mockito.verify(api, Mockito.never()).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        
        // 2. Test avec key vide
        Whitebox.getField(CreerVuePatrimoineTask.class, "key").set(handler, "");
        
        // Appel m�thode
        handler.annuler();
        
        // Test cancel et supprimerProjet
        assertTrue(handler.isCancelled());
        Mockito.verify(api, Mockito.never()).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        
        // 3. test avec key initialis�e non vide
        Whitebox.getField(CreerVuePatrimoineTask.class, "key").set(handler, "key");
        
        // Appel m�thode
        handler.annuler();
        
        // Test cancel et supprimerProjet
        assertTrue(handler.isCancelled());
        Mockito.verify(api, Mockito.times(1)).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
