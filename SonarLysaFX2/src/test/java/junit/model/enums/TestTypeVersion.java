package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeVersion;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeVersion extends TestAbstractEnum<TypeVersion>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVersion.valueOf(TypeVersion.RELEASE.toString())).isEqualTo(TypeVersion.RELEASE);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVersion.values().length).isEqualTo(2);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
