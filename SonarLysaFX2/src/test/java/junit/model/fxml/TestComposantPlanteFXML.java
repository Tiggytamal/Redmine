package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.bdd.ComposantErreur;
import model.fxml.ComposantPlanteFXML;
import model.fxml.ComposantPlanteFXML.ComposantPlanteFXMLGetters;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestComposantPlanteFXML extends TestAbstractModele<ComposantPlanteFXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec initialisation minimum
        ComposantErreur ce = ComposantErreur.build(KEY, NOM);

        // Contrôle
        objetTest = ComposantPlanteFXML.build(ce);
        assertThat(objetTest).isNotNull();
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        assertThat(objetTest.getNom().get(0)).isEqualTo(NOM);
        assertThat(objetTest.getNom().get(1)).isEqualTo(ce.getLiens());
        assertThat(objetTest.getDate()).isEqualTo(ce.getDateDetection().toString());
        assertThat(objetTest.isAPurger()).isTrue();
        assertThat(objetTest.existe()).isTrue();
        assertThat(objetTest.getNbrePurge()).isEqualTo("0");

        // Test avec objet complet
        ce.setNbrePurge(3);
        ce.setaPurger(false);
        ce.setExiste(false);

        // Contrôle
        objetTest = ComposantPlanteFXML.build(ce);
        assertThat(objetTest).isNotNull();
        assertThat(objetTest.isAPurger()).isFalse();
        assertThat(objetTest.existe()).isFalse();
        assertThat(objetTest.getNbrePurge()).isEqualTo("3");
    }

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(ComposantPlanteFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getIndex()).isEqualTo(objetTest.getKey());
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getNom(), l -> objetTest.setNom(l));
    }

    @Test
    public void testGetDate(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDate(), s -> objetTest.setDate(s));
    }

    @Test
    public void testIsAPurger(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isAPurger(), s -> objetTest.setAPurger(s));
    }

    @Test
    public void testExiste(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.existe(), s -> objetTest.setExiste(s));
    }

    @Test
    public void testGetNbrePurge(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNbrePurge(), s -> objetTest.setNbrePurge(s));
    }

    @Test
    public void testComposantPlanteFXMLGettersValues(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ComposantPlanteFXMLGetters.KEY.getGroupe()).isEqualTo("Composant");
        assertThat(ComposantPlanteFXMLGetters.KEY.getAffichage()).isEqualTo("Clef");
        assertThat(ComposantPlanteFXMLGetters.KEY.getNomParam()).isEqualTo("key");
        assertThat(ComposantPlanteFXMLGetters.KEY.getNomMethode()).isEqualTo("getKey");
        assertThat(ComposantPlanteFXMLGetters.KEY.getStyle()).isEqualTo("tableBlue");
        assertThat(ComposantPlanteFXMLGetters.KEY.isAffParDefaut()).isFalse();
        assertThat(ComposantPlanteFXMLGetters.KEY.toString()).isEqualTo("Clef");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
