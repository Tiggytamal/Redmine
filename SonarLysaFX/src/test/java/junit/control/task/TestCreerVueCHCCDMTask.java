package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueCHCCDMTask;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Edition;
import model.enums.CHCouCDM;
import model.sonarapi.Vue;
import utilities.FunctionalException;

public class TestCreerVueCHCCDMTask extends AbstractTestTask<CreerVueCHCCDMTask>
{
    /*---------- ATTRIBUTS ----------*/
    
    private List<String> annees;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws IllegalAccessException
    {
        annees = new ArrayList<>();
        annees.add("2018");
        handler = new CreerVueCHCCDMTask(annees, CHCouCDM.CDM);
        initAPI(CreerVueCHCCDMTask.class, true);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test (expected = FunctionalException.class)
    public void testCreerVueCHCCDMTaskException1()
    {
        new CreerVueCHCCDMTask(null, CHCouCDM.CDM); 
    }
    
    @Test (expected = FunctionalException.class)
    public void testCreerVueCHCCDMTaskException2()
    {
        new CreerVueCHCCDMTask(new ArrayList<>(), CHCouCDM.CDM);
    }
    
    @Test
    public void testCreerVueCHCouCDM() throws Exception
    {
        // test du retour avec les méthode mockées. On appel par la méthode call
        assertTrue(Whitebox.invokeMethod(handler, "call"));
        
        // Vérification qu'on appele au moins une fois, l'api pour supprimer et créer les vues
        Mockito.verify(api, Mockito.atLeast(1)).supprimerProjet(Mockito.anyString(), Mockito.anyBoolean());
        Mockito.verify(api, Mockito.atLeast(1)).creerVue(Mockito.any(Vue.class));
        Mockito.verify(api, Mockito.atLeast(1)).ajouterSousVue(Mockito.any(Vue.class), Mockito.any(Vue.class));
    }
    
    @Test
    public void testSuppressionVuesMaintenance() throws Exception
    {
        Whitebox.invokeMethod(handler, "suppressionVuesMaintenance", CHCouCDM.CHC, annees);
        Mockito.verify(api, Mockito.times(52)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
        Whitebox.invokeMethod(handler, "suppressionVuesMaintenance", CHCouCDM.CDM, annees);
        Mockito.verify(api, Mockito.times(104)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
    }
    
    @Test
    public void testPreparerMapVuesMaintenance() throws Exception
    {
        Map<String, String> map = Whitebox.invokeMethod(handler, "recupererEditions", annees);
        Map<String, Set<String>> retour = Whitebox.invokeMethod(handler, "preparerMapVuesMaintenance", map);
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        for (String key : retour.keySet())
        {
            assertTrue(key.contains(CHCouCDM.CDM.toString()));
        }
    }
    
    @Test
    public void testControle() throws Exception
    {
        String methode = "controle";
        assertTrue(Whitebox.invokeMethod(handler, methode, CHCouCDM.CDM, "CDM2018"));
        assertFalse(Whitebox.invokeMethod(handler, methode, CHCouCDM.CDM, "CHC2018"));
        assertTrue(Whitebox.invokeMethod(handler, methode, CHCouCDM.CHC, "CHC2018"));
        assertFalse(Whitebox.invokeMethod(handler, methode, CHCouCDM.CHC, "CDM2018"));
    }  
    
    @Test
    public void testCreerVuesMaintenance() throws Exception
    {
        // Initialisation mock et map d'entrée
        PowerMockito.when(api.creerVue(Mockito.any(Vue.class))).thenReturn(Status.OK);   
        Map<String, Set<String>> mapVuesACreer = new HashMap<>();
        Set<String> hashSet = new HashSet<>();
        hashSet.add("set1");
        hashSet.add("set2");
        mapVuesACreer.put("key1", hashSet);
        
        // Appel méthode
        Whitebox.invokeMethod(handler, "creerVuesMaintenance", mapVuesACreer);
        
        Mockito.verify(api, Mockito.times(1)).creerVue(Mockito.any(Vue.class));
        Mockito.verify(api, Mockito.times(2)).ajouterSousVue(Mockito.any(Vue.class), Mockito.any(Vue.class));      
    }
    
    @Test
    public void testRecupererEditions() throws Exception
    {
        // Apple méthode normale et vérification que l'on a bien que des édition 2018 aevc CHC ou CDM
        Map<String, String> map = Whitebox.invokeMethod(handler, "recupererEditions", annees);
        for (String string : map.values())
        {
            assertTrue(string.contains(annees.get(0)));
            assertTrue(string.contains("CHC") || string.contains("CDM"));
        }       
        
        // rajout d'une nouvelle valeur dans la map
        Map<String, Edition> mapTest = DaoFactory.getDao(Edition.class).readAllMap();
        mapTest.put("2019", ModelFactory.getModelWithParams(Edition.class, "2019","2019"));
        
        // Appel méthode et controle qu'on a bien supprimé la nouvelle valeur.
        map = Whitebox.invokeMethod(handler, "recupererEditions", annees);
        assertNull(map.get("2019"));       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}