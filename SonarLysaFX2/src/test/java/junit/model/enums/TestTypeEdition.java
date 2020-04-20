package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import model.enums.TypeEdition;
import utilities.Statics;

public class TestTypeEdition extends TestAbstractEnum<TypeEdition>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeEdition.valueOf(TypeEdition.CHC.toString())).isEqualTo(TypeEdition.CHC);
    }

    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeEdition.values().length).isEqualTo(7);
    }

    @Test
    public void testGetTypeEdition(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeEdition.getTypeEdition("CHC")).isEqualTo(TypeEdition.CHC);
        assertThat(TypeEdition.getTypeEdition("CDM")).isEqualTo(TypeEdition.CDM);
        assertThat(TypeEdition.getTypeEdition("FDL")).isEqualTo(TypeEdition.FDL);
        assertThat(TypeEdition.getTypeEdition("INCONNUE")).isEqualTo(TypeEdition.INCONNUE);
        assertThat(TypeEdition.getTypeEdition("MAJEURE")).isEqualTo(TypeEdition.MAJEURE);
        assertThat(TypeEdition.getTypeEdition("MEDIANE")).isEqualTo(TypeEdition.MEDIANE);
        assertThat(TypeEdition.getTypeEdition("CU")).isEqualTo(TypeEdition.CU);
    }
    
    @Test
    public void testGetTypeEdition_INCONNUE(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeEdition.getTypeEdition(Statics.EMPTY)).isEqualTo(TypeEdition.INCONNUE);
        assertThat(TypeEdition.getTypeEdition("autre")).isEqualTo(TypeEdition.INCONNUE);
        assertThat(TypeEdition.getTypeEdition("CDM2")).isEqualTo(TypeEdition.INCONNUE);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
