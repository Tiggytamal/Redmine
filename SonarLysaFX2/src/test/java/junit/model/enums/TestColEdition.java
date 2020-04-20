package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColEdition;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColEdition extends TestAbstractEnum<ColEdition>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColEdition.values().length).isEqualTo(6);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColEdition.LIBELLE.getValeur()).isEqualTo("Libelle");
        assertThat(ColEdition.VERSION.getValeur()).isEqualTo("Numero de version");
        assertThat(ColEdition.COMMENTAIRE.getValeur()).isEqualTo("Commentaire");
        assertThat(ColEdition.SEMAINE.getValeur()).isEqualTo("Semaine");
        assertThat(ColEdition.TYPE.getValeur()).isEqualTo("Type de changement");
        assertThat(ColEdition.EDITION.getValeur()).isEqualTo("Edition majeure");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColEdition.LIBELLE.getNomCol()).isEqualTo("colLib");
        assertThat(ColEdition.VERSION.getNomCol()).isEqualTo("colNumero");
        assertThat(ColEdition.COMMENTAIRE.getNomCol()).isEqualTo("colComment");
        assertThat(ColEdition.SEMAINE.getNomCol()).isEqualTo("colSemaine");
        assertThat(ColEdition.TYPE.getNomCol()).isEqualTo("colType");
        assertThat(ColEdition.EDITION.getNomCol()).isEqualTo("colEdition");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColEdition.valueOf(ColEdition.VERSION.toString())).isEqualTo(ColEdition.VERSION);
    }
}
