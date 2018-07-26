package junit.control.task;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.CreerVueCHCCDMTask;
import control.task.CreerVueDataStageTask;
import junit.JunitBase;

public class TestCreerVueDataStage extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    private CreerVueDataStageTask handler;
    private SonarAPI mock;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws IllegalArgumentException, IllegalAccessException
    {
        mock = Mockito.mock(SonarAPI.class);
        Whitebox.getField(CreerVueCHCCDMTask.class, "api").set(handler, mock);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerVueDatastage() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
