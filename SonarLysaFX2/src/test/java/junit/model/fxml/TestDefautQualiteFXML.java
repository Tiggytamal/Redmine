package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.EtatCodeAppli;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.QG;
import model.fxml.DefautQualiteFXML;
import model.fxml.DefautQualiteFXML.DefautQualiteFXMLGetters;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestDefautQualiteFXML extends TestAbstractModele<DefautQualiteFXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        // Test exception avec lot et compo nuls
        assertThrows(TechnicalException.class, () -> DefautQualiteFXML.build(ModelFactory.build(DefautQualite.class)));

        // Test exception avec compo nul
        assertThrows(TechnicalException.class, () -> { DefautQualite dq = ModelFactory.build(DefautQualite.class); dq.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456)); DefautQualiteFXML.build(dq); });

    }
    @Test
    public void testBuild(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        // Préparation DefautQualite vide sans plantage
        DefautQualite dq = ModelFactory.build(DefautQualite.class);
        LotRTC lot = LotRTC.getLotRTCInconnu(LOT123456);
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        dq.setLotRTC(lot);
        dq.setCompo(compo);
        Whitebox.getField(lot.getClass(), "etatLot").set(lot, null);

        // Appel constructeur
        objetTest = DefautQualiteFXML.build(dq);

        // Tests
        assertThat(objetTest).isNotNull();
        assertThat(objetTest.getComposant().get(0)).isEqualTo(compo.getNom());
        assertThat(objetTest.getComposant().get(1)).isEqualTo(compo.getLiens());
        assertThat(objetTest.getLotRTC().get(0)).isEqualTo(lot.getNumero());
        assertThat(objetTest.getLotRTC().get(1)).isEqualTo(lot.getLiens());

        // Valorisation complète d'un DefautQualite
        lot.setDateMajEtat(today);
        lot.setEtatLot(EtatLot.NOUVEAU);
        ProjetClarity pc = ProjetClarity.getProjetClarityInconnu(LOT123456);
        lot.setProjetClarity(pc);
        compo.setMatiere(Matiere.JAVA);
        compo.setVersion("1.0.0-SNAPSHOT");
        compo.setQualityGate(QG.OK);
        compo.setAppli(Application.getApplication("V360", true));
        dq.setEtatCodeAppli(EtatCodeAppli.OK);
        dq.setDateDetection(today);
        dq.setDateCreation(today);
        dq.setDateMepPrev(today);
        dq.setDateRelance(today);
        dq.setDateReouv(today);
        dq.setDateReso(today);

        // Appel constructeur
        objetTest = DefautQualiteFXML.build(dq);

        // Tests
        assertThat(objetTest).isNotNull();
        assertThat(objetTest.getIndex()).isEqualTo(compo.getMapIndex() + lot.getNumero());
        assertThat(objetTest.getLotRTC().get(0)).isEqualTo(lot.getNumero());
        assertThat(objetTest.getLotRTC().get(1)).isEqualTo(lot.getLiens());
        assertThat(objetTest.getCpiLot()).isEqualTo(lot.getCpiProjet().getNom());
        assertThat(objetTest.getCodeProjetRTC()).isEqualTo(lot.getProjetRTC());
        assertThat(objetTest.getEdition()).isEqualTo(lot.getEdition().getNom());
        assertThat(objetTest.getNumeroAnoRTC().get(0)).isEqualTo(String.valueOf(dq.getNumeroAnoRTC()));
        assertThat(objetTest.getNumeroAnoRTC().get(1)).isEqualTo(dq.getLiensAno());
        assertThat(objetTest.getEtatRTC()).isEqualTo(dq.getEtatAnoRTC());
        assertThat(objetTest.getRemarque()).isEqualTo(dq.getRemarque());
        assertThat(objetTest.getVersion()).isEqualTo(compo.getVersion());
        assertThat(objetTest.getComposant().get(0)).isEqualTo(compo.getNom());
        assertThat(objetTest.getComposant().get(1)).isEqualTo(compo.getLiens());
        assertThat(objetTest.getQg()).isEqualTo(compo.getQualityGate().getValeur());
        assertThat(objetTest.getEtatDefaut()).isEqualTo(dq.getEtatDefaut().toString());
        assertThat(objetTest.getTypeDefaut()).isEqualTo(dq.getTypeDefaut().toString());
        assertThat(objetTest.getCodeClarity()).isEqualTo(pc.getCode());
        assertThat(objetTest.getLibelleClarity()).isEqualTo(pc.getLibelleProjet());
        assertThat(objetTest.getDepartement()).isEqualTo(pc.getDepartement());
        assertThat(objetTest.getDirection()).isEqualTo(pc.getDirection());
        assertThat(objetTest.getService()).isEqualTo(pc.getService());
        assertThat(objetTest.getRespServ()).isEqualTo(pc.getChefService().getNom());
        assertThat(objetTest.getDateMajRTC()).isEqualTo(lot.getDateMajEtat().toString());
        assertThat(objetTest.getAppliOK()).isEqualTo(EtatCodeAppli.OK.getValeur());
        assertThat(objetTest.getMatiere()).isEqualTo(compo.getMatiere().getValeur());
        assertThat(objetTest.getDateCreation()).isEqualTo(dq.getDateCreation().toString());
        assertThat(objetTest.getDateRelance()).isEqualTo(dq.getDateRelance().toString());
        assertThat(objetTest.getDateReso()).isEqualTo(dq.getDateReso().toString());
        assertThat(objetTest.getDateReouv()).isEqualTo(dq.getDateReouv().toString());
        assertThat(objetTest.getDateMepPrev()).isEqualTo(dq.getDateMepPrev().toString());
    }

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(DefautQualiteFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetLotRTC(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getLotRTC(), l -> objetTest.setLotRTC(l));
    }

    @Test
    public void testGetNumeroAnoRTC(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getNumeroAnoRTC(), l -> objetTest.setNumeroAnoRTC(l));
    }

    @Test
    public void testGetEtatRTC(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEtatDefaut(), s -> objetTest.setEtatDefaut(s));
    }

    @Test
    public void testGetSecurite(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSecurite(), s -> objetTest.setSecurite(s));
    }

    @Test
    public void testGetRemarque(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRemarque(), s -> objetTest.setRemarque(s));
    }

    @Test
    public void testGetVersion(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getVersion(), s -> objetTest.setVersion(s));
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateCreation(), s -> objetTest.setDateCreation(s));
    }

    @Test
    public void testGetDateDetection(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateDetection(), s -> objetTest.setDateDetection(s));
    }

    @Test
    public void testGetDateRelance(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateRelance(), s -> objetTest.setDateRelance(s));
    }

    @Test
    public void testGetDateReso(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateReso(), s -> objetTest.setDateReso(s));
    }

    @Test
    public void testGetEtatDefaut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEtatDefaut(), s -> objetTest.setEtatDefaut(s));
    }

    @Test
    public void testGetTypeDefaut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getTypeDefaut(), s -> objetTest.setTypeDefaut(s));
    }

    @Test
    public void testGetDirection(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDirection(), s -> objetTest.setDirection(s));
    }

    @Test
    public void testGetDepartement(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDepartement(), s -> objetTest.setDepartement(s));
    }

    @Test
    public void testGetService(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getService(), s -> objetTest.setService(s));
    }

    @Test
    public void testGetRespServ(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRespServ(), s -> objetTest.setRespServ(s));
    }

    @Test
    public void testGetCodeClarity(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCodeClarity(), s -> objetTest.setCodeClarity(s));
    }

    @Test
    public void testGetLibelleClarity(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelleClarity(), s -> objetTest.setLibelleClarity(s));
    }

    @Test
    public void testGetCpiLot(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCpiLot(), s -> objetTest.setCpiLot(s));
    }

    @Test
    public void testGetEmailCpi(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEmailCpi(), s -> objetTest.setEmailCpi(s));
    }

    @Test
    public void testGetEdition(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEdition(), s -> objetTest.setEdition(s));
    }

    @Test
    public void testGetSolution(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSolution(), s -> objetTest.setSolution(s));
    }

    @Test
    public void testGetMatiere(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMatiere(), s -> objetTest.setMatiere(s));
    }

    @Test
    public void testGetCodeProjetRTC(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCodeProjetRTC(), s -> objetTest.setCodeProjetRTC(s));
    }

    @Test
    public void testGetDateMajRTC(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateMajRTC(), s -> objetTest.setDateMajRTC(s));
    }

    @Test
    public void testGetEtatLotRTC(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEtatLotRTC(), s -> objetTest.setEtatLotRTC(s));
    }

    @Test
    public void testGetDateReouv(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateReouv(), s -> objetTest.setDateReouv(s));
    }

    @Test
    public void testGetDateMepPrev(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDateMepPrev(), s -> objetTest.setDateMepPrev(s));
    }

    @Test
    public void testGetDureeAno(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDureeAno(), s -> objetTest.setDureeAno(s));
    }

    @Test
    public void testGetSelected(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getSelected()).isNotNull();
    }

    @Test
    public void testGetComposant(TestInfo testInfo)
    {
        testSimpleListFXML(testInfo, () -> objetTest.getComposant(), s -> objetTest.setComposant(s));
    }

    @Test
    public void testGetQg(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getQg(), s -> objetTest.setQg(s));
    }

    @Test
    public void testGetAppliOK(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getAppliOK(), s -> objetTest.setAppliOK(s));
    }

    @Test
    public void testDefautQualiteFXMLGettersValues(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(DefautQualiteFXMLGetters.DIRECTION.getGroupe()).isEqualTo("Projet Clarity");
        assertThat(DefautQualiteFXMLGetters.DIRECTION.getAffichage()).isEqualTo("Direction");
        assertThat(DefautQualiteFXMLGetters.DIRECTION.getNomParam()).isEqualTo("direction");
        assertThat(DefautQualiteFXMLGetters.DIRECTION.getNomMethode()).isEqualTo("getDirection");
        assertThat(DefautQualiteFXMLGetters.DIRECTION.getStyle()).isEqualTo("tableBlue");
        assertThat(DefautQualiteFXMLGetters.DIRECTION.isAffParDefaut()).isFalse();
        assertThat(DefautQualiteFXMLGetters.DIRECTION.toString()).isEqualTo("Direction");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
