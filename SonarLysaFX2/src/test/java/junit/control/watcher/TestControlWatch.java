package junit.control.watcher;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.watcher.ControlWatch;
import junit.AutoDisplayName;
import junit.JunitBase;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlWatch extends JunitBase<ControlWatch>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {

    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Disabled(TESTMANUEL)
    public void testLancementBoucle(TestInfo testInfo) throws IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        ControlWatch.INSTANCE.lancementBoucle();
        ControlWatch.INSTANCE.stopBoucle();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
