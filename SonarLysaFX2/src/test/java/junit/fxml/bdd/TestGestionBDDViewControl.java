package junit.fxml.bdd;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;

import com.google.common.truth.Correspondence;

import fxml.bdd.AbstractBDD;
import fxml.bdd.GestionBDD;
import fxml.dialog.LegendeDialog;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ActionDq;
import model.enums.Severity;
import model.fxml.DefautQualiteFXML;
import model.fxml.ListeGetters;
import utilities.FunctionalException;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestGestionBDDViewControl extends TestAbstractFXML<GestionBDD<DefautQualiteFXML, ActionDq, String>>
{
    /*---------- ATTRIBUTS ----------*/

    private SplitPane pane;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        initFromFXML("/fxml/bdd/GestionBDD.fxml");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testControleur(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());
        initSplitPane(robot);

        // Vérification qu'il n'y a que la colonne des boutons de selection de langue au début
        assertThat(pane.getItems()).hasSize(1);
        assertThat(pane.getItems().get(0)).isInstanceOf(HBox.class);

    }

    @ParameterizedTest
    @SuppressWarnings("rawtypes")
    @ValueSource(strings = { "compo", "dq", "compoPlante", "ano", "user" })
    public void testAfficher_Tableau_et_visibilite(String bouton, TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());
        initSplitPane(robot);

        // Click sur chaque bouton pour afficher le tableau correspondant
        robot.clickOn((Button) lookup(robot, bouton));

        // Vérification que l'on rajoute bien le tableau à l'affichage et que celui-ci n'est pas vide
        assertThat(pane.getItems()).hasSize(2);
        assertThat(pane.getItems().get(0)).isInstanceOf(HBox.class);
        assertThat(pane.getItems().get(1)).isInstanceOf(VBox.class);
        TableView table = ((TableView) lookup(robot, "table"));
        assertThat(table.getItems()).isNotEmpty();

        // Click sur le bouton d'affichage des colonnes
        robot.clickOn((Button) lookup(robot, "visibilite"));

        // Deuxième click sur le bouton pour vérifier que l'on garde bien l'affichage et que l'on affichage pas deux tableaux
        robot.clickOn((Button) lookup(robot, bouton));

        // Vérification que l'on rajoute bien le tableau à l'affichage et que celui-ci n'est pas vide
        assertThat(pane.getItems()).hasSize(2);
        assertThat(pane.getItems().get(0)).isInstanceOf(HBox.class);
        assertThat(pane.getItems().get(1)).isInstanceOf(VBox.class);
        table = ((TableView) lookup(robot, "table"));
        assertThat(table.getItems()).isNotEmpty();
    }

    @Test
    public void testExtraire(TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation tableau des défauts qualité
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));

        objetTest = Mockito.spy(objetTest);
        File file = new File(Statics.RESSTEST + "testExtractbis.xlsx");
        Mockito.doReturn(file).when(objetTest).recupererFichier();

        // Lancement extraction
        Whitebox.invokeMethod(objetTest, "extraire");

        // Contrôle fichier
        assertThat(file).isNotNull();

        Workbook wb = WorkbookFactory.create(file);
        assertThat(wb).isNotNull();

        // Contrôle que l'on retourve la bonne feuille dépend du controlleur utilisé
        Sheet sheet = wb.getSheet("Défauts Qualité");
        assertThat(sheet).isNotNull();

        // Contrôle sur la création des titres
        for (Cell cell : sheet.getRow(0))
        {
            assertThat(cell.getCellStyle().getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
            assertThat(cell.getCellStyle().getFillForegroundColor()).isEqualTo(IndexedColors.AQUA.index);
        }

        // On vérifie que l'on a plus que la ligne de titres
        assertThat(sheet.getLastRowNum()).isGreaterThan(1);
    }

    @Test
    public void testGestionColonne(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation avec affichage tableau Défauts Qualité
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));

        // Node correspondant à la liste des colonnes à afficher
        VBox box = ((VBox) lookup(robot, "listeColonne"));

        // Test initial box non visible
        assertThat(box.isVisible()).isFalse();
        assertThat(box.isManaged()).isFalse();

        // Click sur le bouton d'affichage des colonnes
        robot.clickOn((Button) lookup(robot, "visibilite"));

        // Contrôles
        assertThat(box.isVisible()).isTrue();
        assertThat(box.isManaged()).isTrue();

        // Click sur le bouton pour enlever l'affichage
        robot.clickOn((Button) lookup(robot, "visibilite"));

        // Contrôles
        assertThat(box.isVisible()).isFalse();
        assertThat(box.isManaged()).isFalse();
    }

    @Test
    public void testLegendeColonne(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));
        LegendeDialog legendeDialog = (LegendeDialog) getField("legendeDialog");
        Button legende = (Button) lookup(robot, "legende");

        // Contrôle Initial
        assertThat(legendeDialog.isShowing()).isFalse();

        // Affichage de la fenêtre des légendes
        robot.clickOn(legende);

        // Contrôle
        assertThat(legendeDialog.isShowing()).isTrue();

        // Fermeture de la fenêtre des légendes
        robot.clickOn(lookup(robot, "filtre"));
        robot.clickOn(legende);

        // Contrôle
        assertThat(legendeDialog.isShowing()).isFalse();
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testFiltrer(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));
        VBox listeFiltres = (VBox) lookup(robot, "listeFiltres");
        assertThat(listeFiltres.getChildren()).isEmpty();

        // Click pour le choix d'une colonne
        @SuppressWarnings("unchecked")
        ComboBox<ListeGetters> combo = (ComboBox<ListeGetters>) lookup(robot, "comboColonne");
        for (Node child : combo.getChildrenUnmodifiable())
        {
            if (child.getStyleClass().contains("arrow-button"))
            {
                Node arrowRegion = ((Pane) child).getChildren().get(0);
                robot.clickOn(arrowRegion);
                robot.clickOn("Direction");
            }
        }

        // Définition du texte du filtre
        ((TextField) lookup(robot, "filtre")).setText("SRVT");

        // Appel méthode depuis écran
        robot.clickOn((Button) lookup(robot, "ajout"));

        // Contrôles que l'on affiche bien le filtre
        assertThat(listeFiltres.getChildren()).hasSize(2);
        assertThat(listeFiltres.getChildren()).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsExactly(Label.class, Region.class);

        // Contrôle que le predicat est bine créé
        assertThat(((FilteredList) Whitebox.getField(AbstractBDD.class, "filteredList").get((AbstractBDD) getField("controlleur"))).getPredicate()).isNotNull();
    }

    @Test
    public void testFiltrer_Exception_sans_texte(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));

        // Contrôle sans
        FunctionalException e = assertThrows(FunctionalException.class, () -> objetTest.filtrer());
        assertThat(e.getMessage()).isEqualTo("Vous devez choisir un texte pour le filtre et un colonne à filtrer.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testFiltrer_Exception_avec_texte(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        initSplitPane(robot);
        robot.clickOn((Button) lookup(robot, "dq"));

        // Ajout d'un texte
        ((TextField) lookup(robot, "filtre")).setText("SRVT");
        FunctionalException e = assertThrows(FunctionalException.class, () -> objetTest.filtrer());
        assertThat(e.getMessage()).isEqualTo("Vous devez choisir un texte pour le filtre et un colonne à filtrer.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testAjoutertLiens(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // variables
        String hyperliens = "http:////www.qwant.fr";

        // Création wb, feuille, ligne et cellule
        Workbook wb = WorkbookFactory.create(true);
        Cell cell = wb.createSheet("sheet").createRow(0).createCell(0);

        // Controle cell
        assertThat(cell).isNotNull();
        assertThat(cell.getCellType()).isEqualTo(CellType.BLANK);

        // Appel methode avec hyperliens
        invokeMethod(objetTest, METHODEAJOUTERLIENS, wb, cell, hyperliens);

        // Test valeurs
        assertThat(cell.getStringCellValue()).isNotEqualTo(hyperliens);
        assertThat(cell.getHyperlink()).isNotNull();
        assertThat(cell.getHyperlink().getAddress()).isNotNull();
        assertThat(cell.getHyperlink().getAddress()).isEqualTo(hyperliens);
    }

    private static final String METHODEAJOUTERLIENS = "ajouterLiens";

    @Test
    public void testAjoutertLiens_Exceptions(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création wb, feuille, ligne et cellule
        Workbook wb = WorkbookFactory.create(true);
        Cell cell = wb.createSheet("sheet").createRow(0).createCell(0);

        // Test exceptions avec arguments nuls.
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, METHODEAJOUTERLIENS, new Class<?>[]
        { Workbook.class, Cell.class, String.class }, null, cell, EMPTY));
        assertThat(e1.getMessage()).isEqualTo("Le worbook, la cellule ou l'adresse ne peuvent être nuls.");

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, METHODEAJOUTERLIENS, new Class<?>[]
        { Workbook.class, Cell.class, String.class }, wb, null, EMPTY));
        assertThat(e2.getMessage()).isEqualTo("Le worbook, la cellule ou l'adresse ne peuvent être nuls.");

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, METHODEAJOUTERLIENS, new Class<?>[]
        { Workbook.class, Cell.class, String.class }, wb, cell, null));
        assertThat(e3.getMessage()).isEqualTo("Le worbook, la cellule ou l'adresse ne peuvent être nuls.");
    }

    /*---------- METHODES PRIVEES ----------*/

    private void initSplitPane(FxRobot robot)
    {
        pane = (SplitPane) lookup(robot, "splitPane");
    }

    /*---------- ACCESSEURS ----------*/

}
