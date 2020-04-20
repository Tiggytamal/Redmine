package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.bdd.Utilisateur;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.QG;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLotRTC extends TestAbstractBDDModele<LotRTC, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetLotRTCInconnu(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = LotRTC.getLotRTCInconnu(LOT123456);
        assertThat(objetTest.getNumero()).isEqualTo(LOT123456);
        assertThat(objetTest.getLibelle()).isEmpty();
        assertThat(objetTest.getProjetClarityString()).isEmpty();
        assertThat(objetTest.getEditionString()).isEmpty();
        assertThat(objetTest.getCpiProjet()).isEqualTo(Statics.USERINCONNU);
        assertThat(objetTest.getEtatLot()).isEqualTo(EtatLot.NOUVEAU);
        assertThat(objetTest.getProjetRTC()).isEmpty();
        assertThat(objetTest.getNumero()).isEqualTo(LOT123456);
        assertThat(objetTest.getEdition()).isNotNull();
        assertThat(objetTest.getEdition().getNumero()).isEqualTo(Statics.EDINCONNUE);
        assertThat(objetTest.getDateMajEtat()).isNotNull();
        assertThat(objetTest.getDateMajEtat()).isEqualTo(LocalDate.of(2016, 01, 01));
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setNumero("123");
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNumero());
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        final String a = "a";
        final String edition = "E32";
        final String numero = "12345678";

        // Préparation objetTest
        setField("idBase", 12345);
        objetTest.setNumero(numero);
        objetTest.setLibelle(TESTSTRING);
        objetTest.setProjetClarity(ProjetClarity.getProjetClarityInconnu(a));
        objetTest.setProjetClarityString(a);
        objetTest.setEdition(Edition.getEditionInconnue(edition));
        objetTest.setEditionString(edition);
        objetTest.setQualityGate(QG.ERROR);
        objetTest.setCpiProjet(Statics.USERINCONNU);
        objetTest.setEtatLot(EtatLot.ABANDONNE);
        objetTest.setProjetRTC(TESTSTRING);
        objetTest.setDateMajEtat(today);
        objetTest.setDateMep(today);
        objetTest.setDateRepack(today);
        objetTest.setRtcHS(true);

        // Test simple
        LotRTC lot = LotRTC.getLotRTCInconnu("123456");
        testSimpleEquals(lot);

        // Test autres paramètres
        lot.setNumero(numero);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setLibelle(TESTSTRING);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setProjetClarity(ProjetClarity.getProjetClarityInconnu(a));
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setProjetClarityString(a);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setEdition(Edition.getEditionInconnue(edition));
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setEditionString(edition);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setQualityGate(QG.ERROR);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setCpiProjet(Statics.USERINCONNU);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setEtatLot(EtatLot.ABANDONNE);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setProjetRTC(TESTSTRING);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setDateMajEtat(today);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setDateMep(today);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setDateRepack(today);
        assertThat(objetTest.equals(lot)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(lot.hashCode());

        lot.setRtcHS(true);
        assertThat(objetTest.equals(lot)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(lot.hashCode());
    }

    @Test
    public void testUpdate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation update
        LotRTC lot = LotRTC.getLotRTCInconnu(LOT123456);
        lot.setLibelle("A");
        lot.setProjetClarity(ProjetClarity.getProjetClarityInconnu("a"));
        lot.setEtatLot(EtatLot.ABANDONNE);
        lot.setProjetRTC("D");
        lot.setDateMajEtat(Statics.DATEINCONNUE);

        // Update
        objetTest.update(lot);
        assertThat(objetTest.getLibelle()).isEqualTo(lot.getLibelle());
        assertThat(objetTest.getProjetClarity()).isEqualTo(lot.getProjetClarity());
        assertThat(objetTest.getEtatLot()).isEqualTo(lot.getEtatLot());
        assertThat(objetTest.getProjetRTC()).isEqualTo(lot.getProjetRTC());
        assertThat(objetTest.getDateMajEtat()).isEqualTo(lot.getDateMajEtat());
        assertThat(objetTest.getNumero()).isNotEqualTo(lot.getNumero());
        assertThat(objetTest.getDateMajEtat()).isEqualTo(lot.getDateMajEtat());
    }

    @Test
    public void testGetNumero(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNumero(), (s) -> objetTest.setNumero(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLibelle(), s -> objetTest.setLibelle(s));
    }

    @Test
    public void testGetProjetClarity(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getProjetClarity()).isNull();

        // Test setter et getter
        ProjetClarity pc = ProjetClarity.getProjetClarityInconnu("projClarity");
        objetTest.setProjetClarity(pc);
        assertThat(objetTest.getProjetClarity()).isEqualTo(pc);

        // Test protection setter null
        objetTest.setProjetClarity(null);
        assertThat(objetTest.getProjetClarity()).isEqualTo(pc);
    }

    @Test
    public void testGetCpiProjet(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getCpiProjet()).isNull();

        // Test setter et getter
        Utilisateur edition = Statics.USERINCONNU;
        objetTest.setCpiProjet(edition);
        assertThat(objetTest.getCpiProjet()).isEqualTo(edition);

        // Test protection setter null
        objetTest.setCpiProjet(null);
        assertThat(objetTest.getCpiProjet()).isEqualTo(edition);
    }

    @Test
    public void testGetEdition(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getEdition()).isNull();

        // Test setter et getter
        Edition edition = Edition.getEditionInconnue(null);
        objetTest.setEdition(edition);
        assertThat(objetTest.getEdition()).isEqualTo(edition);

        // Test protection setter null
        objetTest.setEdition(null);
        assertThat(objetTest.getEdition()).isEqualTo(edition);
    }

    @Test
    public void testGetEtatLot(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getEtatLot()).isEqualTo(EtatLot.INCONNU);

        // Test setter et getter
        objetTest.setEtatLot(EtatLot.DEVTU);;
        assertThat(objetTest.getEtatLot()).isEqualTo(EtatLot.DEVTU);
    }

    @Test
    public void testGetDateMep(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateMep(), s -> objetTest.setDateMep(s));
    }

    @Test
    public void testGetProjetRTC(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getProjetRTC(), s -> objetTest.setProjetRTC(s));
    }

    @Test
    public void testGetDateMajEtat(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à 2016/01/01
        assertThat(objetTest.getDateMajEtat()).isNotNull();
        assertThat(objetTest.getDateMajEtat()).isEqualTo(LocalDate.of(2016, 01, 01));

        // Test getter et setter
        objetTest.setDateMajEtat(today);
        assertThat(objetTest.getDateMajEtat()).isEqualTo(today);

        // Test protection setter null
        objetTest.setDateMajEtat(null);
        assertThat(objetTest.getDateMajEtat()).isNotNull();
        assertThat(objetTest.getDateMajEtat()).isEqualTo(today);
    }

    @Test
    public void testGetProjetClarityString(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getProjetClarityString(), s -> objetTest.setProjetClarityString(s));
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
    }

    @Test
    public void testGetEditionString(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEditionString(), s -> objetTest.setEditionString(s));
    }

    @Test
    public void testGetDateRepack(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateRepack(), d -> objetTest.setDateRepack(d));
    }

    @Test
    public void testIsRtcHS(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isRtcHS(), (b) -> objetTest.setRtcHS(b));
    }

    @Test
    public void testGetLiens(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec numéro ou projet vide
        objetTest.setNumero(EMPTY);
        objetTest.setProjetRTC(EMPTY);
        assertThat(objetTest.getLiens()).isEmpty();

        objetTest.setProjetRTC(KEY);
        assertThat(objetTest.getLiens()).isEmpty();

        objetTest.setProjetRTC(EMPTY);
        objetTest.setNumero(LOT123456);
        assertThat(objetTest.getLiens()).isEmpty();

        // test liens complet
        objetTest.setNumero(LOT123456);
        objetTest.setProjetRTC(KEY);
        assertThat(objetTest.getLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.LIENSRTC) + KEY + Statics.FINLIENSRTC + LOT123456);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
