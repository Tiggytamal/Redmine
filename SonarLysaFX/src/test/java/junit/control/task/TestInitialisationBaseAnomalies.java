package junit.control.task;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.task.InitBaseAnosTask;

public class TestInitialisationBaseAnomalies extends AbstractTestTask<InitBaseAnosTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {   
        handler = new InitBaseAnosTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCall() throws Exception
    {
       assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
