package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColClarity;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColClarity extends TestAbstractEnum<ColClarity>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColClarity.values().length).isEqualTo(8);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColClarity.ACTIF.getValeur()).isEqualTo("Actif");
        assertThat(ColClarity.CLARITY.getValeur()).isEqualTo("Code projet");
        assertThat(ColClarity.LIBELLE.getValeur()).isEqualTo("Libelle projet");
        assertThat(ColClarity.CPI.getValeur()).isEqualTo("Chef de projet");
        assertThat(ColClarity.EDITION.getValeur()).isEqualTo("Edition");
        assertThat(ColClarity.DIRECTION.getValeur()).isEqualTo("Direction");
        assertThat(ColClarity.DEPARTEMENT.getValeur()).isEqualTo("Departement");
        assertThat(ColClarity.SERVICE.getValeur()).isEqualTo("Service");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColClarity.ACTIF.getNomCol()).isEqualTo("colActif");
        assertThat(ColClarity.CLARITY.getNomCol()).isEqualTo("colClarity");
        assertThat(ColClarity.LIBELLE.getNomCol()).isEqualTo("colLib");
        assertThat(ColClarity.CPI.getNomCol()).isEqualTo("colCpi");
        assertThat(ColClarity.EDITION.getNomCol()).isEqualTo("colEdition");
        assertThat(ColClarity.DIRECTION.getNomCol()).isEqualTo("colDir");
        assertThat(ColClarity.DEPARTEMENT.getNomCol()).isEqualTo("colDepart");
        assertThat(ColClarity.SERVICE.getNomCol()).isEqualTo("colService");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColClarity.valueOf(ColClarity.SERVICE.toString())).isEqualTo(ColClarity.SERVICE);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
