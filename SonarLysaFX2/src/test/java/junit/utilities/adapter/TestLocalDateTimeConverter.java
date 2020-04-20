package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.adapter.LocalDateTimeConverter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalDateTimeConverter extends JunitBase<LocalDateTimeConverter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new LocalDateTimeConverter();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConvertToDatabaseColumn(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet null
        assertThat(objetTest.convertToDatabaseColumn(null)).isNull();

        // Test méthode
        Timestamp date = objetTest.convertToDatabaseColumn(todayTime);
        assertThat(date).isNotNull();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertThat(cal.get(Calendar.YEAR)).isEqualTo(todayTime.getYear());
        assertThat(cal.get(Calendar.MONTH) + 1).isEqualTo(todayTime.getMonthValue());
        assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(todayTime.getDayOfMonth());
        assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(todayTime.getHour());
        assertThat(cal.get(Calendar.MINUTE)).isEqualTo(todayTime.getMinute());
        assertThat(cal.get(Calendar.SECOND)).isEqualTo(todayTime.getSecond());
    }

    @Test
    public void testConvertToEntityAttribute(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Timestamp timestamp = Timestamp.from(Instant.now());
        Calendar cal = Calendar.getInstance();

        // Test objet null
        assertThat(objetTest.convertToEntityAttribute(null)).isNull();

        // Test méthode
        LocalDateTime date = objetTest.convertToEntityAttribute(timestamp);
        assertThat(date).isNotNull();
        cal.setTime(timestamp);
        assertThat(date.getYear()).isEqualTo(cal.get(Calendar.YEAR));
        assertThat(date.getMonthValue()).isEqualTo(cal.get(Calendar.MONTH) + 1);
        assertThat(date.getDayOfMonth()).isEqualTo(cal.get(Calendar.DAY_OF_MONTH));
        assertThat(date.getHour()).isEqualTo(cal.get(Calendar.HOUR_OF_DAY));
        assertThat(date.getMinute()).isEqualTo(cal.get(Calendar.MINUTE));
        assertThat(date.getSecond()).isEqualTo(cal.get(Calendar.SECOND));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
