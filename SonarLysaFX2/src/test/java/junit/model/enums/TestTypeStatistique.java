package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import model.enums.StatistiqueEnum;

public class TestTypeStatistique extends TestAbstractEnum<StatistiqueEnum>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueEnum.valueOf(StatistiqueEnum.DEFAUTSENCOURS.toString())).isEqualTo(StatistiqueEnum.DEFAUTSENCOURS);
    }

    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueEnum.values().length).isEqualTo(3);
    }

    @Test
    public void testGetLabel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueEnum.DEFAUTSENCOURS.getLabel()).isEqualTo("Défauts en cours");
        assertThat(StatistiqueEnum.FICHIERSPARJOUR.getLabel()).isEqualTo("Assemblages effectués");
        assertThat(StatistiqueEnum.COMPOSKO.getLabel()).isEqualTo("Composants QG KO");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
