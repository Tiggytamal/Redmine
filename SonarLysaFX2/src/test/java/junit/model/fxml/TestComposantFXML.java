package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.bdd.Produit;
import model.bdd.Solution;
import model.enums.Param;
import model.fxml.ComposantFXML;
import model.fxml.ComposantFXML.ComposantFXMLGetters;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestComposantFXML extends TestAbstractModele<ComposantFXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec initialisation minimum
        ComposantBase compo = ComposantBase.build(KEY, NOM);

        // Contrôle
        objetTest = ComposantFXML.build(compo);
        assertThat(objetTest).isNotNull();
        assertThat(objetTest.getNumeroLot().get(0)).isEqualTo(EMPTY);
        assertThat(objetTest.getNumeroLot().get(1)).isEqualTo(EMPTY);
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        assertThat(objetTest.getNom().get(0)).isEqualTo(NOM);
        assertThat(objetTest.getBranche()).isEqualTo(compo.getBranche());
        assertThat(objetTest.getQg()).isEqualTo(compo.getQualityGate().getValeur());
        assertThat(objetTest.getVersion()).isEqualTo(compo.getVersion());
        assertThat(objetTest.getVersionMax()).isEqualTo(compo.getVersionMax());
        assertThat(objetTest.getLdc()).isEqualTo(String.valueOf(compo.getLdc()));
        assertThat(objetTest.getSecurityRating()).isEqualTo(compo.getSecurityRating());
        assertThat(objetTest.getMatiere()).isEqualTo(compo.getMatiere().getValeur());
        assertThat(objetTest.getInstance()).isEqualTo(compo.getInstance().getValeur());
        assertThat(objetTest.isDoublon()).isEqualTo(compo.isDoublon());
        assertThat(objetTest.getCodeSolution().get(0)).isEqualTo(EMPTY);
        assertThat(objetTest.getCodeSolution().get(1)).isEqualTo(EMPTY);
        assertThat(objetTest.getCodeAppli().get(0)).isEqualTo(EMPTY);
        assertThat(objetTest.getCodeAppli().get(0)).isEqualTo(EMPTY);

        // Test avec objet complet
        compo.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        compo.setAppli(Application.getApplication("ABCD", true));

        // Contrôle
        objetTest = ComposantFXML.build(compo);
        assertThat(objetTest.getCodeSolution().get(0)).isEqualTo(EMPTY);
        assertThat(objetTest.getCodeSolution().get(1)).isEqualTo(EMPTY);
        assertThat(objetTest.getCodeAppli().get(0)).isEqualTo(compo.getAppli().getCode());
        assertThat(objetTest.getCodeAppli().get(1)).isEqualTo(compo.getAppli().getLiens());
        assertThat(objetTest.getNumeroLot().get(0)).isEqualTo(LOT123456);

        // Test avec autres données
        compo.setAppli(Application.getApplication("ABCD", false));
        compo.getLotRTC().setDateMep(today);

        // Contrôle
        objetTest = ComposantFXML.build(compo);
        assertThat(objetTest.getDateMep()).isEqualTo(compo.getLotRTC().getDateMep().toString());
        
        // Test avec solution sans produit
        Solution solution = ModelFactory.build(Solution.class);
        solution.setNom(NOM);
        compo.setSolution(solution);
        
        // Contrôle
        objetTest = ComposantFXML.build(compo);
        List<String> liste = objetTest.getCodeSolution();
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(2);
        assertThat(liste).contains(NOM);
        assertThat(liste).contains(Statics.EMPTY);
        
        // Test avec solution avec produit
        Produit produit = ModelFactory.build(Produit.class);
        produit.setNom(NOM);
        solution.setProduit(produit);
        
        // Contrôle
        objetTest = ComposantFXML.build(compo);
        liste = objetTest.getCodeSolution();
        assertThat(liste).isNotNull();
        assertThat(liste).hasSize(2);
        assertThat(liste).contains(NOM);
        assertThat(liste).contains(Statics.proprietesXML.getMapParams().get(Param.URLMAPSPRODUIT) + NOM);
    }

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(ComposantFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        assertThat(objetTest.getIndex()).isEqualTo(objetTest.getKey() + objetTest.getBranche());
    }

    @Test
    public void testIsVersionOK_OK(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test cas version OK c'est-à-dire version >= version max
        objetTest.setVersion("1.0");
        objetTest.setVersionMax("1.0");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("1.0-SNAPSHOT");
        objetTest.setVersionMax("1.0");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("1.0");
        objetTest.setVersionMax("1.0-SNAPSHOT");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("1.0-SNAPSHOT");
        objetTest.setVersionMax("1.0-SNAPSHOT");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("2.0");
        objetTest.setVersionMax("1.0-SNAPSHOT");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("2.5-SNAPSHOT");
        objetTest.setVersionMax("1.0");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("2.10.42-SNAPSHOT");
        objetTest.setVersionMax("2.10.38");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("2.10.12");
        objetTest.setVersionMax("2.8.9");
        assertThat(objetTest.isVersionOK()).isTrue();

        objetTest.setVersion("2.1.5.8");
        objetTest.setVersionMax("2.1.5");
        assertThat(objetTest.isVersionOK()).isTrue();
    }
    
    @Test
    public void testIsVersionOK_KO(TestInfo testInfo)
    {
        // Tests cas version KO c'est-à-dire version < version max
        objetTest.setVersion("2.10.9");
        objetTest.setVersionMax("2.10.38");
        assertThat(objetTest.isVersionOK()).isFalse();
    }
    
    @Test
    public void testIsVersionOK_Vide(TestInfo testInfo)
    {
        objetTest.setVersion(Statics.EMPTY);
        objetTest.setVersionMax("2.10.38");
        assertThat(objetTest.isVersionOK()).isTrue();
        
        objetTest.setVersion(Statics.EMPTY);
        objetTest.setVersionMax(Statics.EMPTY);
        assertThat(objetTest.isVersionOK()).isTrue();
        
        objetTest.setVersion("2.10.38");
        objetTest.setVersionMax(Statics.EMPTY);
        assertThat(objetTest.isVersionOK()).isTrue();
    }
    

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getNom()).isNotNull();
        assertThat(objetTest.getNom()).hasSize(0);
        objetTest.setNom(Arrays.asList(LOT123456, TESTSTRING));
        assertThat(objetTest.getNom().get(0)).isEqualTo(LOT123456);
        assertThat(objetTest.getNom().get(1)).isEqualTo(TESTSTRING);

        // Protection setter null
        objetTest.setNom(null);
        assertThat(objetTest.getNom().get(0)).isEqualTo(LOT123456);
        assertThat(objetTest.getNom().get(1)).isEqualTo(TESTSTRING);
    }

    @Test
    public void testGetBranche(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getBranche(), s -> objetTest.setBranche(s));
    }

    @Test
    public void testGetQg(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getQg(), s -> objetTest.setQg(s));
    }

    @Test
    public void testGetNumeroLot(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getNumeroLot(), l -> objetTest.setNumeroLot(l));
    }

    @Test
    public void testGetCodeAppli(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getCodeAppli(), l -> objetTest.setCodeAppli(l));
    }

    @Test
    public void testGetCodeSolution(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getCodeSolution(), l -> objetTest.setCodeSolution(l));
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
    public void testGetLdc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLdc(), s -> objetTest.setLdc(s));
    }

    @Test
    public void testGetSecurityRating(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSecurityRating(), s -> objetTest.setSecurityRating(s));
    }
    
    @Test
    public void testIsControleAppli(TestInfo testInfo)
    {
        assertThat(objetTest.isControleAppli()).isTrue();
        objetTest.setControleAppli(false);
        assertThat(objetTest.isControleAppli()).isFalse();
    }

    @Test
    public void testGetMatiere(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMatiere(), s -> objetTest.setMatiere(s));
    }

    @Test
    public void testGetDateMep(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateMep(), s -> objetTest.setDateMep(s));
    }

    @Test
    public void testGetInstance(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getInstance(), s -> objetTest.setInstance(s));
    }

    @Test
    public void testIsDoublon(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDoublon(), b -> objetTest.setDoublon(b));
    }

    @Test
    public void testComposantFXMLGettersValues(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ComposantFXMLGetters.KEY.getGroupe()).isEqualTo("Identifiants");
        assertThat(ComposantFXMLGetters.KEY.getAffichage()).isEqualTo("Clef");
        assertThat(ComposantFXMLGetters.KEY.getNomParam()).isEqualTo("key");
        assertThat(ComposantFXMLGetters.KEY.getNomMethode()).isEqualTo("getKey");
        assertThat(ComposantFXMLGetters.KEY.getStyle()).isEqualTo("tableBlue");
        assertThat(ComposantFXMLGetters.KEY.isAffParDefaut()).isFalse();
        assertThat(ComposantFXMLGetters.KEY.toString()).isEqualTo("Clef");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
