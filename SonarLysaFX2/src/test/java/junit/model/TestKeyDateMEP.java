package junit.model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.KeyDateMEP;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestKeyDateMEP extends TestAbstractModele<KeyDateMEP>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = KeyDateMEP.build(KEY, NOM, today);
        assertThat(objetTest.getCle()).isEqualTo(KEY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getDate()).isEqualTo(today);
    }

    @Test
    public void testGetCle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCle(), s -> objetTest.setCle(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetDate(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDate(), s -> objetTest.setDate(s));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
