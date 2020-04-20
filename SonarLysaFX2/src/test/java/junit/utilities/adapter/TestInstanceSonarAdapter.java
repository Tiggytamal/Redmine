package junit.utilities.adapter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.InstanceSonar;
import utilities.adapter.InstanceSonarAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestInstanceSonarAdapter extends TestAbstractAdapter<InstanceSonarAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleUnmarshal(testInfo, "legacy", InstanceSonar.LEGACY);
    }

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "legacy", InstanceSonar.LEGACY);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
