package control.view;

import java.io.IOException;

import control.CreerVuePatrimoineTask;
import control.parent.ViewControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PatrimoineViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button creer;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void creerVue() throws IOException
    {
        startTask(new CreerVuePatrimoineTask(), "Vue patrimoine");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}