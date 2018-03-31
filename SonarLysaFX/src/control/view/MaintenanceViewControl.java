package control.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import control.parent.ViewControl;
import control.task.CreerVueCHCCDMTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utilities.Statics;

public class MaintenanceViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private HBox checkBoxPane;
    @FXML
    private Button creer;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioExcel;
    @FXML
    private RadioButton radioCHC;
    @FXML
    private RadioButton radioCHCCDM;
    @FXML
    private VBox selectPane;
    @FXML
    private Button charger;
    @FXML
    private CheckBox suivante;
    @FXML
    private CheckBox precedente;

    private boolean cdm;
    private CreerVueCHCCDMTask task;
    private String titreTask;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        selectPane.getChildren().clear();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void chargerExcel()
    {
        File file = getFileFromFileChooser(TITRE);
        task = new CreerVueCHCCDMTask(file);
        startTask(task, titreTask);
    }

    @Override
    public void afficher(ActionEvent event)
    {
        Object source = event.getSource();
        String id = "";
        if (source instanceof RadioButton)
        {
            selectPane.getChildren().clear();

            id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioExcel" :
                    selectPane.getChildren().add(charger);
                    titreTask = "Vue CHC/CDM par Excel";
                    break;
                case "radioCHC" :
                    selectPane.getChildren().add(checkBoxPane);
                    selectPane.getChildren().add(creer);
                    cdm = false;
                    titreTask = "Vues CHC";
                    break;
                case "radioCHCCDM" :
                    selectPane.getChildren().add(checkBoxPane);
                    selectPane.getChildren().add(creer);
                    cdm = true;
                    titreTask = "Vues CHC_CDM";
                    break;
                default :
                    break;
            }
        }
    }

    @FXML
    public void creerVues()
    {
        // Création de la liste des annèes
        List<String> annees = new ArrayList<>();
        annees.add(String.valueOf(Statics.TODAY.getYear()));
        if (suivante.isSelected())
            annees.add(String.valueOf(Statics.TODAY.getYear() + 1));
        if (precedente.isSelected())
            annees.add(String.valueOf(Statics.TODAY.getYear() - 1));

        // Lancement de la task
        task = new CreerVueCHCCDMTask(annees, cdm);
        startTask(task, titreTask);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}