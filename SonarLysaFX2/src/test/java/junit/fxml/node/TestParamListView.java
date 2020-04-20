package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import com.google.common.truth.Correspondence;

import control.rtc.ControlRTC;
import fxml.node.ParamListView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ParamSpec;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestParamListView extends TestAbstractFXML<ParamListView>
{
    /*---------- ATTRIBUTS ----------*/

    private List<Node> childrens;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws TimeoutException
    {
        stage = FxToolkit.registerPrimaryStage();
        stage.setWidth(800);
        FxToolkit.setupFixture(() -> { 
            objetTest = new ParamListView(ParamSpec.MEMBRESAQP); 
            stage.setScene(new Scene(objetTest)); 
            stage.show(); 
            childrens = objetTest.getChildren(); 
            });
    }

    @BeforeEach
    public void initStage() throws TimeoutException
    {
        // Rien à faire ici.
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test que l aliste des style comprend bien application.css
        assertThat(objetTest.getStylesheets()).comparingElementsUsing(Correspondence.from((a, b) -> ((String) a).contains((String) b), "matches")).containsExactly(Statics.CSS);

        // Test de la classe css
        assertThat(objetTest.getStyleClass()).containsExactly("root", "bgimage");

        // Test du type des objets FXML
        assertThat(childrens).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsAnyOf(HBox.class, Region.class, Separator.class);

        // Test du nombre d'objets FXML
        assertThat(robot.from(childrens).lookup((Node o) -> o instanceof HBox).queryAll()).hasSize(2);
    }

    @Test
    public void testSauverValeurs(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String saveParam = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESAQP);
        objetTest.sauverValeurs(Statics.proprietesXML.getMapParamsSpec());
        Statics.proprietesXML.getMapParamsSpec().put(ParamSpec.MEMBRESAQP, saveParam);
    }

    @Test
    public void testSupprimer(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération des Nodes
        @SuppressWarnings("unchecked")
        ListView<String> liste = ((ListView<String>) lookup(robot, "listView"));
        int size = liste.getItems().size();

        // Sélection d'une ligne et a click sur le bouton supprimer
        String nom = liste.getItems().get(0);
        robot.clickOn(nom);
        robot.clickOn((Button) lookup(robot, "suppr"));

        // Contrôle que la taille de la liste a bien diminuée de 1
        assertThat(liste.getItems()).hasSize(size - 1);
        assertThat(liste.getItems()).doesNotContain(nom);
    }

    @Test
    public void testAjouter(TestInfo testInfo, FxRobot robot) throws Throwable
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération des boutons et des données
        initDataBase();
        ControlRTC.build().connexionSimple();
        @SuppressWarnings("unchecked")
        List<String> liste = ((ListView<String>) lookup(robot, "listView")).getItems();
        int size = liste.size();
        Button ajouter = (Button) lookup(robot, "add");
        TextField valeur = (TextField) lookup(robot, "valeurField");

        // Contrôle ajout d'un nom déjà existant
        robot.clickOn(valeur);
        robot.write(databaseXML.getUsers().get(0).getNom());
        robot.clickOn(ajouter);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat(liste.size()).isEqualTo(size + 1);
        assertThat(liste).contains(databaseXML.getUsers().get(0).getNom());
    }

    @Test
    public void testAjouter_Exception_nom_existant(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération des boutons et des données
        @SuppressWarnings("unchecked")
        List<String> liste = ((ListView<String>) lookup(robot, "listView")).getItems();
        TextField valeur = (TextField) lookup(robot, "valeurField");

        // Contrôle ajout d'un nom déjà existant
        robot.clickOn(valeur);
        robot.write(liste.get(0));
        FunctionalException e = assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "ajouter"));
        assertThat(e.getMessage()).isEqualTo("La valeur est déjà contenu dans la liste.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testAjouter_Exception_nom_inexistant_RTC(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération des boutons et des données
        ControlRTC.build().connexionSimple();
        TextField valeur = (TextField) lookup(robot, "valeurField");

        // Contrôle d'un nom inéxistant dans RTC
        robot.clickOn(valeur);
        valeur.setText(Statics.EMPTY);
        robot.write("Inconnu");
        FunctionalException e = assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "ajouter"));
        assertThat(e.getMessage()).isEqualTo("Le nom n'est pas reconnu dans RTC. Essayez [NOM] [Prenom] sans accent.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testAjouter_Exception_parametre_technique(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Contrôle paramètre technique non géré
        setField("param", ParamSpec.TEXTERELANCEANORTCAPPLI);
        TechnicalException e2 = assertThrows(TechnicalException.class, () -> Whitebox.invokeMethod(objetTest, "ajouter"));
        assertThat(e2.getMessage()).isEqualTo("Méthode fxml.view.ParamListView.ajouter - le type de ParamSpec n'est pas géré : TEXTAREA");
        assertThat(e2.getSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    public void testCalculLongueur(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        List<String> liste = Arrays.asList("test", "123456789", "0");
        assertThat((double) Whitebox.invokeMethod(objetTest, "calculLongueur", liste)).isEqualTo(83d);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
