package junit.utilities.adapter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.QG;
import utilities.adapter.QGAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestQGAdapter extends TestAbstractAdapter<QGAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "ERROR", QG.ERROR);
    }

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleUnmarshal(testInfo, "ERROR", QG.ERROR);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
