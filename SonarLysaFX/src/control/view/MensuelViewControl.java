package control.view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlSonar;
import control.parent.ViewControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class MensuelViewControl extends ViewControl
{
    /* ---------- ATTIBUTS ---------- */
       
    private ControlSonar handler;
    
    @SuppressWarnings("unused")
    private LocalDate dateDebut;
    @SuppressWarnings("unused")
    private LocalDate dateFin;
    
    /* Attributs FXML */
    
    @FXML
    private HBox listeMoisHBox;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private HBox dateDebutHBox;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private HBox dateFinHBox;
    @FXML
    private Button charger;
    @FXML
    private Button creer;
    @FXML
    private RadioButton radioMois;
    @FXML
    private RadioButton radioDate;
    @FXML
    private RadioButton radioActuel;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private VBox selectPane;
    
    /* ---------- CONSTRUCTEURS ---------- */
        
    @FXML
    public void initialize()
    {        
        handler = new ControlSonar();
        selectPane.getChildren().clear();
        backgroundPane.getChildren().remove(creer);       
    }
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    public void saveDebut()
    {
        dateDebut = dateDebutPicker.getValue();
    }

    @FXML
    public void saveFin()
    {
        dateFin = dateFinPicker.getValue();
    }
    
    @FXML
    public void chargerExcel() throws InvalidFormatException, IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("FichierExcel");
        fileChooser.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fileChooser.showOpenDialog(backgroundPane.getScene().getWindow());
        if (file != null)
        {
            handler.creerVueProduction(file);
        }      
    }
    
    @FXML
    public void afficherParMois()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(charger);
    }
    
    @FXML
    public void afficherParDate()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(dateDebutHBox);
        selectPane.getChildren().add(dateFinHBox);
        selectPane.getChildren().add(creer);
    }
    
    @FXML
    public void creerVue()
    {
        throw new FunctionalException(Severity.SEVERITY_INFO, "Pas encore implémenté");
    }
}