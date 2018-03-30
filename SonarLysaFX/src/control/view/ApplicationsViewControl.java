package control.view;

import java.io.IOException;

import control.CreerVueParAppsTask;
import control.parent.ViewControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ApplicationsViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button creer;
    
    /*---------- CONSTRUCTEURS ----------*/   
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void creerVue() throws IOException
    {
        startTask(new CreerVueParAppsTask(), "Vues par applications");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}