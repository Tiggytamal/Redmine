package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.fichiersXML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.CreerVueCHCCDMTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.CHCouCDM;
import model.sonarapi.Vue;
import utilities.FunctionalException;

@RunWith(JfxRunner.class)
public class TestCreerVueCHCCDMTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private CreerVueCHCCDMTask handler;
    private List<String> annees;
    private SonarAPI mock;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws IllegalAccessException
    {
        annees = new ArrayList<>();
        annees.add("2018");
        handler = new CreerVueCHCCDMTask(annees, CHCouCDM.CDM);
        mock = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueCHCCDMTask.class, "api").set(handler, mock);
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
    public void testCall() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    @Test
    public void testCreerVueCHCouCDM() throws Exception
    {
        // test du retour avec les méthode mockées
        assertTrue(Whitebox.invokeMethod(handler, "creerVueCHCouCDM"));
    }
    
    @Test
    public void testSuppressionVuesMaintenance() throws Exception
    {
        Whitebox.invokeMethod(handler, "suppressionVuesMaintenance", CHCouCDM.CHC, annees);
        Mockito.verify(mock, Mockito.times(52)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
        Whitebox.invokeMethod(handler, "suppressionVuesMaintenance", CHCouCDM.CDM, annees);
        Mockito.verify(mock, Mockito.times(104)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
    }
    
    @Test
    public void testCreerVuesMaintenance() throws Exception
    {
        PowerMockito.when(mock.creerVue(Mockito.any(Vue.class))).thenReturn(Status.OK);    
        Map<String, String> map = Whitebox.invokeMethod(handler, "recupererEditions", annees);
        Whitebox.invokeMethod(handler, "creerVueMaintenance", map);
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
        Map<String, String> mapTest = fichiersXML.getMapEditions();
        mapTest.put("2019", "2019");
        
        // Appel méthode et controle qu'on a bien supprimé la nouvelle valeur.
        map = Whitebox.invokeMethod(handler, "recupererEditions", annees);
        assertNull(map.get("2019"));
        
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}