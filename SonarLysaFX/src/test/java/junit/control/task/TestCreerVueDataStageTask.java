package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.CreerVueDataStageTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.sonarapi.Vue;

@RunWith(JfxRunner.class)
public class TestCreerVueDataStageTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    private CreerVueDataStageTask handler;
    private SonarAPI mock;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestCreerVueDataStageTask()
    {
        handler = new CreerVueDataStageTask();
    }
    
    @Override
    public void init() throws IllegalArgumentException, IllegalAccessException
    {
        // Mock de l'api Sonar pour bloquer tous les mises à jour
        mock = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueDataStageTask.class, "api").set(handler, mock);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testAnnuler() throws IllegalArgumentException, IllegalAccessException
    {
        // Test simple avec la vue nulle
        handler.annuler();
        
        // On vérifie que l'on appelle pas supprimer
        Mockito.verify(mock, Mockito.never()).supprimerProjet(Mockito.any(Vue.class), Mockito.anyBoolean());
        Mockito.verify(mock, Mockito.never()).supprimerVue(Mockito.any(Vue.class), Mockito.anyBoolean());
        
        // Instanciation de vue et appel méthode
        Vue vue = new Vue();
        Whitebox.getField(CreerVueDataStageTask.class, "vue").set(handler, vue);       
        handler.annuler();
        
        // On vérifie que l'on appelle les méthodes de suppression 1 fois
        Mockito.verify(mock, Mockito.times(1)).supprimerProjet(Mockito.any(Vue.class), Mockito.anyBoolean());
        Mockito.verify(mock, Mockito.times(1)).supprimerVue(Mockito.any(Vue.class), Mockito.anyBoolean());
        
    }
    
    @Test
    public void testCreerVueDatastage() throws Exception
    {
        // On invoque la méthode call, ce qui correspond à la méthode creerVueDataStage
        // Premier appel simple pour voir que l'on retourne bien true à la fin de la méthode
        assertTrue(Whitebox.invokeMethod(handler, "call"));
        
        // Spy de l'ahndler pour mock de la méthode isCancelled
        handler = Mockito.spy(handler);
        
        // Test sur le premier appel de isCancelled pour finir la méthode
        Mockito.when(handler.isCancelled()).thenReturn(true);
        assertFalse(Whitebox.invokeMethod(handler, "call"));
        
        // Test sur le deuxième appel de isCancelled pour finir la méthode
        Mockito.when(handler.isCancelled()).thenReturn(false).thenReturn(true);
        assertFalse(Whitebox.invokeMethod(handler, "call"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
