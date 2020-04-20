package junit.utilities.adapter;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import utilities.adapter.LocalDateAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalDateAdapter extends TestAbstractAdapter<LocalDateAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleUnmarshal(testInfo, "2018-08-12", LocalDate.of(2018, 8, 12));
    }

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "2018-08-12", LocalDate.of(2018, 8, 12));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
