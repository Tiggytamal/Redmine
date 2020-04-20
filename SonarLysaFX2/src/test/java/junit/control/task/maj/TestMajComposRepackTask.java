package junit.control.task.maj;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.task.maj.MajComposRepackTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMajComposRepackTask extends TestAbstractTask<MajComposRepackTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws Exception
    {
        objetTest = new MajComposRepackTask();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Disabled
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.call();
        // TODO
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
