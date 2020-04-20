package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.job.JobStatistiques;
import control.job.JobVuesCDM;
import control.job.JobVuesCHC;
import junit.AutoDisplayName;
import model.enums.TypePlan;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypePlan extends TestAbstractEnum<TypePlan>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypePlan.values().length).isEqualTo(3);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypePlan.VUECHC.getValeur()).isEqualTo("Vues CHC");
        assertThat(TypePlan.VUECDM.getValeur()).isEqualTo("Vues CHC_CDM");
        assertThat(TypePlan.STATS.getValeur()).isEqualTo("Statistiques");
    }

    @Test
    public void testGetClassJob(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypePlan.VUECHC.getClassJob()).isEqualTo(JobVuesCHC.class);
        assertThat(TypePlan.VUECDM.getClassJob()).isEqualTo(JobVuesCDM.class);
        assertThat(TypePlan.STATS.getClassJob()).isEqualTo(JobStatistiques.class);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypePlan.valueOf(TypePlan.VUECDM.toString())).isEqualTo(TypePlan.VUECDM);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
