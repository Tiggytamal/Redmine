package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.OptionPortfolioTrimestriel;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestOptionVueProduction extends TestAbstractEnum<OptionPortfolioTrimestriel>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(OptionPortfolioTrimestriel.values().length).isEqualTo(2);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(OptionPortfolioTrimestriel.valueOf(OptionPortfolioTrimestriel.ALL.toString())).isEqualTo(OptionPortfolioTrimestriel.ALL);
    }

    @Test
    public void testGetTitre(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(OptionPortfolioTrimestriel.ALL.getTitre()).isEqualTo(Statics.EMPTY);
        assertThat(OptionPortfolioTrimestriel.DATASTAGE.getTitre()).isEqualTo("DataStage");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
