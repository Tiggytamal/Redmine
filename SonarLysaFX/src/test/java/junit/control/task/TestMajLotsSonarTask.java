package junit.control.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

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
        initAPI(MajLotsSonarTask.class, false);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajLotsSonar() throws Exception
    {
        Whitebox.invokeMethod(handler, "majLotsSonar");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
