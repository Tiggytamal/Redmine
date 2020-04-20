package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.ParamAPI;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamAPI extends TestAbstractModele<ParamAPI>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = new ParamAPI(KEY, NOM);
        assertThat(objetTest.getClef()).isEqualTo(KEY);
        assertThat(objetTest.getValeur()).isEqualTo(NOM);
    }

    @Test
    public void testGetClef(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getClef(), s -> objetTest.setClef(s));
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getValeur(), s -> objetTest.setValeur(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
