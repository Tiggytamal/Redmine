package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueCHCCDMTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.CHCouCDM;
import sonarapi.SonarAPI;
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
    public void init() throws IllegalArgumentException, IllegalAccessException
    {
        annees = new ArrayList<>();
        annees.add("2018");
        task = new CreerVueCHCCDMTask(annees, CHCouCDM.CDM);
        SonarAPI mock = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueCHCCDMTask.class, "api").set(task, mock);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test (expected = FunctionalException.class)
    public void creerVueCHCCDMTaskException1()
    {
        new CreerVueCHCCDMTask(null, CHCouCDM.CDM);
    }
    
    @Test (expected = FunctionalException.class)
    public void creerVueCHCCDMTaskException2()
    {
        new CreerVueCHCCDMTask(new ArrayList<>(), CHCouCDM.CDM);
    }
    
    @Test
    public void controle() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(task, "controle", CHCouCDM.CDM, "CDM2018"));
        assertFalse(Whitebox.invokeMethod(task, "controle", CHCouCDM.CDM, "CHC2018"));
        assertTrue(Whitebox.invokeMethod(task, "controle", CHCouCDM.CHC, "CHC2018"));
        assertFalse(Whitebox.invokeMethod(task, "controle", CHCouCDM.CHC, "CDM2018"));
    }
    
    @Test
    public void suppressionVuesMaintenance() throws Exception
    {

        Whitebox.invokeMethod(task, "suppressionVuesMaintenance", CHCouCDM.CHC, annees);
        Mockito.verify(mock, Mockito.times(52)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
        Whitebox.invokeMethod(task, "suppressionVuesMaintenance", CHCouCDM.CDM, annees);
        Mockito.verify(mock, Mockito.times(104)).supprimerProjet(Mockito.anyString(), Mockito.eq(false));
    }
    
    @Test
    public void recupererEditions() throws Exception
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