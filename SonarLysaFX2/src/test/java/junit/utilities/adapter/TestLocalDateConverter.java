package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.adapter.LocalDateConverter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLocalDateConverter extends JunitBase<LocalDateConverter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new LocalDateConverter();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConvertToDatabaseColumn(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet null
        assertThat(objetTest.convertToDatabaseColumn(null)).isNull();

        // Test méthode
        Date date = objetTest.convertToDatabaseColumn(today);
        assertThat(date).isNotNull();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertThat(cal.get(Calendar.YEAR)).isEqualTo(today.getYear());
        assertThat(cal.get(Calendar.MONTH) + 1).isEqualTo(today.getMonthValue());
        assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(today.getDayOfMonth());
    }

    @Test
    public void testConvertToEntityAttribute(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet null
        assertThat(objetTest.convertToEntityAttribute(null)).isNull();

        // Test méthode
        LocalDate date = objetTest.convertToEntityAttribute(new Date(16000000000L));
        assertThat(date).isEqualTo(LocalDate.of(1970, 7, 5));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
