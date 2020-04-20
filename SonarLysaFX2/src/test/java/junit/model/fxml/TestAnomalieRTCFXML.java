package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.bdd.AnomalieRTC;
import model.bdd.ProjetClarity;
import model.fxml.AnomalieRTCFXML;
import model.fxml.AnomalieRTCFXML.AnomalieRTCFXMLGetters;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestAnomalieRTCFXML extends TestAbstractModele<AnomalieRTCFXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetListeGetters(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertArrayEquals(AnomalieRTCFXMLGetters.values(), objetTest.getListeGetters());
    }

    @Test
    public void testGetIndex(TestInfo testInfo)
    {
        objetTest.setNumero(Arrays.asList("123456", Statics.EMPTY));
        assertThat(objetTest.getIndex()).isEqualTo(Integer.valueOf(objetTest.getNumero().get(0)));
    }

    @Test
    public void testBuild(TestInfo testInfo)
    {
        // Initialisation paramètre méthode build
        AnomalieRTC anoRTC = ModelFactory.build(AnomalieRTC.class);
        anoRTC.setTitre(TESTSTRING);
        anoRTC.setEtatAno(Statics.ANOCLOSE);
        anoRTC.setCommentaire(TESTSTRING);
        anoRTC.setNumero(123456);
        anoRTC.setEdition("E32");
        anoRTC.setMatiere("JAVA");
        anoRTC.setDateCreation(today);

        // Appel méthode
        AnomalieRTCFXML ano = AnomalieRTCFXML.build(anoRTC);

        // Contrôles
        assertThat(ano.getTitre()).isEqualTo(TESTSTRING);
        assertThat(ano.getEtatAno()).isEqualTo(Statics.ANOCLOSE);
        assertThat(ano.getCommentaire()).isEqualTo(TESTSTRING);
        assertThat(ano.getNumero().get(0)).isEqualTo(LOT123456);
        assertThat(ano.getNumero().get(1)).isEqualTo(anoRTC.getLiens());
        assertThat(ano.getEdition()).isEqualTo("E32");
        assertThat(ano.getMatiere()).isEqualTo("JAVA");
        assertThat(ano.getDateCrea()).isEqualTo(today.toString());
        assertThat(ano.getCpiLot()).isEmpty();
        assertThat(ano.getEmailCpi()).isEmpty();
        assertThat(ano.getDateReso()).isEmpty();
        assertThat(ano.getDateRelance()).isEmpty();
        assertThat(ano.getCodeClarity()).isEmpty();
        assertThat(ano.getLibelleClarity()).isEmpty();
        assertThat(ano.getDepartement()).isEmpty();
        assertThat(ano.getService()).isEmpty();
        assertThat(ano.getDirection()).isEmpty();
        assertThat(ano.getRespServ()).isEmpty();
        
        // Ajout données supplémentaires
        anoRTC.setDateReso(today);
        anoRTC.setDateRelance(today);
        anoRTC.setClarity(ProjetClarity.getProjetClarityInconnu("PRJF_Test"));
        anoRTC.setCpiLot(Statics.USERINCONNU);
        
        // Appel méthode
        ano = AnomalieRTCFXML.build(anoRTC);

        // Contrôles données supllémenaires
        assertThat(ano.getDateReso()).isEqualTo(today.toString());
        assertThat(ano.getDateRelance()).isEqualTo(today.toString());
        assertThat(ano.getCodeClarity()).isEqualTo("PRJF_Test");
        assertThat(ano.getLibelleClarity()).isEqualTo(ProjetClarity.INCONNU);
        assertThat(ano.getDepartement()).isEqualTo(Statics.INCONNU);
        assertThat(ano.getService()).isEqualTo(Statics.INCONNU);
        assertThat(ano.getDirection()).isEqualTo(Statics.INCONNUE);
        assertThat(ano.getRespServ()).isEqualTo(Statics.CHEFSERVINCONNU.getNom());
        assertThat(ano.getCpiLot()).isEqualTo(Statics.USERINCONNU.getNom());
        assertThat(ano.getEmailCpi()).isEqualTo(Statics.USERINCONNU.getEmail());
    }

    @Test
    void testGetTitre(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getTitre(), s -> objetTest.setTitre(s));
    }

    @Test
    void testGetNumero(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleListFXML(testInfo, () -> objetTest.getNumero(), l -> objetTest.setNumero(l));
    }

    @Test
    void testGetEtatAno(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEtatAno(), s -> objetTest.setEtatAno(s));
    }

    @Test
    void testGetCommentaire(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getCommentaire(), s -> objetTest.setCommentaire(s));
    }

    @Test
    void testGetMatiere(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getMatiere(), s -> objetTest.setMatiere(s));
    }

    @Test
    void testGetCpiLot(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getCpiLot(), s -> objetTest.setCpiLot(s));
    }

    @Test
    void testGetEmailCpi(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEmailCpi(), s -> objetTest.setEmailCpi(s));
    }

    @Test
    void testGetEdition(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getEdition(), s -> objetTest.setEdition(s));
    }

    @Test
    void testGetDateCrea(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getDateCrea(), s -> objetTest.setDateCrea(s));
    }

    @Test
    void testGetDateReso(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getDateReso(), s -> objetTest.setDateReso(s));
    }

    @Test
    void testGetDateRelance(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getDateRelance(), s -> objetTest.setDateRelance(s));
    }

    @Test
    void testGetDirection(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getDirection(), s -> objetTest.setDirection(s));
    }

    @Test
    void testGetDepartement(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getDepartement(), s -> objetTest.setDepartement(s));
    }

    @Test
    void testGetService(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getService(), s -> objetTest.setService(s));
    }

    @Test
    void testGetRespService(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getRespServ(), s -> objetTest.setRespServ(s));
    }

    @Test
    void testGetCodeClarity(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getCodeClarity(), s -> objetTest.setCodeClarity(s));
    }

    @Test
    void testGetLibelleClarity(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleString(testInfo, () -> objetTest.getLibelleClarity(), s -> objetTest.setLibelleClarity(s));
    }

    @Test
    public void testAnomalieRTCFXMLGettersValues(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(AnomalieRTCFXMLGetters.TITRE.getGroupe()).isEqualTo("Informations");
        assertThat(AnomalieRTCFXMLGetters.TITRE.getAffichage()).isEqualTo("Titre");
        assertThat(AnomalieRTCFXMLGetters.TITRE.getNomParam()).isEqualTo("titre");
        assertThat(AnomalieRTCFXMLGetters.TITRE.getNomMethode()).isEqualTo("getTitre");
        assertThat(AnomalieRTCFXMLGetters.TITRE.getStyle()).isEqualTo("tableYellow");
        assertThat(AnomalieRTCFXMLGetters.TITRE.isAffParDefaut()).isTrue();
        assertThat(AnomalieRTCFXMLGetters.TITRE.toString()).isEqualTo("Titre");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
