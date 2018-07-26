package junit.control.task;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerVueParAppsTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.CreerVueParAppsTaskOption;

@RunWith(JfxRunner.class)
public class TestCreerVueParAppsTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private CreerVueParAppsTask handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerVueParApplicationExcelSeul() throws Exception
    {
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.FICHIER, new File("d:\\testExtract1.xlsx")); 
        Whitebox.invokeMethod(handler, "creerVueParApplication");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
