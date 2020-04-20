package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.AnomalieRTC;
import model.bdd.ProjetClarity;
import model.bdd.Utilisateur;
import model.fxml.DefautQualiteFXML;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestAnomalieRTC extends TestAbstractBDDModele<AnomalieRTC, Integer>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String string = objetTest.toString();
        assertThat(string).contains("numero=0");
        assertThat(string).contains("titre=");
        assertThat(string).contains("commentaire=");
        assertThat(string).contains("dateCreation=<null>");
        assertThat(string).contains("dateReso=<null>");
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test après initialisation
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNumero());

        // Test après valorisation numéro
        objetTest.setNumero(123456);
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNumero());
    }

    @Test
    public void testEquals(TestInfo testInfo) throws JAXBException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        initDataBase();
        AnomalieRTC ano = ModelFactory.build(AnomalieRTC.class);

        // Préparation objetTest
        objetTest.setTitre(TESTSTRING);
        objetTest.setNumero(123456);
        objetTest.setEtatAno(Statics.ANOCLOSE);
        objetTest.setCommentaire(TESTSTRING);
        objetTest.setDateCreation(today);
        objetTest.setDateReso(today);
        objetTest.setDateRelance(today);
        objetTest.setMatiere("JAVA");
        objetTest.setEdition("E32");

        // Test simple
        testSimpleEquals(ano);

        // Test paramètres 1 apr 1
        ano.setTitre(TESTSTRING);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setNumero(123456);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setEtatAno(Statics.ANOCLOSE);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setCommentaire(TESTSTRING);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setDateCreation(today);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setDateReso(today);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setDateRelance(today);
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setMatiere("JAVA");
        assertThat(objetTest.equals(ano)).isFalse();

        ano.setEdition("E32");
        assertThat(objetTest.equals(ano)).isTrue();
    }

    @Test
    public void testHashCode(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        initDataBase();
        AnomalieRTC ano = ModelFactory.build(AnomalieRTC.class);

        // Préparation objetTest
        objetTest.setTitre(TESTSTRING);
        objetTest.setNumero(123456);
        objetTest.setEtatAno(Statics.ANOCLOSE);
        objetTest.setCommentaire(TESTSTRING);
        objetTest.setDateCreation(today);
        objetTest.setDateReso(today);
        objetTest.setDateRelance(today);
        objetTest.setMatiere("JAVA");
        objetTest.setEdition("E32");

        // Test paramètres 1 apr 1
        ano.setTitre(TESTSTRING);
        assertThat(objetTest.hashCode()).isNotEqualTo(ano.hashCode());

        ano.setNumero(123456);
        assertThat(objetTest.hashCode()).isNotEqualTo(ano.hashCode());

        ano.setDateCreation(today);
        assertThat(objetTest.hashCode()).isNotEqualTo(ano.hashCode());

        ano.setMatiere("JAVA");
        assertThat(objetTest.hashCode()).isNotEqualTo(ano.hashCode());

        ano.setEdition("E32");
        assertThat(objetTest.hashCode()).isEqualTo(ano.hashCode());
    }

    @Test
    public void testGetLiens(TestInfo testInfo)
    {
        // Test numéro à 0
        assertThat(objetTest.getLiens()).isEmpty();

        // Test autres valeurs
        objetTest.setNumero(123456);
        assertThat(objetTest.getLiens()).isEqualTo("https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/Support%20NICE#action=com.ibm.team.workitem.viewWorkItem&id=123456");
        objetTest.setNumero(111111);
        assertThat(objetTest.getLiens()).isEqualTo("https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/Support%20NICE#action=com.ibm.team.workitem.viewWorkItem&id=111111");
    }

    @Test
    public void testBuildDefautQualiteFXML() throws JAXBException
    {
        initDataBase();

        // Initialisation données avec objets null
        objetTest.setTitre(TESTSTRING);
        objetTest.setNumero(123456);
        objetTest.setEtatAno(Statics.ANOCLOSE);
        objetTest.setCommentaire(TESTSTRING);
        objetTest.setMatiere("JAVA");
        objetTest.setEdition("E32");

        // Appel méthode et contrôles
        DefautQualiteFXML test = objetTest.buildDefautQualiteFXML();
        assertThat(test.getNumeroAnoRTC().get(0)).isEqualTo(String.valueOf(objetTest.getNumero()));
        assertThat(test.getNumeroAnoRTC().get(1)).isEqualTo(objetTest.getLiens());
        assertThat(test.getEtatRTC()).isEqualTo(objetTest.getEtatAno());
        assertThat(test.getRemarque()).isEqualTo(objetTest.getCommentaire());
        assertThat(test.getTypeDefaut()).isEqualTo(objetTest.getTypeDefaut());
        assertThat(test.getMatiere()).isEqualTo(objetTest.getMatiere());
        assertThat(test.getEdition()).isEqualTo(objetTest.getEdition());
        assertThat(test.getDateCreation()).isEmpty();
        assertThat(test.getDateRelance()).isEmpty();
        assertThat(test.getDateReso()).isEmpty();
        assertThat(test.getCpiLot()).isEmpty();
        assertThat(test.getEmailCpi()).isEmpty();
        assertThat(test.getDepartement()).isEmpty();
        assertThat(test.getService()).isEmpty();
        assertThat(test.getDirection()).isEmpty();
        assertThat(test.getLibelleClarity()).isEmpty();
        assertThat(test.getRespServ()).isEmpty();
        assertThat(test.getCodeClarity()).isEmpty();

        // Initialisation avec données non null
        objetTest.setDateCreation(today);
        objetTest.setDateReso(today);
        objetTest.setDateRelance(today);
        objetTest.setCpiLot(databaseXML.getUsers().get(0));
        objetTest.setClarity(ProjetClarity.getProjetClarityInconnu("123456"));

        // Appel méthode et contrôles
        test = objetTest.buildDefautQualiteFXML();
        assertThat(test.getDateCreation()).isEqualTo(objetTest.getDateCreation().toString());
        assertThat(test.getDateRelance()).isEqualTo(objetTest.getDateRelance().toString());
        assertThat(test.getDateReso()).isEqualTo(objetTest.getDateReso().toString());
        assertThat(test.getCpiLot()).isEqualTo(objetTest.getCpiLot().getNom());
        assertThat(test.getEmailCpi()).isEqualTo(objetTest.getCpiLot().getEmail());
        assertThat(test.getDepartement()).isEqualTo(objetTest.getClarity().getDepartement());
        assertThat(test.getService()).isEqualTo(objetTest.getClarity().getService());
        assertThat(test.getDirection()).isEqualTo(objetTest.getClarity().getDirection());
        assertThat(test.getLibelleClarity()).isEqualTo(objetTest.getClarity().getLibelleProjet());
        assertThat(test.getRespServ()).isEqualTo(objetTest.getClarity().getChefService().getNom());
        assertThat(test.getCodeClarity()).isEqualTo(objetTest.getClarity().getCode());

    }

    @Test
    public void testGetTitre(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getTitre(), s -> objetTest.setTitre(s));
    }

    @Test
    public void testGetNumero(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNumero(), i -> objetTest.setNumero(i));
    }

    @Test
    public void testGetCommentaire(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCommentaire(), s -> objetTest.setCommentaire(s));
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateCreation(), d -> objetTest.setDateCreation(d));
    }

    @Test
    public void testGetDateReso(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateReso(), d -> objetTest.setDateReso(d));
    }

    @Test
    public void testGetDateRelance(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateRelance(), d -> objetTest.setDateRelance(d));
    }

    @Test
    public void testGetEtatAno(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEtatAno(), s -> objetTest.setEtatAno(s));
    }

    @Test
    public void testGetMatiere(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMatiere(), s -> objetTest.setMatiere(s));
    }

    @Test
    public void testGetClarity()
    {
        // Test initialisation null
        assertThat(objetTest.getClarity()).isNull();

        // Test setter et getter
        ProjetClarity projet = ProjetClarity.getProjetClarityInconnu("12345678");
        objetTest.setClarity(projet);
        assertThat(objetTest.getClarity()).isEqualTo(projet);

        // Protection null
        objetTest.setClarity(null);
        assertThat(objetTest.getClarity()).isEqualTo(projet);
    }

    @Test
    public void testGetEdition(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEdition(), s -> objetTest.setEdition(s));
    }

    @Test
    public void testGetCpiLot() throws JAXBException
    {
        // Test initialisation null
        assertThat(objetTest.getCpiLot()).isNull();

        // Test setter et getter
        initDataBase();
        Utilisateur user = databaseXML.getUsers().get(0);
        objetTest.setCpiLot(user);
        assertThat(objetTest.getCpiLot()).isEqualTo(user);

        // Protection null
        objetTest.setCpiLot(null);
        assertThat(objetTest.getCpiLot()).isEqualTo(user);
    }

    @Test
    public void testGetTypeDefaut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getTypeDefaut(), s -> objetTest.setTypeDefaut(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
