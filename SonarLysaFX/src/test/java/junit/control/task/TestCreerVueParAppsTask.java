package junit.control.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueParAppsTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestCreerVueParAppsTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private CreerVueParAppsTask handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new CreerVueParAppsTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerVueParApplication() throws Exception
    {
        Whitebox.invokeMethod(handler, "creerVueParApplication");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
