package junit.control.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import control.rtc.ControlRTC;
import control.task.MajSuiviExcelTask;
import control.task.MajSuiviExcelTask.TypeMaj;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestMajSuiviExcelTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private MajSuiviExcelTask handler;
    private ControlRTC control = ControlRTC.INSTANCE;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new MajSuiviExcelTask(TypeMaj.MULTI);
        control.connexion();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajSuiviExcel() throws Exception
    {
        // Whitebox.invokeMethod(handler, "majSuiviExcel");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
