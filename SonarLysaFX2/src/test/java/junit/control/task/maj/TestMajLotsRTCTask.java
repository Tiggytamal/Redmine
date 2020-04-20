package junit.control.task.maj;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import control.rtc.ControlRTC;
import control.task.maj.MajLotsRTCTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMajLotsRTCTask extends TestAbstractTask<MajLotsRTCTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws Exception
    {
        objetTest = new MajLotsRTCTask(null);
        ControlRTC.build().connexionSimple();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Disabled
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();
        // TODO
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
