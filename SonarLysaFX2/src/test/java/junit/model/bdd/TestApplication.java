package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.bdd.Application;
import model.enums.Param;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestApplication extends TestAbstractBDDModele<Application, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetApplicationInconnue(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = Application.getApplication("appli", false);
        assertThat(objetTest.getCode()).isEqualTo("appli");
        assertThat(objetTest.isReferentiel()).isFalse();
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        objetTest.setCode("code");
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getCode());
    }

    @Test
    public void testUpdate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        Application appli = Application.getApplication("appli", true);

        objetTest.update(appli);
        assertThat(objetTest.getCode()).isEmpty();
        assertThat(objetTest.isReferentiel()).isTrue();
    }

    @Test
    public void testGetLiens(TestInfo testInfo)
    {
        assertThat(objetTest.getLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLAPPCATSAPPLI) + objetTest.getCode());
    }

    @Test
    public void testEquals(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        Application appli = Application.getApplication("CODE", true);
        objetTest = Application.getApplication("appli", false);

        testSimpleEquals(appli);

        // Test autres attributs
        appli.setCode(objetTest.getCode());
        assertThat(objetTest.equals(appli)).isFalse();

        appli.setReferentiel(objetTest.isReferentiel());
        assertThat(objetTest.equals(appli)).isTrue();
    }
    
    @Test
    public void testHashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        Application appli = Application.getApplication("CODE", true);
        objetTest = Application.getApplication("appli", false);

        // Test autres attributs
        appli.setCode(objetTest.getCode());
        assertThat(objetTest.hashCode() ==appli.hashCode()).isTrue();
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setCode("code");
        String string = objetTest.toString();
        assertThat(string).contains("code=code");
        assertThat(string).contains("referentiel=false");
        assertThat(string).contains("idBase=0");
    }

    @Test
    public void testGetCode(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getCode()).isEmpty();

        // Test setter et getter
        String code = "Code";
        objetTest.setCode(code);
        assertThat(objetTest.getCode()).isEqualTo(code);

        // Test contrôle setter - protection nullité et taille supérieure à 16
        objetTest.setCode(null);
        assertThat(objetTest.getCode()).isEqualTo(code);
        objetTest.setCode("1234567890123456789");
        assertThat(objetTest.getCode()).isEqualTo("1234567890123456");
    }

    @Test
    public void testIsRefetentiel(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isReferentiel(), b -> objetTest.setReferentiel(b));
    }
    
    @Test
    public void testGetDepartement(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDirection(), b -> objetTest.setDirection(b));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
