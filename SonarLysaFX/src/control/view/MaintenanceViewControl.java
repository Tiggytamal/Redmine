package control.view;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlSonar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utilities.Statics;

public class MaintenanceViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    private GridPane backgroundPane;
    @FXML
    private Button creer;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioExcel;
    @FXML
    private RadioButton radioDirect;
    @FXML
    private VBox selectPane;
    @FXML
    private Button charger;
    
    private ControlSonar handler;
    
    /*---------- CONSTRUCTEURS ----------*/
   
    @FXML
    public void initialize()
    {  
        handler = new ControlSonar(Statics.info.getPseudo(), Statics.info.getMotDePasse());
        selectPane.getChildren().clear();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void chargerExcel() throws InvalidFormatException, IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("FichierExcel");
        fileChooser.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fileChooser.showOpenDialog(backgroundPane.getScene().getWindow());
        if (file != null)
        {
            handler.creerVueCDM(file);
        }      
    }
    
    @FXML
    public void afficherParExcel()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(charger);
    }
    
    @FXML
    public void afficherDirect()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(creer);
    }
    
    @FXML
    public void creerVues()
    {
        handler.creerVueCDM();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}