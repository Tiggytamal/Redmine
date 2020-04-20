package junit.utilities.adapter;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import utilities.adapter.LocalTimeAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalTimeAdapter extends TestAbstractAdapter<LocalTimeAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleUnmarshal(testInfo, "10:10:10", LocalTime.of(10, 10, 10));
    }

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "10:10:10", LocalTime.of(10, 10, 10));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
