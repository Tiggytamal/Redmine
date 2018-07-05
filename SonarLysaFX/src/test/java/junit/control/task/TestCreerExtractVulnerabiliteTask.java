package junit.control.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerExtractVulnerabiliteTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestCreerExtractVulnerabiliteTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private CreerExtractVulnerabiliteTask handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        handler = new CreerExtractVulnerabiliteTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerExtract() throws Exception
    {
        Whitebox.invokeMethod(handler, "creerExtract");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
