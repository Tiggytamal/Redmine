package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testfx.api.FxToolkit;

import com.google.common.truth.Correspondence;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import fxml.dialog.LegendeDialog;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestLegendeDialog extends TestAbstractFXML<LegendeDialog>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        // Chaque méthode créera son Dialog
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        RuntimeException e = assertThrows(RuntimeException.class, () -> FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("faux.fxml")));
        assertThat(e.getCause().getMessage()).isEqualTo("Méthode fxml.dialog.LegendeDialog.init - fxml non traité : faux.fxml");
    }

    @Test
    public void testInitDq(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("/fxml/bdd/DefaultQualiteBDD.fxml"));

        List<Node> children = testCommuns(objetTest);

        testInitLegende(14, children);
    }

    @Test
    public void testInitCompo(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("/fxml/bdd/ComposantBDD.fxml"));

        List<Node> children = testCommuns(objetTest);

        testInitLegende(6, children);
    }

    @Test
    public void testInitCompoPlante(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("/fxml/bdd/ComposantPlanteBDD.fxml"));

        List<Node> children = testCommuns(objetTest);

        testInitLegende(6, children);
    }

    @Test
    public void testInitAno(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("/fxml/bdd/AnomalieRTCBDD.fxml"));

        List<Node> children = testCommuns(objetTest);

        // Test ajout des légendes - ici qu'un seul Label
        assertThat(children).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsExactly(Label.class);
        assertThat(children).hasSize(1);
        assertThat(((Label) children.get(0)).getText()).isEqualTo("Pas d'affichage spécifique pour les lignes de cette table");
    }
    
    @Test
    public void testInitUtilisateur(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        FxToolkit.setupFixture(() -> objetTest = new LegendeDialog("/fxml/bdd/UtilisateurBDD.fxml"));

        List<Node> children = testCommuns(objetTest);

        testInitLegende(2, children);
    }

    /*---------- METHODES PRIVEES ----------*/

    private List<Node> testCommuns(LegendeDialog objetTest) throws IllegalAccessException
    {
        // Test objets communs à tous les dialogues
        assertThat(objetTest.getTitle()).isEqualTo("Légende");
        assertThat(objetTest.getHeaderText()).isEmpty();
        assertThat(objetTest.getDialogPane().getStylesheets()).containsExactly(CSS);
        assertThat(objetTest.getDialogPane().getContent()).isInstanceOf(VBox.class);
        assertThat(objetTest.getDialogPane().getButtonTypes()).containsExactly(ButtonType.OK, getField("close"));

        // Retourne la liste des Node de la VBox contenant tous les objets d'affichage de chaque legende
        return (List<Node>) ((VBox) objetTest.getDialogPane().getContent()).getChildren();
    }

    private void testInitLegende(int nbreLegende, List<Node> children)
    {
        // Test ajout des légendes - vérification du nombre de lignes et la composition correct des lignes.
        assertThat(children).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsAnyOf(HBox.class, Region.class);
        assertThat(children).hasSize(nbreLegende);
        for (Node node : children)
        {
            if (node.getClass().getName().equals("javafx.scene.layout.Region"))
                assertThat(((Region) node).getPrefHeight()).isEqualTo(5);
            else if (node instanceof HBox)
                assertThat(((HBox) node).getChildren()).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsExactly(Label.class,
                        ImageView.class);
        }
    }
    /*---------- ACCESSEURS ----------*/
}
