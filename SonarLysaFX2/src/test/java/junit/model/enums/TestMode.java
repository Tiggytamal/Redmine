package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.Mode;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMode extends TestAbstractEnum<Mode>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        assertThat(Mode.valueOf(Mode.HOURS.toString())).isEqualTo(Mode.HOURS);
    }

    @Override
    public void testLength(TestInfo testInfo)
    {
        assertThat(Mode.values().length).isEqualTo(2);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
