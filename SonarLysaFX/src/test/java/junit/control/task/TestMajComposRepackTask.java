package junit.control.task;

import org.junit.Test;

import control.task.MajComposRepackTask;

public class TestMajComposRepackTask extends AbstractTestTask<MajComposRepackTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new MajComposRepackTask();       
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajComposRepack() throws Exception
    {
        handler.call();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
