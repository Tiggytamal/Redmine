package junit.control.task;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.MajVuesTask;

public class TestMajVuesTask extends AbstractTestTask<MajVuesTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new MajVuesTask();
        initAPI(MajVuesTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajVues() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
        Mockito.verify(api, Mockito.times(1)).majVues();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
