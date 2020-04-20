package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.adapter.LocalTimeConverter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalTimeConverter extends JunitBase<LocalTimeConverter>
{

    /*---------- ATTRIBUTS ----------*/

    private LocalTime localTime;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new LocalTimeConverter();
        localTime = LocalTime.now();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConvertToDatabaseColumn(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Calendar cal = Calendar.getInstance();

        // Test objet null
        assertThat(objetTest.convertToDatabaseColumn(null)).isNull();

        // Test méthode
        Time time = objetTest.convertToDatabaseColumn(localTime);
        assertThat(time).isNotNull();
        cal.setTime(time);
        assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(localTime.getHour());
        assertThat(cal.get(Calendar.MINUTE)).isEqualTo(localTime.getMinute());
        assertThat(cal.get(Calendar.SECOND)).isEqualTo(localTime.getSecond());
    }

    @Test
    public void testConvertToEntityAttribute(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Time time = Time.valueOf(localTime);
        Calendar cal = Calendar.getInstance();

        // Test objet null
        assertThat(objetTest.convertToEntityAttribute(null)).isNull();

        // Test méthode
        LocalTime date = objetTest.convertToEntityAttribute(time);
        assertThat(date).isNotNull();
        cal.setTime(time);
        assertThat(date.getHour()).isEqualTo(cal.get(Calendar.HOUR_OF_DAY));
        assertThat(date.getMinute()).isEqualTo(cal.get(Calendar.MINUTE));
        assertThat(date.getSecond()).isEqualTo(cal.get(Calendar.SECOND));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
