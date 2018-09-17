package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueProductionTask;
import model.sonarapi.Vue;
import utilities.Statics;

public class TestCreerVueProductionTask extends AbstractTestTask<CreerVueProductionTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new CreerVueProductionTask();
        initAPI(CreerVueProductionTask.class, true);        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Override
    public void testAnnuler() throws IllegalArgumentException, IllegalAccessException
    {
        // Test simple avec la vueKey null
        handler.annuler();
        
        // On vérifie que l'on appelle pas supprimer
        Mockito.verify(api, Mockito.never()).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        Mockito.verify(api, Mockito.never()).supprimerVue(Mockito.anyString(), Mockito.anyBoolean());
        
        // Instanciation de vueKey à vide
        Whitebox.getField(CreerVueProductionTask.class, "vueKey").set(handler, Statics.EMPTY);       
        handler.annuler();
        
        // On vérifie que l'on appelle les méthodes de suppression 1 fois
        Mockito.verify(api, Mockito.never()).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        Mockito.verify(api, Mockito.never()).supprimerVue(Mockito.anyString(), Mockito.anyBoolean());        
        
        // Instanciation de vueKey non vide
        Whitebox.getField(CreerVueProductionTask.class, "vueKey").set(handler, "vueKey");       
        handler.annuler();
        
        // On vérifie que l'on appelle les méthodes de suppression 1 fois
        Mockito.verify(api, Mockito.times(1)).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        Mockito.verify(api, Mockito.times(1)).supprimerVue(Mockito.anyString(), Mockito.anyBoolean());
    }
    
    @Test
    public void testRecupetrLotsSonarQube() throws Exception
    {
        // Mock et appel de la méthode
        mockAPIGetSomething(() -> api.getVues());
        Map<String, Vue> retour = Whitebox.invokeMethod(handler, "recupererLotsSonarQube");
        
        // Contrôle sur la liste : non nulle, non vide, et test sur les regex des clefs et des noms des vues
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        for (Map.Entry<String, Vue> entry : retour.entrySet())
        {
            assertTrue(Pattern.compile("^Lot [0-9]{6}").matcher(entry.getValue().getName()).matches());
            assertTrue(Pattern.compile("^[0-9]{6}$").matcher(entry.getKey()).matches());
        }
    }
    
    @Test
    public void testRecuperLotsSonarQubeDataStage() throws Exception
    {
        // Mock et appel de la méthode
        mockAPIGetSomething(() -> api.getVues());
        mockAPIGetSomething(() -> api.getVuesParNom(Mockito.anyString()));
        Map<String, Vue> retour = Whitebox.invokeMethod(handler, "recupererLotsSonarQubeDataStage");
        
        // Contrôle sur la liste : non nulle, non vide, et test sur les regex des clefs et des noms des vues
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        for (Map.Entry<String, Vue> entry : retour.entrySet())
        {
            assertTrue(Pattern.compile("^Lot [0-9]{6}").matcher(entry.getValue().getName()).matches());
            assertTrue(Pattern.compile("^[0-9]{6}$").matcher(entry.getKey()).matches());
        }
    }
    
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



    
}
