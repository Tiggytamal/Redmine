package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColAppliDir;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColAppliDepart extends TestAbstractEnum<ColAppliDir>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColAppliDir.values().length).isEqualTo(2);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColAppliDir.APPLI.getValeur()).isEqualTo("Application");
        assertThat(ColAppliDir.DIR.getValeur()).isEqualTo("Direction");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColAppliDir.APPLI.getNomCol()).isEqualTo("colAppli");
        assertThat(ColAppliDir.DIR.getNomCol()).isEqualTo("colDir");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColAppliDir.valueOf(ColAppliDir.DIR.toString())).isEqualTo(ColAppliDir.DIR);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
