package junit.utilities.adapter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeObjetSonar;
import utilities.adapter.TypeObjetSonarAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeObjetSonarAdapter extends TestAbstractAdapter<TypeObjetSonarAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "DIR", TypeObjetSonar.DIRECTORY);
    }

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "APP", TypeObjetSonar.APPLI);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
