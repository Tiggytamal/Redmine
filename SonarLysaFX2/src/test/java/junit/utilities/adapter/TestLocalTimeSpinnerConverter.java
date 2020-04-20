package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.JunitBase;
import utilities.adapter.LocalTimeSpinnerConverter;

public class TestLocalTimeSpinnerConverter extends JunitBase<LocalTimeSpinnerConverter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new LocalTimeSpinnerConverter();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        assertThat(objetTest.toString(LocalTime.of(10, 00))).isEqualTo("10:00");
    }
    
    @Test
    public void testFromString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        assertThat(objetTest.fromString("10:00")).isEqualTo(LocalTime.of(10, 00));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
