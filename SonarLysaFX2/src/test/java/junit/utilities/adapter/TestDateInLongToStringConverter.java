package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.JunitBase;
import utilities.adapter.DateInLongToStringConverter;

public class TestDateInLongToStringConverter extends JunitBase<DateInLongToStringConverter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @BeforeEach
    @Override
    public void init() throws Exception
    {   
        objetTest = new DateInLongToStringConverter();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.toString(1578870000000L)).isEqualTo("2020-01-13");
    }
    
    @Test
    public void testFromString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.fromString("2020-01-13")).isEqualTo(1578870000000L);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
