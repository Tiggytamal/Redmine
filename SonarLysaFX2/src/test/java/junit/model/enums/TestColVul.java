package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColVul;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColVul extends TestAbstractEnum<ColVul>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColVul.values().length).isEqualTo(9);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColVul.LOT.getValeur()).isEqualTo("Lot");
        assertThat(ColVul.SEVERITE.getValeur()).isEqualTo("Severite");
        assertThat(ColVul.CLARITY.getValeur()).isEqualTo("Code Clarity");
        assertThat(ColVul.STATUS.getValeur()).isEqualTo("Status");
        assertThat(ColVul.MESSAGE.getValeur()).isEqualTo("Message");
        assertThat(ColVul.DATECREA.getValeur()).isEqualTo("Date création");
        assertThat(ColVul.APPLI.getValeur()).isEqualTo("Appli");
        assertThat(ColVul.COMPOSANT.getValeur()).isEqualTo("Composant");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColVul.LOT.getNomCol()).isEqualTo("colLot");
        assertThat(ColVul.SEVERITE.getNomCol()).isEqualTo("colSeverity");
        assertThat(ColVul.CLARITY.getNomCol()).isEqualTo("colClarity");
        assertThat(ColVul.STATUS.getNomCol()).isEqualTo("colStatus");
        assertThat(ColVul.MESSAGE.getNomCol()).isEqualTo("colMess");
        assertThat(ColVul.DATECREA.getNomCol()).isEqualTo("colDateCrea");
        assertThat(ColVul.APPLI.getNomCol()).isEqualTo("colAppli");
        assertThat(ColVul.COMPOSANT.getNomCol()).isEqualTo("colComp");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColVul.valueOf(ColVul.DATECREA.toString())).isEqualTo(ColVul.DATECREA);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
