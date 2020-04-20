package junit.utilities;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.Severity;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTechnicalException extends JunitBase<TechnicalException>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new TechnicalException("message", new RuntimeException());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRunTime(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test si l'exception est bien de type RunTime
        assertThat(objetTest).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testGetSeverity(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test que la sevérité est bien ERROR
        assertThat(objetTest.getSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    public void testGetMessage(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test le message de l'exception
        assertThat(objetTest.getMessage()).isEqualTo("message");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
