package control.view;

import control.ControlSonar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import utilities.Statics;

public class ApplicationsViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private GridPane backgroundPane;
    @FXML
    private Button creer;
    
    private ControlSonar handler;
    
    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        handler = new ControlSonar();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void creerVue()
    {
        handler.creerVueParApplication();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}