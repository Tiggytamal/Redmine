package junit.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import control.parsing.ControlXML;
import fxml.Options;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ColAppliDir;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.enums.ColPic;
import model.enums.ColVul;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestOptions extends TestAbstractFXML<Options>
{
    /*---------- ATTRIBUTS ----------*/

    private ControlXML mock;
    private TreeView<String> tree;
    private ObservableList<Node> root;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws TimeoutException
    {
        initFromFXML("/fxml/Options.fxml");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testSwitchPanel_Nom_Colonnes(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Test sans effet sur "Nom Colonnes"
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4)));
        assertThat(root).isEmpty();
    }

    @Test
    public void testSwitchPanel_Chargement_fichiers(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(0)));
        assertThat(root).contains(lookup(robot, "chargementPane"));
    }

    @Test
    public void testSwitchPanel_Parametres(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Paramètres - On teste le bon chargement de la page puis la sauvegarde des données depuis le bouton
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(1)));
        assertThat(root).contains(prepareTestParam(robot, "paramsPane", 25));
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Parametres_personnels(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);
        Options.disablePerso.set(false);
        
        // Paramètres personnels
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(2)));
        assertThat(root).contains(prepareTestParam(robot, "paramsPersoPane", 10));
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Parametres_particuliers(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Paramètres particuliers
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(3)));
        assertThat(root).contains(prepareTestParam(robot, "paramsAutresPane", 125));
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Clarity(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichiers Clarity
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(0)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColClarity.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Chefs_de_service(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier Chef de service
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(1)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColChefServ.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Editions(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier editions
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(2)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColEdition.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Vulnerabilites(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier extraction composants
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(3)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColVul.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Composants(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier extraction composants
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(4)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColCompo.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Applications_Directions(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier application/direction
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(5)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColAppliDir.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Fichiers_Pic(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareSwitchPanel(robot);

        // Colonnes fichier application/direction
        FxToolkit.setupFixture(() -> tree.getSelectionModel().select(tree.getTreeItem(4).getChildren().get(6)));
        assertThat(root).contains(prepareTestCol(robot));
        assertThat(((VBox) lookup(robot, "colonnesBox")).getChildren()).hasSize(ColPic.values().length);
        Mockito.verify(mock, Mockito.times(1)).saveXML(Mockito.any());
    }

    @Test
    public void testSwitchPanel_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        TreeItem<String> item = new TreeItem<>();
        item.setValue("erreur");
        ObservableValue<TreeItem<String>> observable = new SimpleObjectProperty<>(item);

        // Appel méthode pour remonter l'exception - vu le lancement par le toolkit, l'exception inital est dans une RuneTime
        RuntimeException e = assertThrows(RuntimeException.class, () -> FxToolkit.setupFixture(() -> objetTest.switchPanel(observable)));
        assertThat(e.getCause()).isInstanceOf(TechnicalException.class);
        assertThat(e.getCause().getMessage()).isEqualTo("TreeItem pas géré : erreur");
    }

    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void prepareSwitchPanel(FxRobot robot) throws IllegalAccessException
    {
        // Mock
        mock = Mockito.mock(ControlXML.class);
        setField("controlXML", mock);

        // Variables
        tree = (TreeView<String>) lookup(robot, "treeView");
        root = ((GridPane) lookup(robot, "rightSide")).getChildren();
    }

    private ScrollPane prepareTestParam(FxRobot robot, String id, int scrolling)
    {
        ScrollPane retour = (ScrollPane) lookup(robot, id);
        Button sauve = (Button) robot.from(retour.getChildrenUnmodifiable()).lookup((Predicate<Node>) (t) -> (t instanceof Button) && "Sauvegarder".equals(((Button) t).getText())).query();
        robot.clickOn(retour);
        robot.scroll(scrolling, VerticalDirection.DOWN);
        robot.clickOn(sauve);
        return retour;
    }

    private ScrollPane prepareTestCol(FxRobot robot)
    {
        ScrollPane retour = (ScrollPane) lookup(robot, "colonnesPane");
        robot.scroll(20, VerticalDirection.DOWN);
        Button sauve = (Button) robot.from(retour.getChildrenUnmodifiable()).lookup((Predicate<Node>) (t) -> (t instanceof Button) && "Sauvegarder".equals(((Button) t).getText())).query();
        robot.clickOn(sauve);
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
