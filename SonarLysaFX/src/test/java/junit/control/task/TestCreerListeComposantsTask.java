package junit.control.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerListeComposantsTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestCreerListeComposantsTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private CreerListeComposantsTask handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new CreerListeComposantsTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerListe() throws Exception
    {
        Whitebox.invokeMethod(handler, "creerListe");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
