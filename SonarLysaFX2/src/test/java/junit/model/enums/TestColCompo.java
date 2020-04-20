package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColCompo;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColCompo extends TestAbstractEnum<ColCompo>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColCompo.values().length).isEqualTo(8);
    }

    @Test

    public void getValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColCompo.NOM.getValeur()).isEqualTo("Nom composant");
        assertThat(ColCompo.NOUVEAU.getValeur()).isEqualTo("Nouvelle version");
        assertThat(ColCompo.VERSION.getValeur()).isEqualTo("Version");
        assertThat(ColCompo.QG.getValeur()).isEqualTo("QualityGate");
        assertThat(ColCompo.ANALYSE.getValeur()).isEqualTo("Date dernière analyse");
        assertThat(ColCompo.NBRELIGNE.getValeur()).isEqualTo("Nombre de ligne de code");
        assertThat(ColCompo.COMPON1.getValeur()).isEqualTo("Composant N-1");
        assertThat(ColCompo.QGN1.getValeur()).isEqualTo("Quality Gate N-1");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColCompo.NOM.getNomCol()).isEqualTo("colNom");
        assertThat(ColCompo.NOUVEAU.getNomCol()).isEqualTo("colNouv");
        assertThat(ColCompo.VERSION.getNomCol()).isEqualTo("colVersion");
        assertThat(ColCompo.QG.getNomCol()).isEqualTo("colQG");
        assertThat(ColCompo.ANALYSE.getNomCol()).isEqualTo("colAnalyse");
        assertThat(ColCompo.NBRELIGNE.getNomCol()).isEqualTo("colNbreLigne");
        assertThat(ColCompo.COMPON1.getNomCol()).isEqualTo("colN1");
        assertThat(ColCompo.QGN1.getNomCol()).isEqualTo("colQGN1");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColCompo.valueOf(ColCompo.NOUVEAU.toString())).isEqualTo(ColCompo.NOUVEAU);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
