package junit.utilities;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import utilities.Statics;

public class TestStatics
{
    @Test (expected = AssertionError.class)
    public void testConstructeurException() throws Exception
    {
        // Test que l'on ne peut pas invoquer le constructeur m�me par r�flexion
        Whitebox.invokeConstructor(Statics.class);
    }
}
