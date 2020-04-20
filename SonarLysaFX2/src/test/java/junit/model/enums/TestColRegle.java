package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColRegle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColRegle extends TestAbstractEnum<ColRegle>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColRegle.values().length).isEqualTo(8);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColRegle.NOM.getValeur()).isEqualTo("Nom");
        assertThat(ColRegle.DESCRIPTION.getValeur()).isEqualTo("Description");
        assertThat(ColRegle.KEY.getValeur()).isEqualTo("Clef");
        assertThat(ColRegle.LANGAGE.getValeur()).isEqualTo("Langage");
        assertThat(ColRegle.SEVERITE.getValeur()).isEqualTo("Severite");
        assertThat(ColRegle.TAGS.getValeur()).isEqualTo("Tags");
        assertThat(ColRegle.TYPE.getValeur()).isEqualTo("Type");
        assertThat(ColRegle.ACTIVATION.getValeur()).isEqualTo("Profils activés");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColRegle.NOM.getNomCol()).isEqualTo("colNom");
        assertThat(ColRegle.DESCRIPTION.getNomCol()).isEqualTo("colDesc");
        assertThat(ColRegle.KEY.getNomCol()).isEqualTo("colKey");
        assertThat(ColRegle.LANGAGE.getNomCol()).isEqualTo("colLang");
        assertThat(ColRegle.SEVERITE.getNomCol()).isEqualTo("colSeverite");
        assertThat(ColRegle.TAGS.getNomCol()).isEqualTo("colTags");
        assertThat(ColRegle.TYPE.getNomCol()).isEqualTo("colType");
        assertThat(ColRegle.ACTIVATION.getNomCol()).isEqualTo("colActivation");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColRegle.valueOf(ColRegle.NOM.toString())).isEqualTo(ColRegle.NOM);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
