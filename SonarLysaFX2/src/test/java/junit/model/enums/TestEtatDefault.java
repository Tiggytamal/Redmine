package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.EtatDefaut;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEtatDefault extends TestAbstractEnum<EtatDefaut>
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

        assertThat(EtatDefaut.valueOf(EtatDefaut.NOUVEAU.toString())).isEqualTo(EtatDefaut.NOUVEAU);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatDefaut.values().length).isEqualTo(5);
    }

}
