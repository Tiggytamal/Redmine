package junit.utilities;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAbstractToStringImpl extends JunitBase<TestToString>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new TestToString("a", "b");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getA()).isEqualTo("a");
        assertThat(objetTest.getB()).isEqualTo("b");
        String test = objetTest.toString();
        assertThat(test).contains("junit.utilities.TestToString");
        assertThat(test).contains("a=a");
        assertThat(test).contains("b=b");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSE INTERNE ----------*/

}
