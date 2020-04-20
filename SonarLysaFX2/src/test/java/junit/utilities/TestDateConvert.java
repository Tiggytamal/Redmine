package junit.utilities;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Calendar;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.DateHelper;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDateConvert extends JunitBase<DateHelper>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        // Pas de traitement
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThrows(AssertionError.class, () -> Whitebox.invokeConstructor(DateHelper.class));
    }

    @Test
    public void testConvert(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test Long to LocalDate
        assertThat(DateHelper.convert(LocalDate.class, 125456L)).isEqualTo(LocalDate.ofEpochDay(125456L));

        // Test Instant to LocalDateTime
        Instant instant = Instant.now();
        assertThat(DateHelper.convert(LocalDateTime.class, instant)).isEqualTo(LocalDateTime.now());

        // Test ZonedDateTime to OffsetTime
        ZonedDateTime zonedTime = ZonedDateTime.now();
        assertThat(DateHelper.convert(OffsetTime.class, zonedTime).getClass().getName()).isEqualTo("java.time.OffsetTime");

        // Test LocalDateTime to ZonedDateTime
        LocalDateTime localTime = LocalDateTime.now();
        assertThat(DateHelper.convert(ZonedDateTime.class, localTime).getClass().getName()).isEqualTo("java.time.ZonedDateTime");

        // Test LocalDate to OffsetDateTime
        assertThat(DateHelper.convert(OffsetDateTime.class, today).getClass().getName()).isEqualTo("java.time.OffsetDateTime");

        // Test TimeStamp to Instant
        assertThat(DateHelper.convert(Instant.class, Timestamp.from(instant))).isEqualTo(instant);

        // Test sql.Date to Year
        assertThat(DateHelper.convert(Year.class, new Date(5230L))).isEqualTo(Year.of(1970));

        // Test util.Date to YearMonth
        assertThat(DateHelper.convert(YearMonth.class, new java.util.Date(523L))).isEqualTo(YearMonth.of(1970, 1));

        // Test exceptions avec paramètres incohérents
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(LocalDate.class, 12));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(LocalTime.class, today));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(null, today, ZoneId.systemDefault()));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(LocalDate.class, null, ZoneId.systemDefault()));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(LocalDate.class, null, ZoneId.systemDefault()));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convert(LocalDate.class, today, null));
    }

    @Test
    public void testLocalDate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test date null
        assertThat(DateHelper.localDate(null)).isNull();

        // Test java.sql.Date
        LocalDate localdate = DateHelper.localDate(new Date(1600000000000L));
        assertThat(localdate).isNotNull();
        assertThat(localdate).isEqualTo(LocalDate.of(2020, 9, 13));

        // Test java.util.Date
        localdate = DateHelper.localDate(new java.util.Date());
        assertThat(localdate).isNotNull();
        assertThat(localdate).isEqualTo(today);
    }

    @Test
    public void testLocalDateTime(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test date null
        assertThat(DateHelper.localDateTime(null)).isNull();

        // Test java.sql.Date
        LocalDateTime localdateTime = DateHelper.localDateTime(new Date(1600000000000L));
        assertThat(localdateTime).isNotNull();
        assertThat(localdateTime).isEqualTo(LocalDateTime.of(2020, 9, 13, 14, 26, 40));

        // Test java.util.Date
        localdateTime = DateHelper.localDateTime(new java.util.Date());
        assertThat(localdateTime).isNotNull();
        assertThat(localdateTime.withNano(0)).isAnyOf(todayTime.withNano(0), todayTime.withNano(0).plusSeconds(1));
    }

    @Test
    public void testConvertToOldDate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet null
        assertThat(DateHelper.convertToOldDate(null)).isNull();

        Calendar cal = Calendar.getInstance();

        // Test TimeStamp et zoneID null => zoneId défaut
        java.util.Date date = DateHelper.convertToOldDate(Timestamp.valueOf(todayTime), null);
        String dateString = date.toString();
        String string = todayTime.toString().replace('T', Statics.SPACE);
        assertWithMessage("Mauvaise conversion date : " + dateString + " - " + string).that(string).contains(dateString);

        // LocalDate
        date = DateHelper.convertToOldDate(today);

        // Contrôle date avec Calendar
        cal.setTime(date);
        assertThat(cal.get(Calendar.YEAR)).isEqualTo(today.getYear());
        assertThat(cal.get(Calendar.MONTH) + 1).isEqualTo(today.getMonthValue());
        assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(today.getDayOfMonth());

        // java.util.Date
        controleDate(cal, DateHelper.convertToOldDate(new java.util.Date()));

        // LocalDateTime
        controleDate(cal, date = DateHelper.convertToOldDate(todayTime));

        // ZonedDateTime
        controleDate(cal, DateHelper.convertToOldDate(ZonedDateTime.from(todayTime.atZone(ZoneId.systemDefault()))));

        // Instant
        controleDate(cal, DateHelper.convertToOldDate(Instant.from(todayTime.atZone(ZoneId.systemDefault()))));

        // Test exception
        assertThrows(IllegalArgumentException.class, () -> DateHelper.convertToOldDate(OffsetDateTime.now()));

        assertThrows(IllegalArgumentException.class, () -> Whitebox.invokeMethod(DateHelper.class, "dateFrancais", new Class[]
        { ChronoLocalDate.class, String.class }, new Object[]
        { null, Statics.EMPTY }));

        assertThrows(IllegalArgumentException.class, () -> Whitebox.invokeMethod(DateHelper.class, "dateFrancais", new Class[]
        { ChronoLocalDate.class, String.class }, new Object[]
        { today, null }));

        assertThrows(IllegalArgumentException.class, () -> Whitebox.invokeMethod(DateHelper.class, "dateFrancais", new Class[]
        { ChronoLocalDateTime.class, String.class }, new Object[]
        { null, Statics.EMPTY }));

        assertThrows(IllegalArgumentException.class, () -> Whitebox.invokeMethod(DateHelper.class, "dateFrancais", new Class[]
        { ChronoLocalDateTime.class, String.class }, new Object[]
        { today, null }));
    }

    @Test
    public void testDateFrancais(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec LocalDate
        assertThat(DateHelper.dateFrancais(LocalDate.of(2018, 1, 1), "yyyy-MM-dd")).isEqualTo("2018-01-01");
        assertThat(DateHelper.dateFrancais(LocalDate.of(2018, 8, 2), "yyyy-MMMM-d")).isEqualTo("2018-aout-2");
        assertThat(DateHelper.dateFrancais(LocalDate.of(2018, 2, 15), "yyyy MMM dd")).isEqualTo("2018 fevr. 15");

        // Test avec LocalDateTime
        assertThat(DateHelper.dateFrancais(LocalDateTime.of(2018, 1, 1, 12, 10, 10), "yyyy-MM-dd HH:mm:ss")).isEqualTo("2018-01-01 12:10:10");
        assertThat(DateHelper.dateFrancais(LocalDateTime.of(2018, 1, 1, 1, 10, 10), "yyyy-MMMM-d H:mm:ss")).isEqualTo("2018-janvier-1 1:10:10");
        assertThat(DateHelper.dateFrancais(LocalDateTime.of(2018, 1, 1, 2, 10, 10), "yyyy MMM dd HH mm ss")).isEqualTo("2018 janv. 01 02 10 10");

    }

    /*---------- METHODES PRIVEES ----------*/

    private void controleDate(Calendar cal, java.util.Date date)
    {
        cal.setTime(date);
        assertThat(cal.get(Calendar.YEAR)).isEqualTo(todayTime.getYear());
        assertThat(cal.get(Calendar.MONTH) + 1).isEqualTo(todayTime.getMonthValue());
        assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(todayTime.getDayOfMonth());
        assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(todayTime.getHour());
        assertThat(cal.get(Calendar.MINUTE)).isEqualTo(todayTime.getMinute());
    }

    /*---------- ACCESSEURS ----------*/
}
