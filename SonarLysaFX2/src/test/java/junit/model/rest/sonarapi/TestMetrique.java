package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.Metrique;
import model.rest.sonarapi.Mesure;
import model.rest.sonarapi.Periode;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMetrique extends TestAbstractModele<Mesure>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test constructeur avec vérification valeurs initiales
        objetTest = new Mesure(Metrique.APPLI, "ABCD");
        assertThat(objetTest.getListePeriodes()).isNotNull();
        assertThat(objetTest.getValeur()).isNotNull();
        assertThat(objetTest.getValeur()).isEqualTo("ABCD");
        assertThat(objetTest.getType()).isEqualTo(Metrique.APPLI);

        // Test constructeur avec seulement type de Metrique
        objetTest = new Mesure(Metrique.APPLI);
        assertThat(objetTest.getListePeriodes()).isNotNull();
        assertThat(objetTest.getValeur()).isNotNull();
        assertThat(objetTest.getValeur().length()).isEqualTo(0);
        assertThat(objetTest.getType()).isEqualTo(Metrique.APPLI);

        // Test excpetion
        assertThrows(IllegalArgumentException.class, () -> objetTest = new Mesure(null, null));
    }

    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getType()).isNull();
        objetTest.setType(Metrique.APPLI);
        assertThat(objetTest.getType()).isEqualTo(Metrique.APPLI);

        // Protection setter null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(Metrique.APPLI);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getValeur(), s -> objetTest.setValeur(s));
    }

    @Test
    public void testGetListePeriodes(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListePeriodes(), s -> objetTest.setListePeriodes(s), new Periode(0, NOM), "listePeriodes");
    }

    @Test
    public void testIsBestValue(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isBestValue(), b -> objetTest.setBestValue(b));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
