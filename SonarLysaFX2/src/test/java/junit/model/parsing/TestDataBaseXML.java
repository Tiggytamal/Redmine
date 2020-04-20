package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.DataBaseXML;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDataBaseXML extends TestAbstractModele<DataBaseXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetFile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getFile()).isNotNull();
        assertThat(objetTest.getFile()).isEqualTo(new File(Statics.JARPATH + "\\database.xml"));
    }

    @Test
    public void testControleDonnees(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.controleDonnees()).isNotNull();
        assertThat(objetTest.controleDonnees()).isEqualTo(EMPTY);
    }

    @Test
    public void testGetCompos(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter
        assertThat(objetTest.getCompos()).isNotNull();
        assertThat(objetTest.getCompos()).isEmpty();

        // Protection null
        setField("compos", null);
        assertThat(objetTest.getCompos()).isNotNull();
        assertThat(objetTest.getCompos()).isEmpty();
    }

    @Test
    public void testGetComposErreur(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter
        assertThat(objetTest.getComposErreur()).isNotNull();
        assertThat(objetTest.getComposErreur()).isEmpty();

        // Protection null
        setField("composErreur", null);
        assertThat(objetTest.getComposErreur()).isNotNull();
        assertThat(objetTest.getComposErreur()).isEmpty();
    }

    @Test
    public void testGetDqs(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter
        assertThat(objetTest.getDqs()).isNotNull();
        assertThat(objetTest.getDqs()).isEmpty();

        // Protection null
        setField("dqs", null);
        assertThat(objetTest.getDqs()).isNotNull();
        assertThat(objetTest.getDqs()).isEmpty();
    }

    @Test
    public void testGetLotsRTC(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter
        assertThat(objetTest.getLotsRTC()).isNotNull();
        assertThat(objetTest.getLotsRTC()).isEmpty();

        // Protection null
        setField("lotsRTC", null);
        assertThat(objetTest.getLotsRTC()).isNotNull();
        assertThat(objetTest.getLotsRTC()).isEmpty();
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
