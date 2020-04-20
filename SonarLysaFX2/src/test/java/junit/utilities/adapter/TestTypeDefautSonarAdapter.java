package junit.utilities.adapter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeDefautSonar;
import utilities.adapter.TypeDefautSonarAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeDefautSonarAdapter extends TestAbstractAdapter<TypeDefautSonarAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "BUG", TypeDefautSonar.BUG);
    }

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "VULNERABILITY", TypeDefautSonar.VULNERABILITY);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
