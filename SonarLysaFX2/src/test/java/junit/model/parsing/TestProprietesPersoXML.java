package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.TestXML;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.enums.Param;
import model.enums.ParamSpec;
import model.parsing.ProprietesPersoXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProprietesPersoXML extends TestAbstractModele<ProprietesPersoXML> implements TestXML
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testControleDonnees(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.controleDonnees()).isNull();
    }

    @Test
    public void testCopie(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objet
        ProprietesPersoXML test = ModelFactory.build(ProprietesPersoXML.class);
        Map<Param, String> map = new HashMap<>();
        map.put(Param.URLRTC, EMPTY);
        Whitebox.getField(ProprietesPersoXML.class, "params").set(test, map);
        Map<ParamSpec, String> map2 = new HashMap<>();
        map2.put(ParamSpec.SIGNATURE, EMPTY);
        Whitebox.getField(ProprietesPersoXML.class, "paramsSpec").set(test, map2);
        File file = new File(EMPTY);
        test.setFile(file);

        // Appel méthode
        objetTest.copie(test);

        // Contrôle
        assertThat(objetTest.getParams()).isEqualTo(map);
        assertThat(objetTest.getParamsSpec()).isEqualTo(map2);
        assertThat(objetTest.getFile()).isEqualTo(file);
    }

    @Test
    @Override
    public void testGetFile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation null
        assertThat(objetTest.getFile()).isNull();

        // Test getter et setter
        File file = new File(EMPTY);
        objetTest.setFile(file);
        assertThat(objetTest.getFile()).isEqualTo(file);

        // Protection setter null
        objetTest.setFile(null);
        assertThat(objetTest.getFile()).isEqualTo(file);
    }

    @Test
    public void testGetParams(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation vide
        assertThat(objetTest.getParams()).isNotNull();
        assertThat(objetTest.getParams()).isEmpty();
        assertThat(objetTest.getParams().getClass()).isEqualTo(EnumMap.class);

        // Test getter
        Map<Param, String> map = new HashMap<>();
        map.put(Param.URLRTC, EMPTY);
        setField("params", map);
        assertThat(objetTest.getParams()).isEqualTo(map);

        // Protection getter null
        setField("params", null);
        assertThat(objetTest.getParams()).isNotNull();
        assertThat(objetTest.getParams()).isEmpty();
        assertThat(objetTest.getParams().getClass()).isEqualTo(EnumMap.class);
    }

    @Test
    public void testGetParamsSpec(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation vide
        assertThat(objetTest.getParamsSpec()).isNotNull();
        assertThat(objetTest.getParamsSpec()).isEmpty();
        assertThat(objetTest.getParamsSpec().getClass()).isEqualTo(EnumMap.class);

        // Test getter
        Map<ParamSpec, String> map = new HashMap<>();
        map.put(ParamSpec.SIGNATURE, EMPTY);
        setField("paramsSpec", map);
        assertThat(objetTest.getParamsSpec()).isEqualTo(map);

        // Protection getter null
        setField("paramsSpec", null);
        assertThat(objetTest.getParamsSpec()).isNotNull();
        assertThat(objetTest.getParamsSpec()).isEmpty();
        assertThat(objetTest.getParamsSpec().getClass()).isEqualTo(EnumMap.class);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
