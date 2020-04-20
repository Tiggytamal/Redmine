package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.AbstractBDDModele;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.bdd.Solution;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.QG;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantBase extends TestAbstractBDDModele<ComposantBase, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = ComposantBase.build(KEY, NOM);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getKey()).isEqualTo(KEY);
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getKey());
    }

    @Test
    public void testSetSecurityRatingDepuisSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test données classiques
        final String F = "F";
        objetTest.setSecurityRatingDepuisSonar(null);
        assertThat(objetTest.getSecurityRating()).isEqualTo(F);
        objetTest.setSecurityRatingDepuisSonar(Statics.EMPTY);
        assertThat(objetTest.getSecurityRating()).isEqualTo(F);
        objetTest.setSecurityRatingDepuisSonar("0.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("A");
        objetTest.setSecurityRatingDepuisSonar("1.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("A");
        objetTest.setSecurityRatingDepuisSonar("2.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("B");
        objetTest.setSecurityRatingDepuisSonar("3.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("C");
        objetTest.setSecurityRatingDepuisSonar("4.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("D");
        objetTest.setSecurityRatingDepuisSonar("5.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo("E");
        objetTest.setSecurityRatingDepuisSonar("6.0");
        assertThat(objetTest.getSecurityRating()).isEqualTo(F);
    }

    @Test
    public void testIsSecurite(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test setter et getter
        assertThat(objetTest.isSecurite()).isFalse();
        objetTest.setSecurityRating("E");
        assertThat(objetTest.isSecurite()).isTrue();
        objetTest.setSecurityRating("F");
        assertThat(objetTest.isSecurite()).isTrue();
    }

    @Test
    public void testIsVersionOK(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test version OK avec version >= versionMax
        objetTest.setVersion("2");
        objetTest.setVersionMax("1");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("1");
        assertThat(objetTest.isVersionOK()).isTrue();

        // Test version KO avec version < versionMax
        objetTest.setVersionMax("2");
        assertThat(objetTest.isVersionOK()).isFalse();

        // Test contrôle objets vide
        objetTest.setVersionMax(EMPTY);
        assertThat(objetTest.isVersionOK()).isTrue();
        objetTest.setVersion(EMPTY);
        assertThat(objetTest.isVersionOK()).isTrue();
        objetTest.setVersionMax("1");
        assertThat(objetTest.isVersionOK()).isTrue();
    }

    @Test
    public void testGetLiens(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test InstanceSonar.LEGACYMOBILECENTER
        assertThat(objetTest.getLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONARMC) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS) + objetTest.getKey());

        // Test InstanceSonar.LEGACY
        objetTest.setInstance(InstanceSonar.LEGACY);
        assertThat(objetTest.getLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS) + objetTest.getKey());

        objetTest.setInstance(InstanceSonar.MOBILECENTER);
    }

    @Test
    void testEquals(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setAppli(ModelFactory.build(Application.class));
        objetTest.setBranche(TESTSTRING);
        objetTest.setControleAppli(true);
        objetTest.setDateRepack(today);
        objetTest.setDerniereAnalyse(todayTime);
        objetTest.setDoublon(true);
        objetTest.setInstance(InstanceSonar.MOBILECENTER);
        objetTest.setKey(KEY);
        objetTest.setLdc(100);
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        objetTest.setMatiere(Matiere.COBOL);
        objetTest.setNom(NOM);
        objetTest.setQualityGate(QG.NONE);
        objetTest.setSecurityRating("A");
        objetTest.setSolution(ModelFactory.build(Solution.class));
        objetTest.setVersion("1.0.0");
        objetTest.setVersionMax("1.0.0.5");

        // Test simple
        ComposantBase compo = ComposantBase.build("KEY2", "NOM2");
        testSimpleEquals(compo);

        // Test autres paramètres
        compo.setAppli(ModelFactory.build(Application.class));
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setBranche(TESTSTRING);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setControleAppli(true);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setDateRepack(today);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setDerniereAnalyse(todayTime);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setDoublon(true);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setInstance(InstanceSonar.MOBILECENTER);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setKey(KEY);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setLdc(100);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setMatiere(Matiere.COBOL);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setNom(NOM);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setQualityGate(QG.NONE);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setSecurityRating("A");
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setSolution(ModelFactory.build(Solution.class));
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setVersion("1.0.0");
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setVersionMax("1.0.0.5");
        assertThat(objetTest.equals(compo)).isTrue();
    }

    @Test
    void testHashCode(TestInfo testInfo) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException, SecurityException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setAppli(ModelFactory.build(Application.class));
        objetTest.setBranche(TESTSTRING);
        objetTest.setControleAppli(true);
        objetTest.setDateRepack(today);
        objetTest.setDerniereAnalyse(todayTime);
        objetTest.setDoublon(true);
        objetTest.setInstance(InstanceSonar.MOBILECENTER);
        objetTest.setKey(KEY);
        objetTest.setLdc(100);
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        objetTest.setMatiere(Matiere.COBOL);
        objetTest.setNom(NOM);
        objetTest.setQualityGate(QG.NONE);
        objetTest.setSecurityRating("A");
        objetTest.setSolution(ModelFactory.build(Solution.class));
        objetTest.setVersion("1.0.0");
        objetTest.setVersionMax("1.0.0.5");

        // Préparation comparaison
        ComposantBase compo = ComposantBase.build("KEY2", "NOM2");

        // Test champs dans hashcode
        Field field = AbstractBDDModele.class.getDeclaredField("idBase");
        field.setAccessible(true);
        field.set(compo, 12345);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setBranche(TESTSTRING);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setInstance(InstanceSonar.MOBILECENTER);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setKey(KEY);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setMatiere(Matiere.COBOL);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setNom(NOM);
        assertThat(objetTest.hashCode()).isEqualTo(compo.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetLotRTC(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getLotRTC()).isNull();

        // Test setter et getter
        LotRTC lotRTC = LotRTC.getLotRTCInconnu("lot");
        objetTest.setLotRTC(lotRTC);
        assertThat(objetTest.getLotRTC()).isEqualTo(lotRTC);

        // Test remise à null sans effet
        objetTest.setLotRTC(null);
        assertThat(objetTest.getLotRTC()).isEqualTo(lotRTC);
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetBranche(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getBranche(), s -> objetTest.setBranche(s));
    }

    @Test
    public void testGetAppli(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getAppli()).isNull();

        // Test setter et getter
        Application appli = ModelFactory.build(Application.class);
        appli.setCode("Appli");
        objetTest.setAppli(appli);
        assertThat(objetTest.getAppli()).isEqualTo(appli);

        // Test remise à null sans effet
        objetTest.setAppli(null);
        assertThat(objetTest.getAppli()).isEqualTo(appli);
    }

    @Test
    public void testGetLdc(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getLdc()).isEqualTo(0);

        // Test setter et getter
        int ldc = 123456;
        objetTest.setLdc(ldc);
        assertThat(objetTest.getLdc()).isEqualTo(ldc);

        // Test setter String
        objetTest.setLdc("123");
        assertThat(objetTest.getLdc()).isEqualTo(123);
    }

    @Test
    public void testGetSecurityRating(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSecurityRating(), s -> objetTest.setSecurityRating(s));
    }

    @Test
    public void testGetQualityGate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans initialisation
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);

        // Test valeure nulle
        QG qg = null;
        objetTest.setQualityGate(qg);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.NONE);

        // Test setter et getter
        qg = QG.OK;
        objetTest.setQualityGate(qg);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);

        // Test verison String
        objetTest.setQualityGate("ERROR");
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.ERROR);
    }

    @Test
    public void testGetMatiere(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans initialisation
        assertThat(objetTest.getMatiere()).isEqualTo(Matiere.INCONNUE);

        // Test setter et getter
        Matiere qg = Matiere.ANDROID;
        objetTest.setMatiere(qg);
        assertThat(objetTest.getMatiere()).isEqualTo(Matiere.ANDROID);

        // Test valeure nulle
        qg = null;
        objetTest.setMatiere(qg);
        assertThat(objetTest.getMatiere()).isEqualTo(Matiere.ANDROID);
    }

    @Test
    public void testGetInstance(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans initialisation
        assertThat(objetTest.getInstance()).isNotNull();

        // Test setter et getter
        InstanceSonar ea = InstanceSonar.LEGACY;
        objetTest.setInstance(ea);
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.LEGACY);

        // Test valeure nulle
        ea = null;
        objetTest.setInstance(ea);
        assertThat(objetTest.getInstance()).isEqualTo(InstanceSonar.LEGACY);
    }

    @Test
    public void testGetVersion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersion(), s -> objetTest.setVersion(s));
    }

    @Test
    public void testGetVersionMax(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersionMax(), s -> objetTest.setVersionMax(s));
    }

    @Test
    public void testGetDateRepack(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getDateRepack()).isNull();

        // Test setter et getter
        objetTest.setDateRepack(today);
        assertThat(objetTest.getDateRepack()).isEqualTo(today);

        // Protection set null
        objetTest.setDateRepack(null);
        assertThat(objetTest.getDateRepack()).isEqualTo(today);
    }

    @Test
    public void testGetDerniereAnalyse(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDerniereAnalyse(), s -> objetTest.setDerniereAnalyse(s));
    }

    @Test
    public void testGetSolution(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getSolution()).isNull();

        // Test setter et getter
        Solution solution = ModelFactory.build(Solution.class);
        objetTest.setSolution(solution);
        assertThat(objetTest.getSolution()).isEqualTo(solution);

        // Tes contrôle doublon solution
        objetTest.setSolution(solution);
        assertThat(solution.getListeComposants()).hasSize(1);
    }

    @Test
    public void testIsDoublon(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDoublon(), b -> objetTest.setDoublon(b));
    }

    @Test
    public void testIsControleAppli(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.isControleAppli()).isTrue();
        objetTest.setControleAppli(false);
        assertThat(objetTest.isControleAppli()).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
