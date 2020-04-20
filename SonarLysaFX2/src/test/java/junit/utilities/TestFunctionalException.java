package junit.utilities;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.Severity;
import utilities.FunctionalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestFunctionalException extends JunitBase<FunctionalException>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new FunctionalException(Severity.ERROR, "message");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRunTime(TestInfo testInfo)
    {
        // Test si l'exception est bien de type RunTime
        assertThat(objetTest).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testGetSeverity(TestInfo testInfo)
    {
        // Test que la severite est bien ERROR
        assertThat(objetTest.getSeverity()).isEqualTo(Severity.ERROR);

        objetTest = new FunctionalException(Severity.INFO, "message");

        // Test que la severite est bien INFO
        assertThat(objetTest.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testGetMessage(TestInfo testInfo)
    {
        // Test le message de l'exception
        assertThat(objetTest.getMessage()).isEqualTo("message");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
