package junit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import application.MainScreen;
import fxml.bdd.GestionBDD;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.Statics;

/**
 * Classe des mère des tests pour les vues et les classes de contr^le des vues
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 * @param <T>
 *        Classe à tester.
 */
public abstract class TestAbstractFXML<T> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    /** Stage permettant d'afficher la vue dans une fenêtre pour les tests */
    protected Stage stage;

    protected static Scene scene;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    public void initStage() throws TimeoutException
    {
        stage = FxToolkit.registerPrimaryStage();
        stage.setWidth(800);
        if (scene == null && MainScreen.ROOT.getScene() == null)
            scene = new Scene(MainScreen.ROOT);
        else if (scene == null)
            scene = MainScreen.ROOT.getScene();
    }

    @AfterEach
    public void afterAll() throws TimeoutException
    {
        FxToolkit.setupFixture(() -> stage.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Récupèration d'un Node depuis son id avec le robot
     * 
     * @param robot
     *              robot provenant de la méthode appelante.
     * @param id
     *              id du Node à récupérer sans le #
     * @return
     *         La Node correspondante à l'id.
     */
    protected Node lookup(FxRobot robot, String id)
    {
        return robot.lookup("#" + id).query();
    }

    /**
     * Permet d'initialiser une vue depuis le fichier xml et de l'afficher dans une Scene
     * 
     * @param fxml
     *             Fichier à utiliser.
     * @throws TimeoutException
     *                          Exception lancée par le Toolkit en cas de timeOut du Thread FX.
     */
    protected final void initFromFXML(String fxml) throws TimeoutException
    {
        stage = FxToolkit.registerPrimaryStage();

        FxToolkit.setupFixture(() -> {
            // Chargement de la page depuis le fichier fxml
            FXMLLoader loader = new FXMLLoader(GestionBDD.class.getResource(fxml));

            try
            {
                // Instanciation du Node et du controlleur
                Node load = loader.load();
                objetTest = loader.getController();

                // Ajout de la page dans une Scene pour pouvoir l'afficher
                MainScreen.ROOT.setCenter(load);
                scene.getStylesheets().add(Statics.CSS);
                stage.setScene(scene);
                stage.show();
                stage.toFront();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
