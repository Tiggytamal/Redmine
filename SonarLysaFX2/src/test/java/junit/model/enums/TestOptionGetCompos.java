package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.OptionGetCompos;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestOptionGetCompos extends TestAbstractEnum<OptionGetCompos>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(OptionGetCompos.valueOf(OptionGetCompos.COMPLETE.toString())).isEqualTo(OptionGetCompos.COMPLETE);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(OptionGetCompos.values().length).isEqualTo(3);
    }
}
