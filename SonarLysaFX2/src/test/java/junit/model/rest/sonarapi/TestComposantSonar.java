package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.Metrique;
import model.enums.QG;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Mesure;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantSonar extends TestAbstractModele<ComposantSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation composant initial
        ComposantSonar init = new ComposantSonar();
        init.setId("id");
        init.setBranche("branche");
        init.setKey(KEY);
        init.setNom(NOM);
        init.setDescription("desc");
        init.setType(TypeObjetSonar.APPLI);
        init.setLangage("JAVA");
        init.setPath("path");
        init.setMesures(Arrays.asList(new Mesure(Metrique.APPLI)));

        // Création objet test
        objetTest = ComposantSonar.from(init);

        // Test des valeurs
        assertThat(objetTest.getId()).isEqualTo(init.getId());
        assertThat(objetTest.getBranche()).isEqualTo(init.getBranche());
        assertThat(objetTest.getKey()).isEqualTo(init.getKey());
        assertThat(objetTest.getNom()).isEqualTo(init.getNom());
        assertThat(objetTest.getDescription()).isEqualTo(init.getDescription());
        assertThat(objetTest.getType()).isEqualTo(init.getType());
        assertThat(objetTest.getLangage()).isEqualTo(init.getLangage());
        assertThat(objetTest.getPath()).isEqualTo(init.getPath());
        assertThat(objetTest.getMesures()).isEqualTo(init.getMesures());
    }

    @Test
    public void testGetMapMetriques(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation non null
        assertThat(objetTest.getMapMetriques()).isNotNull();
        assertThat(objetTest.getMapMetriques()).isEmpty();

        // Test avec un objet dans la liste
        Mesure metrique = new Mesure(Metrique.APPLI, "Appli");
        objetTest.getMesures().add(metrique);

        // Test que l'on récupère bien le metrique dans la map
        assertThat(objetTest.getMapMetriques()).isNotEmpty();
        assertThat(objetTest.getMapMetriques().get(Metrique.APPLI)).isEqualTo(metrique);

        // Test que la Map est vide si l'on rajoute un metrique sans clef d'énumeration
        objetTest.getMesures().clear();
        Whitebox.getField(Mesure.class, "type").set(metrique, null);
        objetTest.getMesures().add(metrique);
        assertThat(objetTest.getMapMetriques()).hasSize(0);

        // Test protection null avec introspection
        setField("mesures", null);
        assertThat(objetTest.getMapMetriques()).isNotNull();
        assertThat(objetTest.getMapMetriques()).isEmpty();
    }

    @Test
    public void testGetId(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getId(), s -> objetTest.setId(s));
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetDescritpion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDescription(), s -> objetTest.setDescription(s));
    }

    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getType()).isNull();

        // Test getter et setter
        objetTest.setType(TypeObjetSonar.APPLI);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.APPLI);

        // Protection null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(TypeObjetSonar.APPLI);
    }

    @Test
    public void testGetLangage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLangage(), s -> objetTest.setLangage(s));
    }

    @Test
    public void testGetPath(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getPath(), s -> objetTest.setPath(s));
    }

    @Test
    public void testGetMesures(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getMesures(), l -> objetTest.setMesures(l), new Mesure(Metrique.APPLI), "mesures");
    }

    @Test
    public void testGetProject(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getProjet(), s -> objetTest.setProjet(s));
    }

    @Test
    public void testGetOrganisation(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getOrganisation(), s -> objetTest.setOrganisation(s));
    }

    @Test
    public void testGetQualityGate(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation non null
        assertThat(objetTest.getQualityGate()).isNotNull();
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);

        // Test getter et setter
        objetTest.setQualityGate(QG.OK);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);

        // Protection null
        objetTest.setQualityGate(null);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);

        // Protection null avec introspection
        setField("qualityGate", null);
        assertThat(objetTest.getQualityGate()).isNotNull();
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);
    }

    @Test
    public void testGetBranche(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec getString
        assertThat(objetTest.getBranche()).isNotNull();
        assertThat(objetTest.getBranche()).isEqualTo("master");

        // Test getter et setter
        objetTest.setBranche(TESTSTRING);
        assertThat(objetTest.getBranche()).isEqualTo(TESTSTRING);

        // Test protection null et vide
        objetTest.setBranche(null);
        assertThat(objetTest.getBranche()).isNotNull();
        assertThat(objetTest.getBranche()).isEqualTo(TESTSTRING);

        objetTest.setBranche(EMPTY);
        assertThat(objetTest.getBranche()).isNotNull();
        assertThat(objetTest.getBranche()).isEqualTo(TESTSTRING);
    }

    @Test
    public void testGetDateAnalyse(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateAnalyse(), dt -> objetTest.setDateAnalyse(dt));
    }

    @Test
    public void testGetVersion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersion(), s -> objetTest.setVersion(s));
    }

    @Test
    public void testGetDateLeakPeriod(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateLeakPeriod(), dt -> objetTest.setDateLeakPeriod(dt));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
