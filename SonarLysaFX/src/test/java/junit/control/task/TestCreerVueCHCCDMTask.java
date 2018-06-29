package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.junit.Before;
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


    private CreerVueCHCCDMTask task;
    private List<String> annees;
    private SonarAPI mock;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws IllegalAccessException
    {
        annees = new ArrayList<>();
        annees.add("2018");
        task = new CreerVueCHCCDMTask(annees, CHCouCDM.CDM);
        mock = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueCHCCDMTask.class, "api").set(task, mock);
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
        task = PowerMockito.spy(task);
        assertTrue(Whitebox.invokeMethod(task, "call"));
    }
    
    @Test
    public void testCreerVueCHCouCDM() throws Exception
    {
        // test du retour avec les méthode mockées
        task = PowerMockito.spy(task);
        assertTrue(Whitebox.invokeMethod(task, "creerVueCHCouCDM"));
    }
    
    @Test
    public void testSuppressionVuesMaintenance() throws Exception
    {
        Whitebox.invokeMethod(task, "suppressionVuesMaintenance", CHCouCDM.CHC, annees);
        Mockito.verify(mock, Mockito.times(52)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
        Whitebox.invokeMethod(task, "suppressionVuesMaintenance", CHCouCDM.CDM, annees);
        Mockito.verify(mock, Mockito.times(104)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
    }
    
    @Test
    public void testCreerVuesMaintenance() throws Exception
    {
        PowerMockito.when(mock.getComposants()).thenCallRealMethod();
        PowerMockito.when(mock.appelWebserviceGET(Mockito.anyString(), Mockito.any())).thenCallRealMethod();
        PowerMockito.when(mock.creerVue(Mockito.any(Vue.class))).thenReturn(Status.OK);    
        PowerMockito.doNothing().when(mock).ajouterSousVue(Mockito.any(Vue.class), Mockito.any(Vue.class));  
        @SuppressWarnings("unused")
        Map<String, String> map = Whitebox.invokeMethod(task, "recupererEditions", annees);
//        Whitebox.invokeMethod(task, "creerVueMaintenance", map);
    }
    
    @Test
    public void testControle() throws Exception
    {
        String methode = "controle";
        assertTrue(Whitebox.invokeMethod(task, methode, CHCouCDM.CDM, "CDM2018"));
        assertFalse(Whitebox.invokeMethod(task, methode, CHCouCDM.CDM, "CHC2018"));
        assertTrue(Whitebox.invokeMethod(task, methode, CHCouCDM.CHC, "CHC2018"));
        assertFalse(Whitebox.invokeMethod(task, methode, CHCouCDM.CHC, "CDM2018"));
    }  
    
    @Test
    public void testRecupererEditions() throws Exception
    {
        Map<String, String> map = Whitebox.invokeMethod(task, "recupererEditions", annees);
        for (String string : map.values())
        {
            assertTrue(string.contains(annees.get(0)));
            assertTrue(string.contains("CHC") || string.contains("CDM"));
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
}