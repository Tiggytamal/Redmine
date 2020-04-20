package junit.utilities.adapter;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeBranche;
import utilities.adapter.TypeBrancheAdapter;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeBrancheAdapter extends TestAbstractAdapter<TypeBrancheAdapter>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testMarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "LONG", TypeBranche.LONG);
    }

    @Test
    @Override
    public void testUnmarshal(TestInfo testInfo) throws Exception
    {
        testSimpleMarshal(testInfo, "SHORT", TypeBranche.SHORT);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
