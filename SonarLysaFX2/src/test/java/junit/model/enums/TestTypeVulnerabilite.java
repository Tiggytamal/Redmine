package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeVulnerabilite;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeVulnerabilite extends TestAbstractEnum<TypeVulnerabilite>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVulnerabilite.valueOf(TypeVulnerabilite.OUVERTE.toString())).isEqualTo(TypeVulnerabilite.OUVERTE);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVulnerabilite.values().length).isEqualTo(2);
    }

    @Test
    public void testGetBooleen(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVulnerabilite.OUVERTE.getBooleen()).isEqualTo("false");
        assertThat(TypeVulnerabilite.RESOLUE.getBooleen()).isEqualTo("true");
    }

    @Test
    public void testGetNomSheet(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeVulnerabilite.OUVERTE.getNomSheet()).isEqualTo("Ouvertes");
        assertThat(TypeVulnerabilite.RESOLUE.getNomSheet()).isEqualTo("Resolues");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
