package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.Matiere;
import model.enums.OptionTypeLong;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestOptionTypeLong extends TestAbstractEnum<OptionTypeLong>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        assertThat(OptionTypeLong.valueOf(OptionTypeLong.JOUR.toString())).isEqualTo(OptionTypeLong.JOUR);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        assertThat(Matiere.values().length).isEqualTo(7);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
