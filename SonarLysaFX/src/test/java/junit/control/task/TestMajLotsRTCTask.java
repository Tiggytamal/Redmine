package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.rtc.ControlRTC;
import control.task.MajLotsRTCTask;

public class TestMajLotsRTCTask extends AbstractTestTask<MajLotsRTCTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = new MajLotsRTCTask(null, false);
        ControlRTC.INSTANCE.connexion();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testAnnuler() throws IllegalAccessException
    {
        // Appel méthode
        handler.annuler();

        // Test que l'on n'annule pas la tâche
        assertFalse(handler.isCancelled());
    }
    
    @Test
    public void testMajFichierRTC() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
