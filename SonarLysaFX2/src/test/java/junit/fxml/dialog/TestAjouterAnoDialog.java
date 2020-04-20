package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.TextMatchers;

import com.sun.javafx.scene.control.skin.LabeledText;

import control.rtc.ControlRTC;
import dao.DaoUtilisateur;
import fxml.dialog.AjouterAnoDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.ModelFactory;
import model.bdd.AnomalieRTC;
import model.bdd.ProjetClarity;
import model.enums.Matiere;
import model.enums.TypeDefautQualite;
import utilities.Statics;
import utilities.TechnicalException;

@SuppressWarnings("restriction")
@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestAjouterAnoDialog extends TestAbstractFXML<AjouterAnoDialog>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        // @formatter:off
        FxToolkit.setupFixture(() -> { 
            objetTest = new AjouterAnoDialog(); 
            objetTest.show(); 
            });
        // @formatter:on
    }

    @AfterEach
    public void close(FxRobot robot) throws TimeoutException
    {
        robot.clickOn((LabeledText) robot.from(robot.lookup("#pane")).lookup(TextMatchers.hasText("Fermer")).query());
        FxToolkit.setupFixture(() -> objetTest.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    void testConstructeur(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(getField("numeroAnoField")).isNotNull();
        assertThat(getField("commentairesField")).isNotNull();
        assertThat(getField("matiereField")).isNotNull();
        assertThat(getField("typeDefautField")).isNotNull();
        assertThat(getField("dateResoPicker")).isNotNull();
        assertThat(getField("dateCreationPicker")).isNotNull();
        assertThat(getField("dateRelancePicker")).isNotNull();
        assertThat(getField("editionField")).isNotNull();
        assertThat(getField("codeClarityField")).isNotNull();
        assertThat(getField("idCpiField")).isNotNull();
    }

    @Test
    void testConstructeur_Depuis_anomalie() throws IllegalAccessException, TimeoutException, JAXBException
    {
        initDataBase();

        // Préparation de l'anomalie
        AnomalieRTC ano = ModelFactory.build(AnomalieRTC.class);
        ano.setNumero(123456);
        ano.setCommentaire(TESTSTRING);
        ano.setMatiere(Matiere.JAVA.getValeur());
        ano.setTypeDefaut(TypeDefautQualite.APPLI.name());
        ano.setDateReso(today);
        ano.setDateCreation(today);
        ano.setDateRelance(today);
        ano.setEdition("edition");

        // Appel constructeur
        FxToolkit.setupFixture(() -> objetTest = new AjouterAnoDialog(ano));

        // Contrôles
        assertThat(((TextField) getField("numeroAnoField")).getText()).isEqualTo(String.valueOf(ano.getNumero()));
        assertThat(((TextField) getField("commentairesField")).getText()).isEqualTo(ano.getCommentaire());
        assertThat(((TextField) getField("matiereField")).getText()).isEqualTo(ano.getMatiere());
        assertThat(((TextField) getField("typeDefautField")).getText()).isEqualTo(ano.getTypeDefaut());
        assertThat(((DatePicker) getField("dateResoPicker")).getValue()).isEqualTo(ano.getDateReso());
        assertThat(((DatePicker) getField("dateCreationPicker")).getValue()).isEqualTo(ano.getDateCreation());
        assertThat(((DatePicker) getField("dateRelancePicker")).getValue()).isEqualTo(ano.getDateRelance());
        assertThat(((TextField) getField("editionField")).getText()).isEqualTo(ano.getEdition());
        assertThat(((TextField) getField("codeClarityField")).getText()).isNotNull();
        assertThat(((TextField) getField("codeClarityField")).getText()).isEmpty();;
        assertThat(((TextField) getField("idCpiField")).getText()).isNotNull();
        assertThat(((TextField) getField("codeClarityField")).getText()).isEmpty();

        // Ajout données controlées
        ano.setClarity(ProjetClarity.getProjetClarityInconnu("Clarity"));
        ano.setCpiLot(databaseXML.getUsers().get(0));

        // Appel constructeur
        FxToolkit.setupFixture(() -> objetTest = new AjouterAnoDialog(ano));

        // Contrôles
        assertThat(((TextField) getField("codeClarityField")).getText()).isEqualTo(ano.getClarity().getCode());
        assertThat(((TextField) getField("idCpiField")).getText()).isEqualTo(ano.getCpiLot().getIdentifiant());
    }

    @Test
    void testControle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans valeur
        assertThat((boolean) invokeMethod(objetTest, "controle")).isFalse();
        assertThat((String) getField("donnesIncorrectes")).isEqualTo("Le numéro de l'anomalie n'est pas renseigné.\nLa date de création du lot doit être renseignée.\n");

        // Test numéro d'anomalie ok - erreur RTC sans connexion
        ControlRTC.getInstance().logout();
        ((TextField) getField("numeroAnoField")).setText("423584");
        TechnicalException e = assertThrows(TechnicalException.class, () -> invokeMethod(objetTest, "controle"));
        assertThat(e.getMessage()).isEqualTo("Méthode view.dialog.AjouterAnoDialog.controle - Erreur au moment de récupérer l'anomalie : 423584");

        // Connexion à RTC
        ControlRTC.getInstance().connexionSimple();

        // Test numéro d'anomalie erroné
        ((TextField) getField("numeroAnoField")).setText("000");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isFalse();
        assertThat((String) getField("donnesIncorrectes")).isEqualTo("Le numéro de l'anomalie est erroné.\nLa date de création du lot doit être renseignée.\n");

        // Test numéro d'anomalie ok
        ((TextField) getField("numeroAnoField")).setText("423584");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isFalse();
        assertThat((String) getField("donnesIncorrectes")).isEqualTo("La date de création du lot doit être renseignée.\n");

        // Test avec date de création correcte
        ((DatePicker) getField("dateCreationPicker")).setValue(LocalDate.of(2019, 1, 1));
        assertThat((boolean) invokeMethod(objetTest, "controle")).isTrue();
        assertThat((String) getField("donnesIncorrectes")).isEmpty();

        // Test avec code Clarity erroné
        ((TextField) getField("codeClarityField")).setText("inconnu");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isFalse();
        assertThat((String) getField("donnesIncorrectes")).isEqualTo("Le code clarity n'est pas connu dans la base de données.\n");

        // Test avec code Clarity ok
        ((TextField) getField("codeClarityField")).setText("BE011501");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isTrue();
        assertThat((String) getField("donnesIncorrectes")).isEmpty();

        // Test avec cpiLot erroné
        ((TextField) getField("idCpiField")).setText("inconnu");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isFalse();
        assertThat((String) getField("donnesIncorrectes")).isEqualTo("Aucun utilisateur n'a été trouvé depuis l'id : inconnu\n");

        // Test avec cpiLot ok
        ((TextField) getField("idCpiField")).setText("ETP8137");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isTrue();
        assertThat((String) getField("donnesIncorrectes")).isEmpty();

        // Test avec cpi n'existant pas en base de données
        DaoUtilisateur mockUser = Mockito.mock(DaoUtilisateur.class);
        Mockito.when(mockUser.readAll()).thenReturn(new ArrayList<>());
        setField("daoUser", mockUser);

        ((TextField) getField("idCpiField")).setText("ETP8137");
        assertThat((boolean) invokeMethod(objetTest, "controle")).isTrue();
        assertThat((String) getField("donnesIncorrectes")).isEmpty();

        // Test avec plantage RTC au moment de checher un utilisateur
        ControlRTC.getInstance().logout();
        ((TextField) getField("numeroAnoField")).setText(Statics.EMPTY);
        ((TextField) getField("idCpiField")).setText("ETP8137");
        e = assertThrows(TechnicalException.class, () -> invokeMethod(objetTest, "controle"));
        assertThat(e.getMessage()).isEqualTo("Méthode view.dialog.AjouterAnoDialog.controle - Erreur au moment de récupérer le contributeur : ETP8137");
    }

    @Test
    void testCreerObjet(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test à vide sauf le numéro pour éviter un plantage
        ((TextField) getField("numeroAnoField")).setText("423584");
        AnomalieRTC ano = invokeMethod(objetTest, "retourObjet");
        assertThat(ano).isNotNull();

        // Connexion à RTC
        ControlRTC.build().connexionSimple();

        // test avec données
        ((TextField) getField("idCpiField")).setText("ETP8137");
        ((TextField) getField("matiereField")).setText("JAVA");
        ((TextField) getField("typeDefautField")).setText("LEGACY");
        ((TextField) getField("matiereField")).setText("JAVA");
        ((TextField) getField("editionField")).setText("E32");

        // Appel méthode et contrôle - appel controlCpiLot pour instancier l'utilisateur
        invokeMethod(objetTest, "controlCpiLot");
        ano = invokeMethod(objetTest, "retourObjet");
        assertThat(ano.getCpiLot()).isNotNull();
        assertThat(ano.getCpiLot().getIdentifiant()).isEqualTo("ETP8137");
        assertThat(ano.getNumero()).isEqualTo(423584);
        assertThat(ano.getMatiere()).isEqualTo("JAVA");
        assertThat(ano.getTypeDefaut()).isEqualTo("LEGACY");
        assertThat(ano.getEdition()).isEqualTo("E32");

    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
