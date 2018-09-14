package junit.control.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.MajLotsSonarTask;
import de.saxsys.javafx.test.JfxRunner;

@RunWith(JfxRunner.class)
public class TestMajLotsSonarTask extends AbstractTestTask<MajLotsSonarTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new MajLotsSonarTask();
        initAPI(MajLotsSonarTask.class, true);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajLotsSonar() throws Exception
    {
        mockAPIGetSomething(() -> api.getVues());
        Mockito.when(((SonarAPI)Whitebox.getField(MajLotsSonarTask.class, "api").get(handler)).setManualMesureView(Mockito.anyString())).thenReturn(true);
        Whitebox.invokeMethod(handler, "majLotsSonar");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
