package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.StatistiqueCompoEnum;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatistiqueCompoEnum extends TestAbstractEnum<StatistiqueCompoEnum>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueCompoEnum.valueOf(StatistiqueCompoEnum.NEWLDCTOCOVER.toString())).isEqualTo(StatistiqueCompoEnum.NEWLDCTOCOVER);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueCompoEnum.values().length).isEqualTo(2);
    }

    @Test
    public void testGetLabel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(StatistiqueCompoEnum.NEWLDCTOCOVER.getLabel()).isEqualTo("Nouvelles Lignes de code à couvrir");
        assertThat(StatistiqueCompoEnum.NEWLDCNOCOVER.getLabel()).isEqualTo("Nouvelles lignes de code sans couverture");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
