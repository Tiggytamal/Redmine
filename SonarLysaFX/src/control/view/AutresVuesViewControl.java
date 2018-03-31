package control.view;

import control.parent.ViewControl;
import control.task.CreerVueDataStageTask;
import control.task.CreerVueParAppsTask;
import control.task.CreerVuePatrimoineTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class AutresVuesViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private RadioButton radioDataStage;
    @FXML
    private RadioButton radioPat;
    @FXML
    private RadioButton radioApps;
    @FXML
    private Button creerApps;
    @FXML
    private Button creerPat;
    @FXML
    private Button creerDataStage;
    @FXML
    private VBox selectPane;
    @FXML
    private ToggleGroup toggleGroup;
    
    /*---------- CONSTRUCTEURS ----------*/   
    
    @FXML
    public void initialize()
    {
        selectPane.getChildren().clear();
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void creerApps()
    {
        startTask(new CreerVueParAppsTask(), null);
    }
    
    @FXML
    public void creerPat()
    {
        startTask(new CreerVuePatrimoineTask(), null);
    }
    
    @FXML
    public void creerDataStage()
    {
        startTask(new CreerVueDataStageTask(), null);
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        Object source = event.getSource();
        String id = "";
        if (source instanceof RadioButton)
        {
            selectPane.getChildren().clear();

            id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioDataStage" :
                    selectPane.getChildren().add(creerDataStage);
                    break;
                    
                case "radioPat" :
                    selectPane.getChildren().add(creerPat);
                    break;
                    
                case "radioApps" :
                    selectPane.getChildren().add(creerApps);
                    break;
                    
                default :
                    break;
            }
        }
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}