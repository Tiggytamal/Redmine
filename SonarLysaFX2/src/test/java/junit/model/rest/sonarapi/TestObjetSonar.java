package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.KeyDateMEP;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.ObjetSonar;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestObjetSonar extends TestAbstractModele<ObjetSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test constructeur
        objetTest = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getDescription()).isEqualTo(TESTSTRING);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.PORTFOLIO);
        assertThat(objetTest.getVisibility()).isEqualTo("public");

        // test lancement exceptions avec objets null ou non cohérents
        assertThrows(IllegalArgumentException.class, () -> objetTest = new ObjetSonar(null, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO));
        assertThrows(IllegalArgumentException.class, () -> objetTest = new ObjetSonar(KEY, null, TESTSTRING, TypeObjetSonar.PORTFOLIO));
        assertThrows(IllegalArgumentException.class, () -> objetTest = new ObjetSonar(KEY, NOM, TESTSTRING, null));
        assertThrows(IllegalArgumentException.class, () -> objetTest = new ObjetSonar(EMPTY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO));
        assertThrows(IllegalArgumentException.class, () -> objetTest = new ObjetSonar(KEY, EMPTY, TESTSTRING, TypeObjetSonar.PORTFOLIO));
    }

    @Test
    public void testControle(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test objet correct
        objetTest = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        assertThat(ObjetSonar.controle(objetTest)).isTrue();

        // Test objet null
        assertThat(ObjetSonar.controle(null)).isFalse();

        // Test clé vide
        objetTest.setKey(EMPTY);
        assertThat(ObjetSonar.controle(objetTest)).isFalse();

        // Test nom et clé vides
        objetTest.setKey(KEY);
        objetTest.setNom(EMPTY);
        assertThat(ObjetSonar.controle(objetTest)).isFalse();
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation composant
        ComposantSonar compo = new ComposantSonar();
        compo.setKey(KEY);
        compo.setNom(NOM);
        compo.setDescription(TESTSTRING);
        compo.setType(TypeObjetSonar.PORTFOLIO);

        // Test et contrôle
        objetTest = ObjetSonar.from(compo);
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getDescription()).isEqualTo(TESTSTRING);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.PORTFOLIO);
        assertThat(objetTest.getVisibility()).isEqualTo("public");
    }

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation composant
        KeyDateMEP kdMep = KeyDateMEP.build(KEY, NOM, today);

        // Test et contrôle
        objetTest = ObjetSonar.from(kdMep);
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getDescription()).isEqualTo(EMPTY);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.PROJECT);
        assertThat(objetTest.getVisibility()).isEqualTo("public");
    }

    @Test
    public void testEquals(TestInfo testInfo) throws JAXBException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        ObjetSonar objet = new ObjetSonar("a", "b", "c", TypeObjetSonar.PROJECT);

        // Préparation objetTest
        objetTest = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        objetTest.setVisibility(TESTMANUEL);

        // Test paramètres 1 apr 1
        objet.setNom(NOM);
        assertThat(objetTest.equals(objet)).isFalse();

        objet.setKey(KEY);
        assertThat(objetTest.equals(objet)).isFalse();

        objet.setDescription(TESTSTRING);
        assertThat(objetTest.equals(objet)).isFalse();

        objet.setVisibility(TESTMANUEL);
        assertThat(objetTest.equals(objet)).isFalse();

        objet = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        objet.setVisibility(TESTMANUEL);
        assertThat(objetTest.equals(objet)).isTrue();
    }

    @Test
    public void testHashCode(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        ObjetSonar objet = new ObjetSonar("a", "b", "c", TypeObjetSonar.PROJECT);

        // Préparation objetTest
        objetTest = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        objetTest.setVisibility(TESTMANUEL);

        // Test paramètres 1 apr 1
        objet.setNom(NOM);
        assertThat(objetTest.hashCode()).isNotEqualTo(objet.hashCode());

        objet.setKey(KEY);
        assertThat(objetTest.hashCode()).isNotEqualTo(objet.hashCode());

        objet.setVisibility(TESTSTRING);
        assertThat(objetTest.hashCode()).isNotEqualTo(objet.hashCode());

        objet = new ObjetSonar(KEY, NOM, TESTSTRING, TypeObjetSonar.PORTFOLIO);
        objet.setVisibility(TESTMANUEL);
        assertThat(objetTest.hashCode()).isEqualTo(objet.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetDescription(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDescription(), s -> objetTest.setDescription(s));
    }

    @Test
    public void testGetVisibility(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVisibility(), s -> objetTest.setVisibility(s));
    }

    @Test
    public void testGetType(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.PROJECT);

        // Test getter
        objetTest = new ObjetSonar(KEY, NOM, EMPTY, TypeObjetSonar.APPLI);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.APPLI);

        // Protection null introspection
        setField("type", null);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.PROJECT);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
