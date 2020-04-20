package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import utilities.adapter.LocalDateTimeSonarAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalDateTimeSonarAdapter extends TestAbstractAdapter<LocalDateTimeSonarAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleUnmarshal(testInfo, "1950-06-12T13:08", LocalDateTime.of(1950, 06, 12, 13, 8));

        // Tests supplémentaires
        assertThat(objetTest.unmarshal("2019-12-01T10:10:10+0200")).isEqualTo(LocalDateTime.of(2019, 12, 01, 10, 10, 10));
        assertThat(objetTest.unmarshal("2012-01-20T20:15:25")).isEqualTo(LocalDateTime.of(2012, 01, 20, 20, 15, 25));
    }

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, todayTime.toString(), todayTime);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
