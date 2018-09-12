package control.view;

import control.task.CreerVueDataStageTask;
import control.task.CreerVueParEditionTask;
import control.task.CreerVuePatrimoineTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import utilities.TechnicalException;

/**
 * Controleur de la page de contrôle de gestion des autres vues SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class AutresVuesViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private RadioButton radioDataStage;
    @FXML
    private RadioButton radioPat;
    @FXML
    private RadioButton radioApps;
    @FXML
    private RadioButton radioEditions;
    @FXML
    private Button creerApps;
    @FXML
    private Button creerPat;
    @FXML
    private Button creerDataStage;
    @FXML
    private Button creerEdition;
    @FXML
    private VBox selectPane;
    @FXML
    private CheckBox excelSeul;
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
    public void creerPat()
    {
        startTask(new CreerVuePatrimoineTask());
    }
    
    @FXML
    public void creerDataStage()
    {
        startTask(new CreerVueDataStageTask());
    }
    
    @FXML
    public void creerEdition()
    {
        startTask(new CreerVueParEditionTask());
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        Object source = event.getSource();

        if (source instanceof RadioButton)
        {
            selectPane.getChildren().clear();

            String id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioDataStage" :
                    selectPane.getChildren().add(creerDataStage);
                    break;
                    
                case "radioPat" :
                    selectPane.getChildren().add(creerPat);
                    break;
                    
                case "radioEditions" :
                    selectPane.getChildren().add(creerEdition);
                    break;
                    
                default :
                    throw new TechnicalException("RadioButton pas géré" + id, null);
            }
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
