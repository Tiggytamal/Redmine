package junit.utilities;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.JunitBase;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatics extends JunitBase<Statics>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        // Pas d'instanciation
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    public void testConstructeurException(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test que l'on ne peut pas invoquer le constructeur même par reflexion
        assertThrows(AssertionError.class, () -> Whitebox.invokeConstructor(Statics.class));
    }

}
